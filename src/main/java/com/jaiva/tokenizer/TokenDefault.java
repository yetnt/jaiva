package com.jaiva.tokenizer;

import com.jaiva.errors.JaivaException;

/**
 * TokenDefault class is a default implementation of all the other
 * subclasses/tokens defined in {@link Token}
 */
public class TokenDefault {
    /**
     * The name of the token.
     * <p>
     * If the token is one that doesnt have a definitive name, such as an if
     * statement, it will be the token class name.
     */
    public String name = "";
    /**
     * The tooltip or description of the token.
     */
    public String tooltip = "Jaiva Construct";
    /**
     * The line number associated with the token.
     */
    public int lineNumber = 0;
    /**
     * The JSON representation of the token.
     */
    public ToJson json;
    /**
     * Boolean to check if the token is a symbol that can be exported.
     */
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

    /**
     * Returns the JSON representation of the object as a string.
     * 
     * @return
     * @throws JaivaException For any errors
     */
    public String toJson() throws JaivaException {
        return json.complete();
    }

    /**
     * Returns the JSON (ToJson object) representation of the object as a string
     * such that it can be nested in other constructs.
     * 
     * @return
     * @throws JaivaException For any errors
     */
    public ToJson toJsonInNest() throws JaivaException {
        return json.completeInNest();
    }
}