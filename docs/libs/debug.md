# `debug`
 
The debug librayr. This library has nothing to do with the CLI debugger just some random functions in this
 
## Symbols
 
### `F~d_emit(<-arr?) -> `_**`idk`**_
 
Throws a DebugException to be caught by a Java test class and emits the given variables
 
- _`arr?`_ <- `"[]"` : _The array of values to pass to the exception_
 
Returns :
> _**Physically can't return. As it always throws an error**_
 
> [!NOTE]
> _If you aren't familiar with Java, the language Jaiva is developed in. This will essentially forcefully stop the execution of the interpreter, with the intent for said error to be caught and dealt with by another Java class. This serves 0 purpose if  you're just running a Jaiva file._
 
### `F~d_vfs() -> `_**`[]`**_
 
Returns the current context's vfs.
 
Returns :
> _**A 2d array, first array containing the keys, second array the values.**_
 
> [!NOTE]
> _This function does not allow you to edit the current vfs, only to get everything that is currently within the vfs as an array. Everytime this function is called a new array containing all the stuff is made._
 
### `F~d_link(a, b) -> `_**`idk`**_
 
Links the MapValue instance of 'a' into 'b' such that they hold the same value and if one is edited the other will also have that edit.
 
- **`a`** <- `"idk"` : _The symbol which holds the MapValue to be linked._
- **`b`** <- `"idk"` : _The symbol who's MapValue will either be created or overwritten_
 
Returns :
> _**idk**_
 
**Example:**
```jaiva
maak a <- f~() : 10! @ Lambda that returns 10
maak b <- true! @ boolean value true

@ With normal reassignment syntax, setting b to a then changing b does not update a
b <- a!
b <- 10!
khuluma(a)! @ prints the lambda signature and not 10.

@ With d_link, the exact MapValue held by that alias is copied.
d_link(a, b)!
b <- 10!
khuluma(a)! @ prints 10
a <- 100!
khuluma(b)! @ prints 100
```
 
> [!NOTE]
> _A usual (b <- a) syntax would suffice if you'd like to copy the value of a into b.
However when a is changed, b will stay the value you set earlier. This function fixes that where it will
link the exact MapValue from a into b, discarding b's old MapValue. such that editing any one of the symbols
via the reassignment syntax will update the linked variable.

In the case that the "b" parameter does not actually exist in the symbol table, d_link will try to make it, itself.
_
 
### `F~d_getScope() -> `_**`string`**_
 
gets the scope. wahtd you expect
 
Returns :
> _**the scope string**_
