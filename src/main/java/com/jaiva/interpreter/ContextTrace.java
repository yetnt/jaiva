package com.jaiva.interpreter;

import com.jaiva.tokenizer.TokenDefault;

/**
 * The ContextTrace class is used to keep track of the context in which a
 * variable
 * is defined or used.
 * <p>
 * It contains information about the current context, the token associated with
 * it,
 * the line number where it is defined or used, and the parent context trace if
 * any.
 */
public class ContextTrace {
    /**
     * The current context in which the variable is defined or used.
     * This variable is used to determine the scope of a variable
     */
    public Context current;
    public TokenDefault token;
    public int lineNumber;
    /**
     * The parent context trace, if any.
     * This is used to keep track of the context hierarchy.
     * <p>
     * If null, it means this is the root context trace.
     * If not null, it means this context trace is nested within another context
     * trace.
     */
    public ContextTrace parent;

    /**
     * Default constructor for ContextTrace.
     * <p>
     * This constructor initializes the current context to the global context,
     * token to null, line number to 0, and parent context trace to null.
     */
    public ContextTrace() {
        this.current = Context.GLOBAL;
        this.token = null;
        this.lineNumber = 0;
        this.parent = null;
    }

    /**
     * Constructor for ContextTrace with parent context.
     * <p>
     * This constructor initializes the current context, token, line number, and
     * sets the parent context trace to the provided parent.
     *
     * @param newC       The current context in which the variable is defined or
     *                   used.
     * @param token      The token associated with the current context, if any.
     * @param parent     The parent context trace, if any.
     */
    public ContextTrace(Context newC, TokenDefault token, ContextTrace parent) {
        this.current = newC;
        this.token = token;
        this.lineNumber = token == null ? 0 : token.lineNumber;
        this.parent = parent;
    }

    /**
     * Returns a string representation of the context trace.
     * <p>
     * This method traverses the context trace hierarchy and builds a string
     * representation of the current context, token, and parent contexts.
     *
     * @return A string representation of the context trace.
     */
    @Override
    public String toString() {
        ContextTrace current = this;
        StringBuilder sb = new StringBuilder();
        while (current != null) {
            if (current.current == Context.GLOBAL) {
                sb.append("global");
                current = null; // end the loop
            } else {
                sb.append(
                        current.current == Context.EOF ? "end of file <- "
                                : (current.token.name.equals(current.token.getClass().getSimpleName())
                                        ? ("[" + current.token.getClass().getSimpleName() + " "
                                                + current.token.lineNumber
                                                + "]")
                                        : (("[" + current.token.getClass().getSimpleName() + " "
                                                + current.token.lineNumber + "]") + " : " + current.token.name + "()"))
                                        + " <- ");
                current = current.parent;
            }
        }
        return sb.toString();
    }
}
