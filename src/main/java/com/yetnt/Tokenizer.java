package com.yetnt;

import java.util.*;

import javax.swing.plaf.multi.MultiListUI;

import com.yetnt.Token.TCodeblock;

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
        public boolean isComment = false;
        public String preLine = "";

        public MultipleLinesOutput(int start, int end) {
            startCount = start;
            endCount = end;
        }

        public MultipleLinesOutput(int start, int end, String pString) {
            startCount = start;
            endCount = end;
            preLine = pString;
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
    public static MultipleLinesOutput findEnclosingCharIMultipleLines(String line, String start, String end,
            int startCount, int endCount, String previousLines) {
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
                    return new MultipleLinesOutput(startCount, endCount, previousLines + line);
                }
            } else {
                if (line.charAt(i) == end.charAt(0)) {
                    if (i + 1 < line.length() && line.charAt(i + 1) == end.charAt(1)) {
                        // if (!isStart) {
                        return new MultipleLinesOutput(i, i, previousLines + line);
                        // }
                    }
                    // System.out.println("Found!");
                    isStart = !isStart;
                }
            }
        }
        return new MultipleLinesOutput(startCount, endCount, previousLines + line);
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

    public static Object handleBlocks(boolean isComment, String line,
            MultipleLinesOutput multipleLinesOutput, String entireLine) {
        MultipleLinesOutput m;
        if (multipleLinesOutput != null) {
            // multiple lines output exists. So we need to keep going until we find }
            m = isComment ? findEnclosingCharIMultipleLines(
                    line, '{', '}', multipleLinesOutput.startCount,
                    multipleLinesOutput.endCount)
                    : findEnclosingCharIMultipleLines(
                            line, "->", "<~",
                            multipleLinesOutput.startCount,
                            multipleLinesOutput.endCount, multipleLinesOutput.preLine);
        } else {
            // Not given, but we need to find the closing tag.
            m = isComment ? findEnclosingCharIMultipleLines(
                    line, '{', '}', 0, 0)
                    : findEnclosingCharIMultipleLines(line, "->", "<~", 0, 0, "");
        }

        if (m.endCount == m.startCount && m.startCount != 0 && m.startCount != -1) {
            return isComment ? null : entireLine;
        } else {
            return m;
        }
    }

    /**
     * The BIG BOY!
     * 
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
        boolean cont = multipleLinesOutput instanceof MultipleLinesOutput;
        boolean isComment = (cont && ((MultipleLinesOutput) multipleLinesOutput).isComment)
                || (line.startsWith("{") || (line.indexOf('{') != -1 && line.charAt(line.indexOf('{')) != '*'));
        boolean isCodeBlock = (cont && !((MultipleLinesOutput) multipleLinesOutput).isComment)
                || (line.startsWith("if") || line.startsWith("kwenza") || line.startsWith("colonize"))
        // || (line.contains("->") && (line.indexOf('-') > 0 &&
        // line.charAt(line.indexOf('-') - 1) != '*' && line
        // .charAt(line.indexOf('-') - 1) != '<'))
        ;

        ArrayList<Token<?>> tokens = new ArrayList<>();
        Token<?> tContainer = new Token<>(null);

        String[] lines = line.split("(?<!\\*)!");

        if (lines.length > 1 && !lines[1].isEmpty()) {
            // System.out.println("Multiple lines detected!");
            // multiple lines.
            for (int i = 0; i != lines.length; i++) {
                // System.out.println("Reading line " + i + "...");
                tokens.addAll((ArrayList<Token<?>>) readLine(lines[i] + "!", i == 0 ? previousLine : "", null));
            }
            return tokens;
        }
        line = line.trim(); // The actual current line.
        String tokenizerLine = previousLine.trim() + line; // The entire line to the tokenizer.

        // @comment (single line)

        decimateSingleComments(line);

        /*
         * {
         * multi line comment}
         * and block stuff
         */
        if (cont || isComment || isCodeBlock) {
            Object output = handleBlocks(isComment, line, (MultipleLinesOutput) multipleLinesOutput, tokenizerLine);
            if (output == null)
                return output;

            if (output instanceof MultipleLinesOutput) {
                if (((MultipleLinesOutput) output).endCount != ((MultipleLinesOutput) output).startCount)
                    return output;
            }
            String preLine = ((MultipleLinesOutput) multipleLinesOutput).preLine.replaceFirst("kwenza", "")
                    .replaceFirst("if", "")
                    .replaceFirst("->", "")
                    .replace("*Nn", "");
            ArrayList<Token<?>> nestedTokens = new ArrayList<>();
            nestedTokens.addAll((ArrayList<Token<?>>) readLine(preLine, "", null));
            tokens.add(tContainer.new TCodeblock(nestedTokens).toToken());
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
