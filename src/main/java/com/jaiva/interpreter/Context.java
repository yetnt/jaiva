package com.jaiva.interpreter;

/**
 * Context enum represents the different contexts in which a variable can be
 * defined or used.
 * <p>
 * This enum is used to determine the scope of a variable and how it can be
 * accessed.
 * <p>
 * This is the smallest class in the entire jaiva project.
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
    CATCH
}
