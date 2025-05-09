package com.jaiva.interpreter.symbol;

import com.jaiva.errors.IntErrs.*;
import com.jaiva.tokenizer.TokenDefault;

/**
 * Base class for functions and variables
 * <p>
 * It's essentially anything that can be named. if need a value, actually no. I
 * don't need to document this. Your fault if you're trying to understand MY
 * shit.
 */
public class Symbol {
    /**
     * The name of the symbol
     */
    public String name;
    /**
     * The symbol type
     */
    public SymbolType symbolType;
    /**
     * boolean indicating whether the symbol is "frozen" which means it cannot be
     * modified.
     */
    public boolean isFrozen = false;
    /**
     * The token associated with the symbol.
     */
    public TokenDefault token;

    /**
     * Define a symbol without the token
     * 
     * @param type Symbol type.
     */
    Symbol(SymbolType type) {
        this.symbolType = type;
    }

    /**
     * Define a symbol with the token associated with it.
     * 
     * @param type  Symbol type.
     * @param token Symbol's token.
     */
    Symbol(SymbolType type, TokenDefault token) {
        this.symbolType = type;
        this.token = token;
    }

    /**
     * Freezes this symbol so that modificaitons cannot be made. This is usually
     * called in the constructor of a global variable.
     * <p>
     * A frozen symbol cannot be unfrozen.
     */
    public void freeze() {
        this.isFrozen = true;
    }
}