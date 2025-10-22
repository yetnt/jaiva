package com.jaiva.tokenizer;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jaiva.errors.TokenizerException.*;
import com.jaiva.errors.JaivaException;
import com.jaiva.errors.TokenizerException;
import com.jaiva.lang.Chars;
import com.jaiva.lang.EscapeSequence;
import com.jaiva.lang.Keywords;
import com.jaiva.lang.Keywords.LoopControl;
import com.jaiva.tokenizer.jdoc.JDoc;
import com.jaiva.utils.cd.ContextDispatcher;
import com.jaiva.utils.Find;
import com.jaiva.utils.Validate;
import com.jaiva.utils.cd.ContextDispatcher.To;
import com.jaiva.utils.cd.ReservedCases;

/**
 * The Token class represents a generic token that holds a value of type T.
 * <p>
 * T must extend the {@link TokenDefault} class which is the base class for all
 * tokens.
 * <p>
 * <p>
 * All tokens can be represented as a {@link Token} object, where T is the type
 * of
 * the token, and changed back to it's original type when needed.
 *
 * @param <T>   the type of the value held by this token
 * @param value The value held by this token.
 */
public record Token<T extends TokenDefault>(T value) {

    /**
     * Constructs a new Token with the specified value.
     *
     * @param value the value to be held by this token
     */
    public Token {
    }

    /**
     * Returns the value held by this token.
     *
     * @return the value held by this token
     */
    @Override
    public T value() {
        return value;
    }

    @Override
    public String toString() {
        return "Token { " +
                "name = " + value.name +
                " | value = " + value +
                " }";
    }

    /**
     * Creates a new instance of the {@link TVoidValue} inner class, representing a
     * void value token.
     *
     * @param lineNumber the line number associated with the token (currently
     *                   ignored and set to 0)
     * @return a new {@link TVoidValue} instance
     */
    public static Token.TVoidValue voidValue(int lineNumber) {
        Token<?> container = new Token<>(null);
        return new TVoidValue(lineNumber);
    }

    /**
     * Represents a "null" value such as {@code maak name <- idk!} which is the same
     * as not defining a value at all.
     */
    public static class TVoidValue extends TokenDefault {

        /**
         * Constructor for TVoidValue
         *
         * @param ln The line number.
         */
        public TVoidValue(int ln) {
            super("TVoidValue", ln);
        }

        /**
         * Converts this token to the default {@link Token}
         *
         * @return {@link Token} with a T value of {@link Token.TVoidValue}
         */
        public Token<TVoidValue> toToken() {
            return new Token<>(this);
        }

        @Override
        public String toString() {
            return "idk";
        }
    }

    /**
     * Represents an important statemnent such as:
     * tsea "path"!
     * or
     * {@code tsea "path" <- func1, func2, func3!}
     */
    public static class TImport extends TokenDefault {
        /**
         * Indicates whether this import is a library import.
         * <p>
         * If true, the import is considered a library import.
         */
        public boolean isLib = false;
        /**
         * The import file path.
         */
        public String filePath;
        /**
         * Specified files name without any folders
         */
        public String fileName;
        /**
         * The imported symbols. This arraylist may be empty if no symbols are imported
         * specifically.
         */
        public ArrayList<String> symbols = new ArrayList<>();

        /**
         * Constructor for TImport
         *
         * @param file The file path.
         * @param fileName the file name
         * @param isLib Whther this is part of a library or not
         * @param ln   The line number.
         */
        public TImport(String file, String fileName, boolean isLib, int ln) {
            super("TImport", ln);
            filePath = file;
            this.isLib = isLib;
            this.fileName = fileName;
        }

        /**
         * Constructor for TImport
         *
         * @param file  The file path.
         * @param fileName File name
         * @param isLib Whther its a libaray import
         * @param names The imported symbols.
         * @param ln    The line number.
         */
        public TImport(String file, String fileName, boolean isLib, ArrayList<String> names, int ln) {
            super("TImport", ln);
            filePath = file;
            this.isLib = isLib;
            this.fileName = fileName;
            symbols.addAll(names);
        }

        @Override
        public String toJson() throws JaivaException {
            json.append("filePath", filePath, false);
            json.append("fileName", fileName, false);
            json.append("isLib", isLib, false);
            json.append("symbols", symbols, true);
            return super.toJson();
        }

        /**
         * Converts this token to the default {@link Token}
         *
         * @return {@link Token} with a T value of {@link Token.TImport}
         */
        public Token<TImport> toToken() {
            return new Token<>(this);
        }
    }

    /**
     * This represents a token which it's purpose is only for documentating specific
     * user defined symbols. This token should not be able to be interpreted or be
     * seen in an array or anywhere in a tokens arraylist, as whatever method
     * handling the reading of a list of tokens, Should handle this specific token
     * case before the generic {@link Token}, by setting an outside variable or
     * another
     * way to do such. Then, when we do not receive the TDocsComment and that outside
     * variable is set, we set the "tooltip" property of the new token to that
     * outside variable, therefore adding the documentation.
     * <p>
     * (This ensures if we collect multiple TDocsComment)
     */
    public static class TDocsComment extends TokenDefault {
        /**
         * The documentation comment.
         */
        public String comment;

        /**
         * Constructor for TDocsComment
         *
         * @param c The comment.
         */
        public TDocsComment(String c) {
            super("TDocsComment", -1);
            comment = c + "\n";
        }

        /**
         * Converts this token to the default {@link Token}
         *
         * @return {@link Token} with a T value of {@link Token.TDocsComment}
         */
        public Token<TDocsComment> toToken() {
            return new Token<>(this);
        }
    }

