package com.jaiva.tokenizer;

import java.util.*;

import com.jaiva.errors.TokErrs.*;
import com.jaiva.tokenizer.Token.TArrayVar;
import com.jaiva.tokenizer.Token.TBooleanVar;
import com.jaiva.tokenizer.Token.TCodeblock;
import com.jaiva.tokenizer.Token.TDocsComment;
import com.jaiva.tokenizer.Token.TFunction;
import com.jaiva.tokenizer.Token.TIfStatement;
import com.jaiva.tokenizer.Token.TNumberVar;
import com.jaiva.tokenizer.Token.TStringVar;
import com.jaiva.tokenizer.Token.TTryCatchStatement;
import com.jaiva.tokenizer.Token.TUnknownVar;
import com.jaiva.tokenizer.Token.TVarRef;
import com.jaiva.utils.BlockChain;
import com.jaiva.utils.Find;
import com.jaiva.utils.Tuple2;
import com.jaiva.utils.Validate;
import com.jaiva.utils.Validate.IsValidSymbolName;

public class Tokenizer {

    private static Object handleBlocks(boolean isComment, String line,
            Find.MultipleLinesOutput multipleLinesOutput, String entireLine, String t, String[] args,
            Token<?> blockChain, int lineNumber) {
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
                            t, args, blockChain, lineNumber);
        } else {
            // Not given, but we need to find the closing tag.
            m = isComment ? Find.closingCharIndexML(
                    line, Lang.COMMENT_OPEN, Lang.COMMENT_CLOSE, 0, 0)
                    : Find.closingCharIndexML(line, Lang.BLOCK_OPEN, Lang.BLOCK_CLOSE, 0, 0, "",
                            t, args, blockChain, lineNumber);
        }

        if (m.endCount == m.startCount && m.startCount != 0 && m.startCount != -1) {
            return isComment ? null : m;
        } else {
            return m;
        }
    }

    private static String[] handleArgs(String type, String line) {
        switch (type) {
            case "mara if": {
                // mara condition ->
                // mara (i < 10) ->
                return new String[] {
                        line.substring(line.indexOf(Lang.STATEMENT_OPEN) + 1, line.lastIndexOf(Lang.STATEMENT_CLOSE)),
                        "" };
            }
            case "if": {
                // if (condition) ->
                // if (variable != 100) ->
                return new String[] {
                        line.substring(line.indexOf(Lang.STATEMENT_OPEN), line.lastIndexOf(Lang.STATEMENT_CLOSE)), "" };
            }
            case "nikhil": {
                // nikhil (condition) ->
                // nikhil (i < 10) ->
                return new String[] {
                        line.substring(line.indexOf(Lang.STATEMENT_OPEN) + 1, line.lastIndexOf(Lang.STATEMENT_CLOSE)),
                        "" };
            }
            case "colonize": {
                // colonize declaration | condition | increment ->
                // colonize variableName with array name ->

                // colonize i <- 0 | i <= 10 | + ->
                // colonize pointer with arr ->

                if (line.contains(Character.toString(Lang.FOR_SEPARATOR))) {
                    String[] parts = line.split(Keywords.FOR)[1].trim().split(Lang.BLOCK_OPEN)[0].split("\\|");
                    return new String[] { parts[0].trim(), parts[1].trim(), parts[2].trim() };
                } else {
                    String[] parts = line.split(Keywords.FOR)[1].trim().split(Lang.BLOCK_OPEN)[0]
                            .split(Keywords.FOR_EACH);
                    return new String[] { parts[0].trim(), parts[1].trim(), Keywords.FOR_EACH };
                }
            }
            case "kwenza": {
                // kwenza function_name(...args) ->
                // kwenza addition(param1, param2) ->
                String[] parts = line.split(Keywords.D_FUNCTION)[1].trim().split(Lang.BLOCK_OPEN);
                String functionName = parts[0].substring(0, parts[0].indexOf(Lang.STATEMENT_OPEN));
                String args = parts[0].substring(parts[0].indexOf(Lang.STATEMENT_OPEN) + 1,
                        parts[0].indexOf(Lang.STATEMENT_CLOSE));
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
            Token<?> blockChain, int lineNumber)
            throws Exception {
        type = multipleLinesOutput == null ? type : multipleLinesOutput.type;
        args = multipleLinesOutput == null ? args : multipleLinesOutput.args;
        int newLineNumber = multipleLinesOutput == null ? lineNumber : multipleLinesOutput.lineNumber;
        line = Comments.decimate(line);
        Object output = handleBlocks(isComment, line + "\n", (Find.MultipleLinesOutput) multipleLinesOutput,
                tokenizerLine, type, args, multipleLinesOutput != null ? multipleLinesOutput.specialArg : blockChain,
                newLineNumber);
        if (output == null)
            return output;

        if (output instanceof Find.MultipleLinesOutput) {
            int endCount = ((Find.MultipleLinesOutput) output).endCount;
            int startCount = ((Find.MultipleLinesOutput) output).startCount;
            // multipleLinesOutput = ((FindEnclosing.MultipleLinesOutput) output);
            if (endCount != startCount)
                return output;
        }
        Find.MultipleLinesOutput finalMOutput = ((Find.MultipleLinesOutput) output);
        String preLine = finalMOutput.preLine;
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
        Object stuff = readLine(preLine, "", null, null, finalMOutput.lineNumber + 1);
        try {
            if (stuff instanceof ArrayList) {
                nestedTokens.addAll((ArrayList<Token<?>>) stuff);
            } else if (stuff instanceof Token<?>) {
                nestedTokens.add((Token<?>) stuff);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TCodeblock codeblock = tContainer.new TCodeblock(nestedTokens, finalMOutput.lineNumber, lineNumber);
        // Okay cool, we've parsed everything, but what if its different types?
        Token<?> specific;
        switch (type) {
            case "mara if": {
                Object obj = tContainer.new TStatement(finalMOutput.lineNumber).parse(args[0]);
                if (Validate.isValidBoolInput(obj)) {
                    obj = obj instanceof Token<?> ? ((Token<?>) obj).getValue() : obj;
                } else {
                    throw new TokenizerSyntaxException(
                            "Ayo the condition on line" + finalMOutput.lineNumber + "gotta resolve to a boolean dawg.");
                }
                TIfStatement ifStatement = tContainer.new TIfStatement(obj, codeblock, finalMOutput.lineNumber);
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
                Object obj = tContainer.new TStatement(finalMOutput.lineNumber)
                        .parse(args[0].replaceFirst("\\" + Character.toString(Lang.STATEMENT_OPEN), " "));
                if (Validate.isValidBoolInput(obj)) {
                    obj = obj instanceof Token<?> ? ((Token<?>) obj).getValue() : obj;
                } else {
                    throw new TokenizerSyntaxException(
                            "Ayo the condition on line" + finalMOutput.lineNumber + "gotta resolve to a boolean dawg.");
                }
                specific = tContainer.new TIfStatement(obj, codeblock, finalMOutput.lineNumber).toToken();
                if (line.contains(Keywords.ELSE)) {
                    return new BlockChain(specific, line.replaceFirst(Lang.BLOCK_CLOSE, "").trim());
                }
                break;
            }
            case "zama zama": {
                specific = tContainer.new TTryCatchStatement(codeblock, finalMOutput.lineNumber).toToken();
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
                if (!args[2].equals(Keywords.FOR_EACH)) {
                    TokenDefault var = (TokenDefault) ((ArrayList<Token<?>>) readLine(
                            Keywords.D_VAR + " "
                                    + args[0].replaceFirst("\\" + Character.toString(Lang.STATEMENT_OPEN), " ").trim()
                                    + Character.toString(Lang.END_LINE),
                            "", null,
                            null, finalMOutput.lineNumber))
                            .get(0).getValue();

                    Object obj = tContainer.new TStatement(finalMOutput.lineNumber).parse(args[1]);
                    if (Validate.isValidBoolInput(obj)) {
                        obj = obj instanceof Token<?> ? ((Token<?>) obj).getValue() : obj;
                    } else {
                        throw new TokenizerSyntaxException("Ayo the condition on line" + finalMOutput.lineNumber
                                + "gotta resolve to a boolean dawg.");
                    }
                    specific = tContainer.new TForLoop(
                            var instanceof TUnknownVar ? (TUnknownVar) var : (TNumberVar) var, obj,
                            args[2].replace(Lang.STATEMENT_CLOSE, ' ').trim(),
                            codeblock, finalMOutput.lineNumber).toToken();
                } else {
                    TUnknownVar variable = (Token<TUnknownVar>.TUnknownVar) ((ArrayList<Token<?>>) readLine(
                            Keywords.D_VAR + " "
                                    + args[0].replaceFirst("\\" + Character.toString(Lang.STATEMENT_OPEN), " ").trim()
                                    + Lang.END_LINE,
                            "",
                            null, null,
                            finalMOutput.lineNumber))
                            .get(0).getValue();
                    TVarRef arrayVar = (Token<TVarRef>.TVarRef) ((Token<?>) tContainer.processContext(args[1]
                            .replace(Lang.STATEMENT_CLOSE, ' ')
                            .trim(),
                            finalMOutput.lineNumber))
                            .getValue();
                    specific = tContainer.new TForLoop(variable, arrayVar, codeblock,
                            finalMOutput.lineNumber).toToken();
                }
                break;
            }
            case "kwenza": {
                String[] fArgs = args[1].split(Character.toString(Lang.ARGS_SEPARATOR));
                for (int i = 0; i < fArgs.length; i++)
                    fArgs[i] = fArgs[i].trim();

                IsValidSymbolName IVSN = Validate.isValidSymbolName(args[0]);
                if (!IVSN.isValid)
                    throw new SyntaxCriticalError(IVSN.op + " cannot be used in a variable name zawg.");
                specific = tContainer.new TFunction(args[0], fArgs, codeblock, finalMOutput.lineNumber).toToken();
                break;
            }
            case "nikhil": {
                Object obj = tContainer.new TStatement(finalMOutput.lineNumber).parse(args[0]);
                if (Validate.isValidBoolInput(obj)) {
                    obj = obj instanceof Token<?> ? ((Token<?>) obj).getValue() : obj;
                } else {
                    throw new TokenizerSyntaxException(
                            "Ayo the condition on line" + finalMOutput.lineNumber + "gotta resolve to a boolean dawg.");
                }
                specific = tContainer.new TWhileLoop(obj, codeblock, finalMOutput.lineNumber).toToken();
                break;
            }
            default: {
                throw new TokenizerException("Uhm, something went wrong. This shouldnt happen.");
            }
        }
        tokens.add(specific);
        return tokens;
    }

    private static Token<?> processVariable(String line, Token<?> tContainer, int lineNumber)
            throws SyntaxError, TokenizerException {
        boolean isString = false;
        if (line.indexOf(Lang.ARRAY_ASSIGNMENT) != -1) {
            line = line.trim();
            line = line.substring(4, line.length());
            String[] parts = line.split("<-\\|");
            parts[0] = parts[0].trim();
            if (parts[0].isEmpty()) {
                throw new SyntaxCriticalError("Bro defined a variable on line " + lineNumber + " with no name lmao.");
            }
            ArrayList<Object> parsedValues = new ArrayList<>();

            if (parts.length == 1) {
                // delcared with no value, empty array.
                return tContainer.new TArrayVar(parts[0], parsedValues, lineNumber).toToken();
            }

            tContainer.splitByTopLevelComma(parts[1]).forEach(value -> {
                parsedValues.add(tContainer.processContext(value.trim(), lineNumber));
            });
            return tContainer.new TArrayVar(parts[0], parsedValues, lineNumber).toToken();

        }
        int stringStart = line.indexOf(Lang.STRING);
        int stringEnd = Find.closingCharIndex(line, Lang.STRING, Lang.STRING);
        ArrayList<Tuple2<Integer, Integer>> quotepairs = Find.quotationPairs(line);
        if (stringStart != -1 && stringEnd != -1 && quotepairs.size() == 1
                && (line.charAt(line.length() - 1) == Lang.STRING
                        && line.split(Lang.ASSIGNMENT)[1].trim().charAt(0) == Lang.STRING)) {
            isString = true;
            String encasedString = line.substring(stringStart + 1, stringEnd);
            line = line.substring(0, stringStart - 1) + encasedString;
        }
        line = line.trim();
        line = line.substring(4, line.length());
        String[] parts = line.split(Lang.ASSIGNMENT);
        parts[0] = parts[0].trim();
        IsValidSymbolName IVSN = Validate.isValidSymbolName(parts[0]);
        if (!IVSN.isValid)
            throw new SyntaxCriticalError(IVSN.op + " cannot be used in a variable name zawg.");
        if (parts.length == 1) {
            // they declared the variable with no value. Still valid syntax.
            // unless the name is nothing, then we have a problem.
            if (parts[0].isEmpty()) {
                throw new SyntaxCriticalError("Bro defined a variable on line " + lineNumber + " with no name lmao.");
            }
            if (parts.length == 1 && line.indexOf(Lang.ASSIGNMENT) != -1)
                throw new SyntaxCriticalError(
                        "if you're finna define a variable without a value, remove the assignment operator. (line "
                                + lineNumber + ")");
            return tContainer.new TUnknownVar(parts[0], null, lineNumber).toToken();
        }
        parts[1] = parts[1].trim();

        if (isString) {
            return tContainer.new TStringVar(parts[0], parts[1], lineNumber).toToken();
        } else {
            try {
                return tContainer.new TNumberVar(parts[0], Integer.parseInt(parts[1]), lineNumber).toToken();
            } catch (NumberFormatException e) {
                try {
                    return tContainer.new TNumberVar(parts[0], Double.parseDouble(parts[1]), lineNumber).toToken();
                } catch (Exception e2) {
                    if (parts[1].equals("true") || parts[1].equals("false") || parts[1].equals(Keywords.TRUE)
                            || parts[1].equals(Keywords.FALSE)) {
                        parts[1] = parts[1].replace(Keywords.TRUE, "true").replace(
                                Keywords.FALSE,
                                "false");
                        return tContainer.new TBooleanVar(parts[0], Boolean.parseBoolean(parts[1]),
                                lineNumber).toToken();
                    } else {
                        Object output = tContainer.dispatchContext(parts[1], lineNumber);
                        if (output instanceof Token<?>) {
                            TokenDefault g = ((Token<?>) output).getValue();
                            if (g.name.equals("TStatement")) {
                                return ((Token<?>.TStatement) g).statementType == 0
                                        ? tContainer.new TBooleanVar(parts[0], output,
                                                lineNumber).toToken()
                                        : tContainer.new TNumberVar(parts[0], output, lineNumber).toToken();
                            }
                        }

                        return tContainer.new TUnknownVar(parts[0], output, lineNumber).toToken();
                    }
                }
            }
        }
    }

    private static Token<?> handleImport(String line, Token<?> tContainer, int lineNumber) throws SyntaxCriticalError {
        // tsea "path"
        // tsea "path" <- funcz, funca
        line = line.replace(Keywords.IMPORT.get(0), "").replace(Keywords.IMPORT.get(1), "").trim();

        // "path"
        // "path" <- funcz, funca
        String[] parts = line.split(Lang.ASSIGNMENT);

        ArrayList<Tuple2<Integer, Integer>> quotepairs = Find.quotationPairs(line);
        if (quotepairs.size() == 0) {
            throw new SyntaxCriticalError(
                    "Bro, the file to take from has to be surrounded by qutoes. (Line " + lineNumber + ")");

        }
        int stringStart = line.indexOf(Lang.STRING);
        int stringEnd = Find.closingCharIndex(line, Lang.STRING, Lang.STRING);
        String path = line.substring(stringStart + 1, stringEnd);

        if (parts.length > 1) {
            // ""path"", "funcz, funca"
            ArrayList<String> args = new ArrayList<>();
            for (String arg : parts[1].trim().split(Character.toString(Lang.ARGS_SEPARATOR)))
                args.add(arg.trim());
            return tContainer.new TImport(path, args, lineNumber).toToken();
        } else {
            return tContainer.new TImport(path, lineNumber).toToken();
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
    public static Object readLine(String line, String previousLine, Object multipleLinesOutput, BlockChain blockChain,
            int lineNumber)
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
        String type = multipleLinesOutput != null ? ((Find.MultipleLinesOutput) multipleLinesOutput).type
                : line.startsWith(Keywords.IF) ? Keywords.IF
                        : line.startsWith(Keywords.D_FUNCTION) ? Keywords.D_FUNCTION
                                : line.startsWith(Keywords.FOR) ? Keywords.FOR
                                        : line.startsWith(Keywords.WHILE) ? Keywords.WHILE
                                                : line.replace(Lang.BLOCK_CLOSE, "").trim()
                                                        .startsWith(Keywords.ELSE + " " + Keywords.IF)
                                                                ? Keywords.ELSE + " " + Keywords.IF // check for "ELSE
                                                                                                    // IF"
                                                                                                    // before
                                                                                                    // checking for "IF"
                                                                : line.replace(Lang.BLOCK_CLOSE, "").trim()
                                                                        .startsWith(Keywords.ELSE) ? Keywords.ELSE
                                                                                : line.startsWith(Keywords.TRY)
                                                                                        ? Keywords.TRY
                                                                                        : line.replace(Lang.BLOCK_CLOSE,
                                                                                                "")
                                                                                                .trim()
                                                                                                .startsWith(
                                                                                                        Keywords.CATCH)
                                                                                                                ? Keywords.CATCH
                                                                                                                : null;
        // || (line.contains(Lang.BLOCK_OPEN) && (line.indexOf('-') > 0 &&
        // line.charAt(line.indexOf('-') - 1) != '$' && line
        // .charAt(line.indexOf('-') - 1) != '<'));

        if ((line.contains(Lang.BLOCK_OPEN) && line.indexOf(Lang.BLOCK_OPEN) < line.indexOf("\n")) && type == null
                && !line.startsWith(Character.toString(Lang.COMMENT)))
            // A block of code but the type waws not catched, invaliud keyword then.
            // This is a syntax error.
            throw new SyntaxCriticalError(
                    line.split(" ")[0] + " on line " + lineNumber + " aint a real keyword homie.");

        ArrayList<Token<?>> tokens = new ArrayList<>();
        Token<?> tContainer = new Token<>(null);
        boolean containsNewln = line.contains("\n");
        String[] ls = containsNewln ? line.split("\n")
                : line.split("(?<!\\$)!(?!\\=)");
        String[] lines = Comments.decimate(ls);

        if (lines.length > 1 || ((lines.length == 2 && !lines[1].isEmpty()) && !Comments.arrayIsOnlyComments(lines))) {
            // System.out.println("Multiple lines detected!");
            // multiple lines.
            Find.MultipleLinesOutput m = null;
            BlockChain b = null;
            String comment = null;
            // int ln = lineNumber + 1;
            int ln = lineNumber + 1;
            for (int i = 0; i != ls.length; i++) {
                ln = lineNumber + i;
                String l = "";
                if (b instanceof BlockChain) {
                    l = b.getCurrentLine();
                    i--;
                    ln--;
                } else {
                    l = ls[i] + (!containsNewln ? Character.toString(Lang.END_LINE) : "");
                }
                // System.out.println("Reading line " + i + "...");
                String previousLine2 = i == 0 ? previousLine : ls[i - 1];
                // System.out.println(previousLine2);
                // System.out.println(lines[i]);
                Object something = readLine(l, previousLine2, m, b, ln);
                if (something instanceof Find.MultipleLinesOutput) {
                    m = ((Find.MultipleLinesOutput) something);
                    b = null;
                } else if (something instanceof ArrayList<?>) {
                    m = null;
                    b = null;
                    if (((ArrayList<Token<?>>) something).size() == 1) {
                        for (Token<?> t : (ArrayList<Token<?>>) something) {
                            TokenDefault g = t.getValue();
                            if (comment == null || (!(g instanceof TArrayVar) && !(g instanceof TNumberVar)
                                    && !(g instanceof TStringVar)
                                    && !(g instanceof TBooleanVar) && !(g instanceof TUnknownVar)
                                    && !(g instanceof TFunction)))
                                continue;
                            g.tooltip = comment != null ? comment : g.tooltip;
                            g.json.removeKey("toolTip");
                            g.json.append("toolTip", EscapeSequence.escapeJson(comment).trim(), true);
                        }

                    }
                    comment = null;
                    tokens.addAll((ArrayList<Token<?>>) something);
                } else if (something instanceof BlockChain) {
                    m = null;
                    comment = null;
                    b = (BlockChain) something;
                } else if (something instanceof Token<?> && ((Token<?>) something).getValue() instanceof TDocsComment) {
                    b = null;
                    m = null;
                    comment = (comment == null ? "" : comment)
                            + ((TDocsComment) ((Token<?>) something).getValue()).comment;
                } else if (something instanceof Token<?>) {
                    TokenDefault t = ((TokenDefault) ((Token<?>) something).getValue());
                    t.tooltip = comment != null ? comment : t.tooltip;

                    if (comment != null
                            && ((t instanceof TArrayVar) || (t instanceof TNumberVar) || (t instanceof TStringVar)
                                    || (t instanceof TBooleanVar) || (t instanceof TUnknownVar)
                                    || (t instanceof TFunction))) {

                        t.tooltip = comment != null ? comment : t.tooltip;
                        t.json.removeKey("toolTip");
                        t.json.append("toolTip", EscapeSequence.escapeJson(comment).trim(), true);
                    }
                    tokens.add((Token<?>) something);
                    m = null;
                    b = null;
                    comment = null;
                } else {
                    m = null;
                    b = null;
                    comment = null;
                }
                // if (b == null)
                // ln++;
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
        if (cont || isComment || isCodeBlock) {
            Object k = type == null
                    ? processBlockLines(isComment, line, (Find.MultipleLinesOutput) multipleLinesOutput,
                            tokenizerLine,
                            tokens, tContainer, type,
                            new String[] { "" }, null, lineNumber)
                    : processBlockLines(
                            isComment, line, (Find.MultipleLinesOutput) multipleLinesOutput, tokenizerLine,
                            tokens, tContainer, type,
                            multipleLinesOutput == null ? handleArgs(type, line) : null,
                            (blockChain != null ? blockChain.getInitialIf() : null),
                            lineNumber);
            return k;
        }

        // if its anything after this the line has to end in a ! or else invalid syntax.
        // (All other methods which did the ! will be redundant as we can just check
        // here.)
        Object tryDecimate = Comments.safeDecimate(line);

        if (tryDecimate instanceof Token<?>) {
            return tryDecimate;
        } else {
            line = ((String) tryDecimate).trim();
        }

        if (line.isEmpty())
            return null;

        if (!line.isEmpty() && !line.equals(Lang.BLOCK_CLOSE) && !line.endsWith(Lang.BLOCK_OPEN)
                && (!line.endsWith(Character.toString(Lang.END_LINE)))) {
            throw new SyntaxCriticalError("Ye wena. On line " + lineNumber + " you don't shout your code. Sies.");
        }

        line = line.isEmpty() ? line : line.substring(0, line.length() - 1);

        if (line.startsWith(Keywords.IMPORT.get(0)) || line.startsWith(Keywords.IMPORT.get(1))) {
            return handleImport(line, tContainer, lineNumber);
        }

        // #STRING# is (khutla 100!) syntax which is a function return.

        if (line.startsWith(Keywords.THROW)) {
            // Remove keyword 'cima' and operator '<=='
            line = line.trim();
            String withoutKeyword = line.substring(Keywords.THROW.length()).trim();
            // Split on the operator "<=="
            String[] parts = withoutKeyword.split(Lang.THROW_ERROR);
            if (parts.length != 2) {
                throw new SyntaxCriticalError(
                        "Ehh baba you must use the right syntax if u wanna cima this process. (line " + lineNumber
                                + ")");
            }
            String errorMessage = parts[1].trim();
            if (errorMessage.startsWith("\"") && errorMessage.endsWith("\"") && errorMessage.length() >= 2) {
                errorMessage = errorMessage.substring(1, errorMessage.length() - 1);
                // errorMessage = EscapeSequence.escape(errorMessage.substring(1,
                // errorMessage.length() - 1));
            }
            tokens.add(tContainer.new TThrowError(errorMessage, lineNumber).toToken());
            return tokens;
        }

        line = line.trim();

        if (line.startsWith(Keywords.LC_BREAK) || line.startsWith(Keywords.LC_CONTINUE)) {
            switch (line) {
                case "voetsek", "nevermind":
                    return tContainer.new TLoopControl(line, lineNumber).toToken();
                default:
                    throw new SyntaxCriticalError(
                            "Loop control keywords should be by themselves big bro. Remove whatever is on line "
                                    + lineNumber);
            }
        }

        if (line.startsWith(Keywords.RETURN)) {
            tokens.add(
                    tContainer.new TFuncReturn(tContainer.processContext(line.replace(Keywords.RETURN, ""),
                            lineNumber),
                            lineNumber)
                            .toToken());
            return tokens;
        }

        // #STRING# is a line which contains something that needs to be tokenized.

        if (line.startsWith(Keywords.D_VAR)) {
            tokens.add(processVariable(line, tContainer, lineNumber));
            return tokens;
        }

        // if it doesnt start with declaration nfor var, it might be a reassingment
        String[] parts = line.split(Lang.ASSIGNMENT);
        if (parts.length > 1) {
            String varName = parts[0].trim();
            Object varValue = tContainer.processContext(parts[1].trim(), lineNumber);
            if (varName.contains("]") || varName.contains("[")) {
                // This is an array reassignment.
                tokens.add(tContainer.new TVarReassign(tContainer.processContext(varName,
                        lineNumber), varValue, lineNumber).toToken());
                return tokens;
            } else {
                tokens.add(
                        tContainer.new TVarReassign(parts[0].trim(), tContainer.processContext(parts[1].trim(),
                                lineNumber),
                                lineNumber)
                                .toToken());
            }
            return tokens;
        }

        // TODO: This token only exists for debugging purposes at the moment.
        if (line.equals(Keywords.UNDEFINED)) {
            tokens.add(tContainer.new TVoidValue(lineNumber).toToken());
            return tokens;
        }

        // if we've reached here, then it doesnt have a keyword prolly. meaning
        // TFuncCall
        // (You cant just place a variable out of nowhere with no context around it.)
        // (Therefore i wont parse it.)
        Object token = tContainer.processContext(line, lineNumber);
        if (token instanceof Token<?>) {
            tokens.add((Token<?>) token);
            return tokens;
        }

        // Last resort: Don't fucking know what this line is supposed to tokenize as
        // Therefore:
        return void.class;
    }
}
