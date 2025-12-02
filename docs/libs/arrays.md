# `arrays`
 
Arrays!
 
## Symbols
 
### `F~a_reverse(arr) -> `_**`[]`**_
 
 Reverses the order of elements in an array and returns a new array
 
- **`arr`** <- `"[]"` : _The original array_
 
Returns :
> _**A new array with the elements in reverse order**_
 
**Example:**
```jaiva
maak arr <-| 1, 2, 3, 4, 5!
khuluma(a_reverse(arr))! @ Returns [5, 4, 3, 2, 1]
```
 
> [!NOTE]
> _This uses a colonize loop to iterate through the array backwards and constructs a new array with the elements in reverse order. Throws if the input is not an array._
 
### `F~a_reduce(arr, F~reducer, initial?) -> `_**`idk`**_
 
 Reduces an array to a single value using a reducer function
 
- **`arr`** <- `"[]"` : _The original array_
- **`reducer`** <- `"F~any"` : _A function that takes an accumulator and an_
- _`initial?`_ <- `"any"` : _An optional initial value for the accumulator_
 
Returns :
> _**The final value of the accumulator after processing all elements**_
 
**Example:**
```jaiva
maak arr <-| 1, 2, 3, 4!
maak sumReducer <- f~(acc, el) : acc + el! @ A lambda function to sum two numbers
khuluma(a_reduce(arr, sumReducer, 0))! @ Returns 10
khuluma(a_reduce(arr, sumReducer))! @ Returns 10
```
 
> [!NOTE]
> _The reducer function must take in 2 parameters, the accumulator and the current element, and return the new value of the accumulator. If no initial value is provided, the first element of the array is used as the initial value and the reduction starts from the second element Throws if the first argument is not an array or if the second argument is not a function._
 
### `F~a_push(arr, element) -> `_**`[]`**_
 
 Appends an element to the end of an array and returns the new array.
 
- **`arr`** <- `"[]"` : _The original array_
- **`element`** <- `"any"` : _Element to add_
 
Returns :
> _**A new array with the element appended at the end of the array.**_
 
**Example:**
```jaiva
maak arr <-| 1, 2, 3!
khuluma(a_push(arr, 4))! @ Returns [1, 2, 3, 4]
```
 
> [!NOTE]
> _Throws if the first argument is not an array._
 
### `F~a_unshift(arr, element) -> `_**`[]`**_
 
 Appends an element to the start of an array and returns the new array.
 
- **`arr`** <- `"[]"` : _The original array_
- **`element`** <- `"any"` : _Element to add_
 
Returns :
> _**A new array with the element appended at the start of the array.**_
 
**Example:**
```jaiva
maak arr <-| 2, 3, 4!
khuluma(a_unshift(arr, 1))! @ Returns [1, 2, 3, 4]
```
 
> [!NOTE]
> _Throws if the first argument is not an array._
 
### `F~a_filter(arr, F~predicate) -> `_**`[]`**_
 
 Filter an array based on a predicate function
 
- **`arr`** <- `"[]"` : _The original array_
- **`predicate`** <- `"F~any"` : _A function that takes an element and returns true_
 
Returns :
> _**A new array with elements that satisfy the predicate function.**_
 
**Example:**
```jaiva
maak arr <-| 1, 2, 3, 4, 5, 6!
maak isEven <- f~(num) : num % 2 = 0! @ A lambda function to check if a number is even
khuluma(a_filter(arr, isEven))! @ Returns [2, 4, 6]
```
 
> [!NOTE]
> _The predicate function must take in 1 parameter, the element and return a boolean. Throws if the first argument is not an array or if the second argument is not a function._
 
### `F~a_delete(arr, i) -> `_**`[]`**_
 
 Deletes an element at a specific index in an array and shifts the rest of the elements to the left
 
- **`arr`** <- `"[]"` : _The original array_
- **`i`** <- `"number"` : _The index to delete_
 
Returns :
> _**A new array with the element at index i removed and the rest of the elements shifted to the left**_
 
**Example:**
```jaiva
maak arr <-| 1, 2, 3, 4, 5!
khuluma(a_delete(arr, 2))! @ Returns [1, 2, 4, 5]
```
 
> [!NOTE]
> _Throws if the first argument is not an array or if the index is out of bounds. Not to be confused with a_remove() which removes elements from the end of an array_
 
### `F~a_pushAll(<-params) -> `_**`[]`**_
 
 Appends multiple elements to the end of an array and returns the new array.
 
- **`params`** <- `"idk"` : _A variable number of arguments where the first argument is the original array_
 
Returns :
> _**A new array with the elements appended at the end of the array.**_
 
**Example:**
```jaiva
maak arr <-| 1, 2, 3!
khuluma(a_pushAll(arr, 4, 5, 6))! @ Returns [1, 2, 3, 4, 5, 6]
```
 
> [!NOTE]
> _Throws if the first argument is not an array._
 
### `F~a_replace(arr, i, el) -> `_**`[]`**_
 
 Replace an element at a specific index in an array with a new element
 
- **`arr`** <- `"[]"` : _The original array_
- **`i`** <- `"number"` : _The index to replace_
- **`el`** <- `"any"` : _The new element to insert_
 
