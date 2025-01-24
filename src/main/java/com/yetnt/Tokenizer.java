package com.yetnt;

import java.util.*;

class Token<T> {

    private final T value;

    public Token(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
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

// // Create an outer Token instance
// Token<?> tokenContainer = new Token<>(null);

// // Create a TVar instance and convert it to a Token<TVar>
// Token<Token<TVar>.TVar> tVarToken = tokenContainer.new TVar("myVariable",
// "42").toToken();

// // Retrieve the inner TVar instance and access its parameters
// Token<TVar>.TVar tVarInstance = tVarToken
// .getValue();System.out.println("TVar Name:
// "+tVarInstance.name);System.out.println("TVar Value: "+tVarInstance.value);

public class Tokenizer {
    public static ArrayList<Token<?>> readLine(String line) throws Exception {
        ArrayList<Token<?>> tokens = new ArrayList<>();
        Token<?> tContainer = new Token<>(null);

        if (line.startsWith("maak")) {
            // maak varuiablename = value!
            line = line.trim();
            line = line.substring(0, 4);
            // varuiablename = value!

            String[] parts = line.split(" ");
            // parts = [varuiablename, =, value!]
            if (parts.length < 3 || !parts[1].equals("=") || !parts[2].endsWith("!")) {
                throw new Exception("Invalid syntax!");
            }
            // remove ! from parts[2] (at the end of the line)
            parts[2] = parts[2].substring(0, parts[2].length() - 1);
            // parts = [varuiablename, =, value]

            // Try to convery parts[2] to an integer. if it doesnt work (exce4ption thrown)
            // then treat as string.
            try {
                int value = Integer.parseInt(parts[2]);
                tokens.add(tContainer.new TIntVar(parts[0], value).toToken());
            } catch (NumberFormatException e) {
                tokens.add(tContainer.new TStringVar(parts[0], parts[2]).toToken());
            }

        }

        return tokens;
        // if (!line.endsWith("!")) {
        // return line;
        // }
    }
}
