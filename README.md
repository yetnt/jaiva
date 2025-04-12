# <center> Jaiva!

This esolang of mine is still in development, so expect alot of updates.

Expect : Confusion, Regret, and a bit of fun.

Expect not : A good time, a good language, or a good experience.

To setup, see [Install.md](./Install.md)
To run, see [CLI.md](./CLI.md)

## <center>Index

-   **_[Jaiva!](#jaiva)_**
    -   **[Index](#index)** <- you are here.
    -   **[Prerquisuiesdsfb](#prerquisuiesdsfb)**
    -   **[Syntax](#syntax)**
        -   _[Comments.](#comments)_
        -   _[Assignment operators](#assignment-operators)_
        -   _[Primitives](#primitives)_
            -   [Number](#number)
            -   [Bools](#bools)
            -   [Strings](#strings)
                -   [Escaping characters](#escaping-characters)
            -   [idk](#idk)
        -   _[Arithmatic and Boolean Comparisons](#arithmatic-and-boolean-comparisons)_
        -   _[Blocks](#blocks)_
    -   **[Keywords](#keywords)**
    -   **[Variables](#variables)**
        -   _[Definition](#definition)_
        -   _[Use](#use)_
        -   _[Reassignment](#reassignment)_
        -   _[Arrays](#arrays)_
    -   **[Functions](#functions)**
    -   **[If Statements](#if-statements)**
        -   _[Basic If](#basic-if)_
        -   _[mara (else)](#mara-else)_
        -   _[mara (else) with if](#mara-else-with-if)_
    -   **[Loops](#loops)**
        -   _[Nikhil loops (while loops)](#nikhil-loops-while-loops)_
        -   _[Colonize loops (for loops)](#colonize-loops-for-loops)_
            -   [colonize with (for each)](#colonize-with-for-each)
        -   _[Control flow](#control-flow)_
    -   **[Error handling](#error-handling)**
        -   _[Throw an error](#throw-an-error)_
        -   _[zama zama (try) chaai (catch) block](#zama-zama-try-chaai-catch-block)_
    -   **[Scopes](#scopes)**
    -   **[Globals](#globals)**

## <center>Prerquisuiesdsfb

1. **Get and Install Java 21**
   yeah this isn't a good start 💀 but this shit is amde in java so ye man.
   [Download and install the latest JDK 21](https://jdk.java.net/21)

2. **Set Up Jaiva as a Global Command**  
   Follow the instructions in [Install.md](./Install.md) to configure Jaiva so you can run the `jaiva` command from anywhere on your system.

3. **CLI**
   The basic command to run your file is `jaiva <filePath>` but [CLI.md](./CLI.md) exists.

## <center>Syntax

It's simple really

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

All lines (unless block open and close) must end in an exclamation mark. because why aren't you screaming your code.

```jaiva
maak a <- 10!
```

### Assignment operators

`<-` Is the basic assignment operator in this language. Any assignment you do is with this.

> [!NOTE]
> In the case of defining an array, use `<-|`

### Primitives

#### Number

This is just any number (well at the moment integers, doubles seem to not work lmao)

They don't need any special format, you can just type the number out to use it

```
maak b <- 10!
```

#### Bools

`true` and `false` are supported, but so are `aowa` (maps to false) and `yebo` (maps to true). because come on now this is just cool.

They don't need any special format, you can just type the bool out to use it

```
maak b <- aowa!
```

#### Strings

Strings, stolen striaght from Java use `"` (double quotes) to start and end a string.

These are the only characters for strings. No string literals or multi line strings because wtf dawg.

```
maak b <- "String"!
```

##### Escaping characters

To escape characters in a string use the `$` symbol.

```
maak b <- "String$n"!
```

Table of escape characters

| character                       | escape sequence |
| ------------------------------- | --------------- |
| `=`                             | `$=`            |
| `,`                             | `$,`            |
| `!`                             | `$!`            |
| ` ` (yes you can escape space.) | `$s`            |
| `@`                             | `$@`            |
| `\n` (new line)                 | `$n`            |
| `\t` (tab)                      | `$t`            |
| `\r` (carriage return)          | `$r`            |
| `\b` (backspace)                | `$b`            |
| `\f` (form feed)                | `$f`            |
| `` ` `` (backtick)              | `` $` ``        |
| `"` (double qoutes)             | `$"`            |
| `$`                             | `$$`            |

#### idk

This is a primitive which is used to represent nothingness.

> [!WARNING]
> This hasn't been implemented AT ALL lmao.

> [!NOTE]
> This is like null in Java, but not exactly. It is a primitive type in this language. Think of it like undefined in JavaScript.

```jaiva
maak a <- idk!
```

### Arithmatic and Boolean Comparisons

| operation                       | operator |
| ------------------------------- | -------- |
| modulu                          | `%`      |
| division                        | `/`      |
| multiplication                  | `*`      |
| addition                        | `+`      |
| subtraction                     | `-`      |
| unary minus                     | `-`      |
| is equal to (not double equals) | `=`      |
| is not equal to                 | `!=`     |
| greater than (and equal to)     | `>` `>=` |
| less than (and equal to)        | `<` `<=` |
| braces for ordering             | `(` `)`  |

> [!NOTE]
> You cannot negate an expression. Sorry not sorry.

> [!WARNING]
> Due to my bad code, the unary minus sometimes works and sometimes doesn't. just multiply by -1, since that always works.

> [!WARNING]
> You can't compare strings. Not waisting my time on that one.

### Blocks

Blocks are defined by the `->` and `<~` symbols. The `->` symbol opens a block, and the `<~` symbol closes it.

I think this is where jaiva deviates from normal programming languages, Especially since your usual `{` `}` is reserved for comments.

> [!NOTE]
> You can have multiple blocks in a single line, but this is not recommended. It makes the code hard to read. (And i dont think i implemented or tested that case.)

> [!NOTE]
> You can only have a block after like, an if, or a function and whatever, you cant just open an new arbitrary block in the middle of your code. This is a design choice, and i think it makes sense.

```jiv
if (a = 10) ->
   @ iunner code
<~
```

## <center>Keywords

Alot of the keywords refer to words from south african languages, so if you happen to know one, you've got the advantage
here's a cheat table though

| keyword   | meaning and what it's assigned to                        | language origin |
| --------- | -------------------------------------------------------- | --------------- |
| maak      | make/new (variable declaration keyword)                  | Afrikaans       |
| aowa      | no (`false`)                                             | Sepedi          |
| yebo      | yes (`true`)                                             | Zulu            |
| khuluma   | talk (print to console)                                  | Zulu            |
| mara      | but (`else` block)                                       | Sesotho         |
| kwenza    | does (defines a function)                                | Zulu            |
| khutla    | return (function return keyword)                         | Zulu            |
| colonize  | for (for loop)                                           | English         |
| zama zama | try (try block)                                          | Zulu            |
| cima      | turn off (throw)                                         | Zulu            |
| chaai     | "oh no!" or "oh shit!" (catch block)                     | idk             |
| voetsek   | "fuck off" (`break` keyword)                             | Afrikaans       |
| nevermind | Self-Explanatory (`continue` keyword)                    | English         |
| with      | keyword used to define for each loop along with colonize | English         |

## <center>Variables

Variables are [scoped](#scopes) constructs.

See [Globals](#globals) for a list of global variables that are available to you.

### Definition

```jiv
maak (variable name) <- (value)!
```

Simple asf

```jiv
maak a <- 20!
maak var1 <- "string"!
maak var5 <- aowa!
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

### Arrays

**Arrays are 0-indexed. This isn't Lua afterall**

```jiv
maak a <-| 20, 23, 56, 324, 354!
```

Yknow. Then you can access elements of the array using the `[]` operator.

```jiv
maak a <-| 20, 23, 56, 324, 354!
maak b <- a[0]! @ 20
```

## <center>Functions

Functions are [scoped](#scopes) constructs.

See [Globals](#globals) for a list of global functions that are available to you.

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

For all loops, to forcefully exit a loop, use the `voetsek` keyword. This is the break keyword.

```jiv
nikhil (a = 10) ->
    khuluma(a)! @ 10
    voetsek! @ this will break the loop.
<~
```

To skip to the next iteration of a loop, use the `nevermind` keyword. This is the continue keyword.

```jiv
colonize i <- 0 | i <= 10 | + ->
    khuluma(i)! @ 0, 1, 2, 3, 4, 5
    if (i = 5) ->
        nevermind! @ this will skip the rest of the loop and go to the next iteration.
    <~
<~
```

## <center>Error handling

Jaiva has some sort of error handling. This is done using 2 constructs.

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
<~>
```

### zama zama (try) chaai (catch) block

The `zama zama` keyword (both, one `zama` won't work.) is used to define a try block, and the `chaai` keyword is used to define a catch block.

The `chaai` block is executed if an error is thrown in the `zama zama` block.

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
    @ block of code to try
<~ chaai ->
    khuluma("error msg" + chaai)!
<~
```

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

## <center>Globals

This is a list of global variables and functions that are accessible in any scope. Some (if not all) cannot be modified however.

| variable/function name | type     | description                                                  |
| ---------------------- | -------- | ------------------------------------------------------------ |
| `khuluma(param)`       | function | prints to console                                            |
| `getVarClass(var)`     | function | gets the Java class representation of the specified variable |
| `reservedKeywords`     | array    | an array of all the reserved keywords in the language.       |
| `version`              | string   | the version of jaiva you are using.                          |
