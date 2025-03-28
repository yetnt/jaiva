package com.jaiva.interpreter.symbol;

import java.util.ArrayList;

import com.jaiva.errors.IntErrs.FrozenSymbolException;
import com.jaiva.tokenizer.TokenDefault;

public class BaseVariable extends Symbol {
    private Object scalar;
    public TokenDefault token;
    // we just give access to the array because why not.
    public ArrayList<Object> array = new ArrayList<>();

    public BaseVariable(String name, TokenDefault t) {
        super(SymbolType.VARIABLE);
        this.token = t;
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
}