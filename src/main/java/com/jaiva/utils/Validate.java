package com.jaiva.utils;

import java.util.ArrayList;

import com.jaiva.tokenizer.Lang;
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
            if (Lang.Operators.getArithmetic().contains(String.valueOf(c))) {
                return 1;
            }
            if (Lang.Operators.getBoolean().contains(String.valueOf(c))) {
                return 0;
            }
        }
        return -1;
    }

    /**
     * Determines if a '-' character at a specified index in the input string
     * is a unary minus (as opposed to a subtraction operator).
     *
     * A '-' is considered a unary minus if the substring between the operator
     * before it and the '-' itself is empty or contains only whitespace.
     *
     * @param unaryMinusIndex         The index of the '-' character in the input
     *                                string.
     * @param opBeforeUnaryMinusIndex The index of the operator before the '-'
     *                                character.
     * @param inputString             The input string to analyze.
     * @return {@code true} if the '-' is a unary minus; {@code false} otherwise.
     * @throws StringIndexOutOfBoundsException if the indices are out of bounds
     *                                         for the given input string.
     */
    public static boolean isUnaryMinus(int unaryMinusIndex, int opBeforeUnaryMinusIndex, String inputString) {
        return inputString.substring(opBeforeUnaryMinusIndex + 1, unaryMinusIndex).trim().isEmpty();
    }

    /**
     * Checks if the given operator index is within any pair of quotation marks in
     * the provided line.
     *
     * @param line    The input string to be analyzed.
     * @param opIndex The index of the operator to check within the line.
     * @return The index of the quotation pair (0-based) that contains the operator
     *         index,
     *         or -1 if the operator index is not within any quotation pair.
     */
    public static int isOpInQuotePair(String line, int opIndex) {
        ArrayList<Tuple2<Integer, Integer>> quotePairs = Find.quotationPairs(line);
        for (int i = 0; i < quotePairs.size(); i++) {
            Tuple2<Integer, Integer> tuple2 = quotePairs.get(i);
            if (opIndex > tuple2.first && opIndex < tuple2.second) {
                return i;
            }
        }
        return -1;
    }
}
