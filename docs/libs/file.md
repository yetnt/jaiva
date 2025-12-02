# `file`
 
File I/O library. Where the type [file] is a structured array with the following [filename, fileDir, [filecontent, separatedby, lines], [canRead, canWrite, canExecute]]
 
 
## Functions
 
### `F~f_permsOf(file) -> `_**`function`**_
 
Gets the permissions of the file from a file.
 
- **`file`** <- `"[]"` : _The file array to get the permissions from._
 
Returns :
> _**A function that allows you to return a specific permission or all permissions as an array.**_
 
**Example:**
```jaiva
maak permissions <- f_permsOf(f_this)!
khuluma(permissions())! @ Prints all permissions as an array [canRead, canWrite, canExecute]
khuluma(permissions("read"))! @ Prints whether the file can be read (true/false)
khuluma(permissions("r"))! @ Prints whether the file can be read (true/false)
khuluma(permissions(0))! @ Prints whether the file can be read (true/false)
@ Similarly for "write"/"w"/1 and "execute"/"x"/2
```
 
> [!NOTE]
> _Unlike other functions, this one may require a bit of functional thinking. It returns a function that you can call to get specific permissions or all permissions at once._
 
### `F~f_dirOf(file) -> `_**`string`**_
 
Gets the directory path of the file from a file.
 
- **`file`** <- `"[]"` : _The file array to get the directory path from._
 
Returns :
> _**The directory path of the file as a string.**_
 
**Example:**
```jaiva
khuluma(f_dirOf(f_this))! @ Prints the directory path of the current file.
```
 
### `F~f_contentOf(file) -> `_**`[]`**_
 
Gets the content of the file from a file.
 
- **`file`** <- `"[]"` : _The file array to get the content from._
 
Returns :
> _**The content of the file as an array of strings.**_
 
**Example:**
```jaiva
khuluma(f_contentOf(f_this))! @ Prints the content of the current file.
```
 
### `F~f_file(path) -> `_**`[]`**_
 
Finds the specified file and retrieves it's contents.
 
- **`path`** <- `"string"` : _The path to the file you want to fetch._
 
Returns :
> _**Returns an array containing the properties of the file at the given `path` \n [fileName, fileDir, [contents], [canRead?, canWrite?, canExecute?]]**_
 
**Example:**
```jaiva
maak file <- f_file("data/myFile.txt")!
khuluma("File name: " + file[0])!
khuluma("File directory: " + file[1])!
```
 
### `F~f_new(path, content, canRead?, canWrite?, canExecute?) -> `_**`boolean`**_
 
Creates a new file with the given properties at the given file.
 
- **`path`** <- `"string"` : _The path to the new file to createFunction. Along with the file name and extension_
- **`content`** <- `"idk"` : _The content the file should hold. Either a string or an array of strings._
- _`canRead?`_ <- `"boolean"` : _Whether or not the file can be read. Defaults to true_
- _`canWrite?`_ <- `"boolean"` : _Whether the file can be written to or not. Defaults to true_
- _`canExecute?`_ <- `"boolean"` : _Whether the file can be executed or not. Defaults to true._
 
Returns :
> _**A boolean `true` if the file could be created. `false` otherwise.**_
 
**Example:**
```jaiva
maak success <- f_new("data/newFile.txt", arrLit("Hello, World!", "This is a new file."), true, true, false)!
if (success) ->
    khuluma("File created successfully!")!
<~ else ->
    khuluma("Failed to createFunction file.")!
<~
```
 
### `F~f_nameOf(file) -> `_**`string`**_
 
Gets the name of the file from a file.
 
- **`file`** <- `"[]"` : _The file array to get the name from._
 
Returns :
> _**The name of the file as a string.**_
 
**Example:**
```jaiva
khuluma(f_nameOf(f_this))! @ Prints the name of the current file.
```
## Variables
 
### `f_bin <- `_**`string`**_
 
> [!IMPORTANT]
> _**This symbol is deprecated!**__`As of (one of the versions), the jaiva-cli sh and windows command no longer exist. (This was only used to locate where the /lib/ folder was, however now the lib folder lives within jaiva.jar itself, deprecating the need for locating jaiva.jar)`_
 
Variable that holds the directory where you can find jaiva.jar
 
**Example:**
```jaiva
@ Now. I would give an example for this.
@ However, why should I if it's deprecated?
```
 
### `f_name <- `_**`string`**_
 
Variable that holds the current file's name
 
**Example:**
```jaiva
if (f_name != "myFile.jiv") ->
    khuluma("This is not myFile.jiv!")!
<~
```
 
> [!NOTE]
> _If you call this within the REPL, or somehow the filePath is null, it holds "REPL"_
 
### `f_this <- `_**`[]`**_
 
Returns the current file's properties and contents.
 
**Example:**
```jaiva
f_this[0]! @ holds the file name
f_this[1]! @ holds the file directory
khuluma("File contents: " + f_this[2])! @ holds the file contents as an array
khuluma("Can we write to this file? " + f_this[3][1])! @ holds the file permissions
```
 
> [!NOTE]
> _Returns an array containing the current file's properties \n [fileName, fileDir, [contents], [canRead?, canWrite?, canExecute?]]_
 
### `f_dir <- `_**`string`**_
 
Variable that holds the current file's directory.
 
**Example:**
```jaiva
khuluma("Current file directory is: " + f_dir)!
```
 
> [!NOTE]
> _If you call this within the REPL, or somehow the filePath is null, it holds "REPL"_
