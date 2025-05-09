package com.jaiva.interpreter.globals;

import java.util.HashMap;

import com.jaiva.interpreter.MapValue;
import com.jaiva.tokenizer.Token;

/**
 * Base class for global holder classes.
 */
public class BaseGlobals {
    /**
     * Token container to create tokens
     */
    public final Token<?> container = new Token<>(null);
    /**
     * Variable functions store
     */
    public HashMap<String, MapValue> vfs = new HashMap<String, MapValue>();

    /**
     * Default Constructor.
     */
    BaseGlobals() {

    }
}
