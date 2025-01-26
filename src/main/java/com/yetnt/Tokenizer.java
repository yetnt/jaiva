package com.yetnt;

import java.util.*;

import javax.swing.plaf.multi.MultiListUI;

class EscapeSequence {
    /**
     * Escape a string.
     * 
     * @param str The string to escape.
     * @return
     */
    public static String escape(String str) {
        return str
                .replace("*=", "=")
                .replace("*!", "!")
                .replace("*s", " ")
                .replace("*@", "@")
                .replace("*n", "\n")
                .replace("*t", "\t")
                .replace("*r", "\r")
                .replace("*b", "\b")
                .replace("*f", "\f")
                .replace("*'", "'")
                .replace("*\"", "\"")
                .replace("*\\", "\\");
    }

    /**
     * Unescape a string.
     * 
     * @param str The string to unescape.
     * @return
     */
    public static String unescape(String str) {
        return str
                .replace("=", "*=")
                .replace("!", "*!")
                .replace(" ", "*s")
                .replace("@", "*@")
                .replace("\n", "*n")
                .replace("\t", "*t")
                .replace("\r", "*r")
                .replace("\b", "*b")
                .replace("\f", "*f")
                .replace("'", "*'")
                .replace("\"", "*\"")
                .replace("\\", "*\\");
    }
}

// // Create an outer Token instance
// Token<?> tokenContainer = new Token<>(null);

// // Create a TVar instance and convert it to a Token<TVar>
// Token<Token<TVar>.TVar> tVarToken = tokenContainer.new TVar("myVariable",
// "42").toToken();

// // Retrieve the inner TVar instance and access its parameters
// Token<TVar>.TVar tVarInstance = tVarToken
// .getValue();System.out.println("TVar Name:
// "+tVarInstance.name);System.out.println("TVar Value: "+tVarInstance.value);

public class Tokenizer {
    /**
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
     */
    static class MultipleLinesOutput {
        public int startCount = 0;
        public int endCount = 0;
        public boolean isInBlockComment = false;

        public MultipleLinesOutput(int start, int end) {
            startCount = start;
            endCount = end;
        }

        public MultipleLinesOutput(boolean inBlockComment) {
            isInBlockComment = inBlockComment;
        }

        public MultipleLinesOutput(int start, int end, boolean inBlockComment) {
            isInBlockComment = inBlockComment;
            startCount = start;
            endCount = end;
        }
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
    public static MultipleLinesOutput findEnclosingCharIMultipleLines(String line, char start, char end,
            int startCount,
            int endCount, boolean inBlockComment) {
        boolean isStart = true;
        for (int i = 0; i < line.length(); i++) {
            if (start != end) {
                if (line.charAt(i) == start) {
                    startCount++;
                } else if (line.charAt(i) == end) {
                    endCount++;
                }
                if (startCount == endCount && startCount != 0) {
                    return new MultipleLinesOutput(startCount, endCount, inBlockComment);
                }
            } else {
                if (line.charAt(i) == end) {
                    if (!isStart) {
                        return new MultipleLinesOutput(i, i, inBlockComment);
                    }
                    isStart = !isStart;
                }
            }
        }
        return new MultipleLinesOutput(-1, -1, inBlockComment);
    }

