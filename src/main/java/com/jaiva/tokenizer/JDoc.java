package com.jaiva.tokenizer;

import com.jaiva.lang.Chars;
import com.jaiva.lang.EscapeSequence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * JDoc is a util class for parsing Jaiva Documentation comments into JSON format, for something like
 * an extension to read and yeah.
 */
public class JDoc {

    /**
     * Whitelist of allowed tags.
     * The 0 character tag is to allow multiple lines.
     */
    private final ArrayList<String> whitelist = new ArrayList<>(
            Arrays.asList("", "p", "par", "ret", "rets", "r")
    );

    /**
     * The actual JSON
     */
    private final StringBuilder json = new StringBuilder();

    /**
     * Default Constructor which just initializes the JSON output.
     */
    JDoc() {
        json.append("{");
    }

    /**
     * Constructor with the given input, which just calls {@link JDoc#parse(String)}
     *
     * @param input The given input separated by newlines with @* stripped away
     */
    public JDoc(String input) {
        json.append("{");
        parse(input);
    }


    /**
     * Formats the given input string for JDoc comments.
     * This method replaces occurrences of "---" with a line break and leaves other
     * formatting characters unchanged.
     *
     * @param input The input string to be formatted.
     * @return The formatted string with "---" replaced by line breaks.
     */
    public String format(String input) {
        // general purpose formatting for JDoc comments. More to be added later.
        // 3 dashes become a line break.
        // **, _ stay the same so dont do anything with them.
        input = input.replaceAll("(?m)^\\s*---\\s*$", "\n");
        return input;
    }

    /**
     * Parses the given input string to extract tags and descriptions, and appends them to the JSON output.
     *
     * @param input The input string containing documentation comments.
     * @return The current `JDoc` instance for method chaining.
     */
    public JDoc parse(String input) {
        Scanner s = new Scanner(input);
        ArrayList<String> description = new ArrayList<>();
        StringBuilder tags = new StringBuilder();
        tags.append("\"tags\":[");

        while (s.hasNextLine()) {
            String line = s.nextLine();
            if (line.contains(Chars.DOC_TAG)) {
                String[] args = line.split(Pattern.quote(Chars.DOC_TAG));
                String tag = args[0].trim();
                String content = EscapeSequence.escapeJson(args[1].trim());

                if (!whitelist.contains(tag)) continue;
                if (tag.isEmpty()) {
                    // add the content to the previously added tag.
                    tags.append(" ").append(content);
                } else {
                    if (tags.toString().length() != 8 && !tags.toString().endsWith("},")) tags.append("\"},");
//                        if (tags.toString().length() != 1 && !tags.toString().endsWith("},")) tags.append(",");
                    switch (tag) {
                        case "p", "par" -> {
                            tags.append("{\"type\":\"param\",");
                            // paramName <- expectedType docs.
                            String[] paramArgs = content.split(Chars.ASSIGNMENT);
                            if (paramArgs.length == 0) continue; // invalid syntax, skip.
                            String paramName = paramArgs[0].trim();
                            String type = paramArgs[1].trim().split(" ")[0].trim();
                            String desc = paramArgs[1].trim().substring(type.length()).trim();
                            tags.append(String.format("\"name\":\"%1s\",\"pType\":\"%2s\",\"desc\":\"%3s", paramName, type, desc));
                        }
                        case "ret", "rets", "r" -> {
                            tags.append("{\"type\":\"returns\",").append(String.format("\"desc\":\"%s", content));
                        }
                        default -> {
                            // do nothing
                        }
                    }
                }
            } else {
                description.add(" ");
                description.add(line);
            }
        }
        s.close();
        if (tags.toString().endsWith("\"},")) {
            json.append(tags, 0, tags.toString().length() - 1).append("],");
        } else if (tags.length() == 8) {
            json.append(tags).append("],");
        } else {
            json.append(tags).append("\"}],");
        }
        json.append("\"description\":[");
        for (String desc : description) {
            if (desc.trim().isEmpty()) continue;
            desc = EscapeSequence.escapeJson(format(desc));
            json.append("\"").append(desc).append("\",");
        }
        if (json.charAt(json.length() - 1) == ',')
            json.deleteCharAt(json.length() - 1); // remove last comma

        json.append("]");
        return this;
    }

    @Override
    public String toString() {
        return json + "}";
    }
}
