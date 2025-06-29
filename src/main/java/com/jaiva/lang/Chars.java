package com.jaiva.lang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.jaiva.utils.Find;

/**
 * The Chars class defines a set of constants and utility methods for tokenizing
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
public class Chars {
    /**
     * The escape character used in strings.
     */
    public static char ESCAPE = '$';
    /**
     * The end line character used to denote the end of a line.
     */
    public static char END_LINE = '!';
    /**
     * The character used to denote a single line comment.
     */
    public static char COMMENT = '@';
    /**
     * The character used to denote that the symbol defined should be exported.
     */
    public static char EXPORT_SYMBOL = '*';
    /**
     * The character used to denote a comment which is a documentation comment.
     */
    public static String COMMENT_DOC = "@*";
    /**
     * The character used to denote the opening of a comment block.
     */
    public static char COMMENT_OPEN = '{';
    /**
     * The character used to denote the closing of a comment block.
     */
    public static char COMMENT_CLOSE = '}';
    /**
     * The character used to denote the opening of a block.
     */
    public static String BLOCK_OPEN = "->";
    /**
     * The character used to denote the closing of a block.
     */
    public static String BLOCK_CLOSE = "<~";
    /**
     * The character used to denote the opening of a statement.
     */
    public static char STATEMENT_OPEN = '(';
    /**
     * The character used to denote the closing of a statement.
     */
    public static char STATEMENT_CLOSE = ')';
    /**
     * The character used to denote any assignment.
     */
    public static String ASSIGNMENT = "<-";
    /**
     * The character used to denote an assignment to an error.
     * <p>
     * Why is there a special separate character for this? Yes.
     */
    public static String THROW_ERROR = "<==";
    /**
     * The character used to denote the creation of a new array.
     */
    public static String ARRAY_ASSIGNMENT = "<-|";

    /**
     * The character used to denote the opening of an array access.
     */
    public static char ARRAY_OPEN = '[';
    /**
     * The character used to denote the closing of an array access.
     */
    public static char ARRAY_CLOSE = ']';
    /**
     * The character used to get the length of an array or string variable.
     */
    public static char LENGTH_CHAR = '~';
    /**
     * The character used to separate the arguments in a colonize loop
     */
    public static char FOR_SEPARATOR = '|';
    /**
     * The character used to separate the arguments in a function call.
     */
    public static char ARGS_SEPARATOR = ',';
    /**
     * The character used to denote a string.
     */
    public static char STRING = '"';
    /**
     * The character used to denote a function's argument is optional.
     */
    public static char OPTIONAL_ARG = '?';
    /**
     * Represents the ternary operator symbol used in the language.
     */
    public static String TERNARY = "=>";

    /**
     * The Operators class defines a set of arithmetic and boolean operators.
     */
    public class Operators {
        /**
         * List of exponentiation operators. (Well only one.)
         */
        public static final List<String> Exponentiation = Arrays.asList("^");
        /**
         * List of division, multiplication, and modulus operators.
         */
        public static final List<String> DivMult = Arrays.asList("%", "/", "*");
        /**
         * List of addition and subtraction operators.
         */
        public static final List<String> AddSub = Arrays.asList("+", "-");
        /**
         * Gotta have them shifts ong
         */
        public static final List<String> Shift = Arrays.asList(">>", "<<", ">x", "<x");
        /**
         * Bitwise operations
         */
        public static final List<String> Bitwise = Arrays.asList("&", "|");

        /**
         * Comparison operators
         * <p>
         * The ! operator is listed here, because
         * {@link Find#leastImportantOperator(String)} works on a character by character
         * basis. Therefore we can't actually just get 2 length operators immediately,
         * it will later add the next part of the operator when needed. Therefore,
         * {@code !} only exists, such that {@code !=} can also exist. If you don't get
         * it. Your fault for trying to udnerstand this mess. But an exmaple would be
         * the {@link Chars.Operators#Bitwise} which contains the single operators
         * {@code &} and {@code |}. If they didn't exist, then
         * {@link Chars.Operators#Logical}'s Operators being the {@code &&} and
         * {@code ||} would break. Just like deleting {@code !} would break {@code !=}.
         * Only difference being that {@code !} is not a valid operator, while every
         * other case is.
         */
        public static final List<String> Comparison = Arrays.asList("=", "!=", ">=", "<=", "<", ">", "!", "?");

        /**
         * Logical operators
         */
        public static final List<String> Logical = Arrays.asList("||", "&&", "'");

        // /**
        // * List of boolean operators.
        // */
        // public static final List<String> Bools = Arrays.asList(">", "<", "=", "!",
        // "?");

        // /**
        // * List of double boolean operators.
        // */
        // public static final List<String> DoubleBools = Arrays.asList("||", "&&",
        // "!=", ">=", "<=");

        /**
         * List of all arithmetic operators.
         *
         * This method aggregates elements from the following lists:
         * - Exponentiation
         * - DivMult
         * - AddSub
         *
         * @return A list containing all arithmetic operators from the above categories.
         */
        public static List<String> getArithmetic() {
            List<String> all = new ArrayList<>();
            all.addAll(Exponentiation);
            all.addAll(DivMult);
            all.addAll(AddSub);
            return all;
        }

        /**
         * List of all boolean (Catch all term for anything returning a boolean)
         * operators.
         *
         * This method aggregates elements from the following lists:
         * - Comparison
         * - Logical
         *
         * @return A list containing all boolean operators from the above categories.
         */
        public static List<String> getBoolean() {
            List<String> all = new ArrayList<>();
            all.addAll(Comparison);
            all.addAll(Logical);
            return all;
        }

        public static List<String> getInteger() {
            List<String> all = new ArrayList<>();
            all.addAll(Shift);
            all.addAll(Bitwise);
            return all;
        }

        /**
         * Retrieves a combined list of all elements from various predefined categories.
         *
         * This method aggregates elements from the following lists:
         * - Exponentiation
         * - DivMult
         * - AddSub
         * - Shifts
         * - Bitwise
         * - Comparison
         * - Logical
         *
         * @return A list containing all elements from the above categories.
         */
        public static List<String> getAll() {
            List<String> all = new ArrayList<>();
            all.addAll(Exponentiation);
            all.addAll(DivMult);
            all.addAll(AddSub);
            all.addAll(Shift);
            all.addAll(Bitwise);
            all.addAll(Comparison);
            all.addAll(Logical);
            return all;
        }

        /**
         * Retrieves a combined list of all elements from various predefined categories.
         *
         * This method uses {@link Chars.Operators#getAll()} to get all the operators.
         *
         * @return A list containing all the operators as characters
         */
        public static List<Character> getAllChars() {
            List<Character> all = new ArrayList<>();
            getAll().forEach(str -> all.add(str.charAt(0)));

            return new ArrayList<>(new HashSet<>(all));
        }

        /**
         * Retrieves a list of all predefined lists of strings.
         * 
         * This method aggregates several predefined lists, such as Exponentiation,
         * DivMult, AddSub, Shift, Bitwise, Comparison and Logical, into a single list
         * of lists.
         * 
         * @return A list containing all predefined lists of strings.
         */
        public static List<List<String>> getAllLists() {
            List<List<String>> all = new ArrayList<>();
            all.add(Exponentiation);
            all.add(DivMult);
            all.add(AddSub);
            all.add(Shift);
            all.add(Bitwise);
            all.add(Comparison);
            all.add(Logical);
            return all;
        }

        /**
         * Determines the type of operator based on the provided string.
         * <p>
         * This follows your basic rules of Maths where exponentiation is the highest
         * priority and add/sub is lowest (With boolean operators being even lower.)
         *
         * @param op The operator string to check.
         * @return An integer representing the type of operator:
         *         0 - Exponentiation
         *         1 - Division/Multiplication
         *         2 - Addition/Subtraction
         *         3 - Shifts
         *         4 - Bitwise
         *         5 - Comparison
         *         6 - Logical
         *         -1 - Not an operator
         */
        public static int getType(String op) {
            if (Exponentiation.contains(op))
                return 0;
            else if (DivMult.contains(op))
                return 1;
            else if (AddSub.contains(op))
                return 2;
            else if (Shift.contains(op))
                return 3;
            else if (Bitwise.contains(op))
                return 4;
            else if (Comparison.contains(op))
                return 5;
            else if (Logical.contains(op))
                return 6;
            else
                return -1;
        }
    }

    /**
     * Returns an arraylist of characters that are not allowed in a symbol name.
     * <p>
     * This is the exact same as {@link #dissalowedChars()} but without the
     * {@link Chars#EXPORT_SYMBOL}.
     * 
     * @return An arraylist of characters that are not allowed in a symbol name.
     */
    public static ArrayList<String> dissalowedCharsForSymbolName() {
        ArrayList<String> dList = dissalowedChars();
        dList.remove(Character.toString(EXPORT_SYMBOL));// remove the * symbol
        dList.remove("x"); // removed the letter x that comes from <x and >x
        return dList;
    }

    /**
     * Returns an arraylist of characters that cannot be used randomly.
     * 
     * @return An arraylist of characters that cannot be used randomly.
     */
    public static ArrayList<String> dissalowedChars() {
        StringBuilder all = new StringBuilder();
        all.append(Operators.getAll().toString().replaceAll(",", "").replaceAll(" ", "").replace("[", "").replace("]",
                ""));
        all.append(Character.toString(ESCAPE));
        all.append(Character.toString(OPTIONAL_ARG));
        all.append(Character.toString(END_LINE));
        all.append(Character.toString(COMMENT));
        all.append(Character.toString(COMMENT_OPEN));
        all.append(Character.toString(COMMENT_CLOSE));
        all.append(Character.toString(STATEMENT_OPEN));
        all.append(Character.toString(STATEMENT_CLOSE));
        all.append(BLOCK_OPEN);
        all.append(BLOCK_CLOSE);
        all.append(COMMENT_DOC);
        all.append(ASSIGNMENT);
        all.append(THROW_ERROR);
        all.append(ARRAY_ASSIGNMENT);
        all.append(Character.toString(LENGTH_CHAR));
        all.append(Character.toString(ARRAY_OPEN));
        all.append(Character.toString(ARRAY_CLOSE));
        all.append(Character.toString(ARGS_SEPARATOR));
        all.append(Character.toString(FOR_SEPARATOR));
        all.append(Character.toString(STRING));
        return new ArrayList<>(new HashSet<>(Arrays.asList(all.toString().split(""))));
    }
}