# `types`

This library contains functions for converting stuff to different types.

See [Types.java](../src/main/java/com/jaiva/interpreter/globals/Types.java) where this is defined.

## `t_str(input?, radix?) -> `_*`khutla "string"!`*_

\*_**Independent**_

Attempts to convert the `input` of any given type into it's string form. With an optional `radix` parameter for integers

```jiv
tsea "jaiva/types"!

maak b <- 100!

khuluma(t_str(idk))!        @ "idk"
khuluma(t_str(false))!      @ "aowa"
khuluma(t_str(-10, 16))!    @ "0xFFFFFFF6"
khuluma(t_str(khuluma))!    @ "khuluma(msg, removeNewLn?)"
khuluma(t_str(b))!          @ "100"
```

If an integer with a radix of `2`, `8` or `16` is given, the function will append it's corresponding prefix, otherwise it just returns the string form.

```jiv
tsea "jaiva/types"

khuluma(t_str(10, 16))!     @ "0xA"
khuluma(t_str(10, 2))!      @ "0b1010"
khuluma(t_str(10, 8))!      @ "0c12"
khuluma(t_str(4000, 4))     @ "332200"
```

## `t_num(string, radix?) -> `_*`khutla number!`*_

\*_**Independent**_

Attempts to convert a given `string` to number, with an optional `radix` input for integers.

```jiv
tsea "jaiva/types"!

khuluma(t_num("10.3"))          @ 10.3
khuluma(t_num("A", 16))!        @ 10
khuluma(t_num("0xA"))!          @ 10
khuluma(t_num("0b1001010"))!    @ 74
khuluma(t_num("1010", 2))!      @ 10
khuluma(t_num("23123", 21))!    @ 417231
```

If both a prefixed integer and radix input is given, the `prefix` is used over the `radix`, effectively ignoring it

```jiv
khuluma(t_num("0b1010"))        @ 10
khuluma(t_num("0b1010", 12))!   @ Still outputs 10, because it's prefixed with 0b, which is base 2
```