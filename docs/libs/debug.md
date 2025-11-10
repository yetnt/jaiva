# `debug`

This library contains functions for.. debugging, what'd ya think??

See [Debug.java](../../src/main/java/com/jaiva/interpreter/libs/Debug.java) where this is defined.

## `d_emit(arr) -> `_*`khutla idk!`*_

This function, will forcefully throw a Java Error, [DebugException](../../src/main/java/com/jaiva/errors/JaivaException.java), such that an outer Java testing suite can catch this error and report it with all the information this gives.

This function cna only be called once per file, as it will stop execution of said file at that exact moment it's called.

For your standard Jaiva delevoper this isn't helpful as it serves no purpose and only throws an error. This is meant to be for Jaiva source code developers anyways who need to test whether or not the interpreter is working with varia
ables properly

So lets say you have the following Jaiva file

```jiv
tsea "jaiva/debug"!
d_emit(version)!
```

If you have some Java class, which is testing for this:

```java
class Example {
    @Test
    void method() {
        try {
            // Call the interpreter here
        } catch (DebugException e) {
            System.out.println(e.components); // prints the arraylist given to the function when it was called
            System.out.println(e.lineNumber); // the line number of the d_emit function in this file
            System.out.println(e.vfs); // vfs hashmap at the moment of execution.
            System.out.println(e.config); // Interpreter Configuration (IConfig)
            System.out.println(e.error); // This property only gets a value if the d_emit encoutnered an error while trying to parse th einput given to it. Otherwise this will be null.
        }
    }
}
```
