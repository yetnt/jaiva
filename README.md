# Jaiva!

This esolang of mine is still in development, so expect alot of updates.

An esolang of mine which is way better, here is how you do stuff in my lanuage

## Prerquisuiesdsfb

Step 1: Get Java 21. yeah this isn't a good start 💀

(TODO : Rest of the steps.)

Step X : Run

```sh
> (command) filePath.jiv
```

And get your desired output.

To make a jaiva file. Create a text file with the _`.jiv`_ or _`.jaiva`_ or _`.jva`_ extension.

## CLI

Simple CLI Ngl.

```sh
> (command) [-p]
> (command) <filePath> [-t | -s]
```

### REPL

1. (No flags) launches basic interactive REPL.

    ```sh
    > (command)
    ```

2. Print tokens (more for debugging purposes)
    ```sh
    > (command) -p
    > (command) --print-tokens
    ```

### Read File

This command will attempt to find `<filePath>` and execute the jaiva code.

1. Read file, (No flags.)
    ```sh
    > (command) <filePath>
    ```
2. Returns tokens as strings.
    ```sh
    > (command) <filePath> -s
    > (command) <filePath> --string
    ```
3. Return tokens in JSON format.
    ```sh
    > (command) <filePath> -j
    > (command) <filePath> --json
    ```

## Syntax

All lines end in an exclamation mark! Because why arent you screaming code.

Code blocks start with -> and end in <~.

No types. Fuck types.

## Keywords

Alot of the keywords refer to words from south african languages, so if you happen to know one, you've got the advantage
here's a cheat table though

| keyword   | meaning                                 | language origin |
| --------- | --------------------------------------- | --------------- |
| maak      | make/new (variable declaration keyword) | Afrikaans       |
| aowa      | no (`false`)                            | Sepedi          |
| yebo      | yes (`true`)                            | Zulu            |
| khuluma   | talk (print to console)                 | Zulu            |
| mara      | but (`else` block)                      | Sesotho         |
| kwenza    | does (defines a function)               | Zulu            |
| khutla    | return (function return keyword)        | Zulu            |
| colonize  | for (for loop)                          | English         |
| zama zama | try (try block)                         | Zulu            |
| cima      | turn off (throw)                        | Zulu            |
| chaai     | "oh no!" or "oh shit!" (catch block)    | idk             |
| voetsek   | "fuck off" (`break` keyword)            | idk             |
| nevermind | Self-Explanatory (`continue` keyword)   | English         |

### Variables

```jiv
maak a <- 20!
maak var1 <- "string"!
maak var5 <- true!
```

For boolean values you can also use `aowa` and `yebo`, figure out yourselves which one is true and false

#### Arrays

```jiv
maak a <- 20, 23, 56, 324, 354!
```

This piece of code doesnt work.

### Comments

```jiv
@ single line comment
{
    multi line comment
}
```

### Logging

```jiv
khuluma("hello")!
```

This logs to ur console fr fr.

### if Statements

```jiv
if (variable != 100) ->
    khuluma("Variable is not 100")!
<~ mara ->
    khuluma("It's 10")!
<~
```

Simple nothing crazy, until

### For loops

```jiv
colonize i <- 0 | i <= 10 | + ->
    khuluma(addition(arrayVar[i], 10))!
<~
```

### Function Decleraion

```jiv
kwenza addition(param1, param2) ->
    khutla (param1 + param2)!
<~
```

### Throw-error

```jiv
cima <== "This is an error"!
```

### Try-catch block

```jiv
zama zama ->
    @ block of code to try
<~ chaai ->
    khuluma("error msg" + chaai)!
<~
```

## Language specific features.

### Effeciency

To make your code 20% more efficient, begin your file with

```jiv
@# (My Real life name, replace these brackets with these)
```

why? well-

### Parallel For Loops

```jiv
colonize i, j <- 0 | i >= 10, j <= -10 | +- ->
    left ->
    @ some code that uses the variable i
    @ i will be incremented (the + sign)
    <~
    right ->
    @ some code that uses the variable j
    @ j will be decremented (the - sign)
    <~
<~
```

### Politeness

Sometimes the intepreter may demand you end your file with the following

```jiv
Thank you.
```

or else it will be angry at you.
