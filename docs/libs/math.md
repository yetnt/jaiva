# `math`
 
Math library
 
 
## Functions
 
### `F~m_toDeg(radians) -> `_**`number`**_
 
Converts radians to degrees.
 
- **`radians`** <- `"number"` : _The value in radians._
 
Returns :
> _**The value in degrees.**_
 
**Example:**
```jaiva
maak rad <- 1.5708!
maak deg <- m_toDeg(rad)!
khuluma(deg) @ Output: 90.0002104591497
```
 
### `F~m_asin(value) -> `_**`number`**_
 
Returns the arcsine of a number in radians.
 
- **`value`** <- `"number"` : _The value in radians._
 
Returns :
> _**The arcsine of the given value in radians.**_
 
### `F~m_round(value) -> `_**`number`**_
 
Rounds the given real number to an integer.
 
- **`value`** <- `"number"` : _The input to round up/down._
 
Returns :
> _**An integer value**_
 
**Example:**
```jaiva
khuluma("Rounded value of 4.6 is: " + m_round(4.6))!
```
 
### `F~m_sqrt(value) -> `_**`number`**_
 
Calculates the square root of the input value.
 
- **`value`** <- `"number"` : _The number to find the square root of._
 
Returns :
> _**Returns the (positive) square root of the number provided**_
 
**Example:**
```jaiva
khuluma("The square root of 16 is: " + m_sqrt(16))!
```
 
### `F~m_tan(value) -> `_**`number`**_
 
Returns the tangent of a number in radians.
 
- **`value`** <- `"number"` : _The value in radians._
 
Returns :
> _**The tangent of the given value in radians.**_
 
### `F~m_floor(value) -> `_**`number`**_
 
Returns the largest integer less than or equal to the given value.
 
- **`value`** <- `"number"` : _The value to floor._
 
Returns :
> _**The largest integer less than or equal to the given value.**_
 
### `F~m_sin(value) -> `_**`number`**_
 
Returns the sine of a number in radians.
 
- **`value`** <- `"number"` : _The value in radians._
 
Returns :
> _**The sine of the given value in radians.**_
 
### `F~m_abs(value) -> `_**`number`**_
 
Returns the absolute value of a number.
 
- **`value`** <- `"number"` : _The value to return the value of._
 
Returns :
> _**A positive value.**_
 
**Example:**
```jaiva
khuluma("The absolute value of -5 is: " + m_abs(-5))!
```
 
### `F~m_acos(value) -> `_**`number`**_
 
Returns the arccosine of a number in radians.
 
- **`value`** <- `"number"` : _The value in radians._
 
Returns :
> _**The arccosine of the given value in radians.**_
 
### `F~m_toRad(degrees) -> `_**`number`**_
 
Converts degrees to radians.
 
- **`degrees`** <- `"number"` : _The value in degrees._
 
Returns :
> _**The value in radians.**_
 
**Example:**
```jaiva
maak deg <- 90!
maak rad <- m_toRad(deg)!
khuluma(rad) @ Output: 1.5707963267948966
```
 
### `F~m_random(a?, b?) -> `_**`number`**_
 
Returns a random number in the range of `a` and `b`, If both are omitted, returns a random (double) between 0 and 1.
 
- _`a?`_ <- `"number"` : _The highest number possible between `a` and 0, otherwise the lowest between `a` and `b` (inclusive)_
- _`b?`_ <- `"number"` : _The highest number possible between a and b (inclusive). _
 
Returns :
> _**A random number.**_
 
**Example:**
```jaiva
khuluma("Random number between 1 and 10: " + m_random(1, 10))!
khuluma("Random number between 0 and 5: " + m_random(5))!
khuluma("Random number between 0 and 1: " + m_random())!
```
 
> [!NOTE]
> _Unlike other functions, if you provide no arguments, this function returns a double between 0 and 1. If you provide only one argument, it is treated as the upper bound, with the lower bound being 0._
 
### `F~m_cos(value) -> `_**`number`**_
 
Returns the cosine of a number in radians.
 
- **`value`** <- `"number"` : _The value in radians._
 
Returns :
> _**The cosine of the given value in radians.**_
 
### `F~m_atan(value) -> `_**`number`**_
 
Returns the arctangent of a number in radians.
 
- **`value`** <- `"number"` : _The value in radians._
 
Returns :
> _**The arctangent of the given value in radians.**_
 
### `F~m_ceil(value) -> `_**`number`**_
 
Returns the smallest integer greater than or equal to the given value.
 
- **`value`** <- `"number"` : _The value to ceil._
 
Returns :
> _**The smallest integer greater than or equal to the given value.**_
 
### `F~m_log(value, base?) -> `_**`number`**_
 
Calculates the logarithm of a value with the specified base.
 
- **`value`** <- `"number"` : _The value to calculate the logarithm for._
- _`base?`_ <- `"number"` : _The base of the logarithm, otherwise 10 is used_
 
Returns :
> _**The logarithm of the value with the specified base.**_
 
**Example:**
```jaiva
khuluma("Log base 10 of 1000 is: " + m_log(1000))!
khuluma("Log base 2 of 1024 is: " + m_log(1024, 2))!
```
## Variables
 
### `m_e <- `_**`number`**_
 
The mathematical constant e (Euler's number)
 
**Example:**
```jaiva
khuluma(2 ^ m_e)! @ approximately 7.38905609893065
```
 
> [!NOTE]
> _Just java.lang.Math.E_
 
### `m_phi <- `_**`number`**_
 
The golden ratio φ (phi)
 
**Example:**
```jaiva
@ Calculating the golden rectangle dimensions
maak shortSide <- 10!
maak longSide <- shortSide * m_phi!
khuluma("Long side of the golden rectangle: " + longSide)! @ approximately 16.18033988749895
```
 
> [!NOTE]
> _No note here._
 
### `m_pi <- `_**`number`**_
 
The mathematical constant π (pi)
 
> [!NOTE]
> _It's just java.lang.Math.PI_
 
### `m_tau <- `_**`number`**_
 
The mathematical constant τ (tau), which is equal to 2π
 
**Example:**
```jaiva
@ Using tau to calculate the circumference of a circle with radius 5
maak radius <- 5!
maak circumference <- m_tau * radius!
khuluma(circumference)! @ approximately 31.41592653589793
```
 
> [!NOTE]
> _Just java.lang.Math.TAU_
