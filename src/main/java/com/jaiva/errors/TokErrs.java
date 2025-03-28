package com.jaiva.errors;

import com.jaiva.errors.TokErrs.TokenizerException;

public class TokErrs {

    public static class UnknownSyntaxError extends TokErrs.TokenizerException {
        private static final long serialVersionUID = 1L;

        public UnknownSyntaxError(String message) {
            super(message);
        }
    }

    public static class SyntaxCriticalError extends TokErrs.TokenizerException {
        private static final long serialVersionUID = 1L;

        public SyntaxCriticalError(String message) {
            super(message);
        }
    }

    public static class TokenizerSyntaxException extends TokErrs.TokenizerException {
        private static final long serialVersionUID = 1L;

        public TokenizerSyntaxException(String message) {
            super(message);
        }
    }

    public static class SyntaxWarning extends TokErrs.TokenizerException {
        private static final long serialVersionUID = 1L;

        public SyntaxWarning(String message) {
            super(message);
        }
    }

    public static class SyntaxError extends TokErrs.TokenizerException {
        private static final long serialVersionUID = 1L;

        public SyntaxError(String message) {
            super(message);
        }
    }

    public static class TokenizerException extends Exception {
        private static final long serialVersionUID = 1L;

        public TokenizerException(String message) {
            super(message);
        }
    }

}
