# math/trig (Library)

_All the (basic) math functions related to the illusive triangle (trig)_
## Exports
This library exports the following libraries: 
## Table of Contents
| Alias | Link |
| --- | --- |
| m_sin | [m_sin](#m_sin) |
| m_cos | [m_cos](#m_cos) |
| m_tan | [m_tan](#m_tan) |
| m_asin | [m_asin](#m_asin) |
| m_acos | [m_acos](#m_acos) |
| m_atan | [m_atan](#m_atan) |
| m_toRad | [m_toRad](#m_toRad) |
| m_toDeg | [m_toDeg](#m_toDeg) |
| m_atan2 | [m_atan2](#m_atan2) |
## Functions

### m_sin

This symbol can be reached by the following aliases: _`m_sin`_

_**Returns the sine of a number in radians.**_


#### Definition

```jaiva
F~m_sin(value)
```
- **_value_**  **`<-`** _**number**_
	 - _The value in radians_

_**Returns:**_ **The sine of the given value in radians.**

Since Version: _1.0.2_


---


### m_cos

This symbol can be reached by the following aliases: _`m_cos`_

_**Returns the cosine of a number in radians.**_


#### Definition

```jaiva
F~m_cos(value)
```
- **_value_**  **`<-`** _**number**_
	 - _The value in radians_

_**Returns:**_ **The cosine of the given value in radians.**

Since Version: _1.0.2_


---


### m_tan

This symbol can be reached by the following aliases: _`m_tan`_

_**Returns the trig tangent of a number in radians.**_


#### Definition

```jaiva
F~m_tan(value)
```
- **_value_**  **`<-`** _**number**_
	 - _The value in radians_

_**Returns:**_ **The trig tangent of the given value in radians.**

Since Version: _1.0.2_


---


### m_asin

This symbol can be reached by the following aliases: _`m_asin`_

_**Returns the arc sine of a number in radians.**_


#### Definition

```jaiva
F~m_asin(value)
```
- **_value_**  **`<-`** _**number**_
	 - _The value in radians_

_**Returns:**_ **The arc sine of the given value in radians, or idk if the argument's absolute value is greater than 1**

Since Version: _1.0.2_


---


### m_acos

This symbol can be reached by the following aliases: _`m_acos`_

_**Returns the arc cosine of a number in radians.**_


#### Definition

```jaiva
F~m_acos(value)
```
- **_value_**  **`<-`** _**number**_
	 - _The value in radians_

_**Returns:**_ **The arc cosine of the given value in radians, or idk if the argument's absolute value is greater than 1**

Since Version: _1.0.2_


---


### m_atan

This symbol can be reached by the following aliases: _`m_atan`_

_**Returns the arc tangent of a number in radians.**_


#### Definition

```jaiva
F~m_atan(value)
```
- **_value_**  **`<-`** _**number**_
	 - _The value in radians_

_**Returns:**_ **The arc tangent of the given value in radians.**

Since Version: _1.0.2_


---


### m_toRad

This symbol can be reached by the following aliases: _`m_toRad`_

_**Converts degrees to radians.**_


#### Definition

```jaiva
F~m_toRad(degrees)
```
- **_degrees_**  **`<-`** _**number**_
	 - _The value in degrees_

_**Returns:**_ **The value in radians.**

Since Version: _1.0.2_


#### Example: 

```jaiva
maak deg <- 90!
maak rad <- m_toRad(deg)!
khuluma(rad) @ Output: 1.5707963267948966
```


---


### m_toDeg

This symbol can be reached by the following aliases: _`m_toDeg`_

_**Converts radians to degrees.**_


#### Definition

```jaiva
F~m_toDeg(radians)
```
- **_radians_**  **`<-`** _**number**_
	 - _The value in radians_

_**Returns:**_ **The value in degrees.**

Since Version: _1.0.2_


#### Example: 

```jaiva
maak rad <- 1.5708!
maak deg <- m_toDeg(rad)!
khuluma(deg) @ Output: 90.0002104591497
```


---


### m_atan2

This symbol can be reached by the following aliases: _`m_atan2`_

_**Returns the angle in radians between the positive x-axis and the point (x, y).**_


#### Definition

```jaiva
F~m_atan2(y, x)
```

- **_y_**  **`<-`** _**number**_
	 - _The y-coordinate_
- **_x_**  **`<-`** _**number**_
	 - _The x-coordinate_

_**Returns:**_ **The angle in radians from the positive x-axis to the point (x, y).**

> [!NOTE]
> _honestly, if you're using this function you should probably not be using Jaiva to do whatever the hell you're doing..._


Since Version: _6.0.0-alpha.3_


---

## Variables
