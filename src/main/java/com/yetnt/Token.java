package com.yetnt;

import java.util.ArrayList;

public class Token<T> {

    private final T value;

    public Token(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Token {" +
                "value = " + value +
                " }";
    }

    /**
     * Represents a statement such as {@code 10 + 1} or {@code true && false}
     * This class usually isn't used directly, but rather as a part of another
     * instance.
     */
    public class TStatement {
        public String statement;

        TStatement(String statement) {
            this.statement = statement;
        }

        // Return a Token<TStatement> on initialization
        public Token<TStatement> toToken() {
            return new Token<>(this);
        }
    }

    /**
     * Represents a code block such as {@code -> maak x <- 10; maak y <- 20! <~}
     * This class usually isn't used directly, but rather as a part of another
     * instance
     */
    public class TCodeblock {
        public ArrayList<Token<?>> lines;

        TCodeblock(ArrayList<Token<?>> lines) {
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
    public class TStringVar {
        public String name;
        public String value;

        TStringVar(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public Token<TStringVar> toToken() {
            return new Token<>(this);
        }
    }

    /**
     * Represents a integer variable such as {@code maak age <- 20};
     */
    public class TIntVar {
        public String name;
        public int value;

        TIntVar(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public Token<TIntVar> toToken() {
            return new Token<>(this);
        }
    }

    /**
     * Represents a boolean variable such as {@code maak isTrue <- true};
     */
    public class TBooleanVar {
        public String name;
        public boolean value;

        TBooleanVar(String name, boolean value) {
            this.name = name;
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
    public class TFunc {
        public String name;
        public ArrayList<String> args;
        public TCodeblock body;

        TFunc(String name, ArrayList<String> args, TCodeblock body) {
            this.name = name;
            this.args = args;
            this.body = body;
        }

        // Return a Token<TFunc> on initialization
        public Token<TFunc> toToken() {
            return new Token<>(this);
        }
    }
}