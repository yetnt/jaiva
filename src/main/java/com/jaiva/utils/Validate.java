package com.jaiva.utils;

import java.util.ArrayList;

import com.jaiva.lang.Chars;
import com.jaiva.tokenizer.Token;

/**
 * Validate class is a utils class where methods which "validate" the
 * existance of foo in bar or some other boolean input.
 * <p>
 * Or Validate somethign such that if it fails, it throws an error.
 * 
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
     * <p>
     * 
     * @param c The character
     * @return boolean indicating whether the given char is a string.
     */
    public static boolean isOperator(char c) {
        return Chars.Operators.getAllChars().contains(c);
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
            if (Chars.Operators.getArithmetic().contains(String.valueOf(c))) {
                return 1;
            }
            if (Chars.Operators.getBoolean().contains(String.valueOf(c))) {
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

    /**
     * Class to represent the result of validating a symbol name.
     * This class exists such that if it is a valid symbol, we can use it.
     */
    public static class IsValidSymbolName {
        /**
         * The invalid character found in the symbol name, if any.
         */
        public String op;
        /**
         * Indicates whether the symbol name is valid or not.
         */
        public boolean isValid = true;

        /**
         * Constructor for IsValidSymbolName.
         *
         * @param o the invalid character found in the symbol name
         * @param v the validity status of the symbol name
         */
        public IsValidSymbolName(String o, boolean v) {
            op = o;
            isValid = v;
        }

        /**
         * Default constructor for IsValidSymbolName.
         * Initializes the object with default values.
         */
        public IsValidSymbolName() {
        }
    }

    /**
     * Validates whether a given string is a valid symbol name.
     *
     * This method checks each character in the input string against a set of
     * disallowed characters for symbol names. If any character in the string
     * is found in the disallowed set, the method returns an instance of
     * {@code IsValidSymbolName} indicating the invalid character and a
     * validation status of {@code false}. If no disallowed characters are
     * found, it returns an instance of {@code IsValidSymbolName} with a
     * validation status of {@code true}.
     *
     * @param line the string to validate as a symbol name
     * @return an {@code IsValidSymbolName} object containing the validation
     *         result and the first invalid character (if any)
     */
    public static IsValidSymbolName isValidSymbolName(String line) {
        for (char c : line.toCharArray()) {
            if (Chars.dissalowedCharsForSymbolName().contains(Character.toString(c))) {
                return new IsValidSymbolName(Character.toString(c), false);
            }
        }
        return new IsValidSymbolName();
    }
}
