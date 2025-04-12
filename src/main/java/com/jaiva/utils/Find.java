package com.jaiva.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jaiva.tokenizer.Lang.BODMAS;
import com.jaiva.tokenizer.Token;

public class Find {

    /**
     * i honestly wish i documented this earlier, there are SO MANY overrides
     * for the constructor that i don't even know if half of them are needed
     * I just know if i remove one, Java compiler will strike me down like Zeus.
     * 
     * Class that represents that the intepreter needs to call readline again for
     * some reason.
     * <p>
     * For {@code Find.closingCharIndexML()}
     * <ul>
     * <li>{@code this.startCount == this.endCount} : if their equal then the method
     * ahs found the closing brace and doesnt need to read another line.
     * <li>{@code this.startCount != this.endCount} : if their not equal, no closing
     * brace has been found yet and we should continue
     * <li>{@code this.startCount == this.endCount == -1}: if their both equal to
     * -1, something went wrong.
     * <li>{@code (this.startCount == this.endCount) && this.startCount < 0} : if
     * their lower than -1, this output isnt to be used for this method but rather
     * another method.
     * </ul>
     * 
     * Okay now to talk about the {@code type} field and the {@code args} field.
     * <p>
     * The {@code type} field is used to determine which block it is between an if
     * statement "if", a loop "colonize" or a function "kwenza" or a while loop
     * Keywords.WHILE.
     * <p>
     * The {@code args} field is used to store the arguments for the block.
     * <ul>
     * <li>For an if statement, the first argument is the condition. More blocks
     * will be handled later on in develpment
     * <li>For a loop, the first argument is the variable declaration, second is the
     * condition, third is the increment.
     * <li>For a function, the first argument is the function name, second is the
     * arguments.
     * <li>For a while loop, the first argument is the function name, second is the
     * arguments.
     * </ul>
     */
    public static class MultipleLinesOutput {
        public int startCount = 0;
        public int endCount = 0;
        public boolean isComment = false;
        public String preLine = "";

        public String type = ""; // either "kwenza" or "if" or "colonize"
        public String[] args = new String[3]; // the arguments for it.
        public Token<?> specialArg = null; // these are for else if chains. This represents the original if

        public int lineNumber = -1;

        public MultipleLinesOutput(int start, int end) {
            startCount = start;
            endCount = end;
        }

        public MultipleLinesOutput(int start, int end, String pString) {
            startCount = start;
            endCount = end;
            preLine = pString;
        }

        public MultipleLinesOutput(int start, int end, String pString, String t, String[] a) {
            startCount = start;
            endCount = end;
            preLine = pString;
            type = t;
            args = a;
        }

        public MultipleLinesOutput(int start, int end, String pString, String t, String[] a, Token<?> sArg,
                int ln) {
            startCount = start;
            endCount = end;
            preLine = pString;
            type = t;
            args = a;
            specialArg = sArg;
            lineNumber = ln;
        }

        public MultipleLinesOutput(int start, int end, String pString, boolean inBlockComment) {
            isComment = inBlockComment;
            startCount = start;
            endCount = end;
            preLine = pString;
        }

        public MultipleLinesOutput(boolean inBlockComment) {
            isComment = inBlockComment;
        }

        public MultipleLinesOutput(int start, int end, boolean inBlockComment) {
            isComment = inBlockComment;
            startCount = start;
            endCount = end;
        }
    }

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
                if (startCount == endCount && startCount != 0) {
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
            if (closingCharI == sString.length() - 1)
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
     * Finds the oeprator index in respects to braces. This is meant for the
     * TStatement class.
     * <p>
     * into respects.
     * 
     * @param statement
     * @param operator
     * @return
     */
    public static LeastImportantOperator leastImportantOperator(String statement) {
        int level = 0;
        ArrayList<Integer> indexes1 = new ArrayList<>();
        for (int i = 0; i < statement.length(); i++) {
            char c = statement.charAt(i);
            if (c == '(' || c == '[')
                level++;
            else if (c == ')' || c == ']')
                level--;
            else if (level == 0 && Validate.isOperator(c)) {
                indexes1.add(i);
            }
        }
        if (indexes1.isEmpty())
            return new LeastImportantOperator(); // exit early if no operators are found.
        int group = -1; // 0 = Exponentiation, 1 = DivMult, 2 = AddSub, 3 = Bools
        ArrayList<Integer> indexes2 = new ArrayList<>();
        for (String op : BODMAS.getAll().reversed()) {
            for (int opIndex : indexes1) {
                String opString = statement.substring(opIndex, opIndex + op.length());
                if (!opString.equals(op))
                    continue;
                if (group == -1) {
                    group = BODMAS.getType(op);
                    indexes2.add(opIndex);
                } else if (group == BODMAS.getType(op)) {
                    indexes2.add(opIndex);
                } else {
                    continue;
                }
            }
        }

        List<Character> multiOpChars = Arrays.asList('|', '&', '>', '<', '='); // If the op is 2 chars long, it's last
                                                                               // char must be one of these.

        // The operator can sometimes be the length of 2
        String op = statement.substring(indexes2.getLast(),
                multiOpChars.contains(statement.charAt(indexes2.getLast() + 1)) ? (indexes2.getLast() + 2)
                        : (indexes2
                                .getLast() + 1));

        return indexes2.isEmpty() ? new LeastImportantOperator()
                : new LeastImportantOperator(
                        op, indexes2.getLast(),
                        group);
    }

}
