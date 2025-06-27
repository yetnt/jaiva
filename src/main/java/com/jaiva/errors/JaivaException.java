package com.jaiva.errors;

import java.util.ArrayList;
import java.util.HashMap;

import com.jaiva.interpreter.MapValue;
import com.jaiva.interpreter.runtime.IConfig;

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
     * @param message    The mesage you wanna send.
     * @param lineNumber the line number
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
         * @param file the name or path of the file that could not be found
         */
        public UnknownFileException(String file) {
            super(file + " cannot be found...", -1);
        }
    }

    /**
     * This exception, extending {@link JaivaException}, is thrown when the function
     * {@code d_emit(arr)} is called. It serves as a way for Java test units to
     * break outside of the Jaiva environment and see the components (the arguments
     * passed to the function) and test them.
     * <p>
     * This exception is only really caught by a test class that is testing some
     * jaiva file execution. I know i'm basically writing documentation to myself,
     * but do not add throws declaration to this class or catch this in any other
     * palce or environment.
     * <p>
     * If you see this exception being thrown in the console, your fault why are you
     * using the debug library outside of a test class?
     */
    public static class DebugException extends JaivaException {
        private static final long serialVersionUID = 1L;
        /**
         * The line number where we threw the exception.
         */
        public int lineNumber;
        /**
         * The components that were passed to the debug call.
         */
        public ArrayList<Object> components = new ArrayList<>();
        /**
         * Snapshot of the variable functions store where the dbeug call, also capturing
         * the scope of the debug call and any other variables that were created.
         */
        public HashMap<String, MapValue> vfs = new HashMap<>();
        /**
         * The interpreter configuration used for the debug call.
         */
        public IConfig config;
        /**
         * The error held if while trying to debug, an error was thrown.
         * This typically only happens if you try parsing malformed input into the
         * debugger.
         */
        public Exception error;

        /**
         * Constructor for the DebugException.
         * 
         * @param comp       The components that were passed to the debug call.
         * @param vfs        Snapshot of the variable functions store where the debug
         *                   call was made.
         * @param config     The configuration used for the debug call.
         * @param lineNumber The line number where the exception was thrown.
         */
        public DebugException(ArrayList<Object> comp, HashMap<String, MapValue> vfs, IConfig config,
                int lineNumber) {
            super("A debug error with the following components has been used: " + comp.toString());
            this.lineNumber = lineNumber;
            this.components = comp;
            this.vfs = vfs;
            this.config = config;
            // close();
        }

        /**
         * Constructor for the DebugException that wraps an existing
         * InterpreterException.
         * 
         * @param e The InterpreterException that caused this debug error.
         */
        public DebugException(Exception e) {
            super("A debug error with the following components has been used: " + e.getMessage());
            this.error = e;
            // close();
        }

        /**
         * Closes the console input resource associated with the configuration.
         * This method releases any system resources tied to the console input stream.
         */
        // private void close() {
        // config.resources.release();
        // }

        @Override
        public String toString() {
            return "DebugException [lineNumber=" + lineNumber + ", components=" + components + ", vfs=" + vfs
                    + ", config=" + config + ", error=" + error + "]";
        }
    }
}
