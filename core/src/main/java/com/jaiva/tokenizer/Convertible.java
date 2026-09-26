package com.jaiva.tokenizer;

import com.jaiva.errors.JaivaException;

/**
 * Let's go!
 * <p></p>
 * - Dababy.
 */
public interface Convertible {
    /**
     * A default instance of ToJson that can be used for common JSON operations.
     * This field is initialized to null and should be overridden or used with caution.
     */
    ToJson json = null;

    /**
     * Converts the implementing object to a JSON string representation.
     *
     * @return A JSON string representing the object.
     * @throws JaivaException If an error occurs during the JSON conversion process.
     */
    String toJson() throws JaivaException;

    /**
     * Converts the implementing object to a ToJson object, typically for nested JSON structures.
     *
     * @return A ToJson object representing the nested structure.
     * @throws JaivaException If an error occurs during the conversion to a nested ToJson object.
     */
    ToJson toJsonInNest() throws JaivaException;
}
