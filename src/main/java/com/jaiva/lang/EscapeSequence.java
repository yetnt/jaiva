package com.jaiva.lang;

/**
 * EscapeSequence class is a utility class that provides methods for escaping
 * and
 * unescaping strings.
 * <p>
 * This class is used to escape and unescape strings for use in Jaiva source
 * code.
 * <p>
 * The escape and unescape methods are used to replace special characters with
 * their escaped equivalents and vice versa.
 */
public class EscapeSequence {

    /**
     * Escape a string.
     *
     * @param str The string to escape.
     * @return
     */
    public static String escape(String str) {
        return str
                .replace("$=", "=")
                .replace("$,", ",")
                .replace("$!", "!")
                // .replace("$s", " ")
                .replace("$@", "@")
                .replace("$n", "\n")
                .replace("$t", "\t")
                .replace("$r", "\r")
                .replace("$b", "\b")
                .replace("$f", "\f")
                // .replace("$'", "'")
                .replace("$\"", "\"")
                .replace("$$", "$");
    }

    /**
     * Escape a string for JSON.
     *
     * @param str The string to escape.
     * @return
     */
    public static String escapeJson(String str) {
        return str
                .replace("\\", "\\\\")
                .replace("\n", "\\n")
                .replace("\t", "\\t")
                .replace("\r", "\\r")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\"", "\\\"");
    }

    /**
     * Unescape a string.
     *
     * @param str The string to unescape.
     * @return
     */
    public static String unescape(String str) {
        return str
                .replace("$", "$$")
                .replace("=", "$=")
                .replace(",", "$,")
                .replace("!", "$!")
                // .replace(" ", "$s")
                .replace("@", "$@")
                .replace("\n", "$n")
                .replace("\t", "$t")
                .replace("\r", "$r")
                .replace("\b", "$b")
                .replace("\f", "$f")
                // .replace("'", "$'")
                .replace("\"", "$\"");
    }

    /**
     * Escape all strings in a line.
     * 
     * @param line
     * @return
     */
    public static String escapeAll(String line) {
        // maak a <- "heelo"!
        // func("weird thing", "with multiple strings")
        // We have to escape anything in EVEN number stuff (ODD number in 0-based
        // indexing)
        // 0, 1, 2, 3, 4
        // all the actual strings are stored in 1, 3, etc
        String[] parts = line.split("(?<!\\$)\""); // Negative lookbehind checks that their isnt $ before "
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i % 2 != 0) {
                String unescapedString = parts[i]
                        .split("((?<!\\$)(\\\"|\\!|\\\\|\\\"|@|=))|(\\t|\\n|\\s)").length != 1
                                ? unescape(parts[i])
                                : parts[i];
                b.append("\"" + unescapedString + "\"");
            } else {
                b.append(parts[i]);
            }
        }

        return b.toString();
    }
}