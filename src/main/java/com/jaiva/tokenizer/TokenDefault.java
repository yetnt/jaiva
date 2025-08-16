package com.jaiva.tokenizer;

import com.jaiva.errors.JaivaException;

/**
 * TokenDefault class is a default implementation of all the other
 * subclasses/tokens defined in {@link Token}
 */
public class TokenDefault {
    /**
     * Boolean which defines whether this token is in experimental usage or not.
     * <p>
     * Currently, this is not used in the Jaiva language, but it can be used
     * in the future to mark tokens that are experimental and not yet fully
     * implemented.
     * </p>
     */
    public boolean experimental = false;
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
    public Object tooltip = "Jaiva Construct";
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

    /**
     * Constructor for a basic token
     * 
     * @param name Token name
     * @param line Line number where token appears
     */
    public TokenDefault(String name, int line) {
        this.name = name;
        this.lineNumber = line;
        this.json = new ToJson(false, name, getClass().getSimpleName(), line, tooltip);
    }

    /**
     * Constructor for a symbol token that can be exported
     *
     * @param exportSymbol Boolean to mark this symbol as exportable.
     * @param name Token name
     * @param line Line number where token appears
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
     * @param name Token name
     * @param line Token line number
     * @param tt Token tooltip
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
     * @return the final JSON representation.
     * @throws JaivaException For any errors
     */
    public String toJson() throws JaivaException {
        return json.complete();
    }

    /**
     * Returns the JSON (ToJson object) representation of the object as a string
     * such that it can be nested in other constructs.
     * 
     * @return the final JSON representation, but it returns the ToJson object for further editing
     * @throws JaivaException For any errors
     */
    public ToJson toJsonInNest() throws JaivaException {
        return json.completeInNest();
    }
}