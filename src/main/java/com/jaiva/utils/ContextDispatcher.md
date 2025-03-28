# Problem

TStatement : handles splitting boolean and arithmatic into `lhs` and `rhs`

processContext : processes function calls and variable calls and also array access stuff

The problem comes with both trying to figure out what to do with () as the one uses it for order and the other uses it for function calls.

Now they both call each other to make sure that we are in the correct context, but we need to be careful in which one do we call **first**, How do we know it's an arithmatic operation with a function call inside? or the other way around? Or if its all mixed? Here's my solution

# Context Dispatcher

**_make sure to always `.trim()` the lines_**

`0b`**`abcde`** represents the integer we're trying to make.

`a` - SE : Is the **S**tring **E**mpty?

`b` - EB : Did we **E**ncounter a **B**race before an operand?

`c` - EO : Did we **E**ncoutner an **O**perand?

`d` - BC : All **B**races have been **C**losed on the left hand side of the OUTER operand.

`e` - EIB : Line **E**nds **I**n a **B**race.

| Input statement             | SE  | EB  | EO  | BC  | EIB | The output     |
| --------------------------- | --- | --- | --- | --- | --- | -------------- |
| `a`                         | -   | 0   | 0   | 0   | 0   | processContext |
| impossible case             | -   | 0   | 0   | 0   | 1   | ERROR          |
| impossible case             | -   | 0   | 0   | 1   | 0   | ERROR          |
| imppossible case            | -   | 0   | 0   | 1   | 1   | ERROR          |
| impossible                  | -   | 0   | 1   | 0   | 0   | ERROR          |
| impossible                  | -   | 0   | 1   | 0   | 1   | ERROR          |
| `1 + a`                     | -   | 0   | 1   | 1   | 0   | TStatement     |
| `a + func(b)`               | -   | 0   | 1   | 1   | 1   | TStatement     |
| `1 + a - (a / b)`           | -   | 0   | 1   | 1   | 1   | TStatement     |
| impossible case             | -   | 0   | 1   | 1   | 0   | ERROR          |
| impossible case             | -   | 0   | 1   | 1   | 1   | ERROR          |
| impoissible case            | -   | 1   | 0   | 0   | 0   | ERROR          |
| `(`                         | -   | 1   | 0   | 0   | 0   | ERROR          |
| `)`                         | -   | 1   | 0   | 0   | 1   | ERROR          |
| imnpossible case            | -   | 1   | 0   | 1   | 0   | ERROR          |
| `func()`                    | -   | 1   | 0   | 1   | 1   | processContext |
| `(1 - c) + n`               | -   | 1   | 1   | 0   | 0   | TStatement     |
| `func(a + b)`               | -   | 1   | 1   | 0   | 1   | processContext |
| `func(a + (b - a))`         | -   | 1   | 1   | 0   | 1   | processContext |
| `func(func(a - b))`         | -   | 1   | 1   | 0   | 1   | processContext |
| `func(func(a) \| b)`        | -   | 1   | 1   | 0   | 1   | processContext |
| `func((1 > 1) \| func(10))` | -   | 1   | 1   | 0   | 1   | processContext |
| `func(a) + b`               | -   | 1   | 1   | 1   | 0   | TStatement     |
| `func(q) - a`               | -   | 1   | 1   | 1   | 0   | TStatement     |
| `func(func(b)) - a`         | -   | 1   | 1   | 1   | 0   | TStatement     |
| `func(a + func(c)) + v`     | -   | 1   | 1   | 1   | 0   | TStatement     |
| `(1 > 1) \| func(10)`       | -   | 1   | 1   | 1   | 1   | TStatement     |
| `func(2) > func(4)`         | -   | 1   | 1   | 1   | 1   | TStatement     |
| ``                          | 1   | 0   | 0   | 0   | 0   | Empty String   |

The integers we get are:

TStatement = 6, 7, 12, 14, 15

processContext = 0, 11, 13

Errors:

-   empty string = 16
-   single brace = 9, 8
-   impossible cases = 1, 2, 3, 4, 5, 10, 17 - 31
