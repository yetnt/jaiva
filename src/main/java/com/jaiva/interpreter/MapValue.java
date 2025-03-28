package com.jaiva.interpreter;

import com.jaiva.interpreter.symbol.Symbol;

/**
 * Represents a Map Value instance.
 * This is so that when we're ina scope and variables get copied over,
 * inner stuff still ave access to the exact same instance of something.
 */
public class MapValue {
    private Symbol value;

    MapValue(Symbol v) {
        value = v;
    }

    public Symbol getValue() {
        return value;
    }

    public void setValue(Symbol value) {
        this.value = value;
    }
}
