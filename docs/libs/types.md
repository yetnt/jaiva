# `types`
 
Stuff for handling with types.
 
## Symbols
 
### `F~t_num(string, radix?) -> `_**`number`**_
 
Converts a given **string** to a number with an optional **radix**.
 
- **`string`** <- `"string"` : _The input to convert to a number_
- _`radix?`_ <- `"number"` : _An optional radix to convert to_
 
Returns :
> _**A number. (Either a double or integer)**_
 
**Example:**
```jaiva
khuluma(t_num("0b1010"))! @ prints 10
khuluma(t_num("FF", 16))! @ prints 255
khuluma(t_num("3.14"))! @ prints 3.14
khuluma(t_num("0x1A"))! @ prints 26
```
 
> [!NOTE]
> _Jaiva integer prefixes [such as _0x_ or _0b_], are checked for first before the radix._
 
### `F~t_str(input?, radix?) -> `_**`string`**_
 
Converts any input of any given type to a string.
 
- _`input?`_ <- `"idk"` : _The input to convert_
- _`radix?`_ <- `"number"` : _A given radix for integers_
 
Returns :
> _**A string representation of the given input**_
 
**Example:**
```jaiva
khuluma(t_str(255))! @ prints "255"
khuluma(t_str(true))! @ prints "yebo"
khuluma(t_str(yebo))! @ prints "yebo"
khuluma(t_str(10, 2))! @ prints "0b1010"
```
 
> [!NOTE]
> _If given an input of `idk`, it will return `idk`. Not a string._
