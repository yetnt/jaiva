package com.jaiva.tokenizer;

import java.util.ArrayList;
import java.util.List;

public class Comments {

    /**
     * Processes an array of strings, removing blank or null lines and optionally
     * processing lines that do not contain a newline character.
     *
     * <p>
     * This method iterates through the provided array of strings, trims each line,
     * and applies additional processing to lines that do not contain a newline
     * character.
     * Non-blank and non-null lines are added to a new list, which is then returned
     * as an array.
     *
     * @param lines an array of strings to be processed
     * @return a new array of strings containing only non-blank, non-null lines
     *         after processing
     */
    public static String[] decimate(String[] lines) {
        if (lines.length < 2)
            return lines;
        List<String> newLines = new ArrayList<>();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];

            // if the line does not contain a newline,
            // call a helper function or process it accordingly
            if (!line.contains("\n")) {
                // You might need to adjust this if you're calling
                // a helper method that expects an array...
                line = Comments.decimate(line.trim()).trim();
            }

            if (line != null && !line.isBlank()) {
                newLines.add(line);
            }
        }
        return newLines.toArray(new String[0]);
    }

    /**
     * Remove single line comments. Call this function when appropriate.
     * 
     * @param line The line to make sure there isnt a single line comment
     * @return
     */
    public static String decimate(String line) {
        if (line.indexOf(Lang.COMMENT) == -1)
            return line;

        line = line.substring(0, line.indexOf(Lang.COMMENT));

        return line;
    }

    /**
     * Safely decimates a given string by checking if it starts with a specific
     * comment
     * marker and processes it accordingly.
     *
     * @param o The input string to be processed.
     * @return The original string if it does not start with the comment marker,
     *         otherwise a tokenized representation of the substring up to the
     *         comment marker.
     */
    public static Object safeDecimate(String o) {
        if (o.indexOf(Lang.COMMENT_DOC) != 0)
            return decimate(o);

        return new Token<>(null).new TDocsComment(o.substring(o.indexOf(Lang.COMMENT_DOC) + 2, o.length()))
                .toToken();

    }

    /**
     * Check if the array is only comments. This is used to check if the line
     * contains only comments or not.
     * 
     * This is a helper method where a ! may be splitting inside a comment which had
     * a double @ symbol.
     * 
     * @param arr
     * @return
     */
    static boolean arrayIsOnlyComments(String[] arr) {
        if (arr.length > 2) {
            for (String s : arr) {
                if (!s.trim().isEmpty() && !s.trim().startsWith(Character.toString(Lang.COMMENT))) {
                    return false;
                }
            }
            return true;
        } else {
            // if one of the elements is a comment, and the other isnt, return true.
            // check both elements.
            // otherwise return false.
            if (arr[0].trim().isEmpty() && arr[1].trim().isEmpty()) {
                return true;
            } else if (arr[0].trim().isEmpty() && arr[1].trim().startsWith(Character.toString(Lang.COMMENT))) {
                return true;
            } else if (arr[0].trim().startsWith(Character.toString(Lang.COMMENT)) && arr[1].trim().isEmpty()) {
                return true;
            } else if (arr[0].trim().startsWith(Character.toString(Lang.COMMENT))
                    && !arr[1].trim().startsWith(Character.toString(Lang.COMMENT))
                    || !arr[0].trim()
                            .startsWith(Character.toString(Lang.COMMENT))
                            && arr[1].trim().startsWith(Character.toString(Lang.COMMENT))) {
                return true;
            } else {
                return false;
            }

        }
    }

}