    /**
     * Represents a code block such as {@code -> maak x <- 10; maak y <- 20! <~}
     * This class usually isn't used directly, but rather as a part of another
     * instance
     */
    public static class TCodeblock extends TokenDefault {
        /**
         * The lines of code in the code block.
         */
        public ArrayList<Token<?>> lines;
        /**
         * The line number where the ending delimiter of the code block is found. (<~)
         */
        public int lineNumberEnd;

        /**
         * Constructor for TCodeblock
         *
         * @param lines The lines of code in the code block.
         * @param ln    The line number where the code block starts.
         * @param endLn The line number where the code block ends.
         */
        TCodeblock(ArrayList<Token<?>> lines, int ln, int endLn) {
            super("TCodeblock", ln);
            this.lines = lines;
            this.lineNumberEnd = endLn;
        }

        @Override
        public ToJson toJsonInNest() throws JaivaException {
            json.append("lineEnd", lineNumberEnd, false);
            json.append("lines", lines, true);
            return super.toJsonInNest();
        }

        /**
         * Converts this token to the default {@link Token}
         *
         * @return {@link Token} with a T value of {@link Token.TCodeblock}
         */
        public Token<TCodeblock> toToken() {
            return new Token<>(this);
        }
    }

    /**
     * Represents a ternary expression such as
     * {@code condition => expr1 however expr2}
     */
    public static class TTernary extends TokenDefault {
        /**
         * The condition of the ternary expression.
         */
        public Object condition;
        /**
         * The expression if the condition is true.
         */
        public Object trueExpr;
        /**
         * The expression if the condition is false.
         */
        public Object falseExpr;

        /**
         * Constructor for TTernary
         *
         * @param condition The condition of the ternary expression.
         * @param trueExpr  The expression if the condition is true.
         * @param falseExpr The expression if the condition is false.
         * @param ln        The line number.
         */
        public TTernary(Object condition, Object trueExpr, Object falseExpr, int ln) {
            super("TTernary", ln);
            this.condition = condition;
            this.trueExpr = trueExpr;
            this.falseExpr = falseExpr;
        }

        @Override
        public String toJson() throws JaivaException {
            json.append("condition", condition, false);
            json.append("trueExpr", trueExpr, false);
            json.append("falseExpr", falseExpr, true);
            return super.toJson();
        }

        /**
         * Converts this token to the default {@link Token}
         *
         * @return {@link Token} with a T value of {@link Token.TTernary}
         */
        public Token<TTernary> toToken() {
            return new Token<>(this);
        }
    }

    public static class TVarReassign extends TokenDefault {
        /**
         * The new value of the variable.
         */
        public Object newValue;

        /**
         * Constructor for TVarReassign
         *
         * @param name  The name of the variable.
         * @param value The new value of the variable.
         * @param ln    The line number.
         */
        TVarReassign(String name, Object value, int ln) {
            super(name, ln);
            this.newValue = value;
        }

        /**
         * Constructor for TVarReassign
         *
         * @param name  The name of the variable.
         * @param value The new value of the variable.
         * @param ln    The line number.
         */
        TVarReassign(Object name, Object value, int ln) {
            super("TVarReassign", ln);
            this.newValue = value;
        }

        @Override
        public String toJson() throws JaivaException {
            json.append("value", newValue, true);
            return super.toJson();
        }

        /**
         * Converts this token to the default {@link Token}
         *
         * @return {@link Token} with a T value of {@link Token.TVarReassign}
         */
        public Token<TVarReassign> toToken() {
            return new Token<>(this);
        }
    }

    /**
     * Represents a variable where it's type can only be resolved by the interpeter
     * such as {@code maak name <- functionCall()!}; or if they made a variable but
     * didnt declare the value.
     *
     * @param <Type> The type of the variable.
     */
    public static class TUnknownVar<Type> extends TokenDefault {
        /**
         * The value of the variable.
         */
        public Type value;

        /**
         * Constructor for basic.
         *
         * @param name  The name of the variable.
         * @param value The value of the variable.
         * @param ln    The line number.
         */
        public TUnknownVar(String name, Type value, int ln) {
            super(name.startsWith(Character.toString(Chars.EXPORT_SYMBOL)),
                    (name.startsWith(Character.toString(Chars.EXPORT_SYMBOL))
                            ? name.replaceFirst(Pattern.quote(Character.toString(Chars.EXPORT_SYMBOL)),
                            Matcher.quoteReplacement(""))
                            : name),
                    ln);
            this.value = value;
        }

        /**
         * Constructor for exporting globals.
         *
         * @param name  The name of the variable.
         * @param value The value of the variable.
         * @param ln    The line number.
         */
        public TUnknownVar(String name, Type value, int ln, JDoc customToolTip) {
            super(name, ln, customToolTip);
            this.value = value;
        }

        @Override
        public String toJson() throws JaivaException {
            if (!json.keyExists("value"))
                json.append("value", value, true);
            return super.toJson();
        }

        /**
         * Converts this token to the default {@link Token}
         *
         * @return {@link Token} with a T value of {@link Token.TUnknownVar}
         */
        public Token<TUnknownVar<?>> toToken() {
            return new Token<>(this);
        }
    }

    /**
     * Represents a string variable such as {@code maak name <- "John"};
     */
    public static class TStringVar extends TUnknownVar<String> {

        /**
         * Constructor for TStringVar
         *
         * @param name  The name of the variable.
         * @param value The value of the variable.
         * @param ln    The line number.
         */
        public TStringVar(String name, String value, int ln) {
            super(name, value, ln);
            this.value = value;
        }

        /**
         * Constructor for TStringVar (for exporting globals.)
         *
         * @param name  The name of the variable.
         * @param value The value of the variable.
         * @param ln    The line number.
         */
        public TStringVar(String name, String value, int ln, JDoc customToolTip) {
            super(name, value, ln, customToolTip);
            this.value = value;
        }

