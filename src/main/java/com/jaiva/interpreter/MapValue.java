package com.jaiva.interpreter;

import com.jaiva.interpreter.symbol.Symbol;

/**
 * @class
 *        Represents a Map Value instance.
 *        This is so that when we're ina scope and variables get copied over,
 *        inner stuff still ave access to the exact same instance of something.
 *        <p>
 *        the {@code value} param is populated when given a symbol and then
 *        {@code oValue} param is populated when given an object.
 */
public class MapValue {
    private Object value;

    /**
     * If the value, has a value, use this.
     * 
     * @param v
     */
    public MapValue(Object v) {
        value = v;
    }

    /**
     * If the value is null use this overload instead.
     */
    public MapValue() {
        value = void.class;
    }

    /**
     * Checks if the given MapValue instance is empty.
     *
     * @param m the MapValue instance to check
     * @return {@code true} if the value of the MapValue is of type
     *         {@code void.class},
     *         indicating it is empty; {@code false} otherwise
     */
    public static boolean isEmpty(MapValue m) {
        return m.value == void.class;
    }

    /**
     * Retrieves the value stored in this object.
     *
     * @return the value of this object
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets the value of this object.
     *
     * @param value the new value to be assigned
     */
    public void setValue(Object value) {
        this.value = value;
    }
}
