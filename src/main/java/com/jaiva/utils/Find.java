package com.jaiva.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jaiva.lang.Chars.Operators;
import com.jaiva.tokenizer.Token;

/**
 * This class provides utility methods for finding specific characters or
 * operators in strings,
 * as well as determining the context of mathematical or logical statements.
 * <p>
 * It includes methods for finding the index of enclosing characters,
 * identifying
 * the least important operator,
 * and handling quotation marks in strings.
 * <p>
 * The class also contains inner classes for representing multiple lines output
 * and operator indices.
 * <p>
 * Any if not all functions and or sub classes which are related to "finding"
 * something, are in this class.
 */
public class Find {

    /**
     * Find the index of the enclosing character in a string.
     * This takes in startCount and endCount so that it doesnt have to traverse the
     * entire string again but rather just the new portion.
     * 
     * overload for codeblocks
     * 
     * @param line  The string to search in.
     * @param start The starting character.
     * @param end   The ending character.
     * @return
     */
    public static MultipleLinesOutput closingCharIndexML(String line, String start,
            String end,
            int startCount, int endCount, String previousLines, String type, String[] args, Token<?> blockChain,
            int lineNumber) {
        if (start.length() > 2 || end.length() > 2)
            throw new IllegalArgumentException("Arguments must be at most 2 characters long!");
        boolean isStart = true;

        for (int i = 0; i < line.length(); i++) {
            if (!start.equals(end)) {
                if (line.charAt(i) == start.charAt(0)) {
                    startCount += (i + 1 < line.length() && line.charAt(i + 1) == start.charAt(1)) ? 1 : 0;
                } else if (line.charAt(i) == end.charAt(0)) {
                    endCount += (i + 1 < line.length() && line.charAt(i + 1) == end.charAt(1)) ? 1 : 0;
                }
                if (startCount == endCount && startCount != 0) {
                    return new MultipleLinesOutput(startCount, endCount, previousLines + line, type,
                            args, blockChain, lineNumber);
                }
            } else {
                if (line.charAt(i) == end.charAt(0)) {
                    if (i + 1 < line.length() && line.charAt(i + 1) == end.charAt(1)) {
                        // if (!isStart) {
                        return new MultipleLinesOutput(i, i, previousLines + line, type, args, blockChain, lineNumber);
                        // }
                    }
                    // System.out.println("Found!");
                    isStart = !isStart;
                }
            }
        }
        return new MultipleLinesOutput(startCount, endCount, previousLines + line, type, args, blockChain, lineNumber);
    }

    /**
     * Find the index of the enclosing character in a string.
     * This takes in startCount and endCount so that it doesnt have to traverse the
     * entire string again but rather just the new portion.
     * 
     * @param line  The string to search in.
     * @param start The starting character.
     * @param end   The ending character.
     * @return
     */
    public static MultipleLinesOutput closingCharIndexML(String line, char start, char end,
            int startCount,
            int endCount) {
        boolean isStart = true;
        for (int i = 0; i < line.length(); i++) {
            if (start != end) {
                if (line.charAt(i) == start) {
                    startCount++;
                } else if (line.charAt(i) == end) {
                    endCount++;
                }
                if (startCount == endCount && startCount != 0) {
                    return new MultipleLinesOutput(startCount, endCount, true);
                }
            } else {
                if (line.charAt(i) == end) {
                    if (!isStart) {
                        return new MultipleLinesOutput(i, i, true);
                    }
                    isStart = !isStart;
                }
            }
        }
        return new MultipleLinesOutput(startCount, endCount, true);
    }

    /**
     * Find the index of the enclosing character in a string.
     * 
     * @param line  The string to search in.
     * @param start The starting character.
     * @param end   The ending character.
     * @return
     */
    public static int closingCharIndex(String line, char start, char end) {
        int startCount = 0;
        int endCount = 0;
        boolean isStart = true;
        for (int i = 0; i < line.length(); i++) {
            if (start != end) {
                if (line.charAt(i) == start) {
                    startCount++;
                } else if (line.charAt(i) == end) {
                    endCount++;
                }
                if (startCount == endCount && (startCount != 0)) {
                    return i;
                }
            } else {
                if (line.charAt(i) == end) {
                    if (!isStart) {
                        return i;
                    }
                    isStart = !isStart;
                }
            }
        }
        return -1;
    }

    /**
     * Returns the index of the outermost operator (at top level, i.e. depth==0).
     * Returns -1 if there is no operator at the outer level.
     */
    public static int outermostOperatorIndex(String s) {
        int depth = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(' || c == '[') {
                depth++;
            } else if (c == ')' || c == ']') {
                depth--;
            } else if (depth == 0 && Validate.isOperator(c)) {
                // We found an operator while at depth 0: that's our outermost operator.
                return i;
            }
        }
        return -1;
    }

