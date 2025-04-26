package com.jaiva.tokenizer;

public class TokenDefault {
    public String name = "";
    public String tooltip = "Jaiva Construct";
    public int lineNumber = 0;
    public ToJson json;
    public boolean exportSymbol = false;

    // public TokenDefault(String name) {
    // this.name = name;
    // this.json = new ToJson(name, getClass().getSimpleName(), -1);
    // }

    /**
     * Constructor for a basic token
     * 
     * @param name
     * @param line
     */
    public TokenDefault(String name, int line) {
        this.name = name;
        this.lineNumber = line;
        this.json = new ToJson(false, name, getClass().getSimpleName(), line, tooltip);
    }

    /**
     * Constructor for a symbol token that can be exported
     * 
     * @param name
     * @param line
     */
    public TokenDefault(boolean exportSymbol, String name, int line) {
        this.name = name;
        this.lineNumber = line;
        this.exportSymbol = exportSymbol;
        this.json = new ToJson(exportSymbol, name, getClass().getSimpleName(), line, tooltip);
    }

    /**
     * Constructor for a globals.
     * 
     * @param name
     * @param line
     * @param tt
     */
    public TokenDefault(String name, int line, String tt) {
        this.name = name;
        this.lineNumber = line;
        this.tooltip = tt;
        this.json = new ToJson(false, name, getClass().getSimpleName(), line, tt);
    }

    public String toJson() {
        return json.complete();
    }

    public ToJson toJsonInNest() {
        return json.completeInNest();
    }
}