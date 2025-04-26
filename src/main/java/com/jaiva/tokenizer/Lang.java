package com.jaiva.tokenizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
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
    public static char EXPORT_SYMBOL = '*';
    public static String COMMENT_DOC = "@*";
    public static char COMMENT_OPEN = '{';
    public static char COMMENT_CLOSE = '}';
    public static String BLOCK_OPEN = "->";
    public static String BLOCK_CLOSE = "<~";
    public static char STATEMENT_OPEN = '(';
    public static char STATEMENT_CLOSE = ')';
    public static String ASSIGNMENT = "<-";
    public static String THROW_ERROR = "<==";
    public static String ARRAY_ASSIGNMENT = "<-|";

    public static char ARRAY_OPEN = '[';
    public static char ARRAY_CLOSE = ']';
    public static char LENGTH_CHAR = '~';
    public static char FOR_SEPARATOR = '|';
    public static char ARGS_SEPARATOR = ',';
    public static char STRING = '"';

    public class Operators {
        public static final List<String> Exponentiation = Arrays.asList("^");
        public static final List<String> DivMult = Arrays.asList("%", "/", "*");
        public static final List<String> AddSub = Arrays.asList("+", "-");
        public static final List<String> Bools = Arrays.asList("|", "&", ">", "<", "=", "!");
        public static final List<String> DoubleBools = Arrays.asList("||", "&&", "!=", ">=", "<=");

        public static List<String> getArithmetic() {
            List<String> all = new ArrayList<>();
            all.addAll(Exponentiation);
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

        /**
         * Retrieves a combined list of all elements from various predefined categories.
         *
         * This method aggregates elements from the following lists:
         * - Exponentiation
         * - DivMult
         * - AddSub
         * - DoubleBools
         * - Bools
         *
         * @return A list containing all elements from the above categories.
         */
        public static List<String> getAll() {
            List<String> all = new ArrayList<>();
            all.addAll(Exponentiation);
            all.addAll(DivMult);
            all.addAll(AddSub);
            all.addAll(DoubleBools);
            all.addAll(Bools);
            return all;
        }

        /**
         * Retrieves a list of all predefined lists of strings.
         * 
         * This method aggregates several predefined lists, such as Exponentiation,
         * DivMult, AddSub, DoubleBools, and Bools, into a single list of lists.
         * 
         * @return A list containing all predefined lists of strings.
         */
        public static List<List<String>> getAllLists() {
            List<List<String>> all = new ArrayList<>();
            List<String> bools = new ArrayList<>();
            bools.addAll(Bools);
            bools.addAll(DoubleBools);

            all.add(Exponentiation);
            all.add(DivMult);
            all.add(AddSub);
            all.add(bools);
            return all;
        }

        public static int getType(String op) {
            if (Exponentiation.contains(op))
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

    public static ArrayList<String> dissalowedCharsForSymbolName() {
        ArrayList<String> dList = dissalowedChars();
        dList.remove(Character.toString(EXPORT_SYMBOL));
        return dList;
    }

    public static ArrayList<String> dissalowedChars() {
        StringBuilder all = new StringBuilder();
        all.append(Operators.getAll().toString().replaceAll(",", "").replaceAll(" ", "").replace("[", "").replace("]",
                ""));
        all.append(Character.toString(ESCAPE));
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