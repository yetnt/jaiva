# threads (Library)

_Enables threading via function scopes executing on their own thread. Don't ask me about 2 threads mutating the same variable... That's ur problem bro._
## Exports
This library exports the following libraries: 
## Table of Contents
| Alias | Link |
| --- | --- |
| thread | [thread](#thread) |
## Functions

### thread

This symbol can be reached by the following aliases: _`thread`_

_**Executes the given function in it's own separate thread. If the function aught to fail for some reason, a string and the error message is passed into a secondary cleanup function which is ran on the original thread.**_


#### Definition

```jaiva
F~thread(F~f, F~f2?)
```

- **_f_**  **`<-`** _**F~any**_
	 - _The function to execute in it's own thread. A function that should take no args._
- **_f2_** **`?`** **`<-`** _**F~any**_
	 - _The function to execute as cleanup. This function is expected to take 2 string arguments_

_**Returns:**_ **The thread Id (Long). Idk what you'd do with this**

Since Version: _6.1.0_


#### Example: 

```jaiva
tsea "jaiva/threads"!

kwenza f1() ->
    khuluma("HI")!
    sleep(3000)! @ Sleep for 3 seconds
    khuluma("Damn.")!
<~

kwenza clean(type, err) ->
    if (err~ > 0) ->
        khuluma("An error occured executing f1!")!
        khuluma(err)!
    <~
<~

thread(f1, clean)!
khuluma("OKAY")!
sleep(2000)!
khuluma("WOW")!

{
Output should be:
"OKAY"
"HI"
"WOW"
"Damn."
}
```


---

## Variables
