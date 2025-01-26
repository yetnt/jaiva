package com.yetnt;

import java.util.ArrayList;

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
        return "Token {" +
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
        public String statement;

        TStatement(String statement) {
            super("TStatement");
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