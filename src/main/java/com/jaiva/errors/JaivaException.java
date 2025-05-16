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

    /**
     * <p>
     * This exception extends {@link JaivaException} and provides additional context
     * such as the file name and the line number where the error occurred.
     * </p>
     *
     * @since 1.0
     */
    public static class UnknownFileException extends JaivaException {

        private static final long serialVersionUID = 1L;

        /**
         * Exception thrown when a specified file cannot be found.
         *
         * @param file       the name or path of the file that could not be found
         * 
         * @param lineNumber the line number where the exception occurred
         */
        public UnknownFileException(String file) {
            super(file + " cannot be found...", -1);
        }
    }
}
