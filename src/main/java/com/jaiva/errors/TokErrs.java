package com.jaiva.errors;

import com.jaiva.errors.TokErrs.TokenizerException;

/**
 * Tokenizer Errors
 */
public class TokErrs {

    /**
     * Syntax Error Exception.
     */
    public static class UnknownSyntaxError extends TokenizerException {
        private static final long serialVersionUID = 1L;

        public UnknownSyntaxError(String message) {
            super(message);
        }
    }

    /**
     * Syntax Error Exception.
     */
    public static class SyntaxCriticalError extends TokenizerException {
        private static final long serialVersionUID = 1L;

        public SyntaxCriticalError(String message) {
            super(message);
        }
    }

    /**
     * Syntax Error Exception.
     */
    public static class TokenizerSyntaxException extends TokenizerException {
        private static final long serialVersionUID = 1L;

        public TokenizerSyntaxException(String message) {
            super(message);
        }
    }

    /**
     * Syntax Error Exception.
     */
    public static class SyntaxWarning extends TokenizerException {
        private static final long serialVersionUID = 1L;

        public SyntaxWarning(String message) {
            super(message);
        }
    }

    /**
     * Syntax Error Exception.
     */
    public static class SyntaxError extends TokenizerException {
        private static final long serialVersionUID = 1L;

        public SyntaxError(String message) {
            super(message);
        }
    }

    /**
     * Base Syntax Error Exception.
     */
    public static class TokenizerException extends Exception {
        private static final long serialVersionUID = 1L;

        public TokenizerException(String message) {
            super(message);
        }
    }

}
