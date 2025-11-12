package com.jaiva.utils;

import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public final class MarkDownLiteral {
    /**
     * A Deque to store the parts of the Markdown literal, allowing for easy addition of formatting.
     */
    private final Deque<String> literal = new ArrayDeque<>();
    public static String CHECKBOX = "[]";
    public static String XCHECKBOX = "[x]";
    public static String BQUOTE = ">";

    public MarkDownLiteral(String input) {
        literal.add(input);
    }


    /**
     * Applies bold formatting to the literal.
     *
     * @return The current MarkDownLiteral instance for chaining.
     */
    public MarkDownLiteral bold() {
        literal.addFirst("**");
        literal.add("**");
        return this;
    }

    /**
     * Applies italics formatting to the literal.
     *
     * @return The current MarkDownLiteral instance for chaining.
     */
    public MarkDownLiteral italics() {
        literal.addFirst("_");
        literal.add("_");
        return this;
    }

    /**
     * Applies inline code formatting to the literal.
     *
     * @return The current MarkDownLiteral instance for chaining.
     */
    public MarkDownLiteral inlineCode() {
        literal.addFirst("`");
        literal.add("`");
        return this;
    }

    /**
     * Applies blockquote formatting to the literal.
     *
     * @return The current MarkDownLiteral instance for chaining.
     */
    public MarkDownLiteral blockQuote() {
        literal.addFirst(BQUOTE);
        return this;
    }

    /**
     * Converts the literal into a Markdown link.
     *
     * @param URL The URL to link to.
     * @return The current MarkDownLiteral instance for chaining.
     */
    public MarkDownLiteral linkTo(String URL) {
        literal.addFirst("[");
        literal.add("](" + URL + ")");
        return this;
    }

    /**
     * Applies a title (heading) formatting based on a numerical level.
     *
     * @param amt The heading level (1-6).
     * @return The current MarkDownLiteral instance for chaining.
     */
    public MarkDownLiteral title(int amt) {
        literal.addFirst(Title.fromNumber(amt).toMd());
        return this;
    }

    /**
     * Applies a title (heading) formatting based on a {@link Title} enum.
     *
     * @param title The {@link Title} enum representing the heading level.
     * @return The current MarkDownLiteral instance for chaining.
     */
    public MarkDownLiteral title(Title title) {
        literal.addFirst(title.toMd());
        return this;
    }

    /**
     * Wraps the literal in an HTML comment.
     *
     * @return The current MarkDownLiteral instance for chaining.
     */
    public MarkDownLiteral comment() {
        literal.addFirst("<!-- ");
        literal.add(" -->");
        return this;
    }

    /**
     * Wraps the input list of strings in a Markdown code block with the specified language.
     *
     * @param input The list of strings to be wrapped.
     * @param languageIdentifier The language identifier for the code block.
     * @return An ArrayList of strings representing the formatted code block.
     */
    public static ArrayList<String> asCodeBlock(ArrayList<String> input, String languageIdentifier) {
        ArrayList<String> out = new ArrayList<>();
        out.add("```" + languageIdentifier);
        out.addAll(input);
        out.add("```");
        return out;
    }

    /**
     * Converts headers and rows into a Markdown table format.
     *
     * @param headers The list of header strings.
     * @param rows The list of rows, where each row is a list of strings.
     * @return An ArrayList of strings representing the formatted Markdown table.
     */
    public static ArrayList<String> asTable(ArrayList<String> headers, ArrayList<ArrayList<String>> rows) {
        ArrayList<String> out = new ArrayList<>();
        // Create header row
        String headerRow = "| " + String.join(" | ", headers) + " |";
        out.add(headerRow);
        // Create separator row
        String separatorRow = "| " + String.join(" | ", headers.stream().map(h -> "---").toArray(String[]::new)) + " |";
        out.add(separatorRow);
        // Create data rows
        for (ArrayList<String> row : rows) {
            String dataRow = "| " + String.join(" | ", row) + " |";
            out.add(dataRow);
        }
        return out;
    }

    /**
     * Creates a GitHub-style blockquote with a specific alert type.
     *
     * @param top The type of GitHub blockquote (e.g., WARNING, NOTE).
     * @param str The content of the blockquote.
     * @return An ArrayList of strings representing the formatted GitHub blockquote.
     */
    public static ArrayList<String> GithubBlockQuote(GithubBlockQuote top, String str) {
        ArrayList<String> out = new ArrayList<>();
        out.add("> [!"+top.toString()+"]");
        out.add("> " + str);
        return out;
    }

    /**
     * Converts a list of strings into a Markdown unordered list.
     *
     * @param str The ArrayList of strings to convert.
     * @return An ArrayList of strings, where each string is prefixed with "- ".
     */
    public static ArrayList<String> asList(ArrayList<String> str) {
        ArrayList<String> out = new ArrayList<>(str.size());
        for (String s : str) {
            out.add("- " + s);
        }
        return out;
    }

    /**
     * Converts a list of strings into a Markdown blockquote.
     *
     * @param str The ArrayList of strings to convert.
     * @return An ArrayList of strings, where each string is prefixed with "> ".
     */
    public static ArrayList<String> asBlockQuote(ArrayList<String> str) {
        ArrayList<String> out = new ArrayList<>(str.size());
        for (String s : str) {
            out.add("> " + s);
        }
        return out;
    }

    /**
     * Enum representing different types of GitHub-style blockquote alerts.
     */
    public enum GithubBlockQuote {
        WARNING("WARNING"),
        NOTE("NOTE"),
        IMPORTANT("IMPORTANT");

        private final String txt;
        GithubBlockQuote(String t) {
            txt = t;
        }

        public String getText() {
            return txt;
        }
    }

    /**
     * Enum representing different levels of Markdown headings (titles).
     */
    public enum Title {
        TITLE(1),
        SUBTITLE(2),
        SECTION(3),
        SUBSECTION(4),
        DETAIL(5),
        NOTE(6);

        private final int number;
        Title(int n) {
            number = n;
        }

        public int getNumber() {
            return number;
        }

        /**
         * Returns the {@link Title} enum corresponding to a given heading number.
         *
         * @param n The heading number (1-6).
         * @return The {@link Title} enum. Defaults to NOTE if the number is out of range.
         */
        public static Title fromNumber(int n) {
            assert n <= 6 && n >= 1;
            return Arrays.stream(Title.values())
                    .filter(t -> t.getNumber() == n)
                    .findFirst()
                    .orElse(NOTE); // Default or throw exception
        }

        /**
         * Converts the {@link Title} enum to its Markdown string representation (e.g., "## ").
         *
         * @return The Markdown string for the heading.
         */
        public String toMd() {
            return "#".repeat(this.getNumber()) + " ";
        }
    }

    /**
     * Returns the complete Markdown literal as a single string.
     */
    @Override
    public String toString() {
        return String.join("", literal);
    }
}
