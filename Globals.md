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

The following are the libraries that are available: [types](./libs/types.md), [arrays](./libs/arrays.md), [file](./libs/file.md), [math](./libs/math.md).
