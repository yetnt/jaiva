package com.jaiva.interpreter;

import java.util.Scanner;

/**
 * The {@code GlobalResources} class provides a centralized location for
 * managing
 * global resources used throughout the application. It includes a
 * {@link Scanner}
 * instance for reading input from the console.
 * 
 * <p>
 * Usage of this class ensures that resources are properly managed and released
 * when they are no longer needed, preventing resource leaks.
 * 
 * <p>
 * <strong>Example:</strong>
 * 
 * <pre>{@code
 * GlobalResources resources = new GlobalResources();
 * // Use resources.consoleIn for input
 * resources.release(); // Release resources when done
 * }</pre>
 * 
 * <p>
 * <strong>Note:</strong> Always call the {@link #release()} method to close
 * the input stream and free up resources.
 * 
 * @author Your Name
 * @version 1.0
 * @since 2023
 */
public class GlobalResources {
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
