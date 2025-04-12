package com.jaiva.tokenizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    public static char ESCAPE = '$';
    public static char END_LINE = '!';
    public static char COMMENT = '@';
    public static char COMMENT_OPEN = '{';
    public static char COMMENT_CLOSE = '}';
    public static String BLOCK_OPEN = "->";
    public static String BLOCK_CLOSE = "<~";
    public static String ASSIGNMENT = "<-";
    public static String THROW_ERROR = "<==";
    public static String ARRAY_ASSIGNMENT = "<-|";

    public class Operators {
        public static final String Exponentiation = "^";
        public static final List<String> DivMult = Arrays.asList("/", "*", "%");
        public static final List<String> AddSub = Arrays.asList("+", "-");
        public static final List<String> Bools = Arrays.asList("|", "&", ">", "<", "=");
        public static final List<String> DoubleBools = Arrays.asList("||", "&&", "!=", ">=", "<=");

        public static List<String> getArithmetic() {
            List<String> all = new ArrayList<>();
            all.add(Exponentiation);
            all.addAll(DivMult);
            all.addAll(AddSub);
            return all;
        }

        public static List<String> getBoolean() {
            List<String> all = new ArrayList<>();
            all.addAll(DoubleBools);
            all.addAll(Bools);
            return all;
        }

        public static List<String> getAll() {
            List<String> all = new ArrayList<>();
            all.add(Exponentiation);
            all.addAll(DivMult);
            all.addAll(AddSub);
            all.addAll(DoubleBools);
            all.addAll(Bools);
            return all;
        }

        public static int getType(String op) {
            if (Exponentiation.equals(op))
                return 0;
            else if (DivMult.contains(op))
                return 1;
            else if (AddSub.contains(op))
                return 2;
            else if (Bools.contains(op) || DoubleBools.contains(op))
                return 3;
            return -1;
        }
    }
}