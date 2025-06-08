# `convert`

This library contains functions for converting stuff to different types.

See [Conversions.java](./src/main/java/com/jaiva/interpreter/globals/Conversions.java) where this is defined.

## `c_numToString(num) -> `_*`khutla "string"!`*_

\*_**Independent**_

Returns a string which represents the given number

```jiv
tsea "jaiva/convert"!
khuluma(c_numToString(2.0) + 10)! @ Will print "2.010" as that's string concat
```

## `c_stringToNum(string) -> `_*`khutla number!`*_

\*_**Independent**_

Returns a number which represents the given string

```jiv
tsea "jaiva/convert"!
khuluma(stringToNum("2.4") + 10)! @ Will print 12.4
```