Returns :
> _**A new array with the element at index i replaced with el**_
 
**Example:**
```jaiva
maak arr <-| 1, 2, 3, 4, 5!
khuluma(a_replace(arr, 2, 99))! @ Returns [1, 2, 99, 4, 5]
```
 
> [!NOTE]
> _Throws if the first argument is not an array or if the index is out of bounds._
 
### `F~a_remove(arr, n) -> `_**`[]`**_
 
 Remove n elements from the end of an array
 
- **`arr`** <- `"[]"` : _The original array_
- **`n`** <- `"number"` : _Amount of elements to remove_
 
Returns :
> _**An array which contains the original array with n amount of elements removed from the end.**_
 
**Example:**
```jaiva
maak arr <-| 1, 2, 3, 4, 5!
khuluma(a_remove(arr, 2))! @ Returns [1, 2, 3]
khuluma(a_remove(arr, 0))! @ Returns [1, 2, 3, 4, 5]
khuluma(a_remove(arr, 5))! @ Returns []
```
 
> [!NOTE]
> _Throws if the first argument is not an array or if the second argument is not a non-negative number. Not to be confused with a_delete() which removes elements from a specific index in an array._
 
### `F~a_pop(array) -> `_**`[]`**_
 
<!-- Please properly link the dependencies. Thank you! :) -->
 
> [!WARNING]
> **This symbol depends on: _a_remove_. It may fail if all of these symbols aren't imported!**
 
 Pops the last element off an array and returns a new array without that element.
 
- **`array`** <- `"[]"` : _The original array_
 
Returns :
> _**A new array without the last element of the original array.**_
 
**Example:**
```jaiva
maak arr <-| 1, 2, 3, 4, 5!
khuluma(a_pop(arr))! @ Returns [1, 2, 3, 4]
```
 
> [!NOTE]
> _This is just a wrapper for the _a_remove()_ function, it doesn't matter which one you use Throws if the argument is not an array._
 
### `F~a_from(input, split?) -> `_**`[]`**_
 
 Creates an array from a string, splitting it by a specified delimiter or index.
 
- **`input`** <- `"string"` : _The input string to convert into an array._
- _`split?`_ <- `"idk"` : _An optional delimiter string or index number._
 
Returns :
> _**An array created from the input string, split by the specified delimiter or index.**_
 
**Example:**
```jaiva
khuluma(a_from("hello"))! @ Returns ["h", "e", "l", "l", "o"]
khuluma(a_from("apple$,banana$,cherry", "$,"))! @ Returns ["apple", "banana", "cherry"]
khuluma(a_from("abcdef", 3))! @ Returns ["abc", "def"]
```
 
> [!NOTE]
> _Throws if the input is not a string. Also, this function imports jaiva/types for type conversions. A perfomance hit may be observed the first time this function is called._
 
### `F~a_shift(arr, n) -> `_**`[]`**_
 
 Removes n elements from the start of an array
 
- **`arr`** <- `"[]"` : _The original array_
- **`n`** <- `"number"` : _Amount of elements to remove_
 
Returns :
> _**An array which contains the original array with n amount of elements removed from the start.**_
 
**Example:**
```jaiva
maak arr <-| 1, 2, 3, 4, 5!
khuluma(a_shift(arr, 2))! @ Returns [3, 4, 5]
khuluma(a_shift(arr, 0))! @ Returns [1, 2, 3, 4, 5]
khuluma(a_shift(arr, 5))! @ Returns []
```
 
> [!NOTE]
> _This uses a colonize loop to iterate through the array and skip the first n elements. It may not be the most efficient way to do this, but it works. Throws if the first argument is not an array or if the second argument is not a non-negative number._
 
### `F~a_indexOf(arr, el) -> `_**`number`**_
 
 Finds the first occurrence of an element in an array and returns its index
 
- **`arr`** <- `"[]"` : _The original array_
- **`el`** <- `"any"` : _The element to find_
 
Returns :
> _**The index of the first occurrence of el in arr, or idk if not found**_
 
**Example:**
```jaiva
maak arr <-| "apple", "banana", "cherry", "date", "banana"!
khuluma(a_indexOf(arr, "banana"))! @ Returns 1
khuluma(a_indexOf(arr, "date"))! @ Returns 3
khuluma(a_indexOf(arr, "fig"))! @ Returns idk
```
 
> [!NOTE]
> _This uses the search first approach, it may not be the most efficient way to do this, but it works. Throws if the input is not an array._
 
### `F~a_map(arr, F~mapperFunction) -> `_**`[]`**_
 
 Transforms every element in an array using a provided function.
 
- **`arr`** <- `"[]"` : _The original array to map over._
- **`mapperFunction`** <- `"F~any"` : _A function that takes one element and returns its transformed value._
 
Returns :
> _**A new array populated with the results of calling the mapperFunction on every element.**_
 
**Example:**
```jaiva
maak arr <-| 1, 2, 3, 4!
maak square <- f~(num) : num * num! @ A lambda function to square a number
khuluma(a_map(arr, square))! @ Returns [1, 4, 9, 16]
```
 
> [!NOTE]
> _The mapperFunction must take in 1 parameter (the element). Throws if the first argument is not an array or if the second argument is not a function._