        @Override
        public String toJson() throws JaivaException {
            json.append("value",
                    value != null ? EscapeSequence.fromEscape((String) value, lineNumber) : value,
                    true);
            return super.toJson();
        }

        /**
         * Converts this token to the default {@link Token}
         *
         * @return {@link Token} with a T value of {@link Token.TStringVar}
         */
        @Override
        public Token toToken() {
            return new Token<>(this);
        }
    }

    /**
     * Represents a number variable such as {@code maak age <- 20};
     */
    public static class TNumberVar extends TUnknownVar<Object> {
        /**
         * Constructor for TNumberVar
         *
         * @param name  The name of the variable.
         * @param value The value of the variable.
         * @param ln    The line number.
         */
        TNumberVar(String name, Object value, int ln) {
            super(name, value, ln);
            this.value = value;
        }

        /**
         * Constructor for TNumberVar (used for exporting globals.)
         *
         * @param name  The name of the variable.
         * @param value The value of the variable.
         * @param ln    The line number.
         */
        public TNumberVar(String name, Object value, int ln, JDoc customTooltip) {
            super(name, value, ln, customTooltip);
            this.value = value;
        }

        @Override
        public String toJson() throws JaivaException {
            json.append("value", value, true);
            return super.toJson();
        }

        /**
         * Converts this token to the default {@link Token}
         *
         * @return {@link Token} with a T value of {@link Token.TNumberVar}
         */
        @Override
        public Token toToken() {
            return new Token<>(this);
        }
    }

    /**
     * Represents a boolean variable such as {@code maak isTrue <- true};
     */
    public static class TBooleanVar extends TUnknownVar<Object> {

        /**
         * Constructor for TBooleanVar
         *
         * @param name  The name of the variable.
         * @param value The value of the variable.
         * @param ln    The line number.
         */
        TBooleanVar(String name, boolean value, int ln) {
            super(name, value, ln);
            this.value = value;
        }

        /**
         * Constructor for TBooleanVar
         *
         * @param name  The name of the variable.
         * @param value The value of the variable.
         * @param ln    The line number.
         */
        TBooleanVar(String name, Object value, int ln) {
            super(name, value, ln);
            this.value = value;
        }

        @Override
        public String toJson() throws JaivaException {
            json.append("value", value, true);
            return super.toJson();
        }

        /**
         * Converts this token to the default {@link Token}
         *
         * @return {@link Token} with a T value of {@link Token.TBooleanVar}
         */
        @Override
        public Token toToken() {
            return new Token<>(this);
        }
    }

    /**
     * Represents an array variable such as {@code maak name <-| 1, 2, 3, 4!};
     */
    public static class TArrayVar extends TokenDefault {
        /**
         * The contents of the array variable.
         */
        public ArrayList<Object> contents;

        /**
         * Constructor for TArrayVar
         *
         * @param name     The name of the variable.
         * @param contents The contents of the variable.
         * @param ln       The line number.
         */
        public TArrayVar(String name, ArrayList<Object> contents, int ln) {
            super(name.startsWith(Character.toString(Chars.EXPORT_SYMBOL)),
                    (name.startsWith(Character.toString(Chars.EXPORT_SYMBOL))
                            ? name.replaceFirst(Pattern.quote(Character.toString(Chars.EXPORT_SYMBOL)),
                            Matcher.quoteReplacement(""))
                            : name),
                    ln);
            this.contents = contents;
        }

        /**
         * Constructor for TArrayVar (for exporting globals.)
         *
         * @param name     The name of the variable.
         * @param contents The contents of the variable.
         * @param ln       The line number.
         */
        public TArrayVar(String name, ArrayList<Object> contents, int ln, JDoc customTooltip) {
            super(name, ln, customTooltip);
            this.contents = contents;
        }

        @Override
        public String toJson() throws JaivaException {
            json.append("value", contents, true);
            return super.toJson();
        }

        /**
         * Converts this token to the default {@link Token}
         *
         * @return {@link Token} with a T value of {@link Token.TArrayVar}
         */
        public Token<TArrayVar> toToken() {
            return new Token<>(this);
        }
    }

    /**
     * Represents a function return which can only be defined in a function (Duh)
     * such as {@code khulta 10!}
     */
    public static class TFuncReturn extends TokenDefault {
        /**
         * The value which the function should return.
         */
        public Object value;

        /**
         * Constructor for TFuncReturn
         *
         * @param value The value which the function should return.
         * @param ln    The line number.
         */
        TFuncReturn(Object value, int ln) {
            super("TFuncReturn", ln);
            this.value = value;
        }

        @Override
        public String toJson() throws JaivaException {
            json.append("value", value, true);
            return super.toJson();
        }

        /**
         * Converts this token to the default {@link Token}
         *
         * @return {@link Token} with a T value of {@link Token.TFuncReturn}
         */
        public Token<TFuncReturn> toToken() {
            return new Token<>(this);
        }
    }

    /**
     * Represents a function definition such as
     * {@code kwenza addition(param1, param2) -> khutla (param1 + param2)! <~}
     * This class is
     */
    public static class TFunction extends TokenDefault {
        /**
         * The arguments of the function.
         */
        public String[] args;
        /**
         * The body of the function.
         */
        public TCodeblock body;

        /**
         * An arraylist of booleans that specify which arguments are optional.
         */
        public ArrayList<Boolean> isArgOptional = new ArrayList<>();

        /**
         * Indicates whether the function accepts variable arguments (varargs).
         */
        public boolean varArgs = false;

