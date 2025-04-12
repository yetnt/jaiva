package com.jaiva.utils;

import com.jaiva.tokenizer.Lang;
import com.jaiva.tokenizer.Lang.BODMAS;
import com.jaiva.tokenizer.Token;

/**
 * @class
 *        Validate class is a utils class where methods which "validate" the
 *        existance of foo in bar or some other boolean input.
 */
public class Validate {
    /**
     * These are for your if, while loops, for loops, where we need to make sure
     * that its NOT an integer, string or null.
     * It has to be TFuncCall, TVarRef, TStatement or a primitive boolean.
     * 
     * @param t
     * @return
     */
    public static boolean isValidBoolInput(Object t) {
        return t instanceof Token<?>.TFuncCall || t instanceof Token<?>.TVarRef || t instanceof Token<?>.TIfStatement
                || t instanceof Token<?> || t instanceof Boolean;
    }

    /**
     * Simple Check.
     * <p>
     * This is made only for Context Dispatcher
     * <p>
     * Checks if a character is considered an operator.
     */
    public static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '|' || c == '>' || c == '<' || c == '&' || c == '!'
                || c == '=' | c == '%' || c == '^';
    }

    /**
     * Checks if the given character array contains any arithmetic or boolean
     * operators.
     * Returns 1 if an arithmetic operator is found, 0 if a boolean operator is
     * found, and -1 if no operators are found.
     *
     * @param string
     * @return
     */
    public static int containsOperator(char[] string) {
        for (char c : string) {
            if (Lang.BODMAS.ARITHMATIC_OPERATIONS().contains(String.valueOf(c))) {
                return 1;
            }
            if (Lang.BODMAS.BOOLEAN_OPERATORS().contains(String.valueOf(c))) {
                return 0;
            }
        }
        return -1;
    }

    /**
     * Get the index of the operator in the given character array.
     * 
     * @param string
     * @return
     */
    public static int getOperatorIndex(char[] string) {
        for (int i = 0; i < string.length; i++) {
            if (BODMAS.ARITHMATIC_OPERATIONS().contains(String.valueOf(string[i]))) {
                return i;
            }
            if (BODMAS.BOOLEAN_OPERATORS().contains(String.valueOf(string[i]))) {
                return i;
            }
        }
        return -1;
    }
}
