package com.jaiva.interpreter.symbol;

import java.util.ArrayList;

import com.jaiva.errors.IntErrs.FrozenSymbolException;
import com.jaiva.tokenizer.Token.TVarRef;
import com.jaiva.tokenizer.Token;
import com.jaiva.tokenizer.TokenDefault;

public class BaseVariable extends Symbol {
    private Object scalar;
    // we just give access to the array because why not.
    public ArrayList<Object> array = new ArrayList<>();

    public BaseVariable(String name, TokenDefault t, Object scalar) {
        super(SymbolType.VARIABLE, t);
        this.scalar = scalar;
    }

    public BaseVariable(String name, TokenDefault t, ArrayList<Object> arr) {
        super(SymbolType.VARIABLE, t);
        this.array.addAll(arr);
    }

    public BaseVariable(String name, TokenDefault t) {
        super(SymbolType.VARIABLE, t);
    }

    public BaseVariable(String name) {
        super(SymbolType.VARIABLE);
    }

    public Object getScalar() {
        return scalar;
    }

    public void setScalar(Object value) throws FrozenSymbolException {
        if (isFrozen)
            throw new FrozenSymbolException(this);
        this.scalar = value;
    }

    /**
     * This creates a user-defined variable.
     * <p>
     * If this variable only has a scalar value, create an arraylist with only that
     * 1 value.
     * 
     * @param name Name of the variable.
     * @param t    The TVarRef token.
     * @param arr  The arraylist.
     * @return
     */
    public static DefinedVariable create(String name, TokenDefault t, ArrayList<Object> arr) {
        if (arr.size() == 1)
            return new DefinedVariable(name, t, arr.get(0));
        return new DefinedVariable(name, t, arr);
    }

    public static class DefinedVariable extends BaseVariable {
        DefinedVariable(String name, TokenDefault t, Object scalar) {
            super(name, t, scalar);
        }

        DefinedVariable(String name, TokenDefault t, ArrayList<Object> arr) {
            super(name, t, arr);
        }
    }
}