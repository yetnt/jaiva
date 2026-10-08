# v6.1.0

## New Features

### Libraries

#### jaiva/threads

Enables threading by executing a function body in it's own separate thread.

```jaiva
tsea "jaiva/threads"!

kwenza f1() ->
    khuluma("HI")!
    sleep(3000)! @ Sleep for 3 seconds
    khuluma("Damn.")!
<~

kwenza clean(type, err) ->
    if (err~ > 0) ->
        khuluma("An error occured executing f1!")!
        khuluma(err)!
    <~
<~

thread(f1, clean)!
khuluma("OKAY")!
sleep(2000)!
khuluma("WOW")!

{
  Output order should be:
    "OKAY"
    "HI"
    "WOW"
    "Damn."
}
```

#### jaiva/files/bytes & jaiva/files/bytes/const

Allows byte level reading and writing of input files

```jaiva
maak conf <- f_cwriter()!
    |> "LOCATION"!              @ file loc
    |> true!                    @ Appends to the file

@ Java UTF-8 Uses the normal DataOutputStream modified UTF-8 encoding.
@ W_UTF8 can be used, but we wont know the length.
conf(C_OUTPUT)(W_JAVA_UTF8, "Hello World!!!")!
conf(C_OUTPUT)(W_JAVA_UTF8, "Other things")!
conf(C_FLUSH)()!
conf(C_CLOSE)()!

@ Function to read all bytes of some file
kwenza t() ->
    maak cf <- f_creader()!
    |> "file loc"!          @ File location.
    maak read <- cf(C_INPUT)!
    maak eof <- cf(C_EOF)! @ A function which returns a boolean.

    maak collect <-|!
    maak lastVal!
    nikhil (eof()') ->
        lastVal <- read(R_BYTE)!
        sif (lastVal != idk) ?> collect <- arrLit(collect:::, lastVal)!
    <~

    khuluma("bytes:")!
    khuluma(collect)!

    cf(C_CLOSE)()!
<~
```

**Both return a [Continuous Function](../Continuous-Functions.md) over the file writing resource.**

#### jaiva/continuous

_Contains constants for providing input into continuous functions. A continuous function, is one which closes over some form of system
resources whether it be a file or some networking socket. See [Continuous Function](../Continuous-Functions.md)_

##### Symbol list

