package com.jaiva.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jaiva.lang.Chars;
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
//            boolean char1equals = line.charAt(i) == end.charAt(0);
//            boolean char2Equals = i + 1 < line.length() && line.charAt(i + 1) == end.charAt(1);
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
                        return new MultipleLinesOutput(i, i, previousLines + line, type, args, blockChain, lineNumber);
                    }
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
     * @param line       The string to search in.
     * @param start      The starting character.
     * @param end        The ending character.
     * @param startCount The start count from another {@link MultipleLinesOutput}
     *                   object
     * @param endCount   The end count from another {@link MultipleLinesOutput}
     *                   object
     * @return A MultipleLinesOutput Object indicating that either this line needs continuation or its complete.
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
                    isStart = false;
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
     * @return The index of the closing char. -1 if none found.
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
                    isStart = false;
                }
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
     * @return the index where the outermost brace pair starts
     */
    public static int lastOutermostBracePair(String line) {
        ArrayList<Integer> indexes = new ArrayList<>();
        int depth = 0;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if ((c == '(' || c == '[') && Validate.isOpInQuotePair(line, i) == -1) {
                depth++;
                if (depth == 1)
                    indexes.add(i);
            }
            if ((c == ')' || c == ']') && Validate.isOpInQuotePair(line, i) == -1) {
                depth--;
            }
        }
        List<Integer> rIndexes = indexes.reversed();
        for (Integer i : rIndexes) {
            String sString = line.substring(i);
            char openingChar = line.charAt(i);
            int closingCharI = closingCharIndex(sString, openingChar, openingChar == '(' ? ')' : ']');
            if (closingCharI == sString.length() - 1 || closingCharI == sString.length() - 2)
                return i;
        }

        return -1;
    }

    public static class LeastImportantOperator {
        public String op;
        public int index;
        public int tStatementType;

        public LeastImportantOperator(String op, int index, int group) {
            this.op = op;
            this.index = index;
            switch (group) {
                case 0:// Exponentiation
                case 1:// DivMult
                case 2:// AddSub
                case 3:// Bitwise shifts, Also handled within number handling.
                case 4:// Bitwise operations. Normally this should be by itself, but since the
                       // interprter knows how to handle bitwise stuff and its in the number handling
                       // method, group it under numbers
                    tStatementType = 1;
                    break;
                case 5, 6: // Comparison and logical operators
                    tStatementType = 0;
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

        @Override
        public String toString() {
            return "LeastImportantOperator{" +
                    "op='" + op + '\'' +
                    ", index=" + index +
                    ", tStatementType=" + tStatementType +
                    '}';
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((op == null) ? 0 : op.hashCode());
            result = prime * result + index;
            result = prime * result + tStatementType;
            return result;
        }

        /**
         * Indicates whether some other object is "equal to" this one.
         * <p>
         * The method checks for reference equality, nullity, class type, and then
         * compares
         * the fields {@code op}, {@code index}, and {@code tStatementType} for
         * equality.
         * </p>
         *
         * @param obj the reference object with which to compare
         * @return {@code true} if this object is the same as the obj argument;
         *         {@code false} otherwise
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            LeastImportantOperator other = (LeastImportantOperator) obj;
            if (op == null) {
                if (other.op != null)
                    return false;
            } else if (!op.equals(other.op))
                return false;
            if (index != other.index)
                return false;
            return tStatementType == other.tStatementType;
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
     * <p>
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
        if (statement.trim().isEmpty())
            return new LeastImportantOperator();
        ArrayList<Integer> indexes1 = new ArrayList<>();
        for (int i = 0; i < statement.length(); i++) {
            char c = statement.charAt(i);
            if ((c == '(' || c == '[') && Validate.isOpInQuotePair(statement, i) == -1)
                level++;
            else if ((c == ')' || c == ']') && Validate.isOpInQuotePair(statement, i) == -1)
                level--;
            else if (level == 0 && Validate.isOperator(c) && (Validate.isOpInQuotePair(statement, i) == -1))
                indexes1.add(i);

        }
        indexes1 = sanitizeStatement(statement, indexes1);

        if (indexes1.isEmpty())
            return new LeastImportantOperator(); // exit early if no operators are found.
        int group = -1; // 0 = Exponentiation, 1 = DivMult, 2 = AddSub, 3 = Bitwise, 4 = Comparison, 5 =
                        // Logical

        ArrayList<Tuple2<String, Integer>> indexes2 = new ArrayList<>(); // WHere the string, is the op itself, the
                                                                         // integer is the index.

        List<Character> multiOpChars = Arrays.asList('|', '&', '=', 'x', '>', '<'); // If the op is 2 chars long, it's
                                                                                    // last
        // char must be one of these.
        List<Character> prevOpChars = Arrays.asList('|', '&', '!', '>', '<');
        for (int i = Operators.getAllLists().size() - 1; i >= 0; i--) {
            List<String> list = Operators.getAllLists().get(i);
            char first = 0;
            for (int opIndex : indexes1) {
                String op = statement.substring(opIndex, opIndex + 1);
                boolean isMulti = first != 0 || (opIndex + 1 != statement.length() && multiOpChars.contains(statement.charAt(opIndex + 1)));
                char prevChar = opIndex > 0 ? statement.charAt(opIndex - 1) : 0;
                op = statement.substring(
                        opIndex,
                        isMulti ? (opIndex + 2)
                                : (opIndex + 1))
                        .trim();

                // String test = (first + op).trim();

                if (isMulti && first == 0)
                    first = op.charAt(0);
                else if (isMulti && prevChar != 0 && prevOpChars.contains(prevChar)
                /* && Operators.getType(test) < i */) {
                    first = 0;
                    continue; // skip this iteration as we got it earlier.
                } else {
                    // clean up first
                    first = 0;
                }

                if (!list.contains(op))
                    continue;

                if (group == -1) {
                    group = i;
                    indexes2.add(new Tuple2<String, Integer>(op, opIndex));
                } else if (group == Operators.getType(op)) {
                    indexes2.add(new Tuple2<String, Integer>(op, opIndex));
                }
            }
        }

        if (indexes2.isEmpty())
            return new LeastImportantOperator();

        Tuple2<String, Integer> fTuple2 = indexes2.getLast();

        return new LeastImportantOperator(
                fTuple2.first, fTuple2.second,
                group);
    }

    /**
     * Sanitizes a list of operator indexes in a given statement by removing indexes
     * that correspond to unary minus operators and logical not operators, while
     * preserving other operator
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
        ArrayList<Integer> remInt = removeUNARY(statement, opIndexes);

        return removeNOT(statement, remInt);
    }

    public static ArrayList<Integer> removeUNARY(String statement, ArrayList<Integer> opIndexes) {
        ArrayList<Integer> sanitized = new ArrayList<>();

        for (int i = 0; i < opIndexes.size(); i++) {
            int opIndex = opIndexes.get(i);
            if (i == 0 && opIndexes.size() == 1) {
                // if (!(statement.charAt(opIndex) == '-'))
                sanitized.add(opIndex);
                continue;

            }
            int opIndexBeforeUnary = i != 0 ? opIndexes.get(i - 1) : 0;
            boolean isUnary = Validate.isUnaryMinus(opIndex, opIndexBeforeUnary, statement);
            if (!isUnary)
                sanitized.add(opIndex);
        }

        return sanitized;
    }

    public static ArrayList<Integer> removeNOT(String statement, ArrayList<Integer> opIndexes) {
        ArrayList<Integer> sanitized = new ArrayList<>();

        for (int i = 0; i < opIndexes.size(); i++) {
            int opIndex = opIndexes.get(i);
            if (i == opIndexes.size() - 1 && opIndexes.size() == 1) {
                // if (!(statement.charAt(opIndex) == '\''))
                sanitized.add(opIndex);
                continue;

            }
            int opIndexAfterNOT = i != opIndexes.size() - 1 ? opIndexes.get(i + 1) : statement.length() - 1;
            boolean isLogicalNot = Validate.isLogicalNot(opIndex, opIndexAfterNOT, statement);
            if (!isLogicalNot)
                sanitized.add(opIndex);
        }

        return sanitized;
    }

    /**
     * Get the index of the operator in the given character array.
     * 
     * @param string String to find an operator index.
     * @return An int corresponding to the operator index. Note since it takes in a character array it only finds single operators and not multi
     */
    public static int operatorIndex(char[] string) {
        for (int i = 0; i < string.length; i++) {
            if (Operators.getAllChars().contains(string[i]))
                return i;
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
            char before = i > 0 ? line.charAt(i - 1) : Character.MIN_VALUE;
            char before2 = i > 1 ? line.charAt(i - 2) : Character.MIN_VALUE;
            if (c == '"' && (before != Chars.ESCAPE || before2 == Chars.ESCAPE)) {
                if (oldCharIndex == -1) {
                    oldCharIndex = i;
                } else {
                    arr.add(new Tuple2<>(oldCharIndex, i));
                    oldCharIndex = -1;
                }
            }
        }
        return arr;
    }

    /**
     * Finds and returns all pairs of indices representing the positions of matching
     * closing and opening parenthesis and brackets in the given string. Each pair
     * consists of the starting index
     * of an opening opening brace and the ending index of the corresponding
     * closing brace.
     * <p>
     * Both [] and ()
     *
     * @param line The input string to search for quotation brace pairs.
     * @return A Tuple2 containing two ArrayLists:
     *         - The first ArrayList contains pairs of indices representing the
     *         beginning
     *         and end of matching braces.
     *         - The second ArrayList contains pairs of indices representing
     *         unmatched
     *         braces.
     */
    public static Tuple2<ArrayList<Tuple2<Integer, Integer>>, ArrayList<Tuple2<Integer, Character>>> bracePairs(
            String line) {
        ArrayList<Tuple2<Integer, Integer>> finalArr = new ArrayList<>();
        ArrayList<Tuple2<Integer, Character>> stack = new ArrayList<>();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if ((c == '[' || c == '(') && Validate.isOpInQuotePair(line, i) == -1)
                stack.add(new Tuple2<Integer, Character>(i, c));

            if ((c == ']' || c == ')') && Validate.isOpInQuotePair(line, i) == -1) {
                Tuple2<Integer, Character> t = stack.getLast();
                if ((t.second == '[' && c == ']') || (t.second == '(' && c == ')')) {
                    finalArr.add(new Tuple2<Integer, Integer>(t.first, i));
                    stack.removeLast();
                }
            }
        }

        return new Tuple2<>(finalArr, stack);
    }

    /**
     * Finds the last index of the given input string in statement, while
     * disregarding input enclosed in quotation marks or inside a set of brackets []
     * or parenthesis ()
     * <p>
     * This is meant for {@link Token#processContext(String, int)} to properly split
     * nested {@link Token.TTernary} at the correct places. It can be used elsewhere
     * provided with caution
     * 
     * @param statement The statement to check within
     * @param input     The input to check for
     * @return the integer corresponding to the index of the first character of the
     *          found input in the statement
     *          Else: -1 if no valid index is found.
     */
    public static int lastIndexOf(String statement, String input) {
        int index = -1;
        boolean containsIndex = true;

        while (containsIndex) {
            int i = statement.indexOf(input);

            if (i == -1) {
                containsIndex = false;
                continue;
            }
            if (Validate.isOpInQuotePair(statement, i) == -1
                    && Validate.isOpInPair(i, Find.bracePairs(statement).first) == -1)
                index = i;
            statement = statement.replaceFirst(input, "-".repeat(input.length()));
        }

        return index;
    }

}
