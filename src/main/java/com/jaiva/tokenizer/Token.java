package com.jaiva.tokenizer;

import java.util.*;

import com.jaiva.errors.TokErrs.*;
import com.jaiva.tokenizer.Keywords.LoopControl;
import com.jaiva.utils.ContextDispatcher;
import com.jaiva.utils.Find;
import com.jaiva.utils.Validate;
import com.jaiva.utils.ContextDispatcher.To;
import com.jaiva.utils.Find.TStatementOpIndex;

/**
 * The Token class represents a generic token that holds a value of type T.
 * T must extend the TokenDefault class.
 *
 * @param <T> the type of the value held by this token
 */
public class Token<T extends TokenDefault> {

    private final T value;

    public Token(T value) {
        this.value = value;
    }

    public T getValue() {
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
     * * Represents a default token that can be used as a base class for other. it
     * does nothing.
     */
    public class TVoidValue extends TokenDefault {
        public TVoidValue(int ln) {
            super("TVoidValue", ln);
        }

        public Token<TVoidValue> toToken() {
            return new Token<>(this);
        }
    }

    /**
     * Represents a code block such as {@code -> maak x <- 10; maak y <- 20! <~}
     * This class usually isn't used directly, but rather as a part of another
     * instance
     */
    public class TCodeblock extends TokenDefault {
        public ArrayList<Token<?>> lines;
        public int lineNumberEnd;

        TCodeblock(ArrayList<Token<?>> lines, int ln, int endLn) {
            super("TCodeblock", ln);
            this.lines = lines;
            this.lineNumberEnd = endLn;
        }

        @Override
        public ToJson toJsonInNest() {
            json.append("lineEnd", lineNumberEnd, false);
            json.append("lines", lines, true);
            return super.toJsonInNest();
        }

        // Return a Token<TCodeblockVar> on initialization
        public Token<TCodeblock> toToken() {
            return new Token<>(this);
        }
    }

    /**
     * Represents a variable where it's type can only be resolved by the interpeter
     * such as {@code maak name <- functionCall()!}; or if they made a variable but
     * didnt declare the value.
     */
    public class TUnknownVar extends TokenDefault {
        public Object value;

        public TUnknownVar(String name, Object value, int ln) {
            super(name, ln);
            this.value = value;
        }

        @Override
        public String toJson() {
            json.append("value", value, true);
            return super.toJson();
        }

        public Token<TUnknownVar> toToken() {
            return new Token<>(this);
        }
    }

    public class TVarReassign extends TokenDefault {
        public Object newValue;

        TVarReassign(String name, Object value, int ln) {
            super(name, ln);
            this.newValue = value;
        }

        TVarReassign(Object name, Object value, int ln) {
            super("TVarReassign", ln);
            this.newValue = value;
        }

        @Override
        public String toJson() {
            json.append("value", newValue, true);
            return super.toJson();
        }

        public Token<TVarReassign> toToken() {
            return new Token<>(this);
        }
    }

    /**
     * Represents a string variable such as {@code maak name <- "John"};
     */
    public class TStringVar extends TokenDefault {
        public String value;

        public TStringVar(String name, String value, int ln) {
            super(name, ln);
            this.value = value;
        }

        public TStringVar(String name, String value, int ln, String customToolTip) {
            super(name, ln, customToolTip);
            this.value = value;
        }

        @Override
        public String toJson() {
            json.append("value", value instanceof String ? EscapeSequence.escape((String) value) : value, true);
            return super.toJson();
        }

        public Token<TStringVar> toToken() {
            return new Token<>(this);
        }
    }

    /**
     * Represents a number variable such as {@code maak age <- 20};
     */
    public class TNumberVar extends TokenDefault {
        public Object value;

        TNumberVar(String name, int value, int ln) {
            super(name, ln);
            this.value = value;
        }

        TNumberVar(String name, double value, int ln) {
            super(name, ln);
            this.value = value;
        }

        TNumberVar(String name, Object value, int ln) {
            super(name, ln);
            this.value = value;
        }

        @Override
        public String toJson() {
            json.append("value", value, true);
            return super.toJson();
        }

        public Token<TNumberVar> toToken() {
            return new Token<>(this);
        }
    }

    /**
     * Represents a boolean variable such as {@code maak isTrue <- true};
     */
    public class TBooleanVar extends TokenDefault {
        public Object value;

        TBooleanVar(String name, boolean value, int ln) {
            super(name, ln);
            this.value = value;
        }

        TBooleanVar(String name, Object value, int ln) {
            super(name, ln);
            this.value = value;
        }

        @Override
        public String toJson() {
            json.append("value", value, true);
            return super.toJson();
        }

        public Token<TBooleanVar> toToken() {
            return new Token<>(this);
        }
    }

    public class TArrayVar extends TokenDefault {
        public ArrayList<Object> contents;

        public TArrayVar(String name, ArrayList<Object> contents, int ln) {
            super(name, ln);
            this.contents = contents;
        }

        public TArrayVar(String name, ArrayList<Object> contents, int ln, String customTooltip) {
            super(name, ln, customTooltip);
            this.contents = contents;
        }

        @Override
        public String toJson() {
            json.append("value", contents, true);
            return super.toJson();
        }

        // Return a Token<TArrayVar> on initialization
        public Token<TArrayVar> toToken() {
            return new Token<>(this);
        }
    }

    public class TFuncReturn extends TokenDefault {
        public Object value;

        TFuncReturn(Object value, int ln) {
            super("TFuncReturn", ln);
            this.value = value;
        }

        @Override
        public String toJson() {
            json.append("value", value, true);
            return super.toJson();
        }

        // Return a Token<TFuncReturn> on initialization
        public Token<TFuncReturn> toToken() {
            return new Token<>(this);
        }
    }

    /**
     * Represents a function such as
     * {@code kwenza addition(param1, param2) -> khutla (param1 + param2)! <~}
     * This class is
     */
    public class TFunction extends TokenDefault {
        public String[] args;
        public TCodeblock body;

        public TFunction(String name, String[] args, TCodeblock body, int ln) {
            super("F~" + name, ln);
            this.args = args;
            this.body = body;
        }

        public TFunction(String name, String[] args, TCodeblock body, int ln, String customToolTip) {
            super("F~" + name, ln, customToolTip);
            this.args = args;
            this.body = body;
        }

        @Override
        public String toJson() {
            json.append("args", new ArrayList(Arrays.asList(args)), false);
            json.append("body", body != null ? body.toJsonInNest() : null, true);
            return super.toJson();
        }

        // Return a Token<TFunction> on initialization
        public Token<TFunction> toToken() {
            return new Token<>(this);
        }
    }

    public class TForLoop extends TokenDefault {
        public TokenDefault variable;
        public TVarRef array;
        public Object condition;
        public String increment;
        public TCodeblock body;

        TForLoop(TokenDefault variable, Object condition, String increment, TCodeblock body, int ln) {
            super("TForLoop", ln);
            this.variable = variable;
            this.condition = condition;
            this.increment = increment;
            this.body = body;
        }

        TForLoop(TokenDefault variable, TVarRef array, TCodeblock body, int ln) {
            super("TForLoop", ln);
            this.variable = variable;
            this.array = array;
            this.body = body;
        }

        @Override
        public String toJson() {
            json.append("variable", variable.toJson(), false);
            json.append("arrayVariable", array != null ? array.toJson() : null, false);
            json.append("condition", condition, false);
            json.append("increment", increment, false);
            json.append("body", body.toJsonInNest(), true);
            return super.toJson();
        }

        public Token<TForLoop> toToken() {
            return new Token<>(this);
        }
    }

    public class TWhileLoop extends TokenDefault {
        public Object condition;
        public TCodeblock body;

        TWhileLoop(Object condition, TCodeblock body, int ln) {
            super("TWhileLoop", ln);
            this.condition = condition;
            this.body = body;
        }

        @Override
        public String toJson() {
            json.append("condition", condition, false);
            json.append("body", body.toJsonInNest(), true);
            return super.toJson();
        }

        public Token<TWhileLoop> toToken() {
            return new Token<>(this);
        }
    }

    public class TIfStatement extends TokenDefault {
        public Object condition;
        public TCodeblock body;
        public TCodeblock elseBody = null;
        public ArrayList<TIfStatement> elseIfs = new ArrayList<>();

        TIfStatement(Object condition, TCodeblock body, int ln) {
            super("TIfStatement", ln);
            this.condition = condition;
            this.body = body;
        }

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
        public String toJson() {
            json.append("condition", condition, false);
            json.append("body", body.toJsonInNest(), false);
            json.append("elseIfs", elseIfs != null && elseIfs.size() > 0 ? elseIfs : null, false);
            json.append("elseBody", elseBody != null ? elseBody.toJsonInNest() : null, false);
            return super.toJson();
        }

        public Token<TIfStatement> toToken() {
            return new Token<>(this);
        }
    }

    public class TTryCatchStatement extends TokenDefault {
        public TCodeblock tryBlock;
        public TCodeblock catchBlock;

        TTryCatchStatement(TCodeblock tryBlock, int ln) {
            super("TTryCatchStatement", ln);
            this.tryBlock = tryBlock;
        }

        public void appendCatchBlock(TCodeblock catchBlock) {
            this.catchBlock = catchBlock;
        }

        @Override
        public String toJson() {
            json.append("try", tryBlock.toJsonInNest(), false);
            json.append("catch", catchBlock.toJsonInNest(), true);
            return super.toJson();
        }

        public Token<TTryCatchStatement> toToken() {
            return new Token<>(this);
        }
    }

    public class TThrowError extends TokenDefault {
        public String errorMessage;

        TThrowError(String errorMessage, int ln) {
            super("TThrowError", ln);
            this.errorMessage = errorMessage;
        }

        @Override
        public String toJson() {
            json.append("errorMessage", errorMessage, false);
            return super.toJson();
        }

        public Token<TThrowError> toToken() {
            return new Token<>(this);
        }
    }

    public class TFuncCall extends TokenDefault {
        public Object functionName;
        public ArrayList<Object> args; // can be a TStatement, TFuncCall, TVarRef, or a primitive type
        public boolean getLength = false;

        TFuncCall(Object name, ArrayList<Object> args, int ln) {
            super("TFuncCall", ln);
            this.functionName = name;
            this.args = args;
        }

        TFuncCall(Object name, ArrayList<Object> args, int ln, boolean getL) {
            super("TFuncCall", ln);
            this.functionName = name;
            this.args = args;
            this.getLength = getL;
        }

        @Override
        public String toJson() {
            json.append("functionName", functionName, false);
            json.append("getLength", getLength, false);
            json.append("args", args, true);
            return super.toJson();
        }

        public Token<TFuncCall> toToken() {
            return new Token<>(this);
        }
    }

    /**
     * Represents a variable reference such as {@code name} or {@code age} or
     * {@code array[index]}.
     */
    public class TVarRef extends TokenDefault {
        public int type;
        public Object varName;
        public Object index = null;
        public boolean getLength = false;

        TVarRef(Object name, int ln, boolean getLength) {
            super("TVarRef", ln);
            this.varName = name;
            this.getLength = getLength;
        }

        TVarRef(Object name, Object index, int ln, boolean getLength) {
            super("TVarRef", ln);
            this.index = index;
            this.varName = name;
            this.getLength = getLength;
        }

        @Override
        public String toJson() {
            json.append("varName", varName, false);
            json.append("getLength", getLength, false);
            json.append("index", index != null ? index : null, true);
            return super.toJson();
        }

        public Token<TVarRef> toToken() {
            return new Token<>(this);
        }
    }

    public class TLoopControl extends TokenDefault {
        public Keywords.LoopControl type;

        TLoopControl(String type, int ln) throws UnknownSyntaxError {
            super("TLoopControl", ln);
            if (type.equals(Keywords.LC_CONTINUE)) {
                this.type = LoopControl.CONTINUE;
            } else if (type.equals(Keywords.LC_BREAK)) {
                this.type = LoopControl.BREAK;
            } else {
                throw new UnknownSyntaxError("So we're in LoopControl but not correctly?");
            }
        }

        @Override
        public String toJson() {
            json.append("loopType", type.toString(), true);
            return super.toJson();
        }

        public Token<TLoopControl> toToken() {
            return new Token<>(this);
        }
    }

    /**
     * Represents a statement such as {@code 10 + 1} or {@code true && false}
     * This class usually isn't used directly, but rather as a part of another
     * instance.
     */
    public class TStatement extends TokenDefault {
        public Object lHandSide = "null";
        public String op;
        public Object rHandSide = "null";
        public String statement;
        /**
         * 0 = boolean logic |
         * 1 = int arithmetic
         */
        public int statementType;

        TStatement(int ln) {
            super("TStatement", ln);
        }

        @Override
        public String toJson() {
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
         * 
         * NOTE : This method will call ContextDispatcher and if needed may switch to
         * using processContext if its instead function call or variable call.
         *
         * @param statement The statement to parse.
         */
        public Object parse(String statement) {
            statement = statement.trim();

            ContextDispatcher d = new ContextDispatcher(statement);
            if (d.getDeligation() == To.PROCESS_CONTENT) {
                return processContext(statement, lineNumber);
            }
            this.statement = statement;

            // if (statement.startsWith("\"") && statement.endsWith("\""))
            // return processContext(statement, lineNumber);

            if (statement.startsWith("(") && statement.endsWith(")")) {
                return parse(statement.substring(1, statement.length() - 1).trim());
            }

            Find.LeastImportantOperator info = Find.leastImportantOperator(statement);
            if (info.index == -1) {
                // no operator found, so its a single value
                // TODO: Validate this, Copilot gave me this if but i'm not sure ngl.
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
            return ((Token<?>.TStatement) handleNegatives(this)).toToken();
        }

        public Token<TStatement> toToken() {
            return new Token<>(this);
        }
    }

    private Object handleNegatives(Object s) {
        if (s instanceof Token<?>.TStatement) {
            Token<?>.TStatement statement = (Token<?>.TStatement) s;
            if (statement.lHandSide == null && statement.op.equals("-")) {
                statement.lHandSide = -1;
                statement.op = "*";
            }

            return statement;
        }
        return s;
    }

    // Helper function that splits a string by commas that are at nesting level 0.
    public List<String> splitByTopLevelComma(String argsString) {
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

    private String simplifyIdentifier(String identifier, String prefix) {
        return identifier.contains(")") || identifier.contains("(") || identifier.contains("[")
                || identifier.contains("]") || Validate.containsOperator(identifier.toCharArray()) != -1 ? identifier
                        : prefix + identifier;
    }

    /**
     * This method serves 2 purposes, to tokenize a function call and to tokenize a
     * varibale call too.
     * 
     * The TFuncCall and TVarRef are so versatile, that this method is the ONLY
     * method which isnt static in the entire tokenizer, and also the only method
     * under the Token class.
     * 
     * Because both any token and the tokenizer need thius function, its THAT
     * useful.
     * 
     * NOTE : This function will call ContextDispatcher and if needed will switch to
     * use TStatement and parse as an arithmatioc operation/boolean rather than a
     * plain function call / variable reference
     * 
     * @param line
     * @return
     */
    public Object processContext(String line, int lineNumber) {
        line = line.trim(); // gotta trim da line fr

        ContextDispatcher d = new ContextDispatcher(line);
        if (d.getDeligation() == To.TSTATEMENT) {
            return new TStatement(lineNumber).parse(line);
        }
        int index = Find.lastOutermostBracePair(line);

        if (index == 0) {
            // the outmost pair is just () so its prolly a TStatement, remove the stuff then
            // parse as TStatement
            return new TStatement(lineNumber).parse(line.substring(1, line.length() - 1));
        }

        if (index != -1 && (line.charAt(index) == '(')) {
            // then its a TFuncCall
            String name = line.substring(0, index).trim();
            String params = line.substring(index + 1, line.lastIndexOf(")")).trim();

            ArrayList<String> args = new ArrayList<>(splitByTopLevelComma(params));
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
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                if (line.equals("true") || line.equals("false") || line.equals(Keywords.TRUE)
                        || line.equals(Keywords.FALSE)) {
                    line = line.replace(Keywords.TRUE, "true").replace(Keywords.FALSE, "false");
                    return Boolean.parseBoolean(line);
                } else if (line.contains("\"")) {
                    return line.substring(1, line.length() - 1).replace("\"", "");
                } else if (line.isBlank()) {
                    return null;
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

    public Object dispatchContext(String line, int lineNumber) throws SyntaxCriticalError, SyntaxError {
        line = line.trim();
        ContextDispatcher d = new ContextDispatcher(line);
        switch (d.getDeligation()) {
            case TSTATEMENT:
                return new TStatement(lineNumber).parse(line);
            case PROCESS_CONTENT:
                return processContext(line, lineNumber);
            case SINGLE_BRACE, EMPTY_STRING:
                throw new SyntaxError("malformed string.");
            default:
                throw new SyntaxCriticalError("yeah sum went wrong with ur dispatch code");
        }

    }

}