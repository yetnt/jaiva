package com.yetnt.tokenizer;

import java.util.*;

import com.yetnt.Errors;
import com.yetnt.Errors.SyntaxError;
import com.yetnt.Errors.TokenizerException;
import com.yetnt.tokenizer.Token.TCodeblock;
import com.yetnt.tokenizer.Token.TIfStatement;
import com.yetnt.tokenizer.Token.TIntVar;
import com.yetnt.tokenizer.Token.TStatement;
import com.yetnt.tokenizer.Token.TTryCatchStatement;

import com.yetnt.tokenizer.Keywords;
import com.yetnt.tokenizer.Lang;

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
     * Remove single line comments. Call this function when appropriate.
     * 
     * @param line The line to make sure there isnt a single line comment
     * @return
     */
    private static String decimateSingleComments(String line) {
        if (line.indexOf(Lang.COMMENT) == -1)
            return line;

        return line.substring(0, line.indexOf(Lang.COMMENT));
    }

    private static Object handleBlocks(boolean isComment, String line,
            FindEnclosing.MultipleLinesOutput multipleLinesOutput, String entireLine, String t, String[] args,
            Token<?> blockChain) {
        FindEnclosing.MultipleLinesOutput m;
        if (multipleLinesOutput != null) {
            // multiple lines output exists. So we need to keep going until we find }
            m = isComment ? FindEnclosing.charIMultipleLines(
                    line, Lang.COMMENT_OPEN, Lang.COMMENT_CLOSE, multipleLinesOutput.startCount,
                    multipleLinesOutput.endCount)
                    : FindEnclosing.charIMultipleLines(
                            line, Lang.BLOCK_OPEN, Lang.BLOCK_CLOSE,
                            multipleLinesOutput.startCount,
                            multipleLinesOutput.endCount, multipleLinesOutput.preLine,
                            t, args, blockChain);
        } else {
            // Not given, but we need to find the closing tag.
            m = isComment ? FindEnclosing.charIMultipleLines(
                    line, Lang.COMMENT_OPEN, Lang.COMMENT_CLOSE, 0, 0)
                    : FindEnclosing.charIMultipleLines(line, Lang.BLOCK_OPEN, Lang.BLOCK_CLOSE, 0, 0, "",
                            t, args, blockChain);
        }

        if (m.endCount == m.startCount && m.startCount != 0 && m.startCount != -1) {
            // System.out.println(entireLine);
            return isComment ? null : m.preLine;
        } else {
            return m;

        }
    }

    private static String[] handleArgs(String type, String line) {
        switch (type) {
            case "mara if": {
                // mara condition ->
                // mara (i < 10) ->
                return new String[] { line.substring(line.indexOf("(") + 1, line.lastIndexOf(")")), "" };
            }
            case "if": {
                // if (condition) ->
                // if (variable != 100) ->
                return new String[] { line.substring(line.indexOf("("), line.lastIndexOf(")")), "" };
            }
            case "nikhil": {
                // nikhil (condition) ->
                // nikhil (i < 10) ->
                return new String[] { line.substring(line.indexOf("(") + 1, line.lastIndexOf(")")), "" };
            }
            case "colonize": {
                // colonize declaration | condition | increment ->
                // colonize i <- 0 | i <= 10 | + ->
                String[] parts = line.split(Keywords.FOR)[1].trim().split(Lang.BLOCK_OPEN)[0].split("\\|");
                return new String[] { parts[0].trim(), parts[1].trim(), parts[2].trim() };
            }
            case "kwenza": {
                // kwenza function_name(...args) ->
                // kwenza addition(param1, param2) ->
                String[] parts = line.split(Keywords.D_FUNCTION)[1].trim().split(Lang.BLOCK_OPEN);
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
    private static Object processBlockLines(boolean isComment, String line,
            FindEnclosing.MultipleLinesOutput multipleLinesOutput,
            String tokenizerLine, ArrayList<Token<?>> tokens, Token<?> tContainer, String type, String[] args,
            Token<?> blockChain)
            throws Exception {
        // System.out.println("Processing block lines...");
        type = multipleLinesOutput == null ? type : multipleLinesOutput.type;
        args = multipleLinesOutput == null ? args : multipleLinesOutput.args;
        Object output = handleBlocks(isComment, line + "\n", (FindEnclosing.MultipleLinesOutput) multipleLinesOutput,
                tokenizerLine, type, args, multipleLinesOutput != null ? multipleLinesOutput.specialArg : blockChain);
        if (output == null)
            return output;

        if (output instanceof FindEnclosing.MultipleLinesOutput) {
            int endCount = ((FindEnclosing.MultipleLinesOutput) output).endCount;
            int startCount = ((FindEnclosing.MultipleLinesOutput) output).startCount;
            // System.out.println(startCount + " + " + endCount);
            if (endCount != startCount)
                return output;
        }
        String preLine = ((String) output)
                .replaceFirst(Keywords.D_FUNCTION, "")
                .replaceFirst(Keywords.IF, "")
                .replaceFirst(Keywords.WHILE, "")
                .replaceFirst(Keywords.FOR, "")
                .replace(Keywords.ELSE, "")
                .replace(Keywords.CATCH, "")
                .replace("*Nn", "").trim();
        preLine = preLine.substring(preLine.indexOf(Lang.BLOCK_OPEN) + 2);
        preLine = preLine.substring(0, preLine.indexOf(Lang.BLOCK_CLOSE));
        // System.out.println(preLine);
        ArrayList<Token<?>> nestedTokens = new ArrayList<>();
        try {
            // TODO : Allow blocks here
            nestedTokens.addAll((ArrayList<Token<?>>) readLine(preLine, "", null, null));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        TCodeblock codeblock = tContainer.new TCodeblock(nestedTokens);
        // Okay cool, we've parsed everything, but what if its different types?
        Token<?> specific;
        switch (type) {
            case "mara if": {
                TStatement s = tContainer.new TStatement(args[0]);
                TIfStatement ifStatement = tContainer.new TIfStatement(s, codeblock);
                TIfStatement originalIf = ((TIfStatement) ((FindEnclosing.MultipleLinesOutput) multipleLinesOutput).specialArg
                        .getValue());
                originalIf.appendElseIf(s, ifStatement);
                // ~> mara if () <- ...
                return new BlockChain(originalIf.toToken(), line.replaceFirst(Lang.BLOCK_CLOSE, "").trim());
            }
            case "mara": {
                TIfStatement originalIf = ((TIfStatement) ((FindEnclosing.MultipleLinesOutput) multipleLinesOutput).specialArg
                        .getValue());
                originalIf.appendElse(codeblock);
                specific = originalIf.toToken();
                break;
            }
            case "if": {
                TStatement s = tContainer.new TStatement(args[0]);
                specific = tContainer.new TIfStatement(s, codeblock).toToken();
                if (line.contains(Keywords.ELSE)) {
                    // will turn "~> mara <- ... " into "mara <- ..."
                    // and "~> mara if () <- ..." into "mara if () <- ..."
                    return new BlockChain(specific, line.replaceFirst(Lang.BLOCK_CLOSE, "").trim());
                }
                break;
            }
            case "zama zama": {
                specific = tContainer.new TTryCatchStatement(codeblock).toToken();
                return new BlockChain(specific, line.replaceFirst(Lang.BLOCK_CLOSE, "").trim());
            }
            case "chaai": {
                TTryCatchStatement tryCatch = ((TTryCatchStatement) ((FindEnclosing.MultipleLinesOutput) multipleLinesOutput).specialArg
                        .getValue());
                tryCatch.appendCatchBlock(codeblock);
                specific = tryCatch.toToken();
                break;
            }
            case "colonize": {
                TIntVar variable = (Token<TIntVar>.TIntVar) ((ArrayList<Token<?>>) readLine(
                        "maak " + args[0] + "!", "", null, null)).get(0).getValue();
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

    private static Token<?> processVariable(String line, Token<?> tContainer)
            throws SyntaxError, TokenizerException {
        boolean isString = false;
        // #maak varuiablename <- " value"!#
        // #maak varuiablename <- 49!#
        // #maak varuiablename <- true!#
        // #maak varuiablename <- true! @comment#
        // #maak variablename <-| 100, 23, k!#
        if (line.indexOf(Lang.ARRAY_ASSIGNMENT) != -1) {
            // its an array.
            // #maak variablename <-| 100, 23, k!#
            line = line.trim();
            line = line.substring(4, line.length());
            // #variablename <-| 100, 23, k!#
            String[] parts = line.split("<-\\|");
            // parts = ["variablename ", " 100, 23, k!"]
            parts[0] = parts[0].trim();
            if (!parts[1].endsWith("!")) {
                throw new Errors.SyntaxError("Variable declaration does not end in an exclamation!");
            }
            parts[1] = parts[1].substring(0, parts[1].length() - 1).trim(); // trims ending !
            // parts = ["variablename", "100, 23, k"]
            String[] values = parts[1].split(",");
            // values = ["100", "23", "k"]
            return tContainer.new TArrayVar(parts[0], values).toToken();

        }
        int stringStart = line.indexOf("\"");
        int stringEnd = FindEnclosing.charI(line, '"', '"');
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

        String[] parts = line.split(Lang.ASSIGNMENT);
        // parts = ["varuiablename ", " *svalue!"]
        // parts = ["varuiablename ", " 49!"]
        // parts = ["varuiablename ", " true!"]
        // parts = ["varuiablename ", " true! @comment"]

        if (parts.length != 2 || !parts[1].endsWith("!")) {
            throw new Errors.SyntaxError("Variable declaration does not end in new line!");
        }
        // remove ! from parts[1] (at the end of the line)
        parts[1] = parts[1].substring(0, parts[1].length() - 1).trim();
        parts[0] = parts[0].trim();
        int isStatement = Lang.containsOperator(parts[1].toCharArray());
        // parts = ["varuiablename", "*svalue"]
        // parts = ["varuiablename", "49"]
        // parts = ["varuiablename", "true"]

        if (isString) {
            return tContainer.new TStringVar(parts[0], EscapeSequence.escape(parts[1])).toToken();
        } else if (isStatement > -1) {
            TStatement statement = tContainer.new TStatement(parts[1]);
            switch (isStatement) {
                case 0:
                    return tContainer.new TBooleanVar(parts[0], statement).toToken();
                case 1:
                    return tContainer.new TIntVar(parts[0], statement).toToken();
                default:
                    throw new Errors.TokenizerException("THis isn't suppsoed to happen");
            }
        } else {
            try {

                return tContainer.new TIntVar(parts[0], Integer.parseInt(parts[1])).toToken();
            } catch (NumberFormatException e) {
                if (parts[1].equals("true") || parts[1].equals("false") || parts[1].equals(Keywords.TRUE)
                        || parts[1].equals(Keywords.FALSE)) {
                    parts[1] = parts[1].replace(Keywords.TRUE, "true").replace(
                            Keywords.FALSE,
                            "false");
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
    public static Object readLine(String line, String previousLine, Object multipleLinesOutput, BlockChain blockChain)
            throws Exception {
        boolean cont = multipleLinesOutput instanceof FindEnclosing.MultipleLinesOutput;
        boolean isComment = (cont && ((FindEnclosing.MultipleLinesOutput) multipleLinesOutput).isComment)
                || (line.startsWith("{") || (line.indexOf(Lang.COMMENT_OPEN) != -1
                        && line.charAt(line.indexOf(Lang.COMMENT_OPEN)) != '*'));
        boolean isCodeBlock = (cont && !((FindEnclosing.MultipleLinesOutput) multipleLinesOutput).isComment)
                || (line.startsWith(Keywords.IF) || line.startsWith(Keywords.D_FUNCTION)
                        || line.startsWith(Keywords.FOR))
                || line.startsWith(Keywords.WHILE) || line.startsWith(Keywords.ELSE) || line.startsWith(Keywords.TRY)
                || line.startsWith(Keywords.CATCH);

        String type = line.startsWith(Keywords.IF) ? Keywords.IF
                : line.startsWith(Keywords.D_FUNCTION) ? Keywords.D_FUNCTION
                        : line.startsWith(Keywords.FOR) ? Keywords.FOR
                                : line.startsWith(Keywords.WHILE) ? Keywords.WHILE
                                        : line.replace(Lang.BLOCK_CLOSE, "").trim()
                                                .startsWith(Keywords.ELSE + " " + Keywords.IF)
                                                        ? Keywords.ELSE + " " + Keywords.IF // check for "ELSE IF"
                                                                                            // before
                                                                                            // checking for "IF"
                                                        : line.replace(Lang.BLOCK_CLOSE, "").trim()
                                                                .startsWith(Keywords.ELSE) ? Keywords.ELSE
                                                                        : line.startsWith(Keywords.TRY) ? Keywords.TRY
                                                                                : line.replace(Lang.BLOCK_CLOSE, "")
                                                                                        .trim().trim()
                                                                                        .startsWith(Keywords.CATCH)
                                                                                                ? Keywords.CATCH
                                                                                                : null;
        // || (line.contains(Lang.BLOCK_OPEN) && (line.indexOf('-') > 0 &&
        // line.charAt(line.indexOf('-') - 1) != '*' && line
        // .charAt(line.indexOf('-') - 1) != '<'));

        ArrayList<Token<?>> tokens = new ArrayList<>();
        Token<?> tContainer = new Token<>(null);
        boolean containsNewln = line.contains("\n");
        String[] lines = containsNewln ? line.split("\n") : line.split("(?<!\\*)!(?!\\=)");

        if (lines.length > 1 && !lines[1].isEmpty()) {
            // System.out.println("Multiple lines detected!");
            // multiple lines.
            FindEnclosing.MultipleLinesOutput m = null;
            BlockChain b = null;
            for (int i = 0; i != lines.length; i++) {
                // System.out.println("Reading line " + i + "...");
                String previousLine2 = i == 0 ? previousLine : lines[i - 1];
                // System.out.println(previousLine2);
                // System.out.println(lines[i]);
                Object something = readLine(lines[i] + (!containsNewln ? "!" : ""), previousLine2, m, b);
                if (something instanceof FindEnclosing.MultipleLinesOutput) {
                    m = ((FindEnclosing.MultipleLinesOutput) something);
                } else if (something instanceof ArrayList<?>) {
                    m = null;
                    tokens.addAll((ArrayList<Token<?>>) something);
                } else if (something instanceof BlockChain) {
                    m = null;
                    b = (BlockChain) something;
                } else {
                    b = null;
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
                    ? processBlockLines(isComment, line, (FindEnclosing.MultipleLinesOutput) multipleLinesOutput,
                            tokenizerLine,
                            tokens, tContainer, type,
                            new String[] { "" }, null)
                    : processBlockLines(
                            isComment, line, (FindEnclosing.MultipleLinesOutput) multipleLinesOutput, tokenizerLine,
                            tokens, tContainer, type,
                            handleArgs(type, line), (blockChain != null ? blockChain.getInitialIf() : null));
        }

        // #STRING# is a line which contains something that needs to be tokenized.

        if (line.startsWith(Keywords.D_VAR)) {
            tokens.add(processVariable(line, tContainer));
            return tokens;
        }

        return void.class;
    }
}
