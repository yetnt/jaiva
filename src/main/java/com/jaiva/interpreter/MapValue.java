package com.jaiva.interpreter;

/**
 * Represents a Map Value instance.
 * This is so that when we're ina scope and variables get copied over,
 * inner stuff still ave access to the exact same instance of something.
 */
public class MapValue {
    /**
     * The value of this object.
     * <p>
     * If the value is null, it will be set to {@code void.class} to indicate that
     * it is empty.
     */
    private Object value;

    /**
     * Constructs a new MapValue instance with the specified value.
     *
     * @param v the value to be assigned to this object
     */
    public MapValue(Object v) {
        value = v;
    }

    /**
     * Constructs a MapValue instance without a specified value.
     * <p>
     * This constructor is used to create an empty MapValue.
     * 
     * @param v
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
