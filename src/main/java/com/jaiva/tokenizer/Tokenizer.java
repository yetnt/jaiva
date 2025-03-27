package com.jaiva.tokenizer;

import java.util.*;

import com.jaiva.Errors;
import com.jaiva.Errors.SyntaxError;
import com.jaiva.Errors.TokenizerException;
import com.jaiva.tokenizer.Token.TCodeblock;
import com.jaiva.tokenizer.Token.TIfStatement;
import com.jaiva.tokenizer.Token.TIntVar;
import com.jaiva.tokenizer.Token.TTryCatchStatement;

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
                .replace("$=", "=")
                .replace("$!", "!")
                .replace("$s", " ")
                .replace("$@", "@")
                .replace("$n", "\n")
                .replace("$t", "\t")
                .replace("$r", "\r")
                .replace("$b", "\b")
                .replace("$f", "\f")
                .replace("$'", "'")
                .replace("$\"", "\"")
                .replace("$$", "$");
    }

    /**
     * Unescape a string.
     *
     * @param str The string to unescape.
     * @return
     */
    public static String unescape(String str) {
        return str
                .replace("$", "$$")
                .replace("=", "$=")
                .replace("!", "$!")
                .replace(" ", "$s")
                .replace("@", "$@")
                .replace("\n", "$n")
                .replace("\t", "$t")
                .replace("\r", "$r")
                .replace("\b", "$b")
                .replace("\f", "$f")
                .replace("'", "$'")
                .replace("\"", "$\"")
                .replace("\\", "$\\");
    }

    public static String escapeAll(String line) {
        // maak a <- "heelo"!
        // func("weird thing", "with multiple strings")
        // We have to escape anything in EVEN number stuff (ODD number in 0-based
        // indexing)
        // 0, 1, 2, 3, 4
        // all the actual strings are stored in 1, 3, etc
        String[] parts = line.split("(?<!\\$)\""); // Negative lookbehind checks that their isnt $ before "
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i % 2 != 0) {
                String unescapedString = parts[i]
                        .split("((?<!\\$)(\\\"|\\!|\\\\|\\\"|\\'|@|=))|(\\t|\\n|\\s)").length != 1
                                ? unescape(parts[i])
                                : parts[i];
                b.append("\"" + unescapedString + "\"");
            } else {
                b.append(parts[i]);
            }
        }

        return b.toString();
    }
}

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
        type = multipleLinesOutput == null ? type : multipleLinesOutput.type;
        args = multipleLinesOutput == null ? args : multipleLinesOutput.args;
        Object output = handleBlocks(isComment, line + "\n", (FindEnclosing.MultipleLinesOutput) multipleLinesOutput,
                tokenizerLine, type, args, multipleLinesOutput != null ? multipleLinesOutput.specialArg : blockChain);
        if (output == null)
            return output;

        if (output instanceof FindEnclosing.MultipleLinesOutput) {
            int endCount = ((FindEnclosing.MultipleLinesOutput) output).endCount;
            int startCount = ((FindEnclosing.MultipleLinesOutput) output).startCount;
            // multipleLinesOutput = ((FindEnclosing.MultipleLinesOutput) output);
            if (endCount != startCount)
                return output;
        }
        String preLine = ((String) output);
        if (preLine.startsWith(Keywords.IF)) {
            System.out.println();
        }
        preLine = preLine.startsWith(Keywords.D_FUNCTION) ? preLine.replaceFirst(Keywords.D_FUNCTION, "") : preLine;
        preLine = preLine.startsWith(Keywords.IF) ? preLine.replaceFirst(Keywords.IF, "") : preLine;
        preLine = preLine.startsWith(Keywords.WHILE) ? preLine.replaceFirst(Keywords.WHILE, "") : preLine;
        preLine = preLine.startsWith(Keywords.FOR) ? preLine.replaceFirst(Keywords.FOR, "") : preLine;
        preLine = preLine.startsWith(Keywords.ELSE) ? preLine.replaceFirst(Keywords.ELSE, "") : preLine;
        preLine = preLine.startsWith(Keywords.CATCH) ? preLine.replaceFirst(Keywords.CATCH, "") : preLine;
        preLine = preLine.replace("$Nn", "").trim();
        preLine = preLine.substring(preLine.indexOf(Lang.BLOCK_OPEN) + 2);
        preLine = preLine.substring(0, preLine.lastIndexOf(Lang.BLOCK_CLOSE));
        ArrayList<Token<?>> nestedTokens = new ArrayList<>();
        try {
            nestedTokens.addAll((ArrayList<Token<?>>) readLine(preLine, "", null, null));
        } catch (Exception e) {
            e.printStackTrace();
        }
        TCodeblock codeblock = tContainer.new TCodeblock(nestedTokens);
        // Okay cool, we've parsed everything, but what if its different types?
        Token<?> specific;
        switch (type) {
            case "mara if": {
                Object obj = tContainer.new TStatement().parse(args[0]);
                if (tContainer.isValidBoolInput(obj)) {
                    obj = obj instanceof Token<?> ? ((Token<?>) obj).getValue() : obj;
                } else {
                    throw new Errors.TokenizerSyntaxException("Ayo the condiiton gotta resolve to a boolean dawg.");
                }
                TIfStatement ifStatement = tContainer.new TIfStatement(obj, codeblock);
                TIfStatement originalIf = ((TIfStatement) ((FindEnclosing.MultipleLinesOutput) multipleLinesOutput).specialArg
                        .getValue());
                originalIf.appendElseIf(ifStatement);
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
                Object obj = tContainer.new TStatement().parse(args[0].replace("(", ""));
                if (tContainer.isValidBoolInput(obj)) {
                    obj = obj instanceof Token<?> ? ((Token<?>) obj).getValue() : obj;
                } else {
                    throw new Errors.TokenizerSyntaxException("Ayo the condiiton gotta resolve to a boolean dawg.");
                }
                specific = tContainer.new TIfStatement(obj, codeblock).toToken();
                if (line.contains(Keywords.ELSE)) {
                    // will turn "~> mara <- ... " into "mara <- ..."
                    // and "~> mara if () <- ..." into "mara if () <- ..."
                    String l = line.replaceFirst(Lang.BLOCK_CLOSE, "").trim();
                    System.out.println(l);
                    return new BlockChain(specific, l);
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

                Object obj = tContainer.new TStatement().parse(args[1]);
                if (tContainer.isValidBoolInput(obj)) {
                    obj = obj instanceof Token<?> ? ((Token<?>) obj).getValue() : obj;
                } else {
                    throw new Errors.TokenizerSyntaxException("Ayo the condiiton gotta resolve to a boolean dawg.");
                }
                specific = tContainer.new TForLoop(variable, obj, args[2], codeblock).toToken();
                break;
            }
            case "kwenza": {
                specific = tContainer.new TFunction(args[0], args[1].split(","), codeblock).toToken();
                break;
            }
            case "nikhil": {
                Object obj = tContainer.new TStatement().parse(args[0]);
                if (tContainer.isValidBoolInput(obj)) {
                    obj = obj instanceof Token<?> ? ((Token<?>) obj).getValue() : obj;
                } else {
                    throw new Errors.TokenizerSyntaxException("Ayo the condiiton gotta resolve to a boolean dawg.");
                }
                specific = tContainer.new TWhileLoop(obj, codeblock).toToken();
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
            line = line.substring(0, stringStart - 1) + encasedString;
            // line = line.substring(0, stringStart - 1) +
            // EscapeSequence.unescape(encasedString) + "!";
            // #maak varuiablename = "$svalue"!#
        }
        line = line.trim();
        line = line.substring(4, line.length());
        // #varuiablename <- $svalue!#
        // #varuiablename <- 49!#
        // #varuiablename <- true!#
        // #varuiablename <- true! @comment#

        String[] parts = line.split(Lang.ASSIGNMENT);
        // parts = ["varuiablename ", " $svalue!"]
        // parts = ["varuiablename ", " 49!"]
        // parts = ["varuiablename ", " true!"]
        // parts = ["varuiablename ", " true! @comment"]

        parts[0] = parts[0].trim();
        if (parts.length == 1) {
            // they declared the variable with no value. Still valid syntax.
            return tContainer.new TUnknownVar(parts[0], null).toToken();
        }
        parts[1] = parts[1].trim();

        // int isStatement = Lang.containsOperator(parts[1].toCharArray());
        // parts = ["varuiablename", "$svalue"]
        // parts = ["varuiablename", "49"]
        // parts = ["varuiablename", "true"]

        if (isString) {
            return tContainer.new TStringVar(parts[0], parts[1]).toToken();
            // return tContainer.new TStringVar(parts[0],
            // EscapeSequence.escape(parts[1])).toToken();
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
                    Object output = tContainer.dispatchContext(parts[1]);
                    if (output instanceof Token<?>) {
                        TokenDefault g = ((Token<?>) output).getValue();
                        if (g.name.equals("TStatement")) {
                            return ((Token<?>.TStatement) g).statementType == 0
                                    ? tContainer.new TBooleanVar(parts[0], output).toToken()
                                    : tContainer.new TIntVar(parts[0], output).toToken();
                        }
                    }
                    return tContainer.new TUnknownVar(parts[0], output).toToken();
                    // if (output instanceof Token<?>) {
                    // return ((Token<?>) output);
                    // } else {
                    // throw new TokenizerException("Somethign went wrong..");
                    // }
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
     * Notice how there's more parameters than documented. This tells you that
     * I documented this a bit early. Not adding more.
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
        line = EscapeSequence.escapeAll(line);
        line = line.trim();
        boolean cont = multipleLinesOutput instanceof FindEnclosing.MultipleLinesOutput;
        boolean isComment = (cont && ((FindEnclosing.MultipleLinesOutput) multipleLinesOutput).isComment)
                || (line.startsWith("{") || (line.indexOf(Lang.COMMENT_OPEN) != -1
                        && line.charAt(line.indexOf(Lang.COMMENT_OPEN)) != '$'));
        boolean isCodeBlock = (cont && !((FindEnclosing.MultipleLinesOutput) multipleLinesOutput).isComment)
                || (line.startsWith(Keywords.IF) || line.startsWith(Keywords.D_FUNCTION)
                        || line.startsWith(Keywords.FOR))
                || line.startsWith(Keywords.WHILE) || line.startsWith(Keywords.ELSE) || line.startsWith(Keywords.TRY)
                || line.startsWith(Keywords.CATCH);

        // To the developer who would like to understand this.
        // Don't. Just don't. It's a mess. I am NOT sorry.
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
        // line.charAt(line.indexOf('-') - 1) != '$' && line
        // .charAt(line.indexOf('-') - 1) != '<'));

        ArrayList<Token<?>> tokens = new ArrayList<>();
        Token<?> tContainer = new Token<>(null);
        boolean containsNewln = line.contains("\n");
        String[] lines = containsNewln ? line.split("\n") : line.split("(?<!\\$)!(?!\\=)");

        if (lines.length > 1 && !lines[1].isEmpty()) {
            // System.out.println("Multiple lines detected!");
            // multiple lines.
            FindEnclosing.MultipleLinesOutput m = null;
            BlockChain b = blockChain;
            for (int i = 0; i != lines.length; i++) {
                String l = "";
                if (b instanceof BlockChain) {
                    l = b.getCurrentLine();
                    i--;
                } else {
                    l = lines[i] + (!containsNewln ? "!" : "");
                }
                // System.out.println("Reading line " + i + "...");
                String previousLine2 = i == 0 ? previousLine : lines[i - 1];
                // System.out.println(previousLine2);
                // System.out.println(lines[i]);
                Object something = readLine(l, previousLine2, m, b);
                if (something instanceof FindEnclosing.MultipleLinesOutput) {
                    m = ((FindEnclosing.MultipleLinesOutput) something);
                    b = null;
                } else if (something instanceof ArrayList<?>) {
                    m = null;
                    b = null;
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

        line = decimateSingleComments(line);
        line.trim(); // what if there was a single comment after a bunch of spaces and theres real
                     // code before it?

        /*
         * {
         * multi line comment}
         * and block stuff
         */
        if (cont || isComment || isCodeBlock)
            return type == null
                    ? processBlockLines(isComment, line, (FindEnclosing.MultipleLinesOutput) multipleLinesOutput,
                            tokenizerLine,
                            tokens, tContainer, type,
                            new String[] { "" }, null)
                    : processBlockLines(
                            isComment, line, (FindEnclosing.MultipleLinesOutput) multipleLinesOutput, tokenizerLine,
                            tokens, tContainer, type,
                            handleArgs(type, line), (blockChain != null ? blockChain.getInitialIf() : null));

        // if its anything after this the line has to end in a ! or else invalid syntax.
        // (All other methods which did the ! will be redundant as we can just check
        // here.)

        if (!line.isEmpty() && !line.equals(Lang.BLOCK_CLOSE) && !line.endsWith(Lang.BLOCK_OPEN)
                && (!line.endsWith("!"))) {
            throw new Errors.SyntaxCriticalError("Ye wena shout your code!");
        }

        line = line.isEmpty() ? line : line.substring(0, line.length() - 1);

        // #STRING# is (khutla 100!) syntax which is a function return.

        if (line.startsWith(Keywords.THROW) && line.contains(Lang.THROW_ERROR)) {
            // Remove keyword 'cima' and operator '<=='
            line = line.trim();
            String withoutKeyword = line.substring(Keywords.THROW.length()).trim();
            // Split on the operator "<=="
            String[] parts = withoutKeyword.split(Lang.THROW_ERROR);
            if (parts.length != 2) {
                throw new Errors.SyntaxCriticalError("Ehh baba you must use the right syntax to throw an error.");
            }
            String errorMessage = parts[1].trim();
            if (errorMessage.startsWith("\"") && errorMessage.endsWith("\"") && errorMessage.length() >= 2) {
                errorMessage = errorMessage.substring(1, errorMessage.length() - 1);
                // errorMessage = EscapeSequence.escape(errorMessage.substring(1,
                // errorMessage.length() - 1));
            }
            tokens.add(tContainer.new TThrowError(errorMessage).toToken());
            return tokens;
        }

        if (line.startsWith(Keywords.LC_BREAK) || line.startsWith(Keywords.LC_CONTINUE)) {
            switch (line) {
                case "voetsek", "nevermind":
                    return tContainer.new TLoopControl(line).toToken();
                default:
                    throw new Errors.SyntaxCriticalError("Loop control keywords should be by themselves big bro.");
            }
        }

        if (line.startsWith(Keywords.RETURN)) {
            tokens.add(
                    tContainer.new TFuncReturn(tContainer.processContext(line.replace(Keywords.RETURN, "")))
                            .toToken());
            return tokens;
        }

        // #STRING# is a line which contains something that needs to be tokenized.

        if (line.startsWith(Keywords.D_VAR)) {
            tokens.add(processVariable(line, tContainer));
            return tokens;
        }

        // if it doesnt start with declaration nfor var, it might be a reassingment
        String[] parts = line.split(Lang.ASSIGNMENT);
        if (parts.length > 1) {
            tokens.add(
                    tContainer.new TVarReassign(parts[0].trim(), tContainer.processContext(parts[1].trim())).toToken());
            return tokens;
        }

        // if we've reached here, then it doesnt have a keyword prolly. meaning
        // TFuncCall
        // (You cant just place a variable out of nowhere with no context around it.)
        // (Therefore i wont parse it.)
        Object token = tContainer.processContext(line);
        if (token instanceof Token<?>) {
            tokens.add((Token<?>) token);
            return tokens;
        }

        // Last resort: Don't fucking know what this line is supposed to tokenize as
        // Therefore:
        return void.class;
    }
}
