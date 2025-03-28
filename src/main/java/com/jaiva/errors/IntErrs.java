package com.jaiva.errors;

import com.jaiva.interpreter.symbol.*;

public class IntErrs {

    public static class InterpreterException extends Exception {
        private static final long serialVersionUID = 1L;

        public InterpreterException(String message) {
            super(message);
        }
    }

    public static class FrozenSymbolException extends InterpreterException {
        private static final long serialVersionUID = 1L;

        /**
         * Base error to throw. Use this when you REALLY don't know the symbol instance.
         * This is an esolang but try be as descriptive as possible.
         */
        public FrozenSymbolException() {
            super("You tried to modify a frozen symbol. Don't do that.");
        }

        /**
         * Error to throw when we know the symbol.
         * 
         * @param s The symbol in question
         */
        public FrozenSymbolException(Symbol s) {
            super("The symbol [" + s.name + "] is lowkey frozen. You can't modify it.");
        }

        /**
         * Throw this overload when a user tries to modify a global symbol.
         * 
         * @param s        The symbol in question
         * @param isGlobal true
         */
        public FrozenSymbolException(Symbol s, boolean isGlobal) {
            super("Now tell me. How do you try and modify a global symbol? Specifically, [" + s.name
                    + "]. Shame be upon you!");
        }
    }

    public static class FunctionParametersException extends InterpreterException {
        private static final long serialVersionUID = 1L;

        /**
         * Overload for too many or too little parameters (When it does accept
         * parameters)
         * 
         * @param s        base function instance
         * @param amtGiven the amount of parameters they gave.
         */
        public FunctionParametersException(BaseFunction s, int amtGiven) {
            super(s.name + "() only needs [" + s.token.args.length + "] parameters, and your goofy ahh gave " + amtGiven
                    + ". ☠️");
        }

        public FunctionParametersException(BaseFunction s) {
            super(s.name + "() takes no input, why did you give it input??");
        }
    }

    public static class DualVariableReassignWarning extends InterpreterException {
        private static final long serialVersionUID = 1L;

        public DualVariableReassignWarning(BaseVariable s) {
            // TODO: Implement the ~ as a flag that dual reassingment is allowed in the
            // tokenizer.
            super(s.name
                    + " has already been defined as either an array or some value. Do not reassign it as vice versa. If you know what you are doing however, place a ~ after the variable name in the declaration.");
        }

    }
}
