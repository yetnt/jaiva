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
        public TVoidValue() {
            super("TVoidValue");
        }

        @Override
        public String getContents(int depth) {
            return name;
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

        TCodeblock(ArrayList<Token<?>> lines) {
            super("TCodeblock");
            this.lines = lines;
        }

        @Override
        public String getContents(int depth) {
            StringBuilder sb = new StringBuilder();
            for (Token<?> t : lines) {
                sb.append("\n" + "^ ".repeat(depth) + t.getValue().getContents(depth)).append("\n");
            }
            return sb.toString();
        }

        @Override
        public ToJson toJsonInNest() {
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

        TUnknownVar(String name, Object value) {
            super(name);
            this.value = value;
        }

        @Override
        public String getContents(int depth) {
            return name + " <- " + value.toString();
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

        TVarReassign(String name, Object value) {
            super(name);
            this.newValue = value;
        }

        TVarReassign(Object name, Object value) {
            super("TVarReassign");
            this.newValue = value;
        }

        @Override
        public String getContents(int depth) {
            return name + " <<- " + value.toString();
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

        public TStringVar(String name, String value) {
            super(name);
            this.value = value;
        }

        @Override
        public String getContents(int depth) {
            return name + " <- " + value;
        }

        @Override
        public String toJson() {
            json.append("value", value, true);
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

        TNumberVar(String name, int value) {
            super(name);
            this.value = value;
        }

        TNumberVar(String name, double value) {
            super(name);
            this.value = value;
        }

        TNumberVar(String name, Object value) {
            super(name);
            this.value = value;
        }

        @Override
        public String getContents(int depth) {
            return name + " <- " + (value instanceof Integer ? Integer.toString((Integer) value)
                    : ((TStatement) value).getContents(0));
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

        TBooleanVar(String name, boolean value) {
            super(name);
            this.value = value;
        }

        TBooleanVar(String name, Object value) {
            super(name);
            this.value = value;
        }

        @Override
        public String getContents(int depth) {
            return name + " <- " + (value instanceof Boolean ? Boolean.toString((Boolean) value)
                    : ((TStatement) value).getContents(0));
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

        public TArrayVar(String name, ArrayList<Object> contents) {
            super(name);
            this.contents = contents;
        }

        @Override
        public String getContents(int depth) {
            ArrayList<String> content = new ArrayList<>();
            Arrays.asList(contents).forEach(g -> content.add(g.toString()));
            return name + " <-| " + content;
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

        TFuncReturn(Object value) {
            super("TFuncReturn");
            this.value = value;
        }

        @Override
        public String getContents(int depth) {
            return name + "RETURNS-> " + value.toString();
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

        public TFunction(String name, String[] args, TCodeblock body) {
            super("F~" + name);
            this.args = args;
            this.body = body;
        }

        @Override
        public String getContents(int depth) {
            return name + " " + Arrays.toString(args) + " " + body.getContents(depth + 1);
        }

        @Override
        public String toJson() {
            json.append("args", new ArrayList(Arrays.asList(args)), false);
            json.append("body", body.toJsonInNest(), true);
            return super.toJson();
        }

        // Return a Token<TFunction> on initialization
        public Token<TFunction> toToken() {
            return new Token<>(this);
        }
    }

    public class TForLoop extends TokenDefault {
        public TNumberVar variable;
        public Object condition;
        public String increment;
        public TCodeblock body;

        TForLoop(TNumberVar variable, Object condition, String increment, TCodeblock body) {
            super("TForLoop");
            this.variable = variable;
            this.condition = condition;
            this.increment = increment;
            this.body = body;
        }

        @Override
        public String getContents(int depth) {
            return " [" + variable.getContents(0) + " " + ((Token<?>.TStatement) condition).getContents(0) + " "
                    + increment + "] "
                    + body.getContents(depth + 1);
        }

        @Override
        public String toJson() {
            json.append("variable", variable.toJson(), false);
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

        TWhileLoop(Object condition, TCodeblock body) {
            super("TWhileLoop");
            this.condition = condition;
            this.body = body;
        }

        @Override
        public String getContents(int depth) {
            return " [" + ((Token<?>.TStatement) condition).getContents(0) + "] " + body.getContents(depth + 1);
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

        TIfStatement(Object condition, TCodeblock body) {
            super("TIfStatement");
            this.condition = condition;
            this.body = body;
        }

        // TODO: Add support for else if statements
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
        // This was made by Copilot
        // TODO: Test if this actually works
        public String getContents(int depth) {
            StringBuilder sb = new StringBuilder();
            sb.append("[" + ((Token<?>.TStatement) condition).getContents(0) + "] " + body.getContents(depth + 1));
            if (elseIfs != null) {
                for (TIfStatement t : elseIfs) {
                    sb.append("\n" + "^ ".repeat(depth) + t.getContents(depth));
                }
            }
            if (elseBody != null) {
                sb.append("\n" + "^ ".repeat(depth) + "else " + elseBody.getContents(depth + 1));
            }
            return sb.toString();
        }

        @Override
        public String toJson() {
            json.append("condition", condition, false);
            json.append("body", body.toJsonInNest(), false);
            json.append("elseIfs", elseIfs != null && elseIfs.size() > 0 ? elseIfs : "null", false);
            json.append("elseBody", elseBody != null ? elseBody.toJsonInNest() : "null", false);
            return super.toJson();
        }

        public Token<TIfStatement> toToken() {
            return new Token<>(this);
        }
    }

    public class TTryCatchStatement extends TokenDefault {
        public TCodeblock tryBlock;
        public TCodeblock catchBlock;

        TTryCatchStatement(TCodeblock tryBlock) {
            super("TTryCatchStatement");
            this.tryBlock = tryBlock;
        }

        public void appendCatchBlock(TCodeblock catchBlock) {
            this.catchBlock = catchBlock;
        }

        @Override
        public String getContents(int depth) {
            return "try " + tryBlock.getContents(depth + 1) + "\n" + "^ ".repeat(depth) + "catch "
                    + catchBlock.getContents(depth + 1);
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

        TThrowError(String errorMessage) {
            super("TThrowError");
            this.errorMessage = errorMessage;
        }

        @Override
        public String getContents(int depth) {
            return name + ": " + errorMessage;
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

        TFuncCall(Object name, ArrayList<Object> args) {
            super("TFuncCall");
            this.functionName = name;
            this.args = args;
        }

        @Override
        public String getContents(int depth) {
            return name + " " + args.toString();
        }

        @Override
        public String toJson() {
            json.append("functionName", functionName, false);
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

        TVarRef(Object name) {
            super("TVarRef");
            this.varName = name;
        }

        TVarRef(Object name, Object index) {
            super("TVarRef");
            this.index = index;
            this.varName = name;
        }

        @Override
        public String getContents(int depth) {
            return name;
        }

        @Override
        public String toJson() {
            json.append("varName", varName, false);
            json.append("index", index != null ? index : "null", true);
            return super.toJson();
        }

        public Token<TVarRef> toToken() {
            return new Token<>(this);
        }
    }

    public class TLoopControl extends TokenDefault {
        public Keywords.LoopControl type;

        TLoopControl(String type) throws UnknownSyntaxError {
            super("TLoopControl");
            if (type.equals(Keywords.LC_CONTINUE)) {
                this.type = LoopControl.CONTINUE;
            } else if (type.equals(Keywords.LC_BREAK)) {
                this.type = LoopControl.BREAK;
            } else {
                throw new UnknownSyntaxError("So we're in LoopControl but not correctly?");
            }
        }

        @Override
        public String getContents(int depth) {
            return name;
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

        TStatement() {
            super("TStatement");
        }

        @Override
        public String getContents(int depth) {
            return (lHandSide == null || lHandSide == "null" ? "" : lHandSide.toString()) + " " + (op == null ? "" : op)
                    + " " + (rHandSide == null || rHandSide == "null" ? "" : rHandSide.toString());
        }

        @Override
        public String toString() {
            return getContents(0);
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
                return processContext(statement);
            }
            this.statement = statement;

            if (statement.startsWith("(") && statement.endsWith(")")) {
                return parse(statement.substring(1, statement.length() - 1).trim());
            }

            Find.LeastImportantOperator info = Find.leastImportantOperator(statement);
            if (info.index == -1) {
                // no operator found, so its a single value
                // TODO: Validate this, Copilot gave me this if but i'm not sure ngl.
                return processContext(statement);
            }

            lHandSide = handleNegatives(new TStatement().parse(statement.substring(0, info.index).trim()));
            this.op = info.op.trim();
            rHandSide = handleNegatives(
                    new TStatement().parse(statement.substring(info.index + info.op.length()).trim()));
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
            } else if ((c == ',' && level == 0) && (i - 1 > 0 && (argsString.charAt(i - 1) != '$'))) {
                parts.add(argsString.substring(lastSplit, i).trim());
                lastSplit = i + 1;
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
    public Object processContext(String line) {
        line = line.trim(); // gotta trim da line fr

        ContextDispatcher d = new ContextDispatcher(line);
        if (d.getDeligation() == To.TSTATEMENT) {
            return new TStatement().parse(line);
        }
        int index = Find.lastOutermostBracePair(line);

        if (index == 0) {
            // the outmost pair is just () so its prolly a TStatement, remove the stuff then
            // parse as TStatement
            return new TStatement().parse(line.substring(1, line.length() - 1));
        }

        if (index != -1 && (line.charAt(index) == '(')) {
            // then its a TFuncCall
            String name = line.substring(0, index).trim();
            String params = line.substring(index + 1, line.length() - 1).trim();

            ArrayList<String> args = new ArrayList<>(splitByTopLevelComma(params));
            ArrayList<Object> parsedArgs = new ArrayList<>();
            args.forEach(arg -> parsedArgs.add(processContext((String) arg)));
            return new TFuncCall(processContext(simplifyIdentifier(name, "F~")), parsedArgs).toToken();
        } else if (index != -1 && (line.charAt(index) == '[')) {
            // then its a variable call an array one to be specific
            // name[index]
            String name = line.substring(0, index).trim();
            String arrayIndex = line.substring(index + 1, line.length() - 1).trim();
            return new TVarRef(processContext(simplifyIdentifier(name, "V~")), processContext(arrayIndex))
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
                        return new TVarRef(line).toToken();
                    }
                }
            }
        }
    }

    public Object dispatchContext(String line) throws SyntaxCriticalError, SyntaxError {
        line = line.trim();
        ContextDispatcher d = new ContextDispatcher(line);
        switch (d.getDeligation()) {
            case TSTATEMENT:
                return new TStatement().parse(line);
            case PROCESS_CONTENT:
                return processContext(line);
            case SINGLE_BRACE, EMPTY_STRING:
                throw new SyntaxError("malformed string.");
            default:
                throw new SyntaxCriticalError("yeah sum went wrong with ur dispatch code");
        }

    }

}