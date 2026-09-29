package com.jaiva.interpreter.libs;

import com.jaiva.interpreter.symbol.Symbol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class LibrarySymbol {
    private final Symbol symbol;
    private final ArrayList<String> aliases;

    public LibrarySymbol(
            Symbol symbol,
            String... aliases
    ) {
        this.symbol = symbol;
        this.aliases = new ArrayList<>(Arrays.asList(aliases));
        if (!this.aliases.contains(symbol.name)) // symbol name is itself an alias
            this.aliases.add(symbol.name);
    }

    public Symbol symbol() {
        return symbol;
    }

    public List<String> aliases() {
        return aliases;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (LibrarySymbol) obj;
        return Objects.equals(this.symbol, that.symbol) &&
                this.aliases.equals(that.aliases);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, aliases);
    }

    @Override
    public String toString() {
        return "LibrarySymbol[" +
                "symbol=" + symbol + ", " +
                "aliases=" + aliases + ']';
    }


}
