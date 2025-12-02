# `globals`
 
Globals, The set of variables and functions that are always available.
 
 
## Functions
 
### `F~khuluma(msg?, removeNewLn?) -> `_**`idk`**_
 
Prints any given input to the console.
 
- _`msg?`_ <- `"idk"` : _The message to print._
- _`removeNewLn?`_ <- `"boolean"` : _If true, no new line is printed after the message. Defaults to false._
 
Returns :
> _**idk**_
 
**Example:**
```jaiva
khuluma("Hello, World!")! @ Prints "Hello, World!" to the console with a new line.
@ Then the following prints "Hello" then "World!" on the same line.
khuluma("Hello, ", true)!
khuluma("World!")!
khuluma()! @ Prints just a new line.
```
 
> [!NOTE]
> _For the nerds. This is just System.out.println as default, then if removeNewLn is true, System.out.print. Lol_
 
### `F~clear() -> `_**`idk`**_
 
Clears the console.
 
Returns :
> _**idk**_
 
**Example:**
```jaiva
clear()! @ Clears the console output.
```
 
> [!NOTE]
> _The effectiveness of this function depends on the terminal's support for ANSI escape codes._
 
### `F~getVarClass(var) -> `_**`string`**_
 
Attempts to return the symbol's corresponding Java class in string form. If you're using this then you def don't know what you're doing.
 
- **`var`** <- `"idk"` : _The value to return it's token symbol for_
 
Returns :
> _**The .toString() class representation of the given variable's token**_
 
**Example:**
```jaiva
maak name <- "ayo!"!
khuluma(getVarClass(name))! @ Prints com.jaiva.tokenizer.tokens.Token$TStringVar@(hashcode)
khuluma(getVarClass(reservedKeywords))! @ Prints com.jaiva.tokenizer.tokens.Token$TArrayVar@(hashcode)
```
 
### `F~mamela() -> `_**`string`**_
 
Listens for input from the console.
 
Returns :
> _**The input given from the console as a string**_
 
**Example:**
```jaiva
khuluma("What is your name?")!
maak name <- mamela()! @ Reads input from the user and stores it in the variable name.
khuluma("Hello, " + name + "!")!
```
 
> [!NOTE]
> _GithubBlockQuote: This will pause all execution until input is given._
 
### `F~sleep(milliseconds) -> `_**`idk`**_
 
Pause execution of the interpreter for n amount of milliseconds.
 
- **`milliseconds`** <- `"number"` : _The amount of milliseconds to sleep for_
 
**Example:**
```jaiva
khuluma("yo")!
sleep(1000)! @ pause for 1 second.
khuluma("yo.. again")!
```
 
> [!NOTE]
> _(Keep in mind this function still has to take your value and turn it into a Java primitive and other things, so the delay might not be exact. If you're looking for accuracy maybe remove x amount of ms till it's accurate.)_
 
### `F~neg(input) -> `_**`number`**_
 
> [!IMPORTANT]
> _**This symbol is deprecated!**__`Unary minus works now. Use it or multiply by -1`_
 
Negates the given number. This is because my unary minus shit isnt working so i had to make this.
 
- **`input`** <- `"number"` : _The input to negate_
 
### `F~flat(<-arrays) -> `_**`[]`**_
 
Attempts to flatten (at the top level) the given arrays array1 and array2 into 1 single array.
 
- _`arrays?`_ <- `"[]"` : _Variable amount of arrays to input_
 
**Example:**
```jaiva
maak array1 <-| 1, 2, 3!
maak array2 <-| 4, 5, 6!
maak array3 <- flat(array1, array2)! @ Flattens the two arrays into a new one.
khuluma(array3)! @ Prints [1, 2, 3, 4, 5, 6]
```
 
> [!NOTE]
> _If there are any type mismatches in array1, it will be ignored and the same check is done to array2 and so on. Therefore this function will **always** return an array, whether or not it was successful._
 
### `F~arrLit(<-elements) -> `_**`[]`**_
 
Creates an array literal from the given elements. This is useful if you want to createFunction an array without declaring it to a variable. For example, `arrLit(1, 2, 3)` will return `[1, 2, 3]`. This is needed as Jaiva doesnt have square bracket syntax
 
- _`elements?`_ <- `"[]"` : _Variable amount of elements to take in and turn into a single array._
 
