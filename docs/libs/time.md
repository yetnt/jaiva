# `time`
 
handling time. has basically nothing so far.
 
## Symbols
 
### `F~t_parseDate(dateString, format?, timezone?) -> `_**`number`**_
 
Parses a date string into milliseconds since the Unix epoch.
 
- **`dateString`** <- `"string"` : _The date string to parse._
- _`format?`_ <- `"string"` : _The format of the date string (e.g., "yyyy-MM-dd HH:mm:ss"). Defaults to ISO_LOCAL_DATE_TIME._
- _`timezone?`_ <- `"string"` : _The timezone to parse this date into. You can either put a magic string yourself or use the constants within "jaiva/timezone"_
 
Returns :
> _**A number representing the parsed date in milliseconds.**_
 
**Example:**
```jaiva
@ Import jaiva/time/zone
tsea "jaiva/time/zone" <- TZ_AfricaJohannesburg!
maak ms <- t_parseDate("2023-10-05 14:30:00", "yyyy-MM-dd HH:mm:ss", TZ_AfricaJohannesburg)!
khuluma(ms)! @ Outputs the milliseconds since epoch for the given date in the specified timezone.
```
 
> [!NOTE]
> _If no timezone is provided, a default timezone of UTC is used._
 
### `F~t_now() -> `_**`number`**_
 
Returns the current time in milliseconds since the Unix epoch.
 
Returns :
> _**A number representing the current time in milliseconds.**_
 
### `F~t_msToSec(milliseconds) -> `_**`number`**_
 
Converts milliseconds to seconds.
 
- **`milliseconds`** <- `"number"` : _The number of milliseconds to convert._
 
Returns :
> _**A number representing the converted seconds.**_
