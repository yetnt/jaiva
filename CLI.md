# CLI

Simple CLI Ngl.

```sh
> jaiva [-p | -h | -v | -t | -u]
> jaiva <filePath> [-t | -s]
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
