package com.jaiva.tokenizer;

import java.util.ArrayList;

/**
 * The ToJson class is a utility for constructing JSON-like string
 * representations
 * of objects. It provides methods to append key-value pairs, including support
 * for primitive types, nested objects, and arrays.
 */
class ToJson {
    StringBuilder json = new StringBuilder();

    public ToJson(String name, String type, int ln) {
        json.append("{");
        json.append(" \"type\": \"").append(type).append("\",");
        json.append("  \"name\": \"").append(name).append("\",");
        json.append("  \"lineNumber\": ").append(ln).append(",");
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
     * Appends a key-value pair to the JSON representation.
     * 
     * @param key   The key as a String.
     * @param value The value associated with the key. The value can be one of the
     *              following types:
     *              <ul>
     *              <li>String: Appends the value as a quoted string.</li>
     *              <li>ToJson: Appends the JSON representation of the object
     *              implementing the ToJson interface.</li>
     *              <li>ArrayList: Appends the value as a JSON array. If the array
     *              contains objects implementing the ToJson interface, their JSON
     *              representations are appended; otherwise, the objects are
     *              appended as quoted strings.</li>
     *              <li>Other types: Appends the value as-is.</li>
     *              </ul>
     */
    public void append(String key, Object value, boolean isLast) {
        // Object can be one of these types :
        // String, int, double, boolean. - primitives
        // another object, array - complex types.

        if (value instanceof String && !((String) value).startsWith("{")) {
            json.append("\"").append(key).append("\": \"").append(value).append("\",");
        } else if (value instanceof TokenDefault || value instanceof Token<?>) {
            TokenDefault t = value instanceof TokenDefault ? (TokenDefault) value : ((Token<?>) value).getValue();
            json.append("\"").append(key).append("\": ").append(t.toJson()).append(",");
        } else if (value instanceof ToJson) {
            ToJson v = (ToJson) value;
            json.append("\"").append(key).append("\": ").append(v.json.toString()).append(",");
        } else if (value instanceof ArrayList) {
            ArrayList<?> v = (ArrayList<?>) value;
            json.append("\"").append(key).append("\": [");
            for (int i = 0; i < v.size(); i++) {
                Object obj = v.get(i);
                if (obj instanceof ToJson) {
                    ToJson j = (ToJson) obj;
                    json.append(j.json.toString()).append(",");
                } else if (obj instanceof Token<?>) {
                    Token<?> t = (Token<?>) obj;
                    json.append(t.getValue().toJson()).append(",");
                } else if (obj instanceof TokenDefault) {
                    TokenDefault t = (TokenDefault) obj;
                    json.append(t.toJson()).append(",");
                } else if (obj instanceof String && !(((String) obj).startsWith("{") && ((String) obj).endsWith("}"))) {
                    json.append("\"").append(obj).append("\",");
                } else {
                    json.append(obj).append(",");
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

        // if (isLast) {
        // json.append(",");
        // }
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

public class TokenDefault {
    public String name = "";
    public int lineNumber = 0;
    public ToJson json;

    public String getContents(int depth) {
        return "";
    }

    public TokenDefault(String name) {
        this.name = name;
        this.json = new ToJson(name, getClass().getSimpleName(), -1);
    }

    public TokenDefault(String name, int line) {
        this.name = name;
        this.lineNumber = line;
        this.json = new ToJson(name, getClass().getSimpleName(), line);
    }

    public String toJson() {
        return json.complete();
    }

    public ToJson toJsonInNest() {
        return json.completeInNest();
    }
}