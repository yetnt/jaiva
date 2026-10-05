# math/const (Library)

_The math constants that'll never change_
## Exports
This library exports the following libraries: 
## Table of Contents
| Alias | Link |
| --- | --- |
| m_e | [m_e](#m_e) |
| m_pi | [m_pi](#m_pi) |
| m_tau | [m_tau](#m_tau) |
| m_phi | [m_phi](#m_phi) |
## Functions
## Variables

### m_e

This symbol can be reached by the following aliases: _`m_e`_

_**The mathematical constant e (Euler's number)**_


#### Definition
```jaiva
maak m_e <- (number)
```

> [!NOTE]
> _Just java.lang.Math.E_


#### Example: 

```jaiva
khuluma(2 ^ m_e)! @ approximately 7.38905609893065
```


---


### m_pi

This symbol can be reached by the following aliases: _`m_pi`_

_**The mathematical constant π (pi)**_


#### Definition
```jaiva
maak m_pi <- (number)
```

> [!NOTE]
> _It's just java.lang.Math.PI_


---


### m_tau

This symbol can be reached by the following aliases: _`m_tau`_

_**The mathematical constant τ (tau), which is equal to 2π**_


#### Definition
```jaiva
maak m_tau <- (number)
```

> [!NOTE]
> _Just java.lang.Math.TAU_


#### Example: 

```jaiva
@ Using tau to calculate the circumference of a circle with radius 5
maak radius <- 5!
maak circumference <- m_tau * radius!
khuluma(circumference)! @ approximately 31.41592653589793
```


---


### m_phi

This symbol can be reached by the following aliases: _`m_phi`_

_**The golden ratio φ (phi)**_


#### Definition
```jaiva
maak m_phi <- (number)
```

> [!NOTE]
> _No note here._


#### Example: 

```jaiva
@ Calculating the golden rectangle dimensions
maak shortSide <- 10!
maak longSide <- shortSide * m_phi!
khuluma("Long side of the golden rectangle: " + longSide)! @ approximately 16.18033988749895
```


---

