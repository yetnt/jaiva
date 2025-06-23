# `math`

This library contains (some) math functions. Unless really needed I don't see myself adding more than these.

All functions and variables from this library are prefixed with `m_` to avoid conflicts with other libraries.

See [Math.java](../src/main/java/com/jaiva/interpreter/globals/Math.java) where this is defined.

## Constants

### `m_pi <- `_*`number`*_

Holds the value of π (pi).

```jiv
tsea "jaiva/math"!
khuluma(m_pi)! @ Prints 3.141592653589793
```

### `m_e <- `_*`number`*_

Holds the value of e (Euler's number).

```jiv
tsea "jaiva/math"!
khuluma(m_e)! @ Prints 2.718281828459045
```

### `m_tau <- `_*`number`*_

Holds the value of τ (tau), which is 2π.

```jiv
tsea "jaiva/math"!
khuluma(m_tau)! @ Prints 6.283185307179586
```

### `m_phi <- `_*`number`*_

Holds the value of φ (phi), which is the golden ratio.

```jiv
tsea "jaiva/math"!
khuluma(m_phi)! @ Prints 1.618033988749895
```

## Functions

### `m_abs(value) -> `_*`khutla number!`*_

\*_**Independent**_

Returns the absolute value of the given number. This is useful for getting rid of negative signs.

```jiv
tsea "jaiva/math"!
khuluma(m_abs(-5))! @ Prints 5
khuluma(m_abs(5))! @ Prints 5
```

### `m_sqrt(value) -> `_*`khutla number!`*_

\*_**Independent**_

Returns the square root of the given number.

```jiv
tsea "jaiva/math"!
khuluma(m_sqrt(16))! @ Prints 4
khuluma(m_sqrt(25))! @ Prints 5
```

### `m_pow(base, exponent) -> `_*`khutla number!`*_

\*_**Independent**_

Raises the `base` to the power of `exponent`.

```jiv
tsea "jaiva/math"!
khuluma(m_pow(2, 3))! @ Prints 8
khuluma(m_pow(5, 2))! @ Prints 25
```

### `m_sin(value) -> `_*`khutla number!`*_

\*_**Independent**_

Returns the sine of the given angle in radians.

```jiv
tsea "jaiva/math"!
khuluma(m_sin(m_pi / 2))! @ Prints 1.0
khuluma(m_sin(m_pi))! @ Prints 0.0
```

### `m_cos(value) -> `_*`khutla number!`*_

\*_**Independent**_

Returns the cosine of the given angle in radians.

```jiv
tsea "jaiva/math"!
khuluma(m_cos(0))! @ Prints 1.0
khuluma(m_cos(m_pi))! @ Prints -1.0
```

### `m_tan(value) -> `_*`khutla number!`*_

\*_**Independent**_

Returns the tangent of the given angle in radians.

```jiv
tsea "jaiva/math"!
khuluma(m_tan(m_pi / 4))! @ Prints 1.0
khuluma(m_tan(m_pi / 2))! @ Prints Infinity (or throws an error, depending on the implementation)
```

### `m_asin(value) -> `_*`khutla number!`*_

\*_**Independent**_

Returns the arcsine (inverse sine) of the given value. The input should be between -1 and 1, and the output is in radians.

```jiv
tsea "jaiva/math"!
khuluma(m_asin(1))! @ Prints 1.5707963267948966 (which is π/2)
khuluma(m_asin(0))! @ Prints 0.0
```

### `m_acos(value) -> `_*`khutla number!`*_

\*_**Independent**_

Returns the arccosine (inverse cosine) of the given value. The input should be between -1 and 1, and the output is in radians.

```jiv
tsea "jaiva/math"!
khuluma(m_acos(1))! @ Prints 0.0
khuluma(m_acos(0))! @ Prints 1.5707963267948966 (which is π/2)
```

### `m_atan(value) -> `_*`khutla number!`*_

\*_**Independent**_

Returns the arctangent (inverse tangent) of the given value. The output is in radians.

```jiv
tsea "jaiva/math"!
khuluma(m_atan(1))! @ Prints 0.7853981633974483 (which is π/4)
khuluma(m_atan(0))! @ Prints 0.0
```

### `m_toRad(degrees) -> `_*`khutla number!`*_

\*_**Independent**_

Converts degrees to radians. This is useful for trigonometric functions that expect radians.

```jiv
tsea "jaiva/math"!
khuluma(m_toRad(180))! @ Prints 3.141592653589793 (which is π)
khuluma(m_toRad(90))! @ Prints 1.5707963267948966 (which is π/2)
```

### `m_toDeg(radians) -> `_*`khutla number!`*_

\*_**Independent**_

Converts radians to degrees. This is useful for converting angles to a more human-readable format.

```jiv
tsea "jaiva/math"!
khuluma(m_toDeg(m_pi))! @ Prints 180.0
khuluma(m_toDeg(m_pi / 2))! @ Prints 90.0
```

### `m_floor(value) -> `_*`khutla number!`*_

\*_**Independent**_

Rounds the given real number down to the nearest integer.

```jiv
tsea "jaiva/math"!
khuluma(m_floor(4.51))! @ Prints 4
khuluma(m_floor(-4.51))! @ Prints -5
```

### `m_ceil(value) -> `_*`khutla number!`*_

\*_**Independent**_

Rounds the given real number up to the nearest integer.

```jiv
tsea "jaiva/math"!
khuluma(m_ceil(4.51))! @ Prints 5
khuluma(m_ceil(-4.51))! @ Prints -4
```

### `m_round(value) -> `_*`khutla number!`*_

\*_**Independent**_

Rounds the given real nmber to an integer. This is for the real ones who hate working with precision.

```jiv
tsea "jaiva/math"!

khuluma(m_round(4.51))! @ Prints 5
```

### `m_random(lower, upper) `_*`khutla number!`*_

\*_**Independent**_

Returns a random (integer) between the `lower` and `upper` bounds. (Both inclusive)

```jiv
tsea "jaiva/math"!

khuluma(m_random(0, 10) > 5)! @ Might print true or false
khuluma(m_random(0, 10) > 5)! @ Might print true or false
```
