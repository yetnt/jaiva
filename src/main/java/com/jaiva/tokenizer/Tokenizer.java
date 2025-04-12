package com.jaiva.tokenizer;

import java.util.*;

import com.jaiva.errors.TokErrs.*;
import com.jaiva.tokenizer.Token.TCodeblock;
import com.jaiva.tokenizer.Token.TIfStatement;
import com.jaiva.tokenizer.Token.TNumberVar;
import com.jaiva.tokenizer.Token.TTryCatchStatement;
import com.jaiva.utils.BlockChain;
import com.jaiva.utils.Find;
import com.jaiva.utils.Validate;

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

        line = line.substring(0, line.indexOf(Lang.COMMENT));

        return line;
    }

    /**
     * Check if the array is only comments. This is used to check if the line
     * contains only comments or not.
     * 
     * This is a helper method where a ! may be splitting inside a comment which had
     * a double @ symbol.
     * 
     * @param arr
     * @return
     */
    private static boolean arrayIsOnlyComments(String[] arr) {
        if (arr.length > 2) {
            for (String s : arr) {
                if (!s.trim().isEmpty() && !s.trim().startsWith(Character.toString(Lang.COMMENT))) {
                    return false;
                }
            }
            return true;
        } else {
            // if one of the elements is a comment, and the other isnt, return true.
            // check both elements.
            // otherwise return false.
            if (arr[0].trim().isEmpty() && arr[1].trim().isEmpty()) {
                return true;
            } else if (arr[0].trim().isEmpty() && arr[1].trim().startsWith(Character.toString(Lang.COMMENT))) {
                return true;
            } else if (arr[0].trim().startsWith(Character.toString(Lang.COMMENT)) && arr[1].trim().isEmpty()) {
                return true;
            } else if (arr[0].trim().startsWith(Character.toString(Lang.COMMENT))
                    && !arr[1].trim().startsWith(Character.toString(Lang.COMMENT))
                    || !arr[0].trim()
                            .startsWith(Character.toString(Lang.COMMENT))
                            && arr[1].trim().startsWith(Character.toString(Lang.COMMENT))) {
                return true;
            } else {
                return false;
            }

        }
    }

    private static Object handleBlocks(boolean isComment, String line,
            Find.MultipleLinesOutput multipleLinesOutput, String entireLine, String t, String[] args,
            Token<?> blockChain) {
        Find.MultipleLinesOutput m;
        if (multipleLinesOutput != null) {
            // multiple lines output exists. So we need to keep going until we find }
            m = isComment ? Find.closingCharIndexML(
                    line, Lang.COMMENT_OPEN, Lang.COMMENT_CLOSE, multipleLinesOutput.startCount,
                    multipleLinesOutput.endCount)
                    : Find.closingCharIndexML(
                            line, Lang.BLOCK_OPEN, Lang.BLOCK_CLOSE,
                            multipleLinesOutput.startCount,
                            multipleLinesOutput.endCount, multipleLinesOutput.preLine,
                            t, args, blockChain);
        } else {
            // Not given, but we need to find the closing tag.
            m = isComment ? Find.closingCharIndexML(
                    line, Lang.COMMENT_OPEN, Lang.COMMENT_CLOSE, 0, 0)
                    : Find.closingCharIndexML(line, Lang.BLOCK_OPEN, Lang.BLOCK_CLOSE, 0, 0, "",
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
            Find.MultipleLinesOutput multipleLinesOutput,
            String tokenizerLine, ArrayList<Token<?>> tokens, Token<?> tContainer, String type, String[] args,
            Token<?> blockChain)
            throws Exception {
        type = multipleLinesOutput == null ? type : multipleLinesOutput.type;
        args = multipleLinesOutput == null ? args : multipleLinesOutput.args;
        line = decimateSingleComments(line);
        Object output = handleBlocks(isComment, line + "\n", (Find.MultipleLinesOutput) multipleLinesOutput,
                tokenizerLine, type, args, multipleLinesOutput != null ? multipleLinesOutput.specialArg : blockChain);
        if (output == null)
            return output;

        if (output instanceof Find.MultipleLinesOutput) {
            int endCount = ((Find.MultipleLinesOutput) output).endCount;
            int startCount = ((Find.MultipleLinesOutput) output).startCount;
            // multipleLinesOutput = ((FindEnclosing.MultipleLinesOutput) output);
            if (endCount != startCount)
                return output;
        }
        String preLine = ((String) output);
        preLine = preLine.startsWith(Keywords.D_FUNCTION) ? preLine.replaceFirst(Keywords.D_FUNCTION, "") : preLine;
        preLine = preLine.startsWith(Keywords.IF) ? preLine.replaceFirst(Keywords.IF, "") : preLine;
        preLine = preLine.startsWith(Keywords.WHILE) ? preLine.replaceFirst(Keywords.WHILE, "") : preLine;
        preLine = preLine.startsWith(Keywords.FOR) ? preLine.replaceFirst(Keywords.FOR, "") : preLine;
        preLine = preLine.startsWith(Keywords.ELSE) ? preLine.replaceFirst(Keywords.ELSE, "") : preLine;
        preLine = preLine.startsWith(Keywords.CATCH) ? preLine.replaceFirst(Keywords.CATCH, "") : preLine;
        preLine = preLine.startsWith(Keywords.TRY) ? preLine.replaceFirst(Keywords.TRY, "") : preLine;
        preLine = preLine.replace("$Nn", "").trim();
        preLine = preLine.substring(preLine.indexOf(Lang.BLOCK_OPEN) + 2);
        preLine = preLine.substring(0, preLine.lastIndexOf(Lang.BLOCK_CLOSE));
        ArrayList<Token<?>> nestedTokens = new ArrayList<>();
        Object stuff = readLine(preLine, "", null, null);
        try {
            if (stuff instanceof ArrayList) {
                nestedTokens.addAll((ArrayList<Token<?>>) stuff);
            } else if (stuff instanceof Token<?>) {
                nestedTokens.add((Token<?>) stuff);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TCodeblock codeblock = tContainer.new TCodeblock(nestedTokens);
        // Okay cool, we've parsed everything, but what if its different types?
        Token<?> specific;
        switch (type) {
            case "mara if": {
                Object obj = tContainer.new TStatement().parse(args[0]);
                if (Validate.isValidBoolInput(obj)) {
                    obj = obj instanceof Token<?> ? ((Token<?>) obj).getValue() : obj;
                } else {
                    throw new TokenizerSyntaxException("Ayo the condiiton gotta resolve to a boolean dawg.");
                }
                TIfStatement ifStatement = tContainer.new TIfStatement(obj, codeblock);
                TIfStatement originalIf = ((TIfStatement) ((Find.MultipleLinesOutput) multipleLinesOutput).specialArg
                        .getValue());
                originalIf.appendElseIf(ifStatement);
                specific = originalIf.toToken();
                if (line.contains(Keywords.ELSE)) {
                    return new BlockChain(specific, line.replaceFirst(Lang.BLOCK_CLOSE, "").trim());
                }
                break;
            }
            case "mara": {
                TIfStatement originalIf = ((TIfStatement) ((Find.MultipleLinesOutput) multipleLinesOutput).specialArg
                        .getValue());
                originalIf.appendElse(codeblock);
                specific = originalIf.toToken();
                break;
            }
            case "if": {
                Object obj = tContainer.new TStatement().parse(args[0].replace("(", ""));
                if (Validate.isValidBoolInput(obj)) {
                    obj = obj instanceof Token<?> ? ((Token<?>) obj).getValue() : obj;
                } else {
                    throw new TokenizerSyntaxException("Ayo the condiiton gotta resolve to a boolean dawg.");
                }
                specific = tContainer.new TIfStatement(obj, codeblock).toToken();
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
                TTryCatchStatement tryCatch = ((TTryCatchStatement) ((Find.MultipleLinesOutput) multipleLinesOutput).specialArg
                        .getValue());
                tryCatch.appendCatchBlock(codeblock);
                specific = tryCatch.toToken();
                break;
            }
            case "colonize": {
                TNumberVar variable = (Token<TNumberVar>.TNumberVar) ((ArrayList<Token<?>>) readLine(
                        "maak " + args[0].replace("(", "").trim() + "!", "", null, null)).get(0).getValue();

                Object obj = tContainer.new TStatement().parse(args[1]);
                if (Validate.isValidBoolInput(obj)) {
                    obj = obj instanceof Token<?> ? ((Token<?>) obj).getValue() : obj;
                } else {
                    throw new TokenizerSyntaxException("Ayo the condiiton gotta resolve to a boolean dawg.");
                }
                specific = tContainer.new TForLoop(variable, obj, args[2].replace(")", "").trim(),
                        codeblock).toToken();
                break;
            }
            case "kwenza": {
                String[] fArgs = args[1].split(",");
                for (int i = 0; i < fArgs.length; i++)
                    fArgs[i] = fArgs[i].trim();
                specific = tContainer.new TFunction(args[0], fArgs, codeblock).toToken();
                break;
            }
            case "nikhil": {
                Object obj = tContainer.new TStatement().parse(args[0]);
                if (Validate.isValidBoolInput(obj)) {
                    obj = obj instanceof Token<?> ? ((Token<?>) obj).getValue() : obj;
                } else {
                    throw new TokenizerSyntaxException("Ayo the condiiton gotta resolve to a boolean dawg.");
                }
                specific = tContainer.new TWhileLoop(obj, codeblock).toToken();
                break;
            }
            default: {
                throw new TokenizerException("Uhm, something went wrong. This shouldnt happen.");
            }
        }
        tokens.add(specific);
        return tokens;
    }

    private static Token<?> processVariable(String line, Token<?> tContainer)
            throws SyntaxError, TokenizerException {
        boolean isString = false;
        if (line.indexOf(Lang.ARRAY_ASSIGNMENT) != -1) {
            line = line.trim();
            line = line.substring(4, line.length());
            String[] parts = line.split("<-\\|");
            parts[0] = parts[0].trim();
            if (parts[0].isEmpty()) {
                throw new SyntaxCriticalError("Bro defined a variable with no name lmao.");
            }
            ArrayList<Object> parsedValues = new ArrayList<>();

            if (parts.length == 1) {
                // delcared with no value, empty array.
                return tContainer.new TArrayVar(parts[0], parsedValues).toToken();
            }

            tContainer.splitByTopLevelComma(parts[1]).forEach(value -> {
                parsedValues.add(tContainer.processContext(value.trim()));
            });
            return tContainer.new TArrayVar(parts[0], parsedValues).toToken();

        }
        int stringStart = line.indexOf("\"");
        int stringEnd = Find.closingCharIndex(line, '"', '"');
        if (stringStart != -1 && stringEnd != -1) {
            isString = true;
            String encasedString = line.substring(stringStart + 1, stringEnd);
            line = line.substring(0, stringStart - 1) + encasedString;
        }
        line = line.trim();
        line = line.substring(4, line.length());
        String[] parts = line.split(Lang.ASSIGNMENT);
        parts[0] = parts[0].trim();
        if (parts.length == 1) {
            // they declared the variable with no value. Still valid syntax.
            // unless the name is nothing, then we have a problem.
            if (parts[0].isEmpty()) {
                throw new SyntaxCriticalError("Bro defined a variable with no name or value lmao.");
            }
            if (parts.length == 1 && line.indexOf(Lang.ASSIGNMENT) != -1)
                throw new SyntaxCriticalError(
                        "if you're finna define a variable without a value, remove the assignment operator.");
            return tContainer.new TUnknownVar(parts[0], null).toToken();
        }
        parts[1] = parts[1].trim();

        if (isString) {
            return tContainer.new TStringVar(parts[0], parts[1]).toToken();
        } else {
            try {
                return tContainer.new TNumberVar(parts[0], Integer.parseInt(parts[1])).toToken();
            } catch (NumberFormatException e) {
                try {
                    return tContainer.new TNumberVar(parts[0], Double.parseDouble(parts[1])).toToken();
                } catch (Exception e2) {
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
                                        : tContainer.new TNumberVar(parts[0], output).toToken();
                            }
                        }

                        return tContainer.new TUnknownVar(parts[0], output).toToken();
                    }
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
        line = EscapeSequence.escapeAll(line).trim();
        line = line.trim();
        boolean cont = multipleLinesOutput instanceof Find.MultipleLinesOutput;
        boolean isComment = (cont && ((Find.MultipleLinesOutput) multipleLinesOutput).isComment)
                || (multipleLinesOutput == null && (line.startsWith(Character.toString(Lang.COMMENT_OPEN))
                        || (line.indexOf(Lang.COMMENT_OPEN) != -1
                                && line.charAt(line.indexOf(Lang.COMMENT_OPEN)) != Lang.ESCAPE)));
        boolean isCodeBlock = (cont && !((Find.MultipleLinesOutput) multipleLinesOutput).isComment)
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
                                                                                        .trim()
                                                                                        .startsWith(Keywords.CATCH)
                                                                                                ? Keywords.CATCH
                                                                                                : null;
        // || (line.contains(Lang.BLOCK_OPEN) && (line.indexOf('-') > 0 &&
        // line.charAt(line.indexOf('-') - 1) != '$' && line
        // .charAt(line.indexOf('-') - 1) != '<'));

        if ((line.contains(Lang.BLOCK_OPEN) && line.indexOf(Lang.BLOCK_OPEN) < line.indexOf("\n")) && type == null
                && !line.startsWith(Character.toString(Lang.COMMENT)))
            // A block of code but the type waws not catched, invaliud keyword then.
            // This is a syntax error.
            throw new SyntaxCriticalError(line.split(" ")[0] + " aint a real keyword homie.");

        ArrayList<Token<?>> tokens = new ArrayList<>();
        Token<?> tContainer = new Token<>(null);
        boolean containsNewln = line.contains("\n");
        String[] lines = containsNewln ? line.split("\n") : line.split("(?<!\\$)!(?!\\=)");

        if (lines.length > 1 && !lines[1].isEmpty() && !arrayIsOnlyComments(lines)) {
            // System.out.println("Multiple lines detected!");
            // multiple lines.
            Find.MultipleLinesOutput m = null;
            BlockChain b = null;
            for (int i = 0; i != lines.length; i++) {
                String l = "";
                if (b instanceof BlockChain) {
                    l = b.getCurrentLine();
                    i--;
                } else {
                    l = lines[i] + (!containsNewln ? Character.toString(Lang.END_LINE) : "");
                }
                // System.out.println("Reading line " + i + "...");
                String previousLine2 = i == 0 ? previousLine : lines[i - 1];
                // System.out.println(previousLine2);
                // System.out.println(lines[i]);
                Object something = readLine(l, previousLine2, m, b);
                if (something instanceof Find.MultipleLinesOutput) {
                    m = ((Find.MultipleLinesOutput) something);
                    b = null;
                } else if (something instanceof ArrayList<?>) {
                    m = null;
                    b = null;
                    tokens.addAll((ArrayList<Token<?>>) something);
                } else if (something instanceof BlockChain) {
                    m = null;
                    b = (BlockChain) something;
                } else if (something instanceof Token<?>) {
                    tokens.add((Token<?>) something);
                    m = null;
                    b = null;
                } else {
                    m = null;
                    b = null;
                }
            }
            return tokens;
        }
        String tokenizerLine = previousLine.trim() + line; // The entire line to the tokenizer.

        // System.out.println("sdfsdfd");
        // @comment (single line)

        /*
         * {
         * multi line comment}
         * and block stuff
         */
        if (cont || isComment || isCodeBlock)
            return type == null
                    ? processBlockLines(isComment, line, (Find.MultipleLinesOutput) multipleLinesOutput,
                            tokenizerLine,
                            tokens, tContainer, type,
                            new String[] { "" }, null)
                    : processBlockLines(
                            isComment, line, (Find.MultipleLinesOutput) multipleLinesOutput, tokenizerLine,
                            tokens, tContainer, type,
                            handleArgs(type, line), (blockChain != null ? blockChain.getInitialIf() : null));

        // if its anything after this the line has to end in a ! or else invalid syntax.
        // (All other methods which did the ! will be redundant as we can just check
        // here.)
        line = decimateSingleComments(line).trim();
        if (line.isEmpty())
            return null;

        if (!line.isEmpty() && !line.equals(Lang.BLOCK_CLOSE) && !line.endsWith(Lang.BLOCK_OPEN)
                && (!line.endsWith(Character.toString(Lang.END_LINE)))) {
            throw new SyntaxCriticalError("Ye wena shout your code!");
        }

        line = line.isEmpty() ? line : line.substring(0, line.length() - 1);

        // #STRING# is (khutla 100!) syntax which is a function return.

        if (line.startsWith(Keywords.THROW)) {
            // Remove keyword 'cima' and operator '<=='
            line = line.trim();
            String withoutKeyword = line.substring(Keywords.THROW.length()).trim();
            // Split on the operator "<=="
            String[] parts = withoutKeyword.split(Lang.THROW_ERROR);
            if (parts.length != 2) {
                throw new SyntaxCriticalError("Ehh baba you must use the right syntax if u wanna cima this process.");
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

        line = line.trim();

        if (line.startsWith(Keywords.LC_BREAK) || line.startsWith(Keywords.LC_CONTINUE)) {
            switch (line) {
                case "voetsek", "nevermind":
                    return tContainer.new TLoopControl(line).toToken();
                default:
                    throw new SyntaxCriticalError("Loop control keywords should be by themselves big bro.");
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
            String varName = parts[0].trim();
            Object varValue = tContainer.processContext(parts[1].trim());
            if (varName.contains("]") || varName.contains("[")) {
                // This is an array reassignment.
                tokens.add(tContainer.new TVarReassign(tContainer.processContext(varName), varValue).toToken());
                return tokens;
            } else {
                tokens.add(
                        tContainer.new TVarReassign(parts[0].trim(), tContainer.processContext(parts[1].trim()))
                                .toToken());
            }
            return tokens;
        }

        // TODO: This token only exists for debugging purposes at the moment.
        if (line.equals(Keywords.UNDEFINED)) {
            tokens.add(tContainer.new TVoidValue().toToken());
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
