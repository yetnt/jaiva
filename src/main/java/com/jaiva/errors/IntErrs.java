package com.jaiva.errors;

import com.jaiva.interpreter.symbol.*;
import com.jaiva.tokenizer.TokenDefault;
import com.jaiva.tokenizer.Token.TFuncCall;
import com.jaiva.tokenizer.Token.TFunction;
import com.jaiva.tokenizer.Token.TStatement;
import com.jaiva.tokenizer.Token.TVarReassign;
import com.jaiva.tokenizer.Token.TVarRef;

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
        public FrozenSymbolException(int lineNumber) {
            super("You tried to modify a frozen symbol on line " + lineNumber + ". Don't do that.");
        }

        /**
         * Error to throw when we know the symbol.
         * 
         * @param s The symbol in question
         */
        public FrozenSymbolException(Symbol s, int lineNumber) {
            super("The symbol [" + s.name + "] on line " + lineNumber + " is lowkey frozen. You can't modify it.");
        }

        /**
         * Error to throw when we know the symbol but not the line number.
         * 
         * @param s The symbol in question
         */
        public FrozenSymbolException(Symbol s) {
            super("The symbol [" + s.name + "] on line is lowkey frozen. You can't modify it.");
        }

        /**
         * Throw this overload when a user tries to modify a global symbol.
         * 
         * @param s        The symbol in question
         * @param isGlobal true
         */
        public FrozenSymbolException(Symbol s, boolean isGlobal, int lineNumber) {
            super("Now tell me. How do you try and modify a global symbol? Specifically, [" + s.name
                    + "] on line " + lineNumber + " . Shame be upon you!");
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
        public FunctionParametersException(BaseFunction s, int amtGiven, int lineNumber) {
            super(s.name + "() on line " + lineNumber + " only needs [" + ((TFunction) s.token).args.length
                    + "] parameters, and your goofy ahh gave " + amtGiven
                    + ". ☠️");
        }

        public FunctionParametersException(BaseFunction s, int lineNumber) {
            super(s.name + "() takes no input, why did you give it input on line " + lineNumber + "??");
        }
    }

    public static class DualVariableReassignWarning extends InterpreterException {
        private static final long serialVersionUID = 1L;

        public DualVariableReassignWarning(BaseVariable s, int lineNumber) {
            // tokenizer.
            super(s.name
                    + " on line " + lineNumber
                    + " has already been defined as either an array or some value. Do not reassign it as vice versa. If you know what you are doing however, place a ~ after the variable name in the declaration.");
        }

    }

    public static class TStatementResolutionException extends InterpreterException {
        private static final long serialVersionUID = 1L;

        /**
         * When the lhs or rhs of a TStatement doesnt match the type of the entire
         * statement.
         * 
         * @param s         The tstatement
         * @param side      "lhs" or "rhs"
         * @param incorrect The .toString() of the Object we received.
         */
        public TStatementResolutionException(TStatement s, String side, String incorrect) {
            super("The " + side + " of the statement " + s.statement + " on line " + s.lineNumber
                    + " couldn't be resolved to the correct type." +
                    " The statement is a " + (s.statementType == 0 ? "boolean" : "arithmetic")
                    + " however I received a " + incorrect);
        }

        public TStatementResolutionException(TokenDefault outer, TStatement s, String expected, String received) {
            super("i expected [" + s.statement + "] on line " + outer.lineNumber + " to resolve to a " + expected
                    + ". (I'm pretty sure a " + outer.name
                    + " does not take in a" + received + ")");
        }
    }

    public static class UnknownVariableException extends InterpreterException {
        private static final long serialVersionUID = 1L;

        public UnknownVariableException(TVarRef s) {
            super("Lowkey can't find variable named " + s.varName
                    + " that you used on line " + s.lineNumber + " anywhere. It prolly aint in this block's scope fr.");
        }

        public UnknownVariableException(String name, int lineNumber) {
            super("Lowkey can't find the variable named " + name
                    + " that you used on line " + lineNumber + " anywhere. It prolly aint in this block's scope fr.");
        }

        public UnknownVariableException(TokenDefault s) {
            super("Lowkey can't find the variable named " + s.name
                    + " that you used on line " + s.lineNumber + " anywhere. It prolly aint in this block's scope fr.");
        }

        public UnknownVariableException(TFuncCall s) {
            super("Lowkey can't find the variable named " + s.functionName
                    + " that you used on line " + s.lineNumber + " anywhere. It prolly aint in this block's scope fr.");
        }
    }

    public static class WtfAreYouDoingException extends InterpreterException {
        private static final long serialVersionUID = 1L;

        /**
         * Overload for when the user tries to use a function as if it were a variable
         * or vice versa
         * 
         * @param s            The symbol we got
         * @param correctClass The correct class.
         */
        public WtfAreYouDoingException(Object s, Class<?> correctClass, int lineNumber) {
            super("Alright bub. if you're going to start mixing and mathcing, you might as well write lua code."
                    + "(You tried using a " + s.getClass().getName() + " as if it were a " + correctClass.getName()
                    + " on line " + lineNumber);
        }

        /**
         * Overload for when the uyser's array index input doesnt resolve to an int.
         * 
         * @param s
         */
        public WtfAreYouDoingException(TVarRef s, Class<?> classWeGot) {
            super(s.index
                    + " on line " + s.lineNumber
                    + "could not be resolved to an int which can be used as an array index. deep. we instead got a "
                    + classWeGot.getName());
        }

        /**
         * These are for custom "wtf" errors.
         * 
         * @param s
         */
        public WtfAreYouDoingException(String s) {
            super(s);
        }

    }

    public static class WeirdAhhFunctionException extends InterpreterException {
        private static final long serialVersionUID = 1L;

        /**
         * When a function name does not resolve to a string that can actualy be used in
         * the vfs HashMap.
         * 
         * @param tFuncCall
         */
        public WeirdAhhFunctionException(TFuncCall tFuncCall) {
            super(tFuncCall.name + " on line " + tFuncCall.lineNumber
                    + " could NOT be resolved to a poper function name. Idk what else to tell you zawg");
        }
    }

    public static class StringCalcException extends InterpreterException {
        private static final long serialVersionUID = 1L;

        public StringCalcException(TStatement st) {
            super(st.statement + " on line " + st.lineNumber
                    + " contains invalid string operations. Not telling u what tho lolol.");
        }

        public StringCalcException(TStatement s, Exception e) {
            super(
                    e instanceof StringIndexOutOfBoundsException ? "Bro, whatever you did on line " + s.lineNumber
                            + ", it's out of the string's bounbds. fix it pls 🙏"
                            : "So something on line " + s.lineNumber
                                    + " parsed to a negative number, when sum else shouldnt have it yknow. I'm too lazy to finish writing this error tho.");
        }
    }

    /**
     * * This is a generic exception for when we don't know what the error is.
     * And it's not the user's fault.
     */
    public static class GenericException extends InterpreterException {
        private static final long serialVersionUID = 1L;

        public GenericException(String message) {
            super(message);
        }
    }

    public static class CimaException extends InterpreterException {

        private static final long serialVersionUID = 1L;

        public CimaException(String message, int lineNumber) {
            super("Ight. Stopped program on line " + lineNumber + ". Reason: " + message);
        }
    }

}
