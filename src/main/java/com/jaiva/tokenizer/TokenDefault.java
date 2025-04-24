package com.jaiva.tokenizer;

public class TokenDefault {
    public String name = "";
    public String tooltip = "Jaiva Construct";
    public int lineNumber = 0;
    public ToJson json;

    // public TokenDefault(String name) {
    // this.name = name;
    // this.json = new ToJson(name, getClass().getSimpleName(), -1);
    // }

    public TokenDefault(String name, int line) {
        this.name = name;
        this.lineNumber = line;
        this.json = new ToJson(name, getClass().getSimpleName(), line, tooltip);
    }

    public TokenDefault(String name, int line, String tt) {
        this.name = name;
        this.lineNumber = line;
        this.tooltip = tt;
        this.json = new ToJson(name, getClass().getSimpleName(), line, tt);
    }

    public String toJson() {
        return json.complete();
    }

    public ToJson toJsonInNest() {
        return json.completeInNest();
    }
}