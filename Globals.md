# Globals

These a symbols (variables and functions) that are either avaiable in the global scope, or need to be imported from their respected libraries.

Because why not, in this file, the symbols will contain a link to where exactly it's defined and/or imported from.

> [!NOTE]
> Please note that this is not a complete list of all the symbols in Jaiva. This is just a list of the most common ones.

## Built-in

These are any symbols that are available in any Jaiva program.

(All built-in symbols are **frozen** meaning they're immutable and cannot be changed.)

### <center> Variables

#### `version <- `_*`"string"`*_

The current version of Jaiva that you're using. This string is in the format of `major.minor.patch`

It's determined by the version string set in the Jaiva CLI. This means running `jaiva --version` results in the same string as `version` (Just with ASCII and credits to me lol).

See **[Main.java](./src/main/java/com/jaiva/Main.java)** where this is defined.

```jiv
khuluma(version)! @ Prints 1.0.0-beta.2 (at the time of writing)
```

#### `reservedKeywords <-| `_*`(["string"])`*_

An array of all the reserved keywords in Jaiva. This includes keywords like `if`, `mara`, `nikhil`, `tsea`, etc.

See **[Keywords.java](./src/main/java/com/jaiva/lang/Keywords.java)** where this is defined.

```jiv
khuluma(reservedKeywords)! @ Prints all the reserved keywords.
```

#### `args <-| `_*`(["string"])`*_

An array of all the command line arguments passed to the Jaiva program.

See [Main.java](./src/main/java/com/jaiva/Main.java) where this is defined.
Then See [IConfig.java](./src/main/java/com/jaiva/interpreter/runtime/IConfig.java) where this is used by the interpreter.

```jiv

khuluma(args)! @ Prints all the command line arguments passed to the Jaiva program.
```

#### `uargs <-| `_*`(["string"])`*_

An array of all the user arguments passed to the Jaiva program. This is the same as `args`, but without the file path, and any other optional Jaiva flags you can pass such as the `-d`\\`--debug` flags. (See [CLI](./CLI.md) for more information on the CLI flags.)

This is useful if you want to pass your own arguments to a Jaiva file without Jaiva's flags.

See [Main.java](./src/main/java/com/jaiva/Main.java) where this is defined.
Then See [IConfig.java](./src/main/java/com/jaiva/interpreter/runtime/IConfig.java) where this is used by the interpreter.

```jiv
khuluma(uargs)! @ Prints all the user arguments passed to the Jaiva program.
```

### <center> Functions

#### `scope(<-strings) -> `_*`khutla idk!``

Configures the current scope to whichever settings provided.

This takes in any number of arguments (strings) to use to configure the current scope.

The following table describes what each value is, aliases and what it does.

(Case-insensitive.)

| Property             | Aliases                 | Description                                                            |
|----------------------|-------------------------|------------------------------------------------------------------------|
| `"freezeAll"`        | `"constant"\"freeze"`   | Freezes all symbols defined in the current scope. (After calling this) |
| `"elevateWarnings"`  | `"ew"\"warningsarebad"` | This will force all warnings to throw a fatal error instead of print.  |
| `"suppressWarnings"` | `"sw"\"fuckWarnings"`   | Supresses All warnings, effectively ignoring them.                     |
| `"strict"`           |                         | The same as enabling `"freezeAll"` and `"elevateWarnings"`             |

example:

```jiv
scope("freezeAll")!

kwenza af(a) ->
    khutla a!
<~

af <- 10! @ Errors as the variable af cannot be written to.
```

```jiv
scope("ew")!

@* depr $> This fucntion is deprecated.
kwenza af(a) ->
    khutla a!
<~

af()! @ Errors as the usual deprecation warning is now a fatal error. (Crashes the interpreter)
```

#### `arrLit(<-content) -> `_*`khutla (array)!`*_

This function, takes any number of arguments and returns an array containing those arguments.
Since Jaiva doesn't have square bracket syntax for arrays, and delcaring a new array with `maak` can get tedious, this function is a shorthand for creating arrays without having to assign it to a variable.

See [Globals.java](./src/main/java/com/jaiva/interpreter/libs/Globals.java) where this is defined.

```jiv
maak array1 <- arrLit(1, 2, 3, "hello", aowa, idk)! @ Creates an array with mixed types.
maak array2 <-| 1, 2, 3, "hello", aowa, idk! @ Creates an array with mixed types. (Same as above but with maak syntax)
maak array3 <- arrLit()! @ Creates an empty array.
khuluma(array1)! @ Prints [1, 2, 3, "hello", aowa, idk]
khuluma(array3)! @ Prints []
khuluma(array1 = array2)! @ Prints aowa (false) (I am not implementing array equality via `=` operator anytime soon. It is the exact same array though.)
```

#### `khuluma(msg, removeNewLn?) -> `_*`khutla (idk)!`*_

Prints any given input to the console with a newline afterwards. (Unless `removeNewLn` is set to true, in which case it doesn't add a newline afterwards. This is useful for printing on the same line.)

(It just uses System.out.println() lol) This function returns no value.

See [IOFunctions.java](./src/main/java/com/jaiva/interpreter/libs/IOFunctions.java) where this is defined.

```jiv
khuluma("Hello, world!")!
khuluma("Hello, again", yebo)!
khuluma("Okay")!
```

Console output will be:

```sh
Hello, world!
Hello, againOkay
```

#### `mamela() -> `_*`khutla "string"!`*_

This function is used to read input from the user. (In the console.) It returns a string of the input. This function will block runtime until the user presses enter.

See [IOFunctions.java](./src/main/java/com/jaiva/interpreter/libs/IOFunctions.java) where this is defined.

```jiv
khuluma("What is your name?")!
maak name <- mamela()! @ Reads input from the user and stores it in the variable name.
khuluma("Hello, " + name + "!")!
```

#### `ask(message) -> `_*`khutla "string"!`*_

This function is used to read input from the user. (Via UI.) It returns a string of the input. This function will block runtime until the user presses enter.

See [IOFunctions.java](./src/main/java/com/jaiva/interpreter/libs/IOFunctions.java) where this is defined.

```jiv
maak name <- ask("what's ur name cuh?")! @ Reads input from the user and stores it in the variable name.
khuluma("Hello, " + name + "!")!
```

#### `sleep(ms) -> `_*`khutla idk`*_

Function that pauses execution for `ms` amount of milliseconds. Self-explanatory.

```jiv
khuluma("yo")!
sleep(1000)! @ pause for 1 second.
khuluma("yo.. again")!
```

See [Globals.java](./src/main/java/com/jaiva/interpreter/libs/Globals.java) where this is defined.

#### `clear() -> `_*`khutla idk`*_

Clears the console. I can't really showcase code for this one hey.

See [IOFunctions.java](./src/main/java/com/jaiva/interpreter/libs/IOFunctions.java) where this is defined.

#### `getVarClass(ref) -> `_*`khutla "string"!`*_

This function returns the Java class `.toString()` of the variable passed to it. This is useful for debugging and checking the type of a variable. (Specifically, the class at parse time. This is not the same as the type of the variable at runtime.)
What you will use this for? I don't know, but it's there if you need it.

See [Globals.java](./src/main/java/com/jaiva/interpreter/libs/Globals.java) where this is defined.

```jiv
maak name <- "ayo!"!
khuluma(getVarClass(name))! @ Prints com.jaiva.tokenizer.tokens.Token$TStringVar@(hashcode)
getVarClass(reservedKeywords)! @ Prints com.jaiva.tokenizer.tokens.Token$TArrayVar@(hashcode)
```

#### `flat(array1, array2) -> `_*`khutla (array)!`*_

This function takes two arrays and attempts to flatten them into a single array. This is your only way to "edit" an array in Jaiva. (You cannot change the contents of an array, but you can create a new one with the contents of the two arrays.)

If there are any type mismatches in array1, it will be ignored and the same check is done to array2. Therefore this function will **always** return an array, whether or not it was successful.

See [Gloabals.java](./src/main/java/com/jaiva/interpreter/libs/Globals.java) where this is defined.

```jiv
maak array1 <-| 1, 2, 3!
maak array2 <-| 4, 5, 6!
maak array3 <- flat(array1, array2)! @ Flattens the two arrays into a new one.
khuluma(array3)! @ Prints [1, 2, 3, 4, 5, 6]
```


#### `typeOf(input?) -> ` _*`khutla string!`*_

Returns the type of any given input. Which could be "array", "string", "boolean", "number", "function", or the primitive `idk` otherwise.

```jiv
maak b <- 100!

khuluma(typeOf(b))!                   @ "number"
khuluma(typeOf(typeOf))!                @ "function"
khuluma(typeOf())!                    @ idk
khuluma(typeOf(aowa))!                @ "boolean"
khuluma(typeOf("what the f"))!        @ "string"
khuluma(typeOf(reservedKeywords))!    @ "array"
khuluma(typeOf(idk))!                 @ idk
```

Keep in mind, it will never return a string form of `idk`, it always returns `idk` as a value if no parameters were passed or the value itself was `idk`


## Libraries

These are symbols that are either available in the `/lib/` folder of the Jaiva bin directory, or have a Java file associated with them.
These are typically frozen symbols, but some might not be.

See [Importing and Exporting](./README.md#tsea-import-and-exporting-files) for more information on how to import libraries.

But it's generally:

```jiv
tsea "jaiva/(module name)"!
```

> [!NOTE]
> Even though all jaiva files have the extensions `.jiv`, `.jaiva` and `.jva`, All of them (The library files) have the `.jiv` extension. This is just the convention i decided to use.

> [!NOTE]
> Unlike built-in symbols where I can link you to implementation, I cannot do that for libraries because they are not part of the Jaiva source code, but rather part of the downloadable zip package. (The bin)

The following are the libraries that are available: [types](./libs/types.md), [arrays](./libs/arrays.md), [file](./libs/file.md), [math](./libs/math.md).
