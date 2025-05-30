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

### <center> Functions

#### `khuluma(msg, removeNewLn?) -> `_*`khutla (idk)!`*_

Prints any given input to the console with a newline afterwards. (Unless `removeNewLn` is set to true, in which case it doesn't add a newline afterwards. This is useful for printing on the same line.)

(It just uses System.out.println() lol) This function returns no value.

See [IOFunctions.java](./src/main/java/com/jaiva/interpreter/globals/IOFunctions.java) where this is defined.

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

See [IOFunctions.java](./src/main/java/com/jaiva/interpreter/globals/IOFunctions.java) where this is defined.

```jiv
khuluma("What is your name?")!
maak name <- mamela()! @ Reads input from the user and stores it in the variable name.
khuluma("Hello, " + name + "!")!
```

#### `ask(message) -> `_*`khutla "string"!`*_

This function is used to read input from the user. (Via UI.) It returns a string of the input. This function will block runtime until the user presses enter.

See [IOFunctions.java](./src/main/java/com/jaiva/interpreter/globals/IOFunctions.java) where this is defined.

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

See [Globals.java](./src/main/java/com/jaiva/interpreter/globals/Globals.java) where this is defined.

#### `clear() -> `_*`khutla idk`*_

Clears the console. I can't really showcase code for this one hey.

See [IOFunctions.java](./src/main/java/com/jaiva/interpreter/globals/IOFunctions.java) where this is defined.

#### `getVarClass(ref) -> `_*`khutla "string"!`*_

This function returns the Java class `.toString()` of the variable passed to it. This is useful for debugging and checking the type of a variable. (Specifically, the class at parse time. This is not the same as the type of the variable at runtime.)
What you will use this for? I don't know, but it's there if you need it.

See [Globals.java](./src/main/java/com/jaiva/interpreter/globals/Globals.java) where this is defined.

```jiv
maak name <- "ayo!"!
khuluma(getVarClass(name))! @ Prints com.jaiva.tokenizer.Token$TStringVar@(hashcode)
getVarClass(reservedKeywords)! @ Prints com.jaiva.tokenizer.Token$TArrayVar@(hashcode)
```

#### `flat(array1, array2) -> `_*`khutla (array)!`*_

This function takes two arrays and attempts to flatten them into a single array. This is your only way to "edit" an array in Jaiva. (You cannot change the contents of an array, but you can create a new one with the contents of the two arrays.)

If there are any type mismatches in array1, it will be ignored and the same check is done to array2. Therefore this function will **always** return an array, whether or not it was successful.

See [Gloabals.java](./src/main/java/com/jaiva/interpreter/globals/Globals.java) where this is defined.

```jiv
maak array1 <-| 1, 2, 3!
maak array2 <-| 4, 5, 6!
maak array3 <- flat(array1, array2)! @ Flattens the two arrays into a new one.
khuluma(array3)! @ Prints [1, 2, 3, 4, 5, 6]
```

## Libraries

These are symbols that are either available in the `/lib/` folder of the Jaiva bin directory, or have a Java file associated with them.
These are typically frozen symbols, but some might not be.

See [Importing and Exporting](./README.md#tsea-import-and-exporting-files) for more information on how to import libraries.

But it's generally:

```jiv
tsea "jaiva/(module name)"!
@ or if you want an absolute path for some reason
tsea "C:/path/to/jaiva/lib/(module name)"!
```

> [!NOTE]
> Even though all jaiva files have the extensions `.jiv`, `.jaiva` and `.jva`, All of them (The library files) have the `.jiv` extension. This is just the convention i decided to use.

> [!NOTE]
> Unlike built-in symbols where I can link you to implementation, I cannot do that for libraries because they are not part of the Jaiva source code, but rather part of the downloadable zip package. (The bin)

The following are the libraries that are available

### `file`

This library contains variables (and soon maybe functions) for handling file related things.

#### `[file]` Structure

Some of these functions return an array which contains relevant information about the file in question.

This is the format all these arrays follow

```jaiva
[fileName, fileDir, [contents], [canRead, canWrite, canExecute]]
```

So for a file "C:/Users/Documents/myfile.js", with the content:

```js
console.log("I love Brotherkupa.");
console.log("Nobody asked.");
```

the array will contain the following:

```json
[
    "myfile.js",
    "C:/Users/Documents/"
    [
        "console.log(\"I love Brotherkupa.\");",
        "console.log(\"Nobody asked.\");"
    ],
    [
        true, true, true
    ]
]
```

See [IOFile.java](./src/main/java/com/jaiva/interpreter/globals/IOFile.java) where this is defined.

For all exmaples, the "current" file is located in C:\Users\me\file.jiv

#### `f_this <- `_*`[file]!`*_

Returns an array containing the current file's properties ([Structure](#file-structure))

```jiv
tsea "jaiva/file"!
maak fileInfo <- f_this()! @ Gets the current file's properties.
khuluma(fileInfo)! @ Prints the array with file name, directory, contents, and permissions.
```

#### `f_file(path) -> `_*`khutla ([file])!`*_

Returns an array containing the properties of the file at the given `path` ([Structure](#file-structure)).

```jiv
tsea "jaiva/file"!
maak fileInfo <- f_file("C:/Users/me/anotherfile.js")! @ Gets the properties of anotherfile.js.
khuluma(fileInfo)! @ Prints the array with file name, directory, contents, and permissions.
```

#### `f_name <- `_*`"string"`*_

\*_**Independent**_

Returns the current file name

```jiv
tsea "jaiva/file"!
khuluma(f_name)! @ file.jiv
```

#### `f_dir <- `_*`"string"`*_

\*_**Independent**_

Returns the current working directory

```jiv
tsea "jaiva/file"!
khuluma(f_dir)! @ C:\Users\me\file.jiv
```

#### `f_bin <- `_*`"string"`*_

\*_**Independent**_

Returns the directory where you can find the `jaiva.jar` file

```jiv
tsea "jaiva/file"!
khuluma(f_bin)! @ C:\Users\jaiva\
```

### `math`

This library contains (some) math functions. Unless really needed I don't see myself adding more than these.

See [Math.java](./src/main/java/com/jaiva/interpreter/globals/Math.java) where this is defined.

#### `m_round(value) -> `_*`khutla number!`*_

\*_**Independent**_

Rounds the given real nmber to an integer. This is for the real ones who hate working with precision.

```jiv
tsea "jaiva/math"!

khuluma(m_round(4.51))! @ Prints 5
```

#### `m_random(lower, upper) `_*`khutla number!`*_

\*_**Independent**_

Returns a random (integer) between the `lower` and `upper` bounds. (Both inclusive)

```jiv
tsea "jaiva/math"!

khuluma(m_random(0, 10) > 5)! @ Might print true or false
khuluma(m_random(0, 10) > 5)! @ Might print true or false
```

### `convert`

This library contains functions for converting stuff to different types.

See [Conversions.java](./src/main/java/com/jaiva/interpreter/globals/Conversions.java) where this is defined.

#### `c_numToString(num) -> `_*`khutla "string"!`*_

\*_**Independent**_

Returns a string which represents the given number

```jiv
tsea "jaiva/convert"!
khuluma(c_numToString(2.0) + 10)! @ Will print "2.010" as that's string concat
```

#### `c_stringToNum(string) -> `_*`khutla number!`*_

\*_**Independent**_

Returns a number which represents the given string

```jiv
tsea "jaiva/convert"!
khuluma(stringToNum("2.4") + 10)! @ Will print 12.4
```

### `arrays`

This library contains functions for manipulating arrays.

If you ask why not just make arrays mutable, Jaiva is a functional programming language. I'm doing you a favour by already having these implemented. I could've not had arrays at all. (TLDR; I'm too lazy to implement an array index reassignment token or adjust my old tokens for that.)

I can't link you anywhere, if you want to see the file go to your jaiva folder, because this is where it is mann.

> [!WARNING]
> Some functions use other functions. It's best to just import the whole library instead of trying to import only the functions you need. (e.g. If you import only `a_pop` you will get an error because `a_pop` uses `a_remove` internally.). I will however, list the functions that depend on other functions.

#### `a_remove(arr, n) -> `_*`khutla (array)!`*_

\*_**Independent**_

Returns an array which contains the original array (`arr`) with `n` amount of elements removed from the end.

```jiv
tsea "jaiva/arrays"! @ Import the arrays library
maak array <-| 1, 2, 3, 4, 5, 6, 7, 8, 9, 10!
maak newArray <- a_remove(array, 5)! @ Removes 5 elements from the end of the array.
khuluma(newArray)! @ Prints [1, 2, 3, 4, 5]
```

#### `a_unshift(arr, n) -> `_*`khutla (array)!`*_

\*_**Independent**_

Returns an array which contains the original array (`arr`) with `n` amount of elements removed from the start.

```jiv
tsea "jaiva/arrays"! @ Import the arrays library
maak array <-| 1, 2, 3, 4, 5, 6, 7, 8, 9, 10!
maak newArray <- a_unshift(array, 5)! @ Removes 5 elements from the start of the array.
khuluma(newArray)! @ Prints [6, 7, 8, 9, 10]
```

#### `a_pop(arr) -> `_*`khutla (array)!`*_

\*_**Depends on** [`a_remove()`](#a_removearr-n---khutla-array)_

Returns a new array without the last element of the original array (`arr`).

This is the same as `a_remove(arr, 1)` (It's actually exactly what the function's defintion is) So you can use either one.

```jiv
tsea "jaiva/arrays"! @ Import the arrays library
maak array <-| 1, 2, 3, 4, 5, 6, 7, 8, 9, 10!
maak newArray <- a_pop(array)!
khuluma(newArray)! @ Prints [2, 3, 4, 5, 6, 7, 8, 9, 10]
```

#### `a_push(arr, element) -> `_*`khutla (array)!`*_

\*_**Independent**_

Returns a new array with the `element` appended at the end of the original array (`arr`).

```jiv
tsea "jaiva/arrays"! @ Import the arrays library
maak array <-| 1, 2, 3, 4, 5, 6, 7, 8, 9, 10!
maak newArray <- a_push(array, 11)! @ Appends 11 to the end of the array.
khuluma(newArray)! @ Prints [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
```

#### `a_shift(arr, element) -> `_*`khutla (array)!`*_

\*_**Independent**_

Returns a new array with the `element` appended at the start of the original array (`arr`) and shifts all other elements 1 up.

```jiv
tsea "jaiva/arrays"! @ Import the arrays library
maak array <-| 1, 2, 3, 4, 5, 6, 7, 8, 9, 10!
maak newArray <- a_shift(array, 0)! @ Appends 0 to the start of the array.
khuluma(newArray)! @ Prints [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
```

#### `a_filter(arr, predicate) -> `_*`khutla (array)!`*_

\*_**Independent**_

Filters through elements in the given array and return an array whose elements satisfy the `predicate` function.

The `predicate` function takes a single argument and returns a boolean value. If the value is true, the element is included in the new array. If false, it is not included.

```jiv
tsea "jaiva/arrays"! @ Import the arrays library
maak array <-| 1, 2, 3, 4, 5, 6, 7, 8, 9, 10!

kwenza isEven(n) ->
    khutla n % 2 = 0! @ Returns true if n is even
<~

maak newArray <- a_filter(array, isEven)! @ Filters the array with the isEven function.
khuluma(newArray)! @ Prints [2, 4, 6, 8, 10]!
```
