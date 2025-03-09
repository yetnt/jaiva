package com.yetnt;

import java.util.*;

import com.yetnt.Errors.SyntaxError;
import com.yetnt.Token.TCodeblock;
import com.yetnt.Token.TIntVar;
import com.yetnt.Token.TStatement;;

class EscapeSequence {

    final String endOfLineChar = "!";

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
                .replace("**", "*");
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
    static class MultipleLinesOutput {
        public int startCount = 0;
        public int endCount = 0;
        public boolean isComment = false;
        public String preLine = "";

        public String type = ""; // either "kwenza" or "if" or "colonize"
        public String[] args = new String[3]; // the arguments for it.

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
            int startCount, int endCount, String previousLines, String type, String[] args) {
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
                    return new MultipleLinesOutput(startCount, endCount, previousLines + line, type, args);
                }
            } else {
                if (line.charAt(i) == end.charAt(0)) {
                    if (i + 1 < line.length() && line.charAt(i + 1) == end.charAt(1)) {
                        // if (!isStart) {
                        return new MultipleLinesOutput(i, i, previousLines + line, type, args);
                        // }
                    }
                    // System.out.println("Found!");
                    isStart = !isStart;
                }
            }
        }
        return new MultipleLinesOutput(startCount, endCount, previousLines + line, type, args);
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
            MultipleLinesOutput multipleLinesOutput, String entireLine, String t, String[] args) {
        MultipleLinesOutput m;
        if (multipleLinesOutput != null) {
            // multiple lines output exists. So we need to keep going until we find }
            m = isComment ? findEnclosingCharIMultipleLines(
                    line, '{', '}', multipleLinesOutput.startCount,
                    multipleLinesOutput.endCount)
                    : findEnclosingCharIMultipleLines(
                            line, "->", "<~",
                            multipleLinesOutput.startCount,
                            multipleLinesOutput.endCount, multipleLinesOutput.preLine,
                            t, args);
        } else {
            // Not given, but we need to find the closing tag.
            m = isComment ? findEnclosingCharIMultipleLines(
                    line, '{', '}', 0, 0)
                    : findEnclosingCharIMultipleLines(line, "->", "<~", 0, 0, "",
                            t, args);
        }

        if (m.endCount == m.startCount && m.startCount != 0 && m.startCount != -1) {
            // System.out.println(entireLine);
            return isComment ? null : entireLine;
        } else {
            return m;

        }
    }

    public static String[] handleArgs(String type, String line) {
        switch (type) {
            case "if": {
                // if (condition) ->
                // if (variable != 100) ->
                return new String[] { line.substring(line.indexOf("("), line.lastIndexOf(")")), "" };
            }
            case "nikhil": {
                // nikhil (condition) ->
                // nikhil (i < 10) ->
                return new String[] { line.substring(line.indexOf("("), line.lastIndexOf(")")), "" };
            }
            case "colonize": {
                // colonize declaration | condition | increment ->
                // colonize i <- 0 | i <= 10 | + ->
                String[] parts = line.split(Keywords.FOR)[1].trim().split("->")[0].split("\\|");
                return new String[] { parts[0].trim(), parts[1].trim(), parts[2].trim() };
            }
            case "kwenza": {
                // kwenza function_name(...args) ->
                // kwenza addition(param1, param2) ->
                String[] parts = line.split(Keywords.D_FUNCTION)[1].trim().split("->");
                String functionName = parts[0].substring(0, parts[0].indexOf("("));
                String args = parts[0].substring(parts[0].indexOf("(") + 1, parts[0].indexOf(")"));
                return new String[] { functionName, args };
            }
            default: {
                return new String[] { "" };
            }
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Object processBlockLines(boolean isComment, String line, MultipleLinesOutput multipleLinesOutput,
            String tokenizerLine, ArrayList<Token<?>> tokens, Token<?> tContainer, String type, String[] args)
            throws Exception {
        // System.out.println("Processing block lines...");
        type = multipleLinesOutput == null ? type : multipleLinesOutput.type;
        args = multipleLinesOutput == null ? args : multipleLinesOutput.args;
        Object output = handleBlocks(isComment, line + "\n", (MultipleLinesOutput) multipleLinesOutput,
                tokenizerLine, type, args);
        if (output == null)
            return output;

        if (output instanceof MultipleLinesOutput) {
            int endCount = ((MultipleLinesOutput) output).endCount;
            int startCount = ((MultipleLinesOutput) output).startCount;
            // System.out.println(startCount + " + " + endCount);
            if (endCount != startCount)
                return output;
        }
        String preLine = ((MultipleLinesOutput) multipleLinesOutput).preLine.replaceFirst(Keywords.D_FUNCTION, "")
                .replaceFirst(Keywords.IF, "")
                .replaceFirst(Keywords.WHILE, "")
                .replaceFirst(Keywords.FOR, "")
                .replace("*Nn", "");
        preLine = preLine.substring(preLine.indexOf("->") + 2);
        // System.out.println(preLine);
        ArrayList<Token<?>> nestedTokens = new ArrayList<>();
        try {
            nestedTokens.addAll((ArrayList<Token<?>>) readLine(preLine, "", null));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        TCodeblock codeblock = tContainer.new TCodeblock(nestedTokens);
        // Okay cool, we've parsed everything, but what if its different types?
        Token<?> specific;
        switch (type) {
            case "if": {
                TStatement s = tContainer.new TStatement(args[0]);
                specific = tContainer.new TIfStatement(s, codeblock).toToken();
                break;
            }
            case "colonize": {
                TIntVar variable = (Token<TIntVar>.TIntVar) ((ArrayList<Token<?>>) readLine(
                        "maak " + args[0] + "!", "", null)).get(0).getValue();
                TStatement condition = tContainer.new TStatement(args[1]);
                specific = tContainer.new TForLoop(variable, condition, args[2], codeblock).toToken();
                break;
            }
            case "kwenza": {
                specific = tContainer.new TFunc(args[0], args[1].split(","), codeblock).toToken();
                break;
            }
            case "nikhil": {
                TStatement s = tContainer.new TStatement(args[0]);
                specific = tContainer.new TWhileLoop(s, codeblock).toToken();
                break;
            }
            default: {
                throw new Errors.TokenizerException("Uhm, something went wrong. This shouldnt happen.");
            }
        }
        tokens.add(specific);
        return tokens;
    }

    public static Token<?> processVariable(String line, Token<?> tContainer)
            throws SyntaxError {
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
            throw new Errors.SyntaxError("Variable declaration does not end in new line!");
        }
        // remove ! from parts[2] (at the end of the line)
        parts[1] = parts[1].substring(0, parts[1].length() - 1).trim();
        parts[0] = parts[0].trim();
        // parts = ["varuiablename", "*svalue"]
        // parts = ["varuiablename", "49"]
        // parts = ["varuiablename", "true"]

        if (isString) {
            return tContainer.new TStringVar(parts[0], EscapeSequence.escape(parts[1])).toToken();
        } else {
            try {

                return tContainer.new TIntVar(parts[0], Integer.parseInt(parts[1])).toToken();
            } catch (NumberFormatException e) {
                if (parts[1].equals("true") || parts[1].equals("false") || parts[1].equals(Keywords.TRUE)
                        || parts[1].equals(Keywords.FALSE)) {
                    parts[1] = parts[1].replace(Keywords.TRUE, "true").replace(Keywords.FALSE, "false");
                    return tContainer.new TBooleanVar(parts[0], Boolean.parseBoolean(parts[1])).toToken();
                } else {
                    throw new Errors.SyntaxError("Invalid variable declaration!");
                }
            }
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
    @SuppressWarnings("unchecked")
    public static Object readLine(String line, String previousLine, Object multipleLinesOutput) throws Exception {
        boolean cont = multipleLinesOutput instanceof MultipleLinesOutput;
        boolean isComment = (cont && ((MultipleLinesOutput) multipleLinesOutput).isComment)
                || (line.startsWith("{") || (line.indexOf('{') != -1 && line.charAt(line.indexOf('{')) != '*'));
        boolean isCodeBlock = (cont && !((MultipleLinesOutput) multipleLinesOutput).isComment)
                || (line.startsWith(Keywords.IF) || line.startsWith(Keywords.D_FUNCTION)
                        || line.startsWith(Keywords.FOR))
                || line.startsWith(Keywords.WHILE);

        String type = line.startsWith(Keywords.IF) ? Keywords.IF
                : line.startsWith(Keywords.D_FUNCTION) ? Keywords.D_FUNCTION
                        : line.startsWith(Keywords.FOR) ? Keywords.FOR
                                : line
                                        .startsWith(Keywords.WHILE) ? Keywords.WHILE : null;
        // || (line.contains("->") && (line.indexOf('-') > 0 &&
        // line.charAt(line.indexOf('-') - 1) != '*' && line
        // .charAt(line.indexOf('-') - 1) != '<'))
        ;

        ArrayList<Token<?>> tokens = new ArrayList<>();
        Token<?> tContainer = new Token<>(null);
        boolean containsNewln = line.contains("\n");
        String[] lines = containsNewln ? line.split("\n") : line.split("(?<!\\*)!");

        if (lines.length > 1 && !lines[1].isEmpty()) {
            // System.out.println("Multiple lines detected!");
            // multiple lines.
            MultipleLinesOutput m = null;
            for (int i = 0; i != lines.length; i++) {
                // System.out.println("Reading line " + i + "...");
                String previousLine2 = i == 0 ? previousLine : lines[i - 1];
                // System.out.println(previousLine2);
                // System.out.println(lines[i]);
                Object something = readLine(lines[i] + (!containsNewln ? "!" : ""), previousLine2, m);
                if (something instanceof MultipleLinesOutput) {
                    m = ((MultipleLinesOutput) something);
                } else if (something instanceof ArrayList<?>) {
                    m = null;
                    tokens.addAll((ArrayList<Token<?>>) something);
                } else {
                    m = null;
                }
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
            return type == null
                    ? processBlockLines(isComment, line, (MultipleLinesOutput) multipleLinesOutput, tokenizerLine,
                            tokens, tContainer, type,
                            new String[] { "" })
                    : processBlockLines(
                            isComment, line, (MultipleLinesOutput) multipleLinesOutput, tokenizerLine, tokens,
                            tContainer, type,
                            handleArgs(type, line));
        }

        // #STRING# is a line which contains something that needs to be tokenized.

        if (line.startsWith(Keywords.D_VAR)) {
            tokens.add(processVariable(line, tContainer));
            return tokens;
        }

        return void.class;
    }
}
