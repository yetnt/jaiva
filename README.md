<h1> <center> <img src="images/jaiva-full-logo.png" width="50"> Jaiva!</h1>

(Current) Version : **_1.0.2_**

(Beta) Version : **_2.0.0-beta.1_**

![isTen](./images/isTen.png)

![screenshot of the testType.jiv program](./images/testType.png)

<!-- ![sc](./images/game.png) -->

---

This esolang of mine is still in development, so expect alot of updates.

This is for the most part _supposed_ to be a functional programming language.

To setup, see [Install.md](./Install.md)
To run, see [CLI.md](./CLI.md)

For a list of global variables and functions, see [Globals.md](./Globals.md)

Jaiva files end in the _`.jiv`_ or _`.jaiva`_ or _`.jva`_ extension.

## <center>Prerquisuiesdsfb

1. **Get and Install (at least) Java 21**
   yeah this isn't a good start 💀 but this shit is amde in java so ye man.
   [Download and install the latest JDK 21](https://jdk.java.net/21)

2. **Set Up Jaiva as a Global Command**  
   Follow the instructions in [Install.md](./Install.md) to configure Jaiva so you can run the `jaiva` command from anywhere on your system.

3. **CLI**
   The basic command to run your file is `jaiva <filePath>` but [CLI.md](./CLI.md) exists.

## <center>Index

-   **[Jaiva!](#jaiva)**
    -   **[Prerequisites](#prerequisites)**
    -   **[Index](#index)** <- you are here.
    -   **[Syntax](#syntax)**
        -   _[Comments](#comments)_
        -   _[Assignment Operators](#assignment-operators)_
        -   _[Types](#types)_
            -   [Number](#number)
            -   [Booleans](#booleans)
            -   [Strings](#strings)
                -   [String Operations](#string-operations)
                -   [Escaping Characters](#escaping-characters)
            -   [idk](#idk)
        -   _[Operators](#operators)_
        -   _[Blocks](#blocks)_
        -   _[Chaining](#chaining)_
    -   **[Keywords](#keywords)**
    -   **[Variables](#variables)**
        -   _[Definition](#definition)_
        -   _[Usage](#usage)_
        -   _[Reassignment](#reassignment)_
    -   **[Arrays](#arrays)**
    -   **[Functions](#functions)**
        -   _[Definition](#definition-1)_
        -   _[Calling](#calling)_
        -   _[Referencing](#referencing)_
        -   _[Parameters](#parameters)_
            -   [Higher-Order Functions](#higher-order-functions)
            -   [Optional Arguments](#optional-arguments)
    -   **[If Statements](#if-statements)**
        -   _[Basic If](#basic-if)_
        -   _[mara (else)](#mara-else)_
        -   _[mara (else) with if](#mara-else-with-if)_
        -   _[Ternary Ifs](#ternary-ifs)_
    -   **[Loops](#loops)**
        -   _[Nikhil Loops (While Loops)](#nikhil-loops-while-loops)_
        -   _[Colonize Loops (For Loops)](#colonize-loops-for-loops)_
            -   [Colonize with (For Each)](#colonize-with-for-each)
        -   _[Control Flow](#control-flow)_
    -   **[Error Handling](#error-handling)**
        -   _[Throw an Error](#throw-an-error)_
        -   _[zama zama (Try) chaai (Catch) Block](#zama-zama-try-chaai-catch-block)_
    -   **[Scopes](#scopes)**
    -   **[Tsea (Import) and Exporting Files](#tsea-import-and-exporting-files)**

## <center>Syntax

It's simple really, to start off with:

All lines (unless block open and close) must end in an exclamation mark. because why aren't you screaming your code.

```jaiva
maak a <- 10!
```

### Comments.

Single line comments start with an `@`

```jaiva
@ single line comment
```

And multi line comments open and close with the `{` `}`

```jaiva
{
    wus good shawty
    - stfu
}
```

#### Documentation

If you're using something like vscode and would like to document your [variables](#variables) or [functions](#functions), you can use the following syntax

```jaiva
@* My lovely variable.
maak a <- 10!
```

### Assignment operators

`<-` Is the basic assignment operator in this language. Any assignment you do is with this.

Accept for arrays, thats specifically `<-|` (See [Arrays](#arrays))

And also for throwing errors, thats `<==` (See [Throw an error](#throw-an-error))

### Types

In terms of actual types, Jaiva doesnt have type syntax like Java or Typescript. This is a loosely typed language. However some functions do error when you do not provide the correct type, so keep that in mind.

#### Number

This is just any integer or real number

They don't need any special format, you can just type the number out to use it

```jiv
maak b <- 10!
maak c <- 9.321!
```

(also scientific notation is supported)

```jiv
maak b <- 1e4! @ This gets parsed as 10000
```

even binary, hex and octal notation is supported! (it always gets parsed as an integer)

```jiv
maak c <- 0b1010    @ Integer 10 in binary
maak d <- 0xA       @ Integer 10 in hexadecimal
maak e <- 0c12      @ Integer 10 in Octal
```

#### Bools

`true` and `false` are supported, but so are `aowa` (maps to false) and `yebo` (maps to true). because come on now this is just cool.

They don't need any special format, you can just type the bool out to use it

```jiv
maak b <- aowa!
```

#### Strings

Strings, stolen striaght from Java use `"` (double quotes) to start and end a string.

These are the only characters for strings. No string literals or multi line strings because wtf dawg.

```jiv
maak b <- "String"!
```

To get the length of a string use the `~` operator

```jiv
maak b <- "let's gooo"!
khuluma(b~)! @ returns 10
```

##### String operations.

1.  Concatenation

    `"a" + "string"` _returns_ **`"astring"`**

    `1 + "string"` _returns_ **`"1string"`**

    `"string" + 1` _returns_ **`"string1"`**

2.  Substring (first occurance)

    `"string" - "tri"` _returns_ **`"sing"`** (removes the first occuramce of rhs from the lhs. Sometimes this dont work tho lol.)

    `"string" - 2` _returns_ **`"stri"`**

    `2 - "string"` _returns_ **`"ring"`**

3.  Multiplication

    `"String" * 3` _returns_ **`"StringStringString"`**

4.  Substring (all occurances)

    `"remove all es please" / "e"` _returns_ **`"rmov all s plas"`** (removes ALL occurences of rhs from the lhs. Sometimes this dont work too lol.)

    `"Hello ong World" / 2` _returns_ **`"Hello o"`** (returns the substring in the range _[0, (lhs' length)/rhs)_ )

    `4 / "Hello ong World"` _returns_ **`"lo ong World"`** (returns the substring in the range _[(rhs' length)/lhs, rhs.length-1)_ )

    And of course, you can compare strings to each other using `=` and `!=`

5.  Contains

    `"string" ? "tri"` _returns_ **`true`** (checks if the left-hand string contains the right-hand string)

    `"string" ? "xyz"` _returns_ **`false`**

##### Escaping characters

To escape characters in a string use the `$` symbol.

```
maak b <- "String$n"!
```

Table of escape characters

| character              | escape sequence |
| ---------------------- | --------------- |
| `=`                    | `$=`            |
| `,`                    | `$,`            |
| `!`                    | `$!`            |
| `@`                    | `$@`            |
| `\n` (new line)        | `$n`            |
| `\t` (tab)             | `$t`            |
| `\r` (carriage return) | `$r`            |
| `\b` (backspace)       | `$b`            |
| `\f` (form feed)       | `$f`            |
| `"` (double qoutes)    | `$"`            |
| `$`                    | `$$`            |

#### idk

This is a constant and value which is used to represent nothingness.

```jaiva
maak a <- idk!  @ Using idk as a value
maak b!         @ Creating a variable with no value assigns idk to it
maak c <- 10!

khuluma(a = idk)! @ true
khuluma(b = idk)! @ true
khuluma(c != idk)! @ true

kwenza func(t?) ->
    khutla t!
<-

khuluma(func())! @ Prints idk, as the parameter t did not get a value.
```

> [!NOTE]
> In the case of passing `idk` into a function call, unless the paramter is marked as optional, you cannot pass `idk` into a required parameter.

### Operators

| operation                       | operator |
| ------------------------------- | -------- |
| modulu                          | `%`      |
| power                           | `^`      |
| division                        | `/`      |
| multiplication                  | `*`      |
| addition                        | `+`      |
| subtraction                     | `-`      |
| unary minus                     | `-`      |
| is equal to (not double equals) | `=`      |
| is not equal to                 | `!=`     |
| greater than (and equal to)     | `>` `>=` |
| less than (and equal to)        | `<` `<=` |
| logical and                     | `&&`     |
| logical or                      | `\|\|`   |
| bitwise AND                     | `&`      |
| bitwise OR                      | `\|`     |
| braces for ordering             | `(` `)`  |

#### Order of Operations

All Operations follow this exact order:

1. Exponentiation
2. Division, Multiplcation, Modulo
3. Addition, Subtraction
4. Bitwise Operators
5. Comparisons
6. Logical Operators

Use `(`braces`)` to change the order however you wish.

> [!NOTE]
> You cannot negate a boolean expression. Sorry not sorry.

### Blocks

Blocks are defined by the `->` and `<~` symbols. The `->` symbol opens a block, and the `<~` symbol closes it.

I think this is where jaiva deviates from normal programming languages, Especially since your usual `{` `}` is reserved for comments.

> [!NOTE]
> You can have multiple blocks in a single line, but this is not recommended. It makes the code hard to read. (And i dont think i implemented or tested that case, ur crazy if u think im finna allow that )

> [!NOTE]
> You can only have a block after like, an if, or a function and whatever, you cant just open an new arbitrary block in the middle of your code. This is a design choice, and i think it makes sense.

```jiv
if (a = 10) ->
   @ inner code
<~
```

### Chaining

In Jaiva you have array indexing with `[]` (See [Arrays](#arrays)) and function calling with `()` (See [Functions](#functions))

Naturally, you can make some code like this:

```jiv
kwenza returnArray() ->
    maak arr <-| 10, 39, returnArray, 4!    @ Creates an array with values and at index 2, contains a reference to itself.
    khutla arr!
<~
```

Instead of assigning each value returned to a variable, you can chain array indexing and function calls together, multiple times, in any order or any fashion

```jiv
returnArray()[1]!               @ holds 39
returnArray()[1 + 1]!           @ ([1+1] becomes [2]) holds the referencing to itself, allowing it to call itself.
returnArray()[-1 + 3]()!        @ ([-1+3] becomes [2]) returns the same array.
returnArray()[2]()[4/2]!        @ ([4/2] becomes [2]) holds the exact same reference to itself, allowing infinite calls to itself

@ holds the exact same reference, every time.
returnArray()[2]()[2]()[2]()[2]()[2]()[2]()[2]()[2]!
```

One of my favourite examples is the infinity function

```jiv
kwenza infinity() ->
    khutla infinity!
<~

infinity()!             @ Returns itself
infinity()()()()!       @ Returns itself
infinity()()()()()()()()()()()()()()()()()()()()()()! @ Still returns itself.
```

Jaiva's Interpreter however will take a while to resolve this mess of code when the chaining gets absurdly long. But if you give it time it will work.

## <center>Keywords

Alot of the keywords refer to words from South African languages, so if you happen to know one, you've got the advantage
here's a cheat table though

| keyword   | meaning                      | use in jaiva                                                  | language origin       | reference                                   |
| --------- | ---------------------------- | ------------------------------------------------------------- | --------------------- | ------------------------------------------- |
| maak      | "make"                       | variable declaration keyword                                  | Afrikaans             | [Variables](#variables)                     |
| aowa      | "no"                         | `false`                                                       | Sepedi                | [Bools](#bools)                             |
| yebo      | "yes"                        | `true`                                                        | Zulu                  | [Bools](#bools)                             |
| if        | -                            | conditional execution keyword                                 | English               | [If Statements](#if-statements)             |
| mara      | "but"                        | keyword to start an `else` block                              | Sesotho               | [Mara (else)](#mara-else)                   |
| kwenza    | "does"                       | function definition keyword                                   | Zulu                  | [Functions](#functions)                     |
| khutla    | "return"                     | function return keyword                                       | Zulu                  | [Functions](#functions)                     |
| colonize  | -                            | loop constructs (for loop and for-each loop)                  | English               | [Colonize loops](#colonize-loops-for-loops) |
| nikhil    | -                            | while loop keyword                                            | it's a name, not sure | [Colonize loops](#colonize-loops-for-loops) |
| zama zama | "try"                        | try block                                                     | Zulu (informal?)      | [Errror Handling](#error-handling)          |
| cima      | "turn off"                   | keyword to throw an error                                     | Zulu                  | [Throw an error](#throw-an-error)           |
| chaai     | "oh no" (subjective meaning) | catch block                                                   | lowkey dont know      | [Error Handling](#error-handling)           |
| voetsek   | "fuck off"                   | `break` keyword                                               | Afrikaans             | [Control flow](#control-flow)               |
| nevermind | -                            | `continue` keyword                                            | English               | [Control flow](#control-flow)               |
| with      | -                            | keyword used to define for each loop along with colonize      | English               | [Colonize loops](#colonize-loops-for-loops) |
| tsea      | "take"                       | keyword to import symbols from another file                   | Sepedi                | [Tsea](#tsea-import-and-exporting-files)    |
| idk       | -                            | This is a special keyword, as it also acts as a value. (null) | English               | [idk](#idk)                                 |
| however   | -                            | (`else` keyword in a ternary)                                 | English               | [Ternary ifs](#ternary-ifs)                 |

## <center>Variables

Variables are [scoped](#scopes) constructs.

See [Globals](./Globals.md) for a list of global variables that are available to you.

### Definition

```jiv
maak (variable name) <- (value)!
```

Simple asf

```jiv
maak a <- 20!           @ number
maak var1 <- "string"!  @ string
maak var5 <- aowa!      @ boolean
maak f!                 @ define without a value.
```

Also a neat feature, since only a specifc set of chars are reserved, this allows for some weird variable names that is allowed.

| Statement                                 | Variable Name |
| ----------------------------------------- | ------------- |
| `maak a b <- 10!`                         | `a b`         |
| `maak a  b <- 100!` (diferent from above) | `a  b`        |
| `maak #b... <- 20!`                       | `#b...`       |
| `maak \ <- 10!`                           | `\`           |

And more crazy combos you can come up with. if it doesnt result in a generic Java error, it's probably valid. Go wild.

### Use

```jiv
maak a <- 20!
maak b <- 10!
maak c <- a + b! @ 30
```

### Reassignment

```jiv
maak a <- 20!

a <- 10!
a <- "string"!
```

You can reassign any type really. This shit aint type safe.

## <center> Arrays

**Arrays are 0-indexed. This isn't Lua afterall**

**Array literals do not exist. Sorry not sorry.**

```jiv
maak a <-| 20, 23, 56, 324, 354!
maak b <-|! @ Empty array.
```

Yknow. Then you can access elements of the array using the `[]` operator.

```jiv
maak a <-| 20, 23, 56, 324, 354!
maak b <- a[0]! @ 20
```

To get the length of an array use the `~` operator.

```jiv
maak a <-| 10, 23, 984!
khuluma(a~)! @ returns 3
```

## <center>Functions

Functions are [scoped](#scopes) constructs.

See [Globals](./Globals.md) for a list of global functions that are available to you.

### Defintion

Functions are defined using the `kwenza` keyword, and return values using the `khutla` keyword.

```jiv
kwenza addition(param1, param2) ->
    khutla (param1 + param2)!
<~
```

### Calling

Just like any other language, you can call a function by just using the name of the function and passing in the parameters.

```jiv
maak a <- 10!
maak b <- 20!
maak c <- addition(a, b)! @ 30
@ or
maak c <- addition(10, 20)! @ 30
```

Function parameters can take any type of variable, including arrays and other functions.

### Referencing

You can reference a function by excluding the parameter braces.

```jiv
khuluma(additon)! @ This will print to the console, the function's defintion. So "addition(param1, param2)"
```

And you can pass this reference as a value anywhere you want really

(See [Higher-Order Functions](#higher-order-functions) for passing function references in other functions)

```jiv
maak new <- addition!   @ This will mkae the variable new hold the reference to the function addition
khuluma(new(10, 10))!   @ Now new can be used as a function, and will act exactly like addition
khuluma(new)!           @ Printing new will give you the original function's declaration so "addition(param1, param2)"
```

And some more funky examples

```jiv
kwenza anotherFunc() ->
    khutla 10!
<-

maak t <- anotherFunc!  @ This assigns t, the reference to anotherFunc
anotherFunc <- khuluma! @ This reassigns anotherFunc as a reference to khuluma

khuluma(t)!             @ Print "anotherFunc()"
khuluma(t())!           @ Prints 10
khuluma(anotherFunc)    @ Prints "khuluma(msg, removeNewLn?)"
anotherFunc("hello")!   @ Prints "hello" (Since this is just khuluma.)
```

### Parameters

#### Higher-Order Functions

In the case wehere you want to pass a function into another function, you will have to input `F~` to the name of the parameter to explicity tell the interpreter that the shi u finna input should be treated as a function.

`V~` is available for variables, but its not needed as without this typing, its always assumed to be a variable

```jiv
kwenza function(F~ref) ->
    khutla ref()!
<~

kwenza returnNum() ->
    khutla 100!
<~

khuluma(function(returnNum))! @ prints 10
```

be careful when you start doing this though as when you start calling and chaining these, you loose variables in scopes since it doesnt know an shi.

#### Optional Arguments

In the case where you need certain arguments to be optional, stolen straight from typescript, all you gotta do is suffix the parameter name with `?` then it will not be required when calling the function.

```jiv
kwenza function(param?) ->
    khuluma(param = idk)! @ When a parameter is optional and it is not given,
                           @ It will be set to the type idk
<~

function()! @ prints true
function(10)! @ prints false
```

> [!WARNING]
> When a parameter is required, it CANNOT be set to [idk](#idk) or be empty in the parameters list of the function call. That's the whole point of a required parameter, that it is a value.

## <center>If Statements

For if statements, `if` is the keyword, and `mara` is the else statement.

### Basic If

```jiv
if (condition) ->
    @ block to execute.
<~
```

```jiv
maak variable <- 10!
if (variable != 100) ->
    khuluma("Variable isn't 100")!
<~
```

### mara (else)

```jiv
if (condition) ->
    @ block to execute
<~ mara ->
    @ block to execute if the condition is false
<~
```

```jiv
if (variable != 100) ->
    khuluma("Variable is not 100")!
<~ mara ->
    khuluma("It's 10")!
<~
```

> [!NOTE]
> Other languages allow you to put a variable as a the condition to check for truthiness. We don't allow that here, check it against [true](#bools) or [idk](#idk)

> [!WARNING]
> You cannot put the mara keyword underneath the end of an if block, it HAS to be on the same line as the closing `<~` symbol. This makes it easier for me lol.

### mara (else) with if

```jiv
if (condition) ->
    @ block to execute
<~ mara if (condition) ->
    @ another block.
<~
```

```jiv
maak variable <- 10!
if (variable != 100) ->
    khuluma("Variable is not 100")!
<~ mara if (variable = 10) ->
    khuluma("Variable is 10")!
<~
```

You can chain as many mara if statements as you want, but this is not recommended. (It makes the code hard to read, and this language is already hard enough to read)

You can also nest if statements. because why not.

> [!WARNING]
> You can use a normal `mara` block along with other `mara if` blocks. However the `mara` block must be the LAST block of the entire if chain.

### Ternary Ifs

This is your shorthand one line if statement that returns a value too.

This is the syntax

```jiv
(condition) => (expression2) however (expression1)
```

**(With or without braces)**

(Yes including the `however` keyword)

So you can do things like

```jiv
maak control!
@* braces are added to make it more readable, you don't need them
maak a <- ((control > 10) => 10 however control)!
```

Where if the variable `control` is bigger than `10`, the variable `a` will be set to `10`. Capping it to a maximum of `10` and below.

And you can chain them

```jiv
maak input <- mamela("enter a number")! @ Waits for number input fom the console.

kwenza clamp(number, min, max) ->
    khutla (number > max => max however (number < min => min however number))!
<~

khuluma(clamp(input, 0, 50))!
```

Here we define a function `clamp` which will... clamp, what'd you think, the given number to the given minimum value and maximum value.

> [!WARNING]
> You cant have ternaries on new lines, they hve to be in the sam eline if you chain them unfortunately

## <center>Loops

### Nikhil loops (while loops)

Shoutout to Nikhil for this one.

[Twitter](https://x.com/NikhilLala11)
[Instagram](https://www.instagram.com/_nikhil1707_/) <br>
YouTube @nikhil17 (probably idk) <br>
Discord @Nikx17 <br>

```jiv
nikhil (condition) ->
    @ block to execute
<~
```

Where

> `condition` is the condition to check for the loop to continue. This is a boolean expression.

### Colonize loops (for loops)

This ones a bit weird.

```jiv
colonize (variable init) | (condition) | (increment) ->
    @ block to execute
<~
```

Where

> `variable init` is the variable to use in the for loop, and the initial value.

> `condition` is the condition to check for the loop to continue. This is a boolean expression.

> `increment` is the increment to use for the loop. This can be a `+` or a `-` sign.

> and the `|` is the separator between the three parts of the for loop.

```jiv
colonize i <- 0 | i <= 10 | + ->
    khuluma(i)! @ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10
<~
```

#### colonize with (for each)

```jiv
colonize (variable name) with (array) ->
    @ block
<~
```

Where

> `variable name` is the variable to use in the for loop, and the initial value.

> `array` is the array to loop through.

```jiv
colonize word with reservedKeywords ->
    khuluma(word)! @ prints all the reserved keywords
<~
```

### Control flow

For all loops, to forcefully exit a loop, use the `voetsek` keyword.

```jiv
nikhil (a = 10) ->
    khuluma(a)! @ 10
    voetsek! @ this will break out of the loop, stopping it.
<~
```

To skip to the next iteration of a loop, use the `nevermind` keyword.

```jiv
colonize i <- 0 | i <= 10 | + ->
    if (i = 5) ->
        nevermind! @ this will skip the rest of the loop and go to the next iteration.
    <~
    khuluma(i)! @ 0, 1, 2, 3, 4, 6, 7, 8, 9, 10
<~
```

## <center>Error handling

Jaiva has error handling and throwing! This is done using 2 constructs.

### Throw an error

Use the `cima` keyword follwed by the `<==` operator, then followed by the error message.

`<==` is the assignment operator for errors. Literally not used anywhere else but for this.

```jiv
cima <== (error message)!
```

```jiv
maak a <- 10!
if (a = 10) ->
    khuluma("a is 10")!
<~ mara ->
    cima <== "a is not 10"! @ this will throw an error.
<~
```

### zama zama (try) chaai (catch) block

The `zama zama` keyword (both, one `zama` won't work.) is used to define a try block, and the `chaai` keyword is used to define a catch block.

The `chaai` block is executed if an error is thrown in the `zama zama` block.

> [!NOTE]
> When in a chaai block, you have access to a special variable called

> [!WARNING]
> You cannot have a `zama zama` block without a `chaai` block. This is a design choice, and it makes sense.

> [!WARNING]
> You cannot put the `chaai` keyword underneath the end of a `zama zama` block, it HAS to be on the same line as the closing `<~` symbol. This makes it easier for me lol.

```jiv
zama zama ->
    @ block of code to try
<~ chaai ->
    @ block of code to execute if an error is thrown
<~
```

```jiv
zama zama ->
    khuluma(true + 1)!
<~ chaai ->
    khuluma(error)!
<~
```

#### Error variable

When in a `chaai` block, Jaiva gives you access to a variable named `"error"` which holds the error that ocurred in string form however if there already is a variable named error, it will increment the new error variable by 1.

```jiv
zama zama ->
    khuluma(idk - 1)! @ throws error
<~ chaai ->
    zama zama ->
        khuluma("an error bruh" - idk)! @ another error
    <~ chaai ->
        khuluma(error)!     @ This is the (idk - 1) error
        khuluma(error1)!    @ This is this context's error (So the ("an error bruh" - idk) error)
    <~
<~
```

> [!NOTE]
> The way the code works, is it looks in it's current context for any variable which contains the exact word "error", so if you have like 4 variables named error, and you decide to open a chaai block, the name of the error variable will be "error4" (it counts the amount of current variables named error, and adds it to the variable name)
> Just keep this in mind
> This is defined in [Interpreter.java on line 499](/src/main/java/com/jaiva/interpreter/Interpreter.java)

## <center>Scopes

Everytime you open a block, you create a new scope. This means that any variables or functions you define in that block are not accessible outside of that block.

```jiv
maak a <- 10!
maak b <- 20!
maak c <- 30!

if (a = 10) ->
    maak d <- 40!
    khuluma(d)! @ 40
    kwenza addition(param1, param2) ->
        khuluma(param1 + param2)!
    <~
<~ mara ->
    khuluma(d)! @ This will error, because d isn't defined in this scope.
    khuluma(a)! @ 10. This works because a is defined in the global scope.
<~

addition(10, 20)! @ Will also error, because addition() is not defined in this scope.
```

## <center>Tsea (import) and exporting files

Importing isn't that bad. Here's a simple import.

Say you have "`file.jiv`" with the following code.

```jiv
maak *a <- 10! @ Pre-pend * to the variable name to mark it as a exported variable.
kwenza *addition(param1, param2) -> @ Pre-pend * to the function name to mark it as a exported function.
    khuluma(param1 + param2)!
<~

@ Note: Using the variables/functions are still normal. When using them you don't have to prepend the * to them. It's just simply a marker for the tokenizer to know that this variable/function is exported.
```

Then in your main file, you can import it like this.

```jiv
tsea "file.jiv"! @ or you can have an absolute import.
khuluma(addition(a, 1))! @ 11
```

Or if you want to import a specific variable or function, you can do this.

```jiv
tsea "file.jiv" <- addition!
```

> [!NOTE]
> File paths are relative to the current working directory. So if you have a file in a different directory, you will have to use the full path to the file.

> [!NOTE]
> You can also import files from the Jaiva library. Example to import the arrays files
>
> ```jiv
> tsea "jaiva/arrays"!
> ```
>
> See [Globals](./Globals.md) for a list of global variables and functions that are available to you, and others that you can import.

> [!NOTE]
> Omitting the file extension is okay, however it will default to `.jiv`
