# `utils`
 
math/utils library. which will hold multiple number theory related stuff.
 
## Symbols
 
### `F~m_lcm(<-nums) -> `_**`number`**_
 
Calculates the least common multiple (LCM) of a list of numbers.
 
- _`nums?`_ <- `"array"` : _Variable amount of numbers to calculate the LCM for._
 
Returns :
> _**The least common multiple of the provided numbers.**_
 
**Example:**
```jaiva
khuluma(m_lcm(4, 5, 6))! @ Outputs: 60
khuluma(m_lcm(7, 3, 14))! @ Outputs: 42
```
 
### `F~m_gcd(<-nums) -> `_**`number`**_
 
Calculates the greatest common divisor (GCD) of a list of numbers.
 
- _`nums?`_ <- `"array"` : _Variable amount of numbers to calculate the GCD for._
 
Returns :
> _**The greatest common divisor of the provided numbers.**_
 
**Example:**
```jaiva
khuluma(m_gcd(10, 20, 42, 20))! @ Outputs: 2
khuluma(m_gcd(54, 24, 36))! @ Outputs: 6
```
