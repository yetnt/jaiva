package com.jaiva.interpreter.symbol;

import com.jaiva.errors.IntErrs.*;

enum SymbolType {
    VARIABLE, FUNCTION
}

/**
 * Base class for functions and variables
 * <p>
 * It's essentially anything that can be named. if need a value, actually no. I
 * don't need to document this. Your fault if you're trying to understand MY
 * shit.
 */
public class Symbol {
    public String name;
    public SymbolType symbolType;
    public boolean isFrozen = false;

    Symbol(SymbolType type) {
        this.symbolType = type;
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