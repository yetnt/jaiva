package com.jaiva.interpreter.runtime;

import java.nio.file.Path;
import java.util.Scanner;

/**
 * The {@code GlobalResources} class provides a centralized container for
 * managing
 * global resources used throughout the application. It includes configuration
 * settings,
 * input handling, and file path management.
 * 
 * <p>
 * This class is designed to simplify resource management by encapsulating
 * commonly
 * used resources such as configuration, console input, and file paths. It also
 * provides
 * a method to release resources when they are no longer needed.
 * 
 * <p>
 * Usage example:
 * 
 * <pre>
 * {@code
 * GlobalResources resources = new GlobalResources("path/to/current/file");
 * // Use resources.config, resources.consoleIn, etc.
 * resources.release(); // Release resources when done
 * }
 * </pre>
 * 
 * <p>
 * <strong>Note:</strong> Always call the {@link #release()} method to close the
 * input stream and free up resources to avoid resource leaks.
 * 
 * <p>
 * Fields:
 * <ul>
 * <li>{@code config} - An instance of {@code IConfig} for managing
 * configuration settings.</li>
 * <li>{@code consoleIn} - A {@code Scanner} object for reading input from the
 * console.</li>
 * <li>{@code filePath} - A {@code Path} object representing the current file
 * path.</li>
 * <li>{@code fileDirectory} - A {@code Path} object representing the directory
 * of the current file.</li>
 * </ul>
 * 
 * <p>
 * Constructor:
 * <ul>
 * <li>{@link #GlobalResources(String)} - Initializes the global resources with
 * the specified file path.</li>
 * </ul>
 * 
 * <p>
 * Methods:
 * <ul>
 * <li>{@link #release()} - Releases the global resources by closing the console
 * input stream.</li>
 * </ul>
 */
public class GlobalResources {
    /**
     * Console in scanner for the `mamela()` gtlobal function.
     */
    public Scanner consoleIn = new Scanner(System.in);

    public GlobalResources() {
    }

    /**
     * Releases the global resources by closing the input stream associated with the
     * console.
     * This method should be called to free up resources when they are no longer
     * needed.
     */
    public void release() {
        consoleIn.close();
    }
}
