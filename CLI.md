# CLI

Simple CLI Ngl.

```sh
> jaiva [-p | -h | -v | -t | -u]
> jaiva <filePath> [-j | -jg | -s]
```

1. (No flags) launches interactive REPL.

    ```sh
    > jaiva
    ```

2. Get help instructions.

    ```sh
    > jaiva -h
    > jaiva --help
    ```

3. Launches REPL but also prints tokens only (more for debugging purposes)

    ```sh
    > jaiva -p
    > jaiva --print-tokens
    ```

4. Print the version of Jaiva.

    ```sh
    > jaiva -v
    > jaiva --version
    ```

5. Run's a test command i have set. Because why not

    ```sh
    > jaiva -t
    > jaiva --test
    ```

6. Get update instructions.

    ```sh
    > jaiva -u
    > jaiva --update
    ```

## Read File

This command will attempt to find `<filePath>` and execute the jaiva code.

1. Read file, (No flags.)
    ```sh
    > jaiva <filePath>
    ```
2. Returns tokens as strings.
    ```sh
    > jaiva <filePath> -s
    > jaiva <filePath> --string
    ```
3. Return tokens in JSON format.

    ```sh
    > jaiva <filePath> -j
    > jaiva <filePath> --json
    ```

4. Return tokens in JSON format including globals.

    ```sh
    > jaiva <filePath> -jg
    > jaiva <filePath> --json-with-globals
    ```

5. Read the file and throw Java errors with stack traces if any.

    ```sh
    > jaiva <filePath> -is
    > jaiva <filePath> --include-stacks
    ```

6. Enable debugging mode.

    ```sh
    > jaiva <filePath> -d
    > jaiva <filePath> --debug
    ```

## Debugger Commands

The debugger commands are available when the `-d` or `--debug` flag is used. The following commands are available:

1. Toggle breakpoints

```sh
> breakpoint <subcommand> [arg]
> bp <subcommand> [arg]
```

_Subcommands:_

```sh
- add <line_number> : Add a breakpoint at the specified line number.
- remove <line_number> : Remove a breakpoint at the specified line number.
- list : List all breakpoints.
- clear : Clear all breakpoints.
```

2. Start the debugger

```sh
> start
```

3. Step through code (Skips the current line and moves to the next one)

```sh
> step
```

4. Continue execution until the next breakpoint or end of file

```sh
> continue
> cont
```

5. Pause the debugger

```sh
> pause
```

6. Exit the debugger

```sh
> exit
> quit
```

7. Manage the variable functions hashmap (vfs):

```sh
> vfs <subcommand> [arg]
```

_Subcommands:_

```sh
- get <variable_name> : Get the value of a variable from the vfs.
- dump [all] : Dump the current variable functions hashmap (vfs).
    - If 'all' is specified, it will show all symbols, otherwise only show user defined symbols.
```

8. Print help text

```sh
> help
> h
```

9. Print stack trace
```sh
- stack trace
- st
- scope
```
