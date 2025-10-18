# Installing Jaiva as a Global Command

After unzipping the distribution, follow these instructions based on your operating system.

## On Windows

1. Extract the ZIP to a folder, e.g., `C:\jaiva`.
2. Ensure the launcher batch file (`jaiva.cmd`) is in the same folder as `jaiva.jar`.
3. Add the folder to your PATH:
    - Open “Edit the system environment variables.”
    - Click “Environment Variables…” and add `C:\jaiva` to the PATH variable.
4. Open a new Command Prompt and type:
    ```batch
    jaiva
    ```

```sh
> jaiva filePath.jiv
```

And get your desired output.

## On Linux and macOS

1. Extract the ZIP to a folder, for example `/opt/jaiva`.
2. Ensure the launcher script (named `jaiva`) is in the same folder as `jaiva.jar`.
3. Make the script executable:

    ```sh
    chmod +x /opt/jaiva/jaiva
    ```

> [!WARNING]
> If you install an update, which means replacing the old folder/jar file with the new one, you may want to run the above script again, just to make sure the permissions are ight yknow.

4. Add the folder to your PATH by adding this line to your shell configuration file (e.g., `~/.bashrc`):
    ```sh
    export PATH="$PATH:/opt/jaiva"
    ```
5. Reload your shell configuration:
    ```sh
    source ~/.bashrc  # or ~/.zshrc
    ```
6. You can now run Jaiva by simply typing:
    ```sh
    jaiva
    ```
