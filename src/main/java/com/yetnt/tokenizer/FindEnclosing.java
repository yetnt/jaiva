package com.yetnt.tokenizer;

public class FindEnclosing {

    /**
     * i honestly wish i documented this earlier, there are SO MANY overrides
     * for the constructor that i don't even know if half of them are needed
     * I just know if i remove one, Java compiler will strike me down like Zeus.
     * 
     * Class that represents that the intepreter needs to call readline again for
     * some reason.
     * <p>
     * For {@code findEnclosingCharIMultipleLines()}
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

        public MultipleLinesOutput(int start, int end, String pString, String t, String[] a, Token<?> sArg) {
            startCount = start;
            endCount = end;
            preLine = pString;
            type = t;
            args = a;
            specialArg = sArg;
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
    static MultipleLinesOutput charIMultipleLines(String line, String start,
            String end,
            int startCount, int endCount, String previousLines, String type, String[] args, Token<?> blockChain) {
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
                            args, blockChain);
                }
            } else {
                if (line.charAt(i) == end.charAt(0)) {
                    if (i + 1 < line.length() && line.charAt(i + 1) == end.charAt(1)) {
                        // if (!isStart) {
                        return new MultipleLinesOutput(i, i, previousLines + line, type, args, blockChain);
                        // }
                    }
                    // System.out.println("Found!");
                    isStart = !isStart;
                }
            }
        }
        return new MultipleLinesOutput(startCount, endCount, previousLines + line, type, args, blockChain);
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
    static MultipleLinesOutput charIMultipleLines(String line, char start, char end,
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
    static int charI(String line, char start, char end) {
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

}
