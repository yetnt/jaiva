package com.jaiva.lang;

import java.util.ArrayList;
import java.util.List;

import com.jaiva.tokenizer.Token;

/**
 * Comments class is a utility class that provides methods for processing and
 * manipulating comments in strings.
 * <p>
 * This class includes methods for removing single line comments, checking if an
 * array of strings contains only comments, and safely decimating strings to
 * remove comments.
 */
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
        for (String s : lines) {
            String line = s;

            // if the line does not contain a newline,
            // call a helper function or process it accordingly
            if (!line.contains("\n")) {
                // You might need to adjust this if you're calling
                // a helper method that expects an array...
                line = Comments.decimate(line.trim()).trim();
            }

            if (!line.isBlank()) {
                newLines.add(line);
            }
        }
        return newLines.toArray(new String[0]);
    }

    /**
     * Remove single line comments. Call this function when appropriate.
     * 
     * @param line The line to make sure there isnt a single line comment
     * @return The line without single line comments.
     */
    public static String decimate(String line) {
        if (line.indexOf(Chars.COMMENT) == -1)
            return line;

        line = line.substring(0, line.indexOf(Chars.COMMENT));

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
        if (o.indexOf(Chars.COMMENT_DOC) != 0)
            return decimate(o);

        return new Token.TDocsComment(o.substring(o.indexOf(Chars.COMMENT_DOC) + 2))
                .toToken();

    }

    /**
     * Check if the array is only comments. This is used to check if the line
     * contains only comments or not.
     * <p>
     * This is a helper method where a ! may be splitting inside a comment which had
     * a double @ symbol.
     * 
     * @param arr Array to check against
     * @return boolean indicating whether the array of lines only has comments.
     */
    public static boolean arrayIsOnlyComments(String[] arr) {
        if (arr.length > 2) {
            for (String s : arr) {
                if (!s.trim().isEmpty() && !s.trim().startsWith(Character.toString(Chars.COMMENT))) {
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
            } else if (arr[0].trim().isEmpty() && arr[1].trim().startsWith(Character.toString(Chars.COMMENT))) {
                return true;
            } else if (arr[0].trim().startsWith(Character.toString(Chars.COMMENT)) && arr[1].trim().isEmpty()) {
                return true;
            } else return arr[0].trim().startsWith(Character.toString(Chars.COMMENT))
                    && !arr[1].trim().startsWith(Character.toString(Chars.COMMENT))
                    || !arr[0].trim()
                    .startsWith(Character.toString(Chars.COMMENT))
                    && arr[1].trim().startsWith(Character.toString(Chars.COMMENT));

        }
    }

}