        /**
         * Constructor for TFunction
         *
         * @param name The name of the function.
         * @param args The arguments of the function.
         * @param body The body of the function.
         * @param ln   The line number.
         */
        public TFunction(String name, String[] args, TCodeblock body, int ln) {
            super(name.startsWith(Character.toString(Chars.EXPORT_SYMBOL)),
                    "F~" + (name.startsWith(Character.toString(Chars.EXPORT_SYMBOL)) ? name
                            .replaceFirst(Pattern.quote(Character.toString(Chars.EXPORT_SYMBOL)),
                                    Matcher.quoteReplacement(""))
                            : name),
                    ln);
            String[] newArgs = new String[args.length];
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (i == 0 && arg.startsWith(Chars.ASSIGNMENT)) {
                    String newArg = arg.substring(2);
                    if (!newArg.isBlank()) {
                        varArgs = true;
                        isArgOptional.add(true);
                        newArgs[i] = newArg;
                        break;
                    }
                }
                isArgOptional.add(arg.endsWith("?"));
                if (arg.endsWith("?"))
                    newArgs[i] = arg.substring(0, arg.length() - 1);
                else newArgs[i] = arg;
            }
            this.args = newArgs;
            this.body = body;
        }

        /**
         * Constructor for TFunction (for exporting globals.)
         *
         * @param name          The name of the function.
         * @param args          The arguments of the function.
         * @param body          The body of the function.
         * @param ln            The line number.
         * @param customToolTip The custom tooltip for the function.
         */
        public TFunction(String name, String[] args, TCodeblock body, int ln, JDoc customToolTip) {
            super("F~" + name, ln, customToolTip);

            String[] newArgs = new String[args.length];
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (i == 0 && arg.startsWith(Chars.ASSIGNMENT)) {
                    String newArg = arg.substring(2);
                    if (!newArg.isBlank()) {
                        varArgs = true;
                        isArgOptional.add(true);
                        newArgs[i] = newArg;
                        break;
                    }
                }
                isArgOptional.add(arg.endsWith("?"));
                if (arg.endsWith("?"))
                    newArgs[i] = arg.substring(0, arg.length() - 1);
            }
            this.args = newArgs;
            this.body = body;
        }

        @Override
        public String toJson() throws JaivaException {
            json.append("args", new ArrayList<>(Arrays.asList(args)), false);
            json.append("isArgOptional", isArgOptional, false);
            json.append("varArgs", varArgs, false);
            json.append("body", body != null ? body.toJsonInNest() : null, true);
            return super.toJson();
        }

        /**
         * Converts this token to the default {@link Token}
         *
         * @return {@link Token} with a T value of {@link Token.TFunction}
         */
        public Token<TFunction> toToken() {
            return new Token<>(this);
        }
    }

    /**
     * Represents a for loop, both with an iterator and a condition
     * {@code colonize i <- 0 | i < 10 | + -> ... <~} and a for each loop which
     * iterates over arrays {@code colonize el with array -> ... <~}
     */
    public static class TForLoop extends TokenDefault {
        /**
         * The variable used in the for loop.
         */
        public TokenDefault variable;
        /**
         * The array variable used in the for loop.
         */
        public TVarRef array;
        /**
         * The condition of the for loop.
         */
        public Object condition;
        /**
         * The increment of the for loop.
         */
        public String increment;
        /**
         * The body of the for loop.
         */
        public TCodeblock body;

        /**
         * Constructor for TForLoop (for iterators)
         *
         * @param variable  The variable used in the for loop.
         * @param condition The condition of the for loop.
         * @param increment The increment of the for loop.
         * @param body      The body of the for loop.
         * @param ln        The line number.
         */
        TForLoop(TokenDefault variable, Object condition, String increment, TCodeblock body, int ln) {
            super("TForLoop", ln);
            this.variable = variable;
            this.condition = condition;
            this.increment = increment;
            this.body = body;
        }

        /**
         * Constructor for TForLoop (for each loop)
         *
         * @param variable The variable used in the for loop.
         * @param array    The array variable used in the for loop.
         * @param body     The body of the for loop.
         * @param ln       The line number.
         */
        TForLoop(TokenDefault variable, TVarRef array, TCodeblock body, int ln) {
            super("TForLoop", ln);
            this.variable = variable;
            this.array = array;
            this.body = body;
        }

        @Override
        public String toJson() throws JaivaException {
            json.append("variable", variable.toJson(), false);
            json.append("arrayVariable", array != null ? array.toJson() : null, false);
            json.append("condition", condition, false);
            json.append("increment", increment, false);
            json.append("body", body.toJsonInNest(), true);
            return super.toJson();
        }

        /**
         * Converts this token to the default {@link Token}
         *
         * @return {@link Token} with a T value of {@link Token.TForLoop}
         */
        public Token<TForLoop> toToken() {
            return new Token<>(this);
        }
    }

    /**
     * Represents a while loop such as {@code nikhil (i > 10) -> ... <~}
     */
    public static class TWhileLoop extends TokenDefault {
        /**
         * The condition of the while loop.
         */
        public Object condition;
        /**
         * The body of the while loop.
         */
        public TCodeblock body;

        /**
         * Constructor for TWhileLoop
         *
         * @param condition The condition of the while loop.
         * @param body      The body of the while loop.
         * @param ln        The line number.
         */
        TWhileLoop(Object condition, TCodeblock body, int ln) {
            super("TWhileLoop", ln);
            this.condition = condition;
            this.body = body;
        }

        @Override
        public String toJson() throws JaivaException {
            json.append("condition", condition, false);
            json.append("body", body.toJsonInNest(), true);
            return super.toJson();
        }

        /**
         * Converts this token to the default {@link Token}
         *
         * @return {@link Token} with a T value of {@link Token.TWhileLoop}
         */
        public Token<TWhileLoop> toToken() {
            return new Token<>(this);
        }
    }

    /**
     * Represents an if statement such as {@code if (i > 10) -> ... <~}, also can
     * have chains of else ifs and a last else if.
     * {@code if (i > 10) -> ... <~ mara if (i > 4) -> ... <~ mara -> ... <~}
     * <p>
     * If this is in an else if of a base if statement, it will not contain an else
     * body nor its own chain of elseIfs. only the original if contaisn all the else
     * ifs and the last else block.
     */
    public static class TIfStatement extends TokenDefault {
        /**
         * The condition of the if statement.
         */
        public Object condition;
        /**
         * The body of the if statement.
         */
        public TCodeblock body;
        /**
         * The else body of the if statement.
         */
        public TCodeblock elseBody = null;
        /**
         * The chain of else if statements.
         */
        public ArrayList<TIfStatement> elseIfs = new ArrayList<>();

        /**
         * Constructor for TIfStatement
         *
         * @param condition The condition of the if statement.
         * @param body      The body of the if statement.
         * @param ln        The line number.
         */
        TIfStatement(Object condition, TCodeblock body, int ln) {
            super("TIfStatement", ln);
            this.condition = condition;
            this.body = body;
        }

        /**
         * Appends the provided else if body to the current token.
         *
         * @param body the TIfStatement representing the else if body to be appended
         */
        public void appendElseIf(TIfStatement body) {
            if (elseIfs == null) {
                elseIfs = new ArrayList<>();
            }
            elseIfs.add(body);
        }

        /**
         * Appends the provided else body to the current token.
         *
         * @param elseBody the TCodeblock representing the else body to be appended
         */
        public void appendElse(TCodeblock elseBody) {
            this.elseBody = elseBody;
        }

        @Override
        public String toJson() throws JaivaException {
            json.append("condition", condition, false);
            json.append("body", body.toJsonInNest(), false);
            json.append("elseIfs", elseIfs != null && !elseIfs.isEmpty() ? elseIfs : null, false);
            json.append("elseBody", elseBody != null ? elseBody.toJsonInNest() : null, false);
            return super.toJson();
        }

        /**
         * Converts this token to the default {@link Token}
         *
         * @return {@link Token} with a T value of {@link Token.TIfStatement}
         */
        public Token<TIfStatement> toToken() {
            return new Token<>(this);
        }
    }

    /**
     * Represents a try-catch statement such as
     * {@code zama zama -> ... <~ chaai -> ... <~}
     */
    public static class TTryCatchStatement extends TokenDefault {
        /**
         * The try block of the try-catch statement.
         */
        public TCodeblock tryBlock;
        /**
         * The catch block of the try-catch statement.
         */
        public TCodeblock catchBlock;

        /**
         * Constructor for TTryCatchStatement
         *
         * @param tryBlock The try block of the try-catch statement.
         * @param ln       The line number.
         */
        TTryCatchStatement(TCodeblock tryBlock, int ln) {
            super("TTryCatchStatement", ln);
            this.tryBlock = tryBlock;
        }

        /**
         * Appends the provided catch block to the current token.
         *
         * @param catchBlock the TCodeblock representing the catch block to be appended
         */
        public void appendCatchBlock(TCodeblock catchBlock) {
            this.catchBlock = catchBlock;
        }

        @Override
        public String toJson() throws JaivaException {
            json.append("try", tryBlock.toJsonInNest(), false);
            json.append("catch", catchBlock.toJsonInNest(), true);
            return super.toJson();
        }

        /**
         * Converts this token to the default {@link Token}
         *
         * @return {@link Token} with a T value of {@link Token.TTryCatchStatement}
         */
        public Token<TTryCatchStatement> toToken() {
            return new Token<>(this);
        }
    }

    /**
     * Represents a throw error statement such as {@code cima "Error message!"}
     */
    public static class TThrowError extends TokenDefault {
        /**
         * The error message to be thrown.
         */
        public Object errorMessage;

        /**
         * Constructor for TThrowError
         *
         * @param errorMessage The error message to be thrown.
         * @param ln           The line number.
         */
        TThrowError(Object errorMessage, int ln) {
            super("TThrowError", ln);
            this.errorMessage = errorMessage;
        }

        @Override
        public String toJson() throws JaivaException {
            json.append("errorMessage", errorMessage, false);
            return super.toJson();
        }

        /**
         * Converts this token to the default {@link Token}
         *
         * @return {@link Token} with a T value of {@link Token.TThrowError}
         */
        public Token<TThrowError> toToken() {
            return new Token<>(this);
        }
    }

    /**
     * Represents a function call such as {@code func1(10, 20)} or {@code func2()}
     * or {@code func3(10, 20) -> ... <~} or {@code func4(10, 20)!}. Any, if not ALL
     * function calls are possible.
     */
    public static class TFuncCall extends TokenDefault {
        /**
         * The name of the function being called.
         * <p>
         * This is an object due to the fact that it might itself be a TVarRef which
         * retrns a function, or another function that returns a function or any other
         * case like that.
         */
        public Object functionName;
        /**
         * The arguments of the function call.
         * <p>
         * This is an arraylist of objects which can be a TStatement, TFuncCall,
         * TVarRef, or a primitive type.
         */
        public ArrayList<Object> args; // can be a TStatement, TFuncCall, TVarRef, or a primitive type
        /**
         * Indicates whether the function call is a length call.
         */
        public boolean getLength = false;

        /**
         * Constructor for TFuncCall
         *
         * @param name The name of the function being called.
         * @param args The arguments of the function call.
         * @param ln   The line number.
         */
        TFuncCall(Object name, ArrayList<Object> args, int ln) {
            super("TFuncCall", ln);
            this.functionName = name;
            this.args = args;
        }

        /**
         * Constructor for TFuncCall
         *
         * @param name The name of the function being called.
         * @param args The arguments of the function call.
         * @param ln   The line number.
         * @param getL Indicates whether the function call is a length call.
         */
        TFuncCall(Object name, ArrayList<Object> args, int ln, boolean getL) {
            super("TFuncCall", ln);
            this.functionName = name;
            this.args = args;
            this.getLength = getL;
        }

        @Override
        public String toJson() throws JaivaException {
            json.append("functionName", functionName, false);
            json.append("getLength", getLength, false);
            json.append("args", args, true);
            return super.toJson();
        }

        /**
         * Converts this token to the default {@link Token}
         *
         * @return {@link Token} with a T value of {@link Token.TFuncCall}
         */
        public Token<TFuncCall> toToken() {
            return new Token<>(this);
        }
    }

    /**
     * Represents a variable reference such as {@code name} or {@code age} or
     * {@code array[index]}.
     */
    public static class TVarRef extends TokenDefault {
        /**
         * The type of the variable reference.
         */
        public int type;
        /**
         * The name of the variable being referenced.
         * <p>
         * This is an object due to the fact that it might itself be a TVarRef which
         * retrns a function, or another function that returns a function or any other
         * case like that.
         */
        public Object varName;
        /**
         * The index of the variable reference.
         * <p>
         * This is an object due to the fact that it might itself be a TVarRef which
         * retrns a function, or another function that returns a function or any other
         * case like that.
         */
        public Object index = null;
        /**
         * Indicates whether the variable reference is a length call.
         */
        public boolean getLength = false;

        /**
         * Constructor for TVarRef
         *
         * @param name The name of the variable being referenced.
         * @param ln   The line number.
         */
        TVarRef(Object name, int ln, boolean getLength) {
            super("TVarRef", ln);
            if (name instanceof String n)
                this.varName = n.endsWith(Chars.LENGTH_CHAR + "") ? n.substring(0, n.length() - 1) : n;
            else
                this.varName = name;
            this.getLength = getLength;
        }

        /**
         * Constructor for TVarRef
         *
         * @param name      The name of the variable being referenced.
         * @param index     The index of the variable reference.
         * @param ln        The line number.
         * @param getLength Indicates whether the variable reference is a length call.
         */
        TVarRef(Object name, Object index, int ln, boolean getLength) {
            super("TVarRef", ln);
            this.index = index;
            if (name instanceof String n)
                this.varName = n.endsWith(Chars.LENGTH_CHAR + "") ? n.substring(0, n.length() - 1) : n;
            else
                this.varName = name;
            this.getLength = getLength;
        }

        @Override
        public String toJson() throws JaivaException {
            json.append("varName", varName, false);
            json.append("getLength", getLength, false);
            json.append("index", index != null ? index : null, true);
            return super.toJson();
        }

        /**
         * Converts this token to the default {@link Token}
         *
         * @return {@link Token} with a T value of {@link Token.TVarRef}
         */
        public Token<TVarRef> toToken() {
            return new Token<>(this);
        }
    }

    /**
     * Represents a loop control statement such as {@code voetsek!} or
     * {@code nevermind!}
     */
    public static class TLoopControl extends TokenDefault {
        /**
         * The type of the loop control statement.
         */
        public LoopControl type;

        /**
         * Constructor for TLoopControl
         *
         * @param type The type of the loop control statement.
         * @param ln   The line number.
         */
        TLoopControl(String type, int ln) throws TokenizerException {
            super("TLoopControl", ln);
            if (type.equals(Keywords.LC_CONTINUE)) {
                this.type = LoopControl.CONTINUE;
            } else if (type.equals(Keywords.LC_BREAK)) {
                this.type = LoopControl.BREAK;
            } else {
                throw new CatchAllException("So we're in LoopControl but not correctly?", ln);
            }
        }

        @Override
        public String toJson() throws JaivaException {
            json.append("loopType", type.toString(), true);
            return super.toJson();
        }

        /**
         * Converts this token to the default {@link Token}
         *
         * @return {@link Token} with a T value of {@link Token.TLoopControl}
         */
        public Token<TLoopControl> toToken() {
            return new Token<>(this);
        }
    }

    /**
     * Represents a statement such as {@code 10 + 1} or {@code true && false}
     * This class usually isn't used directly, but rather as a part of another
     * instance.
     */
    public static class TStatement extends TokenDefault {
        /**
         * The left hand side of the statement.
         * <p>
         * This is an object due to the fact that it might itself be a TVarRef which
         * retrns a function, or another function that returns a function or any other
         * case like that.
         */
        public Object lHandSide = "null";
        /**
         * The operator of the statement.
         */
        public String op;
        /**
         * The right hand side of the statement.
         * <p>
         * This is an object due to the fact that it might itself be a TVarRef which
         * retrns a function, or another function that returns a function or any other
         * case like that.
         */
        public Object rHandSide = "null";
        /**
         * The statement as a string.
         */
        public String statement;
        /**
         * 0 = boolean logic |
         * 1 = int arithmetic
         */
        public int statementType;

        /**
         * Constructor for TStatement
         *
         * @param ln The line number.
         */
        TStatement(int ln) {
            super("TStatement", ln);
        }

        @Override
        public String toJson() throws JaivaException {
            json.append("lhs", lHandSide, false);
            json.append("op", op, false);
            json.append("rhs", rHandSide, false);
            json.append("statementType", statementType, false);
            json.append("statement", statement.replace("\"", "\\\""), true);

            return super.toJson();
        }

        /**
         * Parses a given string. This assumes you've already used braces to
         * indicate the correct order of operations.
         * <p>
         * NOTE : This method will call ContextDispatcher and if needed may switch to
         * using processContext if its instead function call or variable call.
         *
         * @param statement The statement to parse.
         */
        public Object parse(String statement) {
            statement = statement.trim();

            if (statement.isEmpty()) {
                return null;
            }
            ContextDispatcher d = new ContextDispatcher(statement);
            if (d.getDeligation() == To.PROCESS_CONTENT) {
                return processContext(statement, lineNumber);
            }
            this.statement = statement;

            int lastBraceIndex = Find.lastOutermostBracePair(statement);
            if ((statement.startsWith("(") && statement.endsWith(")")) && lastBraceIndex == 0) {
                return parse(statement.substring(1, statement.length() - 1).trim());
            }

            Find.LeastImportantOperator info = Find.leastImportantOperator(statement);
            if (info.index == -1) {
                // no operator found, so its a single value
                return processContext(statement, lineNumber);
            }
            if (info.op.equals("=") && statement.charAt(info.index - 1) == '!') {
                info.index--;
                info.op = "!=";
            }
            if (info.op.equals("=") && statement.charAt(info.index - 1) == '<') {
                info.index--;
                info.op = "<=";
            }
            if (info.op.equals("=") && statement.charAt(info.index - 1) == '>') {
                info.index--;
                info.op = ">=";
            }

            lHandSide = handleNegatives(new TStatement(lineNumber).parse(statement.substring(0, info.index).trim()));
            this.op = info.op.trim();
            rHandSide = handleNegatives(
                    new TStatement(lineNumber).parse(statement.substring(info.index + info.op.length()).trim()));
            statementType = info.tStatementType;
            return ((TStatement) handleNegatives(this)).toToken();
        }

        /**
         * Converts this token to the default {@link Token}
         *
         * @return {@link Token} with a T value of {@link Token.TStatement}
         */
        public Token<TStatement> toToken() {
            return new Token<>(this);
        }
    }

    /**
     * Helper function that handles negatives in a statement. This is used to handle
     * the case where a negative sign is used as a unary operator.
     *
     * @param s The statement to handle.
     * @return The handled statement.
     */
    private static Object handleNegatives(Object s) {
        if (s instanceof TStatement statement) {
            if (statement.lHandSide == null && statement.op.equals("-")) {
                statement.lHandSide = -1;
                statement.op = "*";
            }
            // handled by the interpreter

            return statement;
        }
        return s;
    }

    /**
     * Splits a string by top-level commas, ignoring commas inside parentheses or
     * brackets.
     *
     * @param argsString The string to split.
     * @return A list of strings split by top-level commas.
     */
    public static List<String> splitByTopLevelComma(String argsString) {
        List<String> parts = new ArrayList<>();
        int level = 0;
        int lastSplit = 0;
        for (int i = 0; i < argsString.length(); i++) {
            char c = argsString.charAt(i);
            if (c == '(' /* || c == '[' */) {
                level++;
            } else if (c == ')' /* || c == ']' */) {
                level--;
            } else if ((c == ',' && level == 0)) {
                if (i == 0) {
                    parts.add(argsString.substring(lastSplit, i).trim());
                    lastSplit = i + 1;
                } else if ((argsString.charAt(i - 1) != '$')) {
                    parts.add(argsString.substring(lastSplit, i).trim());
                    lastSplit = i + 1;
                }
            }
        }
        parts.add(argsString.substring(lastSplit).trim());
        return parts;
    }

    /**
     * Simplifies an identifier by adding a prefix if it doesn't contain any
     * parentheses or brackets.
     *
     * @param identifier The identifier to simplify.
     * @param prefix     The prefix to add.
     * @return The simplified identifier.
     */
    private static String simplifyIdentifier(String identifier, String prefix) {
        return identifier.contains(")") || identifier.contains("(") || identifier.contains("[")
                || identifier.contains("]") || Validate.containsOperator(identifier.toCharArray()) != -1 ? identifier
                : prefix + identifier;
    }

    /**
     * ALl this method does is to parse any given input into either:
     * - A function call
     * - A variable reference (can be both a function reference or array access call
     * or just standrard variable)
     * - Ternary operators
     * - Or Primitives
     * <p>
     * Anything handled by this method means it can be placed as a valid construct
     * in any other construct in Jaiva.
     * <p>
     * <p>
     * NOTE : This function will call ContextDispatcher and if needed will switch to
     * use TStatement and parse as an arithmatioc operation/boolean rather than a
     * plain function call / variable reference
     *
     * @param line given line.
     * @return an atomic token
     */
    public static Object processContext(String line, int lineNumber) {
        line = line.trim(); // gotta trim da line fr

        ContextDispatcher d = new ContextDispatcher(line);
        if (d.getDeligation() == To.TSTATEMENT) {
            return new TStatement(lineNumber).parse(line);
        }
        int index = Find.lastOutermostBracePair(line);

        if (index == 0) {
            // the outmost pair is just () so its prolly a TStatement, remove the stuff then
            // parse as TStatement, if it isnt a TStatement,TStatement will route back here
            // anyways.
            return new TStatement(lineNumber).parse(line.substring(1, line.length() - 1));
        }

        if (d.bits == ReservedCases.TERNARY.code()) {
            // 18 corresponds to the Ternary case from ContextDispatcher. See
            // ../utils/ContextDispatcher.md
            // Ternary expression
            // (cond) => true however false
            // String c = line.substring(0, line.indexOf(Chars.TERNARY)).trim();
            String c = line.substring(0, Find.lastIndexOf(line, Chars.TERNARY)).trim();
            String expressions = line.substring(Find.lastIndexOf(line, Chars.TERNARY) + 2).trim();

            Object condition = new TStatement(lineNumber).parse(c.trim());
            String expr1 = expressions.substring(0, Find.lastIndexOf(expressions, Keywords.TERNARY)).trim();
            String expr2 = expressions.substring(Find.lastIndexOf(
                            expressions, Keywords.TERNARY) + Keywords.TERNARY.length())
                    .trim();

            return new TTernary(condition, processContext(expr1, lineNumber),
                    processContext(expr2, lineNumber), lineNumber).toToken();
        } else if (index != -1 && (line.charAt(index) == '(')) {
            // then its a TFuncCall
            String name = line.substring(0, index).trim();
            String params = line.substring(index + 1, line.lastIndexOf(")")).trim();

            ArrayList<String> args = new ArrayList<>(Token.splitByTopLevelComma(params));
            ArrayList<Object> parsedArgs = new ArrayList<>();
            args.forEach(arg -> parsedArgs.add(processContext((String) arg, lineNumber)));
            return new TFuncCall(processContext(simplifyIdentifier(name, "F~"), lineNumber), parsedArgs,
                    lineNumber, line.charAt(line.length() - 1) == '~').toToken();
        } else if (index != -1 && (line.charAt(index) == '[')) {
            // then its a variable call an array one to be specific
            // name[index]
            String name = line.substring(0, index).trim();
            String arrayIndex = line.substring(index + 1, line.lastIndexOf("]")).trim();
            return new TVarRef(processContext(simplifyIdentifier(name, "V~"),
                    lineNumber),
                    processContext(arrayIndex,
                            lineNumber),
                    lineNumber, line.charAt(line.length() - 1) == '~')
                    .toToken();
        } else {
            // here, processTFuncCall will try to parse "line" as a primitive type. (if not
            // a bool, int, or string)
            // if it fails, return TVarRef
            // if it succeeds, return the primitive type
            try {
                return parseIntegerLiteral(line);
            } catch (NumberFormatException e) {
                try {
                    return Double.parseDouble(line);
                } catch (NumberFormatException e2) {
                    if (line.equals("true") || line.equals("false") || line.equals(Keywords.TRUE)
                            || line.equals(Keywords.FALSE)) {
                        line = line.replace(Keywords.TRUE, "true").replace(Keywords.FALSE, "false");
                        return Boolean.parseBoolean(line);
                    } else if (line.contains("\"")) {
                        return line.substring(1, line.length() - 1).replace("\"", "");
                    } else if (line.isBlank()) {
                        return null;
                    } else if (line.equals(Keywords.UNDEFINED)) {
                        return new TVoidValue(lineNumber);
                    } else {
                        if (line.startsWith("V~") || line.startsWith("F~")) {
                            return line.substring(2);
                        } else {
                            return new TVarRef(line, lineNumber, line.charAt(line.length() - 1) == '~').toToken();
                        }
                    }
                }
            }
        }
    }

    /**
     * Dispatches the context based on the line and line number.
     *
     * @param line       The line to dispatch.
     * @param lineNumber The line number of the line.
     * @return The dispatched object.
     * @throws MalformedSyntaxException If there is a syntax
     *                                  critical
     *                                  error.
     * @throws TokenizerException       If there is a syntax
     *                                  error.
     */
    public static Object dispatchContext(String line, int lineNumber)
            throws TokenizerException {
        line = line.trim();
        ContextDispatcher d = new ContextDispatcher(line);
        switch (d.getDeligation()) {
            case TSTATEMENT:
                return new TStatement(lineNumber).parse(line);
            case PROCESS_CONTENT:
                return processContext(line, lineNumber);
            case SINGLE_BRACE, EMPTY_STRING:
                throw new MalformedSyntaxException("Okay so uhm, there's a malformed string somewhere there",
                        lineNumber);
            default:
                throw new CatchAllException("yeah sum went wrong with ur dispatch code", lineNumber);
        }

    }

    /**
     * Parses a string representing an integer literal in decimal, hexadecimal,
     * binary, or octal format.
     * <p>
     * Supported formats:
     * <ul>
     * <li>Decimal: e.g., "42", "-17"</li>
     * <li>Hexadecimal: prefixed with "0x" or "0X", e.g., "0x2A", "-0X1F"</li>
     * <li>Binary: prefixed with "0b" or "0B", e.g., "0b1010", "-0B1101"</li>
     * <li>Octal: prefixed with "0c" or "0C", e.g., "0c52", "-0C17"</li>
     * </ul>
     * Leading and trailing whitespace is ignored. Negative numbers are supported.
     *
     * @param input the string to parse as an integer literal
     * @return the parsed integer value
     * @throws NumberFormatException if the input is not a valid integer literal
     */
    public static int parseIntegerLiteral(String input) throws NumberFormatException {
        input = input.trim();
        if (input.isBlank())
            throw new NumberFormatException();
        boolean negation = input.charAt(0) == '-';
        input = negation ? input.substring(1) : input; // remove the negation
        String prefix = input.length() > 1 ? input.substring(0, 2) : null;
        if (new ArrayList<>(Arrays.asList("0x", "0X", "0b", "0B", "0c", "0C")).contains(prefix)) {
            String literal = (negation ? "-" : "") + input.substring(2);
            switch (prefix) {
                case "0x", "0X":
                    return Integer.parseInt(literal, 16);
                case "0b", "0B":
                    return Integer.parseInt(literal, 2);
                case "0c", "0C":
                    return Integer.parseInt(literal, 8);
            }
        }
        return Integer.parseInt(negation ? "-" + input : input); // allows this method to throw.
    }

}