package com.yetnt.tokenizer;

import java.util.Set;

/**
 * The Lang class defines a set of constants and utility methods for tokenizing
 * and parsing JAIVA. It includes definitions for comment markers,
 * block delimiters, assignment operators, and sets of arithmetic and boolean
 * operators.
 * <p>
 * Constants:
 * - COMMENT: Character used to denote a comment.
 * - COMMENT_OPEN: Character used to open a comment block.
 * - COMMENT_CLOSE: Character used to close a comment block.
 * - BLOCK_OPEN: String used to open a block.
 * - BLOCK_CLOSE: String used to close a block.
 * - ASSIGNMENT: String used for assignment operations.
 * - ARRAY_ASSIGNMENT: String used for array assignment operations.
 * - ARITHMATIC_OPERATIONS: Set of strings representing arithmetic operations.
 * - BOOLEAN_OPERATORS: Set of strings representing boolean operators.
 * <p>
 * Methods:
 * - containsOperator(char[] string): Checks if the given character array
 * contains
 * any arithmetic or boolean operators. Returns 1 if an arithmetic operator is
 * found, 0 if a boolean operator is found, and -1 if no operators are found.
 * <p>
 * Usage:
 * This class can be used to tokenize and parse JAIVA by identifying
 * various operators and delimiters defined in the constants.
 */
public class Lang {
    public static char COMMENT = '@';
    public static char COMMENT_OPEN = '{';
    public static char COMMENT_CLOSE = '}';
    public static String BLOCK_OPEN = "->";
    public static String BLOCK_CLOSE = "<~";
    public static String ASSIGNMENT = "<-";
    public static String THROW_ERROR = "<==";
    public static String ARRAY_ASSIGNMENT = "<-|";
    public static Set<String> ARITHMATIC_OPERATIONS = Set.of("+", "-", "*", "/", "=");
    public static Set<String> BOOLEAN_OPERATORS = Set.of(">=", "<=", "!=", "<", ">", "&", "|", "&&", "||");

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
            if (ARITHMATIC_OPERATIONS.contains(String.valueOf(c))) {
                return 1;
            }
            if (BOOLEAN_OPERATORS.contains(String.valueOf(c))) {
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
            if (ARITHMATIC_OPERATIONS.contains(String.valueOf(string[i]))) {
                return i;
            }
            if (BOOLEAN_OPERATORS.contains(String.valueOf(string[i]))) {
                return i;
            }
        }
        return -1;
    }
}