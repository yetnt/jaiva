# CLI

Simple CLI Ngl.

```sh
> jaiva [-p | -h]
> jaiva <filePath> [-t | -s]
```

1. (No flags) launches basic interactive REPL.

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
