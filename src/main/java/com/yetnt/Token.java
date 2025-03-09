package com.yetnt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

class TokenDefault {
    public String name = "";

    public

    TokenDefault(String name) {
        this.name = name;
    }
}

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
        public Object lHandSide;
        public String op;
        public Object rHandSide;
        /**
         * 0 = boolean logic |
         * 1 = int arithmetic
         */
        public int statementType;
        private static final Set<String> BOOLEAN_OPERATORS = Set.of("&&", "||");
        private static final Set<String> ARITHMETIC_OPERATORS = Set.of("+", "-", "*", "/", "=", "<", ">", "!");

        TStatement(String statement) {
            super("TStatement");
            parse(statement);
        }

        /**
         * Parses a given string. This assumes you've already used braces to
         * indicate the correct order of operations.
         *
         * @param statement The statement to parse.
         */
        private void parse(String statement) {
            statement = statement.trim();

            if (statement.matches("true|false")) {
                lHandSide = Boolean.parseBoolean(statement);
                statementType = 0;
                return;
            } else if (statement.matches("\\d+")) {
                lHandSide = Integer.parseInt(statement);
                statementType = 1;
                return;
            }

            if (statement.startsWith("(") && statement.endsWith(")")) {
                parse(statement.substring(1, statement.length() - 1).trim());
                return;
            }

            for (String op : BOOLEAN_OPERATORS) {
                int index = findOperatorIndex(statement, op);
                if (index != -1) {
                    lHandSide = new TStatement(statement.substring(0, index).trim());
                    this.op = op;
                    rHandSide = new TStatement(statement.substring(index + op.length()).trim());
                    statementType = 0;
                    return;
                }
            }

            for (String op : ARITHMETIC_OPERATORS) {
                int index = findOperatorIndex(statement, op);
                if (index != -1) {
                    lHandSide = new TStatement(statement.substring(0, index).trim());
                    this.op = op;
                    rHandSide = new TStatement(statement.substring(index + op.length()).trim());
                    statementType = 1;
                    return;
                }
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
            super("TCodeBlock");
            this.lines = lines;
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

        public Token<TStringVar> toToken() {
            return new Token<>(this);
        }
    }

    /**
     * Represents a integer variable such as {@code maak age <- 20};
     */
    public class TIntVar extends TokenDefault {
        public int value;

        TIntVar(String name, int value) {
            super(name);
            this.value = value;
        }

        public Token<TIntVar> toToken() {
            return new Token<>(this);
        }
    }

    /**
     * Represents a boolean variable such as {@code maak isTrue <- true};
     */
    public class TBooleanVar extends TokenDefault {
        public boolean value;

        TBooleanVar(String name, boolean value) {
            super(name);
            this.value = value;
        }

        public Token<TBooleanVar> toToken() {
            return new Token<>(this);
        }
    }

    /**
     * Represents a function such as
     * {@code kwenza addition(param1, param2) -> khutla (param1 + param2)! <~}
     * This class is
     */
    public class TFunc extends TokenDefault {
        public String name;
        public ArrayList<String> args;
        public TCodeblock body;

        TFunc(String name, ArrayList<String> args, TCodeblock body) {
            super(name);
            this.args = args;
            this.body = body;
        }

        // Return a Token<TFunc> on initialization
        public Token<TFunc> toToken() {
            return new Token<>(this);
        }
    }
}