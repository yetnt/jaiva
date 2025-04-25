package com.jaiva.interpreter.globals;

import java.util.HashMap;

import com.jaiva.interpreter.MapValue;
import com.jaiva.tokenizer.Token;

public class BaseGlobals {
    private final Token<?> tContainer = new Token<>(null);
    public HashMap<String, MapValue> vfs = new HashMap<String, MapValue>();

    BaseGlobals() {

    }
}