    /**
     * Find the index of the enclosing character in a string.
     * 
     * @param line  The string to search in.
     * @param start The starting character.
     * @param end   The ending character.
     * @return
     */
    public static int findEnclosingCharI(String line, char start, char end) {
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
     * Remove single line comments. Call this function when appropriate.
     * 
     * @param line The line to make sure there isnt a single line comment
     * @return
     */
    public static String decimateSingleComments(String line) {
        if (line.indexOf('@') == -1)
            return line;

        return line.substring(0, line.indexOf('@'));
    }

    /**
     * Read a line and tokenize it.
     * May return an arraylist of tokens or a string to be used as the previosu
     * string of another readLine call.
     * 
     * @param line         The entire line. Will be concatenated with previousLine
     *                     at the start
     * @param previousLine The previous line
     * @return ArrayList<Token<?>> or String
     * @throws Exception
     */
    public static Object readLine(String line, String previousLine, Object multipleLinesOutput) throws Exception {
        ArrayList<Token<?>> tokens = new ArrayList<>();
        Token<?> tContainer = new Token<>(null);

        String[] lines = line.split("(?<!\\*)!");
        if (lines.length > 1 && !lines[1].isEmpty()) {
            System.out.println("Multiple lines detected!");
            // multiple lines.
            for (int i = 0; i != lines.length; i++) {
                System.out.println("Reading line " + i + "...");
                tokens.addAll((ArrayList<Token<?>>) readLine(lines[i] + "!", i == 0 ? previousLine : "", null));
            }
            return tokens;
        }
        String actualLine = line.trim(); // The actual current line.
        line = previousLine.trim() + actualLine; // The entire line to the tokenizer.

        // @comment (single line)

        decimateSingleComments(line);

        /*
         * {
         * multi line comment
         * 
         * }
         */
        if (line.startsWith("{") || (line.indexOf('{') != -1 && line.charAt(line.indexOf('{')) != '*')) {
            System.out.println("E");
            MultipleLinesOutput m;
            if (multipleLinesOutput instanceof MultipleLinesOutput) {
                // multiple lines output exists. So we need to keep going until we find }
                MultipleLinesOutput outerM = (MultipleLinesOutput) multipleLinesOutput;
                m = findEnclosingCharIMultipleLines(actualLine, '{', '}', outerM.startCount, outerM.endCount, true);
            } else {
                // Not given, but we need to find the closing tag.
                m = findEnclosingCharIMultipleLines(actualLine, '{', '}', 0, 0, true);
            }

            if (m.endCount == m.startCount && m.startCount != 0) {
                // Their equal and not zero, meaning we found the closing pair.
                // so exit. since its a comment
                return null;
            } else {
                // their not equal or their equal to -1. So keep going and find the enclosing
                // tag.
                return m;
            }
        }

        // #STRING# is a line which contains something that needs to be tokenized.

        if (line.startsWith("maak")) {
            boolean isString = false;
            // #maak varuiablename <- " value"!#
            // #maak varuiablename <- 49!#
            // #maak varuiablename <- true!#
            // #maak varuiablename <- true! @comment#
            int stringStart = line.indexOf("\"");
            int stringEnd = findEnclosingCharI(line, '"', '"');
            if (stringStart != -1 && stringEnd != -1) {
                isString = true;
                String encasedString = line.substring(stringStart + 1, stringEnd);
                line = line.substring(0, stringStart - 1) + EscapeSequence.unescape(encasedString) + "!";
                // #maak varuiablename = "*svalue"!#
            }
            line = line.trim();
            line = line.substring(4, line.length());
            // #varuiablename <- *svalue!#
            // #varuiablename <- 49!#
            // #varuiablename <- true!#
            // #varuiablename <- true! @comment#

            String[] parts = line.split("<-");
            // parts = ["varuiablename ", " *svalue!"]
            // parts = ["varuiablename ", " 49!"]
            // parts = ["varuiablename ", " true!"]
            // parts = ["varuiablename ", " true! @comment"]

            if (parts.length != 2 || !parts[1].endsWith("!")) {
                throw new Exception("Invalid syntax!");
            }
            // remove ! from parts[2] (at the end of the line)
            parts[1] = parts[1].substring(0, parts[1].length() - 1).trim();
            parts[0] = parts[0].trim();
            // parts = ["varuiablename", "*svalue"]
            // parts = ["varuiablename", "49"]
            // parts = ["varuiablename", "true"]

            if (isString) {
                tokens.add(tContainer.new TStringVar(parts[0], EscapeSequence.unescape(parts[1])).toToken());
            } else {
                try {

                    tokens.add(tContainer.new TIntVar(parts[0], Integer.parseInt(parts[1])).toToken());
                } catch (NumberFormatException e) {
                    if (parts[1].equals("true") || parts[1].equals("false") || parts[1].equals("yebo")
                            || parts[1].equals("aowa")) {
                        parts[1] = parts[1].replace("yebo", "true").replace("aowa", "false");
                        tokens.add(tContainer.new TBooleanVar(parts[0], Boolean.parseBoolean(parts[1])).toToken());
                    } else {
                        throw new Errors.TokenizerSyntaxError("Invalid variable declaration!");
                    }
                    // tokens.add(tContainer.new TBooleanVar(parts[0],
                    // Boolean.parseBoolean(parts[1])).toToken());
                }
            }

        }
        return tokens;
        // if (!line.endsWith("!")) {
        // return line;
        // }
    }
}