| Alias         | Link                                                     |
|---------------|----------------------------------------------------------|
| C_INPUT       | [C_INPUT](./docs/libs/continuous.md#c_input)             |
| C_READ        | [C_INPUT](./docs/libs/continuous.md#C_INPUT)              |
| C_OUTPUT      | [C_OUTPUT](./docs/libs/continuous.md#C_OUTPUT)           |
| C_WRITE       | [C_OUTPUT](./docs/libs/continuous.md#C_OUTPUT)            |
| C_FLUSH       | [C_FLUSH](./docs/libs/continuous.md#C_FLUSH)             |
| C_END_OF_FILE | [C_EOF](./docs/libs/continuous.md#C_END_OF_FILE) |
| C_EOF         | [C_EOF](./docs/libs/continuous.md#C_END_OF_FILE)         |
| C_END         | [C_CLOSE](./docs/libs/continuous.md#C_END)                 |
| C_FINISH      | [C_CLOSE](./docs/libs/continuous.md#C_END)              |
| C_CLOSE       | [C_CLOSE](./docs/libs/continuous.md#C_END)               |

#### New functions in other libraries

- `t_strFromByteArr` - Converts an incoming byte array into a UTF-8 String.

```jaiva
tsea "jaiva/types/numbers"!

maak byteArr <-| 72, 101, 108, 108, 111!

khuluma(t_strFromByteArr(byteArr:::))! @ Prints "Hello"
```

See [Continuous Functions Section](#continuous-functions)

### Shorthand Ifs

Copied from [Shorthand Ifs in Language.md](./docs/Language.md#shorthand-ifs)

Believe me when i tell you it gets tiring to write a single conditional everytime.
e.g.
```jaiva
if (value != z && (s << 2 == r~)) ->
    value <- z!
<~
```

That's 3 whole lines. `s`horthand `if`s however, or `sif`s, collpase this into a single line!

```jaiva
sif (value != z && (s << 2 == r~)) ?> value <- z!
```

Where:

```jaiva
sif (CONDITION) ?> (STATEMENT)!
```

is exactly the same as writing
```jaiva
if (CONDITION) ->
    (STATEMENT)!
<~
```

> [!NOTE]
> This only works for single statement blocks, and only works for a singular `if`, it doesn't
> have a `mara if` or `mara` counterpart.

> [!NOTE]
> Where this differs from [Ternaries](#ternary-ifs) is that, a ternary is used to output a
> **_value_** with both a `true` case and a `false` case. A Shorthand-If however, is purely
> used to execute a **_statement_** only if, the condition is `true`

### Continuous Functions

Coped from [Continuous Functions doc](#continuous-functions)

This won't make sense if you don't have the basic understanding of [Jaiva Functions](./Language.md#functions)

Unlike normal functions, a _**continuous**_ function provides a persistent closure over a resource (A resource being
streamed file input, a network connection (soon), etc)

```jaiva
@ Given "function" has some internal resources such as an open file we can read via a continuous function
maak cont <- function()! @ cont is the reference to the continuous function

@ Get the read function
maak read <- cont("read")!
read()! @ Read

cont("close")()! @ Close and free resources
```

> [!NOTE]
> Instead of passing magic strings, you can se the constants defined in [jaiva/continuous](./libs/continuous.md) library

They specifically, return smaller closures over specific "tasks" relevant to the resource, but are ultimately still
responsible for managing said resource.

#### Closures

This is the list of smaller closures which a Continuous Function _may_ provide.

| `id`     | Description                                                                                           | Closure Return Type          |
|----------|-------------------------------------------------------------------------------------------------------|------------------------------|
| `read`   | Returns the closure associated with reading input                                                     | Any. Depends on the provider |
| `write`  | Returns the closure associated with writing output                                                    | `idk`                        |
| `flush`* | Returns the closure which flushes buffered output resources                                           | `idk`                        |
| `eof`*   | Returns the closure which signifies the end of a file or data                                         | `boolean`                    |
| `close`  | Returns the closure which ends the data stream and relinquishes the resources to avoid resource leaks | `idk`                        |

> [!NOTE]
> `flush` is only relevant if the continuous function is of an output related resource, and similar to `eof` for an input
> however that's more provider's implementation based.

If a hypothetical function `foo` from some internal package say

```jaiva
maak conf <- foo()!
```

listens to incoming network requests via a continuous function
then it is implied calling `foo()` will return a continuous function relating to `read` related resources. Hence:

```jaiva
maak read <- conf("read")!
```

will return `foo`'s incoming network request reader function. Unless `foo` specifies, `write` has no semantic meaning and will
do nothing.

---

#### Provider Implementation

The provider of the Continuous Function is responsible for what certain functions actually mean, in that the `write` function
made by one continuous provider may not have the exact same semantics as another `write` continuous provider.


e.g. Take the [f_cwriter](./libs/file-bytes.md#f_cwriter) from [jaiva/file/bytes](./libs/file-bytes.md):

```jaiva
tsea "jaiva/file/bytes"!
tsea "jaiva/continuous"!

maak continuous <- f_cwriter("... filePath")!

maak write <- continuous(C_OUTPUT)!        @ Get the (output) writer

write(W_INT, 10)!                           @ Write the integer 10
write(W_MOD_UTF8, "Hello")!                 @ Write the string "Hello" as (Java Modified) UTF-8

continuous(C_FLUSH)()!                      @ Get and immediately invoke the flush closure
continuous(C_CLOSE)()!                      @ Get and immediately invoke the end closure to free resources as we are done
                                            @ writing to the file.
```
In this case `f_cwriter` returns a continuous function which:
- Is of **_write_** semantics (writing to a file), hence calling `read` will be useless.
- Enforces that the returned `write` closure, takes at least 2 arguments. The type to write, and the actual input.
- Provides an implementation of `flush` as this is an output resource (that is buffered+)

Whereas it's opposite in the same library [f_creader](./libs/file-bytes.md#f_creader) uses the reader version:

```jaiva
tsea "jaiva/file/bytes"!
tsea "jaiva/continuous"!

maak continuous <- f_creader("... filePath")!

maak read <- continuous(C_INPUT)!           @ Get the (input) reader
maak eof <- continuousI(C_EOF)!             @ Get the EOF function (Returns true, if EOF was reached)

nikhil (eof()') ->
    khuluma(read(R_BYTE))!                  @ Keep reading and printing bytes until EOF is reached
<~

continuous(C_CLOSE)()!                      @ Close and free resources again
```

here, `f_creader` returns a continuous function which:
- Is of **_read_** semantics (reading a file), hence calling `write` will be useless.
- Enforces that the `read` closure must take at a minimum, 1 argument. The type to read the next byte/byte(s) as
- Provides an implementation of `eof` which when the end of the file is reached, the function will
  return true.

## Nerd Corner

### Continuous Functions

They are a normal `BaseFunction`, which provides a closure over an `InputStream` or an `OutputStream`.
Normally it's not both at once, but this may change if i consider making a Socket API.

These functions then further given input return smaller closures over specific semantics operations
such as `flush`, `eof`, `close`, `read`, `write`. Although the provider of a continuous Function, the function
which instantiated it, is responsible for providing semantic implementation of each.

### GlobalResources & Threading

Before GlobalResources used to be a single class within `IConfig` that held a `Scanner` of `System.in`.

Very limiting.

Now, `GlobalResources` is instead, an actual globally available via `IConfig`, Map of `Thread.threadId()` to a `Resources`
object, where every newly created `Thread` can own their own share of resources, and hence clean-up once they exit
or error.

The main execution thread is added to the `GlobalResources` map everytime a new `IConfig` object is instantiated.

### Short Bursts

- Encapsulate fields in `IConfig`
- Update to ytils `1.2.0`

## Bug Fixes

- Fix bug where `nikhil` loops could not use `nevermind` loop control keyword
- Fix bug where extending params of an empty function call would just crash.