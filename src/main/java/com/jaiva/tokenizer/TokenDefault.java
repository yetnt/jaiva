package com.jaiva.tokenizer;

public class TokenDefault {
    public String name = "";
    public int lineNumber = 0;

    public String getContents(int depth) {
        return "";
    }

    public TokenDefault(String name) {
        this.name = name;
    }

    public TokenDefault(String name, int line) {
        this.name = name;
        this.lineNumber = line;
    }
}