# Jaiva!

This esolang of mine is still in development, so expect alot of updates.

An esolang of mine which is way better, here is how you do stuff in my lanuage

To make a jaiva file. Create a text file with the _`.jiv`_ or _`.jaiva`_ or _`.jva`_ extension.

## Syntax

All lines end in an exclamation mark! Because why arent you screaming code.

Code blocks start with -> and end in <~.

No types. Fuck types.

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
kuluma("hello")!
```

This logs to ur console fr fr.

### if Statements

```jiv
if (variable != 100) ->
    kuluma("Variable is not 100")!
<~ mara ->
    kuluma("It's 10")!
<~
```

Simple nothing crazy, until

### For loops

```jiv
colonize i <- 0 | i <= 10 | + ->
    kuluma(addition(arrayVar[i], 10))!
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
    kuluma("error msg" + chaai)!
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
