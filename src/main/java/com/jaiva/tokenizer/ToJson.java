package com.jaiva.tokenizer;

import java.util.ArrayList;

import com.jaiva.errors.JaivaException;
import com.jaiva.tokenizer.jdoc.JDoc;

/**
 * The ToJson class is a utility for constructing JSON-like string
 * representations
 * of objects. It provides methods to append key-value pairs, including support
 * for primitive types, nested objects, and arrays.
 */
public class ToJson {
    /**
     * The StringBuilder object used to construct the JSON string.
     */
    private final StringBuilder json = new StringBuilder();

    /**
     * Constructor for the ToJson class.
     * Initializes the JSON string with the specified parameters.
     *
     * @param exportSymbol Indicates whether to export the symbol or not.
     * @param name         The name of the object.
     * @param type         The type of the object.
     * @param ln           The line number associated with the object.
     * @param tooltip      The tooltip or description of the object.
     */
    public ToJson(boolean exportSymbol, String name, String type, int ln, Object tooltip) {
        json.append("{");
        json.append("\"type\": \"").append(type).append("\",");
        json.append("\"name\": \"").append(name).append("\",");
        json.append("\"exportSymbol\": \"").append(exportSymbol).append("\",");
        json.append("\"toolTip\": \"").append(tooltip.toString()).append("\",");
        json.append("\"lineNumber\": ").append(ln).append(",");
    }

    /**
     * Checks if a key exists in the JSON string.
     *
     * @param key The key to check for existence in the JSON string.
     * @return true if the key exists, false otherwise.
     */
    public boolean keyExists(String key) {
        // Check if the key exists in the JSON string
        return json.indexOf("\"" + key + "\":") != -1;
    }

    /**
     * Removes a key-value pair from the JSON string based on the specified key.
     *
     * @param key the key of the key-value pair to be removed from the JSON string.
     *            If the key is not found, the method does nothing.
     */
    public void removeKey(String key) {
        // remove the key from the json string
        int startIndex = json.indexOf("\"" + key + "\":");
        if (startIndex != -1) {
            int endIndex = json.indexOf(",", startIndex) + 1;
            json.delete(startIndex, endIndex);
        }
    }

    /**
     * Appends a key-value pair to the JSON string. The value can be of various
     * types,
     * including
     * primitive types, complex objects, and arrays.
     *
     * @param key    The key to be added to the JSON string.
     * @param value  The value associated with the key. It can be of various types,
     *               including
     *               primitive types, complex objects, and arrays.
     * @param isLast Indicates whether this is the last key-value pair to be added.
     * @throws JaivaException when siome goes wrong. ill doc later trust
     */
    public void append(String key, Object value, boolean isLast) throws JaivaException {
        // Object can be one of these types :
        // String, int, double, boolean. - primitives
        // another object, array - complex types.

        if (value instanceof JDoc) {
            json.append("\"").append(key).append("\":").append(value.toString()).append(",");
        } else if (value instanceof String && !((String) value).startsWith("{")) {
            json.append("\"").append(key).append("\": \"").append(value).append("\",");
        } else if (value instanceof TokenDefault || value instanceof Token<?>) {
            TokenDefault t = value instanceof TokenDefault ? (TokenDefault) value : ((Token<?>) value).value();
            json.append("\"").append(key).append("\": ").append(t.toJson()).append(",");
        } else if (value instanceof ToJson v) {
            json.append("\"").append(key).append("\": ").append(v.json.toString()).append(",");
        } else if (value instanceof ArrayList<?> v) {
            json.append("\"").append(key).append("\": [");
            for (Object obj : v) {
                switch (obj) {
                    case ToJson j -> json.append(j.json.toString()).append(",");
//                    case Comments.JDoc doc -> json.append().append(",")
                    case Token<?> t -> json.append(t.value().toJson()).append(",");
                    case TokenDefault t -> json.append(t.toJson()).append(",");
                    case String s when !(s.startsWith("{") && s.endsWith("}")) ->
                            json.append("\"").append(obj).append("\",");
                    case null, default -> json.append(obj).append(",");
                }
            }
            // remove the last comma
            if (json.lastIndexOf(",") != -1 && json.lastIndexOf(",") > json.lastIndexOf("[")) {
                json.deleteCharAt(json.lastIndexOf(","));
            }
            json.append("],");
        } else {
            json.append("\"").append(key).append("\": ").append(value).append(",");
        }
    }

    /**
     * Completes the JSON string construction by removing the last comma, if
     * present,
     * and appending a closing curly brace followed by a newline character.
     *
     * @return The finalized JSON string.
     */
    public String complete() {
        // remove the last comma
        if (json.lastIndexOf(",") != -1) {
            json.deleteCharAt(json.lastIndexOf(","));
        }
        json.append("}");
        return json.toString();
    }

    /**
     * Completes the construction of a JSON object by removing the last comma, if
     * present,
     * and appending a closing curly brace ('}'). This ensures the JSON structure is
     * properly
     * formatted. Returns the current instance for method chaining.
     *
     * @return The current instance of {@code ToJson} for method chaining.
     */
    public ToJson completeInNest() {
        // remove the last comma
        if (json.lastIndexOf(",") != -1) {
            json.deleteCharAt(json.lastIndexOf(","));
        }
        json.append("}");
        return this;
    }

}