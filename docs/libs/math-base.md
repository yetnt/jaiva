# math/base (Library)

_Contains the math functions like sqrt or ceil which don't really have their own home unique to them..._
## Exports
This library exports the following libraries: 
## Table of Contents
| Alias | Link |
| --- | --- |
| m_random | [m_random](#m_random) |
| m_round | [m_round](#m_round) |
| m_abs | [m_abs](#m_abs) |
| m_sqrt | [m_sqrt](#m_sqrt) |
| m_floor | [m_floor](#m_floor) |
| m_ceil | [m_ceil](#m_ceil) |
| m_log | [m_log](#m_log) |
| m_gcd | [m_gcd](#m_gcd) |
| m_lcm | [m_lcm](#m_lcm) |
## Functions

### m_random

This symbol can be reached by the following aliases: _`m_random`_

_**Returns a random number in the range of `a` and `b`, If both are omitted, returns a random (double) between 0 and 1.**_


#### Definition

```jaiva
F~m_random(a?, b?)
```

- **_a_** **`?`** **`<-`** _**number**_
	 - _The highest number possible between `a` and 0, otherwise the lwoest between `a` and `b` (inclusive)_
- **_b_** **`?`** **`<-`** _**number**_
	 - _The highest number possible between a and b (inclusive)_

_**Returns:**_ **A random number.**

> [!NOTE]
> _Unlike other functions, if you provide no arguments, this function returns a double between 0 and 1. If you provide only one argument, it is treated as the upper bound, with the lower bound being 0. If you're coming from a normal programming lang, don't worry both ints are inclusive_


Since Version: _1.0.0_


#### Example: 

```jaiva
khuluma("Random number between 1 and 10: " + m_random(1, 10))!
khuluma("Random number between 0 and 5: " + m_random(5))!
khuluma("Random number between 0 and 1: " + m_random())!
```


---


### m_round

This symbol can be reached by the following aliases: _`m_round`_

_**Rounds the given real number to an integer.**_


#### Definition

```jaiva
F~m_round(value)
```
- **_value_**  **`<-`** _**number**_
	 - _The input to round_

_**Returns:**_ **An integer value, or a long if the input was already a long**

Since Version: _1.0.0_


#### Example: 

```jaiva
khuluma("Rounded value of 4.6 is: " + m_round(4.6))!
```


---


### m_abs

This symbol can be reached by the following aliases: _`m_abs`_

_**Returns the absolute value of a number.**_


#### Definition

```jaiva
F~m_abs(value)
```
- **_value_**  **`<-`** _**number**_
	 - _The value to return the value of._

_**Returns:**_ **A positive value.**

Since Version: _1.0.2_


#### Example: 

```jaiva
khuluma("The absolute value of -5 is: " + m_abs(-5))!
```


---


### m_sqrt

This symbol can be reached by the following aliases: _`m_sqrt`_

_**Calculates the (positive) square root of the input value.**_


#### Definition

```jaiva
F~m_sqrt(value)
```
- **_value_**  **`<-`** _**number**_
	 - _The input to sqrt_

_**Returns:**_ **The suare root, or idk if the returned value is NaN**

Since Version: _1.0.2_


#### Example: 

```jaiva
khuluma("The square root of 16 is: " + m_sqrt(16))!
m_sqrt(-1)! @ Returns idk
```


---


### m_floor

This symbol can be reached by the following aliases: _`m_floor`_

_**Returns the largest integer less than or equal to the given value.**_


#### Definition

```jaiva
F~m_floor(value)
```
- **_value_**  **`<-`** _**number**_
	 - _The input to round_

_**Returns:**_ **The largest integer less than or equal to the given value.**

Since Version: _1.0.2_


---


### m_ceil

This symbol can be reached by the following aliases: _`m_ceil`_

_**Returns the smallest integer greater than or equal to the given value.**_


#### Definition

```jaiva
F~m_ceil(value)
```
- **_value_**  **`<-`** _**number**_
	 - _The input to ceil_

_**Returns:**_ **The smallest integer greater than or equal to the given value.**

Since Version: _1.0.2_


---


### m_log

This symbol can be reached by the following aliases: _`m_log`_

_**Calculates the logarithm of a value with the specified base.**_


#### Definition

```jaiva
F~m_log(value, base?)
```

- **_value_**  **`<-`** _**number**_
	 - _The value to calculate the logarithm of._
- **_base_** **`?`** **`<-`** _**number**_
	 - _The base of the logarithm, otherwise 10 is used_

_**Returns:**_ **The logarithm of the value with the specified base.**

Since Version: _5.0.0_


#### Example: 

```jaiva
khuluma("Log base 10 of 1000 is: " + m_log(1000))!
khuluma("Log base 2 of 1024 is: " + m_log(1024, 2))!
```


---


### m_gcd

This symbol can be reached by the following aliases: _`m_gcd`_

_**Calculates the greatest common divisor (GCD) of a list of numbers.**_


#### Definition

```jaiva
F~m_gcd(<-nums?)
```
- **_nums_** **`?`** **`<-`** _**[]**_
	 - _Variable amount of numbers to calculate the greatest common divisor of_

_**Returns:**_ **The greatest common divisor of the provided numbers.**

Since Version: _5.0.0_


#### Example: 

```jaiva
khuluma(m_gcd(10, 20, 42, 20))! @ Outputs: 2
khuluma(m_gcd(54, 24, 36))! @ Outputs: 6
```


---


### m_lcm

This symbol can be reached by the following aliases: _`m_lcm`_

_**Calculates the lowest common multiple (LCM) of a list of numbers.**_


#### Definition

```jaiva
F~m_lcm(<-nums?)
```
- **_nums_** **`?`** **`<-`** _**[]**_
	 - _Variable amount of numbers to calculate the lowest common multiple of_

_**Returns:**_ **The lowest common multiple of the provided numbers.**

Since Version: _5.0.0_


#### Example: 

```jaiva
khuluma(m_lcm(4, 5, 6))! @ Outputs: 60
khuluma(m_lcm(7, 3, 14))! @ Outputs: 42
```


---

## Variables
