# `debug`
 
Debug stuff. You dont need these
 
## Symbols
 
### `F~d_emit(<-arr) -> `_**`idk`**_
 
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
