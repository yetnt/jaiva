package com.yetnt.tokenizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

class TokenDefault {
    public String name = "";

    public String getContents(int depth) {
        return "";
    }

    TokenDefault(String name) {
        this.name = name;
    }
}

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
     * Represents a statement such as {@code 10 + 1} or {@code true && false}
     * This class usually isn't used directly, but rather as a part of another
     * instance.
     */
    public class TStatement extends TokenDefault {
        public Object lHandSide = "null";
        public String op;
        public Object rHandSide = "null";
        /**
         * 0 = boolean logic |
         * 1 = int arithmetic
         */
        public int statementType;

        TStatement(String statement) {
            super("TStatement");
            parse(statement);
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

        /**
         * Parses a given string. This assumes you've already used braces to
         * indicate the correct order of operations.
         *
         * @param statement The statement to parse.
         */
        private void parse(String statement) {
            // System.out.println("Parsing: " + statement);
            statement = statement.trim();

            if (statement.matches("true|false")) {
                lHandSide = Boolean.parseBoolean(statement);
                statementType = 0;
                return;
            } else if (statement.matches("aowa|yebo")) {
                lHandSide = statement.matches("aowa") ? false : true;
                statementType = 0;
                return;
            } else if (statement.matches("\\d+")) {
                lHandSide = Integer.parseInt(statement);
                statementType = 1;
                return;
            } else {
                // probs a variable.
                lHandSide = statement;
            }

            if (statement.startsWith("(") && statement.endsWith(")")) {
                parse(statement.substring(1, statement.length() - 1).trim());
                return;
            }

            for (String op : Lang.BOOLEAN_OPERATORS) {
                int index = findOperatorIndex(statement, op);
                if (index == -1)
                    continue;

                lHandSide = new TStatement(statement.substring(0, index).trim());
                this.op = op.trim();
                rHandSide = new TStatement(statement.substring(index + op.length()).trim());
                statementType = 0;
                return;
            }

            for (String op : Lang.ARITHMATIC_OPERATIONS) {
                int index = findOperatorIndex(statement, op);
                if (index == -1)
                    continue;
                lHandSide = new TStatement(statement.substring(0, index).trim());
                this.op = op.trim();
                System.out.println(statement.substring(index + op.length()));
                rHandSide = new TStatement(statement.substring(index + op.length()).trim());
                statementType = 1;
                return;
            }
        }

        private int findOperatorIndex(String statement, String operator) {
            int level = 0;
            for (int i = 0; i < statement.length(); i++) {
                char c = statement.charAt(i);
                if (c == '(')
                    level++;
                else if (c == ')')
                    level--;
                else if (level == 0 && statement.startsWith(operator, i)) {
                    return i;
                }
            }
            return -1;
        }

        public Token<TStatement> toToken() {
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

        // Return a Token<TCodeblockVar> on initialization
        public Token<TCodeblock> toToken() {
            return new Token<>(this);
        }
    }

    /**
     * Represents a string variable such as {@code maak name <- "John"};
     */
    public class TStringVar extends TokenDefault {

        public String value;

        TStringVar(String name, String value) {
            super(name);
            this.value = value;
        }

        @Override
        public String getContents(int depth) {
            return name + " <- " + value;
        }

        public Token<TStringVar> toToken() {
            return new Token<>(this);
        }
    }

    /**
     * Represents a integer variable such as {@code maak age <- 20};
     */
    public class TIntVar extends TokenDefault {
        public Object value;

        TIntVar(String name, int value) {
            super(name);
            this.value = value;
        }

        TIntVar(String name, TStatement value) {
            super(name);
            this.value = value;
        }

        @Override
        public String getContents(int depth) {
            return name + " <- " + (value instanceof Integer ? Integer.toString((Integer) value)
                    : ((TStatement) value).getContents(0));
        }

        public Token<TIntVar> toToken() {
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

        TBooleanVar(String name, TStatement value) {
            super(name);
            this.value = value;
        }

        @Override
        public String getContents(int depth) {
            return name + " <- " + (value instanceof Boolean ? Boolean.toString((Boolean) value)
                    : ((TStatement) value).getContents(0));
        }

        public Token<TBooleanVar> toToken() {
            return new Token<>(this);
        }
    }

    public class TArrayVar extends TokenDefault {
        public Object[] contents;

        TArrayVar(String name, Object[] contents) {
            super(name);
            this.contents = contents;
        }

        @Override
        public String getContents(int depth) {
            ArrayList<String> content = new ArrayList<>();
            Arrays.asList(contents).forEach(g -> content.add(g.toString()));
            return name + " <-| " + content;
        }

        // Return a Token<TArrayVar> on initialization
        public Token<TArrayVar> toToken() {
            return new Token<>(this);
        }
    }

    /**
     * Represents a function such as
     * {@code kwenza addition(param1, param2) -> khutla (param1 + param2)! <~}
     * This class is
     */
    public class TFunc extends TokenDefault {
        public String[] args;
        public TCodeblock body;

        TFunc(String name, String[] args, TCodeblock body) {
            super("F~" + name);
            this.args = args;
            this.body = body;
        }

        @Override
        public String getContents(int depth) {
            return name + " " + Arrays.toString(args) + " " + body.getContents(depth + 1);
        }

        // Return a Token<TFunc> on initialization
        public Token<TFunc> toToken() {
            return new Token<>(this);
        }
    }

    public class TForLoop extends TokenDefault {
        public TIntVar variable;
        public TStatement condition;
        public String increment;
        public TCodeblock body;

        TForLoop(TIntVar variable, TStatement condition, String increment, TCodeblock body) {
            super("TForLoop");
            this.variable = variable;
            this.condition = condition;
            this.increment = increment;
            this.body = body;
        }

        @Override
        public String getContents(int depth) {
            return " [" + variable.getContents(0) + " " + condition.getContents(0) + " " + increment + "] "
                    + body.getContents(depth + 1);
        }

        public Token<TForLoop> toToken() {
            return new Token<>(this);
        }
    }

    public class TWhileLoop extends TokenDefault {
        public TStatement condition;
        public TCodeblock body;

        TWhileLoop(TStatement condition, TCodeblock body) {
            super("TWhileLoop");
            this.condition = condition;
            this.body = body;
        }

        @Override
        public String getContents(int depth) {
            return " [" + condition.getContents(0) + "] " + body.getContents(depth + 1);
        }

        public Token<TWhileLoop> toToken() {
            return new Token<>(this);
        }
    }

    public class TIfStatement extends TokenDefault {
        public TStatement condition;
        public TCodeblock body;
        public TCodeblock elseBody = null;
        public ArrayList<TIfStatement> elseIfs = new ArrayList<>();

        TIfStatement(TStatement condition, TCodeblock body) {
            super("TIfStatement");
            this.condition = condition;
            this.body = body;
        }

        // TODO: Add support for else if statements
        public void appendElseIf(TStatement condition, TIfStatement body) {
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
            sb.append("[" + condition.getContents(0) + "] " + body.getContents(depth + 1));
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

        public Token<TTryCatchStatement> toToken() {
            return new Token<>(this);
        }
    }
}