Returns :
> _**The input given, as an array**_
 
**Example:**
```jaiva
maak array1 <- arrLit(1, 2, 3, "hello", aowa, idk)! @ Creates an array with mixed types.
maak array2 <-| 1, 2, 3, "hello", aowa, idk! @ Creates an array with mixed types. (Same as above but with maak syntax)
maak array3 <- arrLit()! @ Creates an empty array.
khuluma(array1)! @ Prints [1, 2, 3, "hello", aowa, idk]
khuluma(array3)! @ Prints []
khuluma(array1 = array2)! @ Prints aowa (false) (I am not implementing array equality via `=` operator anytime soon. It is the exact same array though.)
```
 
### `F~scope(<-strings) -> `_**`idk`**_
 
Configures the current scope to whichever settings provided.
 
- _`strings?`_ <- `"[]"` : _variable amount of strings to input._
 
Returns :
> _**idk**_
 
**Example:**
```jaiva
scope("freezeAll")!
kwenza af(a) ->
    khutla a!
<~
af <- 10! @ Errors as the variable af cannot be written to.

@ Or

scope("ew")!
@* depr $> This fucntion is deprecated.
kwenza af(a) ->
    khutla a!
<~
af()! @ Errors as the usual deprecation warning is now a fatal error. (Crashes the interpreter)
```
 
> [!NOTE]
> _The following are accepted strings: 
    "sw" to suppress all warnings. 
    "ew" to elevate all warnings. 
    "constant" to make all symbols given constant. and 
    "strict" which toggles "ew" and "constant"
_
 
### `F~ask(message) -> `_**`string`**_
 
> [!IMPORTANT]
> _**This symbol is deprecated!**__`This function serves no real purpose. (it doesn't work in most environments)`_
 
Prompts the user for input via a UI dialog.
 
- **`message`** <- `"string"` : _The message to display in the dialog._
 
Returns :
> _**The user's input as a string.**_
 
**Example:**
```jaiva
maak name <- ask("what's ur name cuh?")! @ Reads input from the user and stores it in the variable name.
khuluma("Hello, " + name + "!")!
```
 
### `F~typeOf(input?) -> `_**`string`**_
 
Returns the type of any given input.
 
- _`input?`_ <- `"idk"` : _The input to check the type against_
 
Returns :
> _**Returns the string form of the typ, which could be "array", "string", "boolean", "number", "function", or the primitive idk.**_
 
**Example:**
```jaiva
maak b <- 100!

khuluma(typeOf(b))!                   @ "number"
khuluma(typeOf(typeOf))!                @ "function"
khuluma(typeOf())!                    @ idk
khuluma(typeOf(aowa))!                @ "boolean"
khuluma(typeOf("what the f"))!        @ "string"
khuluma(typeOf(reservedKeywords))!    @ "array"
khuluma(typeOf(idk))!                 @ idk
```
## Variables
 
### `uargs <- `_**`[]`**_
 
The command-line arguments without Jaiva-specific arguments.
 
Returns :
> _**An array of strings, representing the sanitized command-line arguments.**_
 
**Example:**
```jaiva
khuluma(uargs[0])! @ Prints any other argument given after the Jaiva specific ones.
khuluma(uargs[1])! @ Prints the second user argument if provided.
khuluma(uargs[2])! @ And so on...
```
 
### `version <- `_**`string`**_
 
What do you think this returns.
 
**Example:**
```jaiva
khuluma(version)! @ Prints 5.0.0 (at the time of writing)
```
 
### `reservedKeywords <- `_**`[]`**_
 
An array containing jaiva's reserved keywords that you cannot use as symbol names.
 
**Example:**
```jaiva
khuluma(reservedKeywords)! @ Prints all the reserved keywords.
```
 
### `args <- `_**`[]`**_
 
The command-line arguments passed to the Jaiva command.
 
Returns :
> _**An array of strings, where each string is a command-line argument.**_
 
**Example:**
```jaiva
khuluma(args[0])! @ Prints "jaiva" if the command was 'jaiva myscript.jv'
khuluma(args[1])! @ Prints "myscript.jv" if the command was 'jaiva myscript.jv'
```
 
> [!NOTE]
> _This is the raw arguments given from Main.args[], including Jaiva-specific arguments._
