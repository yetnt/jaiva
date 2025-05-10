package com.jaiva.errors;

/**
 * Base class for any exceptions we run into while trying to parse or run jaiva
 * code.
 */
public class JaivaException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for child classes that extend of JaivaException to print with a
     * line number.
     * 
     * @param message The mesage you wanna send.
     */
    public JaivaException(String message, int lineNumber) {
        super("[" + lineNumber + "] " + message);
    }

    /**
     * Default Constructor
     * 
     * @param message
     */
    public JaivaException(String message) {
        super(message);
    }
}
