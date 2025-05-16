package com.jaiva.interpreter.globals;

import java.util.HashMap;

import com.jaiva.interpreter.MapValue;
import com.jaiva.tokenizer.Token;

/**
 * Base class for global holder classes.
 */
public class BaseGlobals<T extends GlobalType> {
    /**
     * Token container to create tokens
     */
    public final Token<?> container = new Token<>(null);

    /**
     * The type of the BaseGlobal container.
     */
    public T type;
    /**
     * The path if this container is defined as {@link GlobalType}.LIB
     */
    public String path = null;
    /**
     * Variable functions store
     */
    public HashMap<String, MapValue> vfs = new HashMap<String, MapValue>();

    /**
     * Default Constructor.
     * 
     * @param value The type of this Globals holder.
     */
    BaseGlobals(T value) {
        type = value;
    }

    /**
     * Constructor for holder classes that need to be imported.
     * 
     * @param value The type of this Globals holder.
     * @param p     The "filename" (without the extension)
     */
    BaseGlobals(T value, String p) {
        type = value;
        path = p;
    }
}
