package com.yetnt;

public class Errors {
    public static class TokenizerException extends Exception {
        private static final long serialVersionUID = 1L;

        public TokenizerException(String message) {
            super(message);
        }
    }

    public static class TokenizerSyntaxException extends TokenizerException {
        private static final long serialVersionUID = 1L;

        public TokenizerSyntaxException(String message) {
            super(message);
        }
    }

    public static class TokenizerSyntaxWarning extends TokenizerException {
        private static final long serialVersionUID = 1L;

        public TokenizerSyntaxWarning(String message) {
            super(message);
        }
    }

    public static class TokenizerSyntaxError extends TokenizerException {
        private static final long serialVersionUID = 1L;

        public TokenizerSyntaxError(String message) {
            super(message);
        }
    }

    public static class TokenizerSyntaxCriticalError extends TokenizerException {
        private static final long serialVersionUID = 1L;

        public TokenizerSyntaxCriticalError(String message) {
            super(message);
        }
    }
}
