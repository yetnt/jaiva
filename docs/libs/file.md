# `file`

This library contains variables (and soon maybe functions) for handling file related things.

> [!WARNING]
> `f_bin` has been deprecated as of `v4.0.0`

## `[file]` Structure

Some of these functions return an array which contains relevant information about the file in question.

This is the format all these arrays follow

```jaiva
[fileName, fileDir, [contents], [canRead, canWrite, canExecute]]
```

So for a file "C:/Users/Documents/myfile.js", with the content:

```js
console.log("I love Brotherkupa.");
console.log("Nobody asked.");
```

the array will contain the following:

```json
[
    "myfile.js",
    "C:/Users/Documents/",
    [
        "console.log(\"I love Brotherkupa.\");",
        "console.log(\"Nobody asked.\");"
    ],
    [
        true, true, true
    ]
]
```

See [IOFile.java](../../src/main/java/com/jaiva/interpreter/libs/IOFile.java) where this is defined.

For all exmaples, the "current" file is located in C:\Users\me\file.jiv

## Constants

### `f_this <- `_*`[file]!`*_

Returns an array containing the current file's properties ([Structure](#file-structure))

```jiv
tsea "jaiva/file"!
maak fileInfo <- f_this()! @ Gets the current file's properties.
khuluma(fileInfo)! @ Prints the array with file name, directory, contents, and permissions.
```

### `f_name <- `_*`"string"`*_


Returns the current file name

```jiv
tsea "jaiva/file"!
khuluma(f_name)! @ file.jiv
```

### `f_dir <- `_*`"string"`*_


Returns the current working directory

```jiv
tsea "jaiva/file"!
khuluma(f_dir)! @ C:\Users\me\file.jiv
```

## Functions

### `f_file(path) -> `_*`khutla ([file])!`*_

Returns an array containing the properties of the file at the given `path` ([Structure](#file-structure)).

```jiv
tsea "jaiva/file"!
maak fileInfo <- f_file("C:/Users/me/anotherfile.js")! @ Gets the properties of anotherfile.js.
khuluma(fileInfo)! @ Prints the array with file name, directory, contents, and permissions.
```

### `f_new(path, content, canRead?, canWrite?, canExecute?) -> `_*`khutla boolean`*_


Creates a new file at the given `path` with the given `content`. The `canRead`, `canWrite`, and `canExecute` parameters are optional and default to true if not provided.

Returns true if the file was created successfully, false otherwise.
Therefore if the file already exists or could not be created, it will return false.

```jiv
tsea "jaiva/file"!
maak filePath <-  "C:/Users/Documents/somewhere/newfile.jiv"! @ Creates a new file
f_new(filePath, "khuluma($"Hello, world$!$")!")! @ Creates a new file with the given content.
khuluma("File created at " + filePath)! @ Prints the file path
```