    /**
     * Method will return the index of the opening brace which is part of the last,
     * outermost brace pair. Both () and [] are checked for agasinst each other.
     * This will r
     * <p>
     * <blockquote>
     * 
     * <pre>
     * "func()" returns 4
     * "()[]" returns 2
     * "()" returns 0
     * </pre>
     * 
     * </blockquote>
     * 
     * @param line Input.
     * @returns int
     */
    public static int lastOutermostBracePair(String line) {
        ArrayList<Integer> indexes = new ArrayList<>();
        int depth = 0;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '(' || c == '[') {
                depth++;
                if (depth == 1)
                    indexes.add(i);
            }
            if (c == ')' || c == ']') {
                depth--;
            }
        }
        List<Integer> rIndexes = indexes.reversed();
        for (Integer i : rIndexes) {
            String sString = line.substring(i, line.length());
            char openingChar = line.charAt(i);
            int closingCharI = closingCharIndex(sString, openingChar, openingChar == '(' ? ')' : ']');
            if (closingCharI == sString.length() - 1 || closingCharI == sString.length() - 2)
                return i;
        }

        return -1;
    }

    public class TStatementOpIndex {
        public String op;
        public int index;
        public int tStatementType;

        public TStatementOpIndex(String op, int index, int type) {
            this.op = op;
            this.index = index;
            switch (type) {
                case 0:
                    tStatementType = 1; // Exponentiation
                    break;
                case 1:
                    tStatementType = 1; // DivMult
                    break;
                case 2:
                    tStatementType = 1; // AddSub
                    break;
                case 3:
                    tStatementType = 0; // Bools
                    break;
                default:
                    throw new IllegalArgumentException("Invalid type: " + type);
            }
        }
    }

    public static class LeastImportantOperator {
        public String op;
        public int index;
        public int tStatementType;

        public LeastImportantOperator(String op, int index, int group) {
            this.op = op;
            this.index = index;
            switch (group) {
                case 0:
                    tStatementType = 1; // Exponentiation
                    break;
                case 1:
                    tStatementType = 1; // DivMult
                    break;
                case 2:
                    tStatementType = 1; // AddSub
                    break;
                case 3:
                    tStatementType = 0; // Bools
                    break;
                default:
                    throw new IllegalArgumentException("Invalid group: " + group);
            }
        }

        /**
         * Constructor for no return value.
         */
        public LeastImportantOperator() {
            this.op = null;
            this.index = -1;
            this.tStatementType = -1;
        }
    }

    /**
     * Determines the least important operator in a given mathematical or logical
     * statement.
     * The method analyzes the statement to find operators outside of parentheses or
     * brackets,
     * categorizes them by precedence, and identifies the least important operator
     * based on
     * its type and position.
     * 
     * This is for the TStatement class to use.
     *
     * @param statement The input string representing a mathematical or logical
     *                  expression.
     *                  It may contain operators, parentheses, and brackets.
     * @return A {@code LeastImportantOperator} object containing the operator, its
     *         position,
     *         and its precedence group. If no operator is found, an empty
     *         {@code LeastImportantOperator} object is returned.<br>
     *
     *         The method works as follows:<br>
     *         1. Iterates through the statement to identify operators outside of
     *         parentheses or brackets.<br>
     *         2. Categorizes operators by precedence groups (e.g., exponentiation,
     *         multiplication/division,
     *         addition/subtraction, boolean operators).<br>
     *         3. Identifies the least important operator based on its precedence
     *         group and position.<br>
     *         4. Handles multi-character operators (e.g., "&&", "||", ">=", "<=",
     *         "==").<br>
     *         <br>
     *
     *         Note: The method assumes the existence of helper classes and methods
     *         such as<br>
     *         {@code Validate.isOperator(char)}, {@code Operators.getAll()}, and
     *         {@code Operators.getType(String)}.
     */
    public static LeastImportantOperator leastImportantOperator(String statement) {
        int level = 0;
        ArrayList<Integer> indexes1 = new ArrayList<>();
        for (int i = 0; i < statement.length(); i++) {
            char c = statement.charAt(i);
            if ((c == '(' || c == '[') && Validate.isOpInQuotePair(statement, i) == -1)
                level++;
            else if ((c == ')' || c == ']') && Validate.isOpInQuotePair(statement, i) == -1)
                level--;
            else if (level == 0 && Validate.isOperator(c) && (Validate.isOpInQuotePair(statement, i) == -1)) {
                indexes1.add(i);
            }
        }
        indexes1 = sanitizeStatement(statement, indexes1);

        if (indexes1.isEmpty())
            return new LeastImportantOperator(); // exit early if no operators are found.
        int group = -1; // 0 = Exponentiation, 1 = DivMult, 2 = AddSub, 3 = Bools

        ArrayList<Integer> indexes2 = new ArrayList<>();

        // old for
        // for (String op : Operators.getAll().reversed()) {
        // for (int opIndex : indexes1) {
        // String opString = statement.substring(opIndex, opIndex + op.length());
        // if (!opString.equals(op))
        // continue;
        // if (group == -1) {
        // group = Operators.getType(op);
        // indexes2.add(opIndex);
        // } else if (group == Operators.getType(op)) {
        // indexes2.add(opIndex);
        // } else {
        // continue;
        // }
        // }
        // }

        // new for
        for (int i = Operators.getAllLists().size() - 1; i >= 0; i--) {
            List<String> list = Operators.getAllLists().get(i);
            for (int opIndex : indexes1) {
                String op = statement.substring(opIndex, opIndex + 1); // stuff thats 2 chars long are generally in the
                                                                       // same gorup.
                if (!list.contains(op))
                    continue;
                if (group == -1) {
                    group = i;
                    indexes2.add(opIndex);
                } else if (group == Operators.getType(op)) {
                    indexes2.add(opIndex);
                } else {
                    continue;
                }
            }
        }

        List<Character> multiOpChars = Arrays.asList('|', '&', '>', '<', '='); // If the op is 2 chars long, it's last
                                                                               // char must be one of these.

        if (indexes2.isEmpty())
            return new LeastImportantOperator();
        int finalIndex = indexes2.getLast();

        // The operator can sometimes be the length of 2
        String op = statement.substring(finalIndex,
                multiOpChars.contains(statement.charAt(finalIndex + 1)) ? (finalIndex + 2)
                        : (finalIndex + 1));

        return new LeastImportantOperator(
                op, finalIndex,
                group);
    }

    /**
     * Sanitizes a list of operator indexes in a given statement by removing indexes
     * that correspond to unary minus operators, while preserving other operator
     * indexes.
     *
     * @param statement The input string containing the statement to be analyzed.
     * @param opIndexes A list of integer indexes representing the positions of
     *                  operators
     *                  in the statement.
     * @return A sanitized list of operator indexes, excluding those that
     *         correspond to unary minus operators.
     */
    public static ArrayList<Integer> sanitizeStatement(String statement, ArrayList<Integer> opIndexes) {
        ArrayList<Integer> sanitized = new ArrayList<>();

        for (int i = 0; i < opIndexes.size(); i++) {
            int opIndex = opIndexes.get(i);
            if (i == 0) {
                if (opIndexes.size() == 1 || !(statement.charAt(opIndex) == '-'))
                    sanitized.add(opIndex);
                continue;
            }
            int opIndexBeforeUnary = opIndexes.get(i - 1);
            boolean isUnary = Validate.isUnaryMinus(opIndex, opIndexBeforeUnary, statement);
            if (!isUnary)
                sanitized.add(opIndex);
        }

        return sanitized;
    }

    /**
     * Get the index of the operator in the given character array.
     * 
     * @param string
     * @return
     */
    public static int operatorIndex(char[] string) {
        for (int i = 0; i < string.length; i++) {
            if (Operators.getArithmetic().contains(String.valueOf(string[i]))) {
                return i;
            }
            if (Operators.getBoolean().contains(String.valueOf(string[i]))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds and returns all pairs of indices representing the positions of matching
     * quotation marks in the given string. Each pair consists of the starting index
     * of an opening quotation mark and the ending index of the corresponding
     * closing
     * quotation mark.
     *
     * @param line The input string to search for quotation mark pairs.
     * @return An ArrayList of Tuple2 objects, where each Tuple2 contains two
     *         integers:
     *         the starting and ending indices of a pair of matching quotation
     *         marks.
     *         If no pairs are found, an empty list is returned.
     */
    public static ArrayList<Tuple2<Integer, Integer>> quotationPairs(String line) {
        ArrayList<Tuple2<Integer, Integer>> arr = new ArrayList<>();
        int oldCharIndex = -1;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (oldCharIndex == -1) {
                    oldCharIndex = i;
                } else {
                    arr.add(new Tuple2<Integer, Integer>(oldCharIndex, i));
                    oldCharIndex = -1;
                }
            }
        }
        return arr;
    }

}
