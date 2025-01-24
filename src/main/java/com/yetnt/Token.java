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

    // Inner class TVar
    class TStringVar {
        public String name;
        public String value;

        TStringVar(String name, String value) {
            this.name = name;
            this.value = value;
        }

        // Return a Token<TVar> on initialization
        public Token<TStringVar> toToken() {
            return new Token<>(this);
        }
    }

    class TIntVar {
        public String name;
        public int value;

        TIntVar(String name, int value) {
            this.name = name;
            this.value = value;
        }

        // Return a Token<TVar> on initialization
        public Token<TIntVar> toToken() {
            return new Token<>(this);
        }
    }

    // Inner class TFunc
    class TFunc {
        public String name;
        public ArrayList<String> args;
        public String body;

        TFunc(String name, ArrayList<String> args, String body) {
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