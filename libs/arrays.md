# `arrays`

This library contains functions for manipulating arrays.

If you ask why not just make arrays mutable, Jaiva is a functional programming language. I'm doing you a favour by already having these implemented. I could've not had arrays at all. (TLDR; I'm too lazy to implement an array index reassignment token or adjust my old tokens for that.)

I can't link you anywhere, if you want to see the file go to your jaiva folder, because this is where it is mann.

> [!WARNING]
> Some functions use other functions. It's best to just import the whole library instead of trying to import only the functions you need. (e.g. If you import only `a_pop` you will get an error because `a_pop` uses `a_remove` internally.). I will however, list the functions that depend on other functions.

## `a_remove(arr, n) -> `_*`khutla (array)!`*_

\*_**Independent**_

Returns an array which contains the original array (`arr`) with `n` amount of elements removed from the end.

```jiv
tsea "jaiva/arrays"! @ Import the arrays library
maak array <-| 1, 2, 3, 4, 5, 6, 7, 8, 9, 10!
maak newArray <- a_remove(array, 5)! @ Removes 5 elements from the end of the array.
khuluma(newArray)! @ Prints [1, 2, 3, 4, 5]
```

## `a_unshift(arr, n) -> `_*`khutla (array)!`*_

\*_**Independent**_

Returns an array which contains the original array (`arr`) with `n` amount of elements removed from the start.

```jiv
tsea "jaiva/arrays"! @ Import the arrays library
maak array <-| 1, 2, 3, 4, 5, 6, 7, 8, 9, 10!
maak newArray <- a_unshift(array, 5)! @ Removes 5 elements from the start of the array.
khuluma(newArray)! @ Prints [6, 7, 8, 9, 10]
```

## `a_pop(arr) -> `_*`khutla (array)!`*_

\*_**Depends on** [`a_remove()`](#a_removearr-n---khutla-array)_

Returns a new array without the last element of the original array (`arr`).

This is the same as `a_remove(arr, 1)` (It's actually exactly what the function's defintion is) So you can use either one.

```jiv
tsea "jaiva/arrays"! @ Import the arrays library
maak array <-| 1, 2, 3, 4, 5, 6, 7, 8, 9, 10!
maak newArray <- a_pop(array)!
khuluma(newArray)! @ Prints [2, 3, 4, 5, 6, 7, 8, 9, 10]
```

## `a_push(arr, element) -> `_*`khutla (array)!`*_

\*_**Independent**_

Returns a new array with the `element` appended at the end of the original array (`arr`).

```jiv
tsea "jaiva/arrays"! @ Import the arrays library
maak array <-| 1, 2, 3, 4, 5, 6, 7, 8, 9, 10!
maak newArray <- a_push(array, 11)! @ Appends 11 to the end of the array.
khuluma(newArray)! @ Prints [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
```

## `a_shift(arr, element) -> `_*`khutla (array)!`*_

\*_**Independent**_

Returns a new array with the `element` appended at the start of the original array (`arr`) and shifts all other elements 1 up.

```jiv
tsea "jaiva/arrays"! @ Import the arrays library
maak array <-| 1, 2, 3, 4, 5, 6, 7, 8, 9, 10!
maak newArray <- a_shift(array, 0)! @ Appends 0 to the start of the array.
khuluma(newArray)! @ Prints [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
```

## `a_filter(arr, predicate) -> `_*`khutla (array)!`*_

\*_**Independent**_

Filters through elements in the given array and return an array whose elements satisfy the `predicate` function.

The `predicate` function takes a single argument and returns a boolean value. If the value is true, the element is included in the new array. If false, it is not included.

```jiv
tsea "jaiva/arrays"! @ Import the arrays library
maak array <-| 1, 2, 3, 4, 5, 6, 7, 8, 9, 10!

kwenza isEven(n) ->
    khutla n % 2 = 0! @ Returns true if n is even
<~

maak newArray <- a_filter(array, isEven)! @ Filters the array with the isEven function.
khuluma(newArray)! @ Prints [2, 4, 6, 8, 10]!
```
