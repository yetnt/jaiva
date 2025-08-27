package com.jaiva.interpreter;

import com.jaiva.interpreter.runtime.DebugController;

/**
 * Context enum represents the different contexts in which a variable can be
 * defined or used.
 * <p>
 * This is the smallest class in the entire jaiva project.
 * <p>
 * Used in conjunction with {@link Scope} to keep track of the context in
 * which a variable is defined or used.
 */
public enum Context {
    /**
     * Represents a global context.
     * <p>
     * This context is used for variables that are defined in the global scope and
     * can be accessed from anywhere in the program.
     */
    GLOBAL,
    /**
     * Represents a local context, specifically an if block.
     * <p>
     * This context is used for variables that are defined in a local scope and can
     * only be accessed from within that scope.
     */
    IF,
    /**
     * Represents a local context, specifically an else block.
     * <p>
     * This context is used for variables that are defined in a local scope and can
     * only be accessed from within that scope.
     */
    ELSE,
    /**
     * Represents a local context, specifically a while loop.
     * <p>
     * This context is used for variables that are defined in a local scope and can
     * only be accessed from within that scope.
     */
    WHILE,
    /**
     * Represents a local context, specifically a for loop.
     * <p>
     * This context is used for variables that are defined in a local scope and can
     * only be accessed from within that scope.
     */
    FOR,
    /**
     * Represents a local context, specifically a function block.
     * <p>
     * This context is used for variables that are defined in a local scope and can
     * only be accessed from within that scope.
     */
    FUNCTION,
    /**
     * Represents a local context, specifically a try block.
     * <p>
     * This context is used for variables that are defined in a local scope and can
     * only be accessed from within that scope.
     */
    TRY,
    /**
     * Represents a local context, specifically a catch block.
     * <p>
     * This context is used for variables that are defined in a local scope and can
     * only be accessed from within that scope.
     */
    CATCH,
    /**
     * Represents an import context.
     */
    IMPORT,
    /**
     * Represents the end of a file.
     * <p>
     * This context is used to indicate that the end of a file has been reached
     * and there are no more lines to interpret.
     * It is used only in {@link DebugController#endOfFile(Scope)}
     */
    EOF
}
