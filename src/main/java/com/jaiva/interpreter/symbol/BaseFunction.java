package com.jaiva.interpreter.symbol;

import com.jaiva.tokenizer.Token.TFunction;

public class BaseFunction extends Symbol {
    public TFunction token;

    @SuppressWarnings("rawtypes")
    public BaseFunction(TFunction token) {
        super(SymbolType.FUNCTION);
        this.token = token;
    }

    public BaseFunction(String name) {
        super(SymbolType.FUNCTION);
        this.name = name;
    }

    public BaseFunction(String name, TFunction token) {
        super(SymbolType.FUNCTION);
        this.token = token;
        this.name = name;
    }

    /**
     * Calls the specified function.
     * 
     * @return
     */
    public Object call(Object[] params) {
        return void.class;
    }
}