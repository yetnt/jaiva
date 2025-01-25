package com.yetnt;

import java.util.*;

class EscapeSequence {
    /**
     * Escape a string.
     * 
     * @param str The string to escape.
     * @return
     */
    public static String escape(String str) {
        return str
                .replace("*=", "=")
                .replace("*!", "!")
                .replace("*s", " ")
                .replace("*n", "\n")
                .replace("*t", "\t")
                .replace("*r", "\r")
                .replace("*b", "\b")
                .replace("*f", "\f")
                .replace("*'", "'")
                .replace("*\"", "\"")
                .replace("*\\", "\\");
    }

    /**
     * Unescape a string.
     * 
     * @param str The string to unescape.
     * @return
     */
    public static String unescape(String str) {
        return str
                .replace("=", "*=")
                .replace("!", "*!")
                .replace(" ", "*s")
                .replace("\n", "*n")
                .replace("\t", "*t")
                .replace("\r", "*r")
                .replace("\b", "*b")
                .replace("\f", "*f")
                .replace("'", "*'")
                .replace("\"", "*\"")
                .replace("\\", "*\\");
    }
}

// // Create an outer Token instance
// Token<?> tokenContainer = new Token<>(null);

// // Create a TVar instance and convert it to a Token<TVar>
// Token<Token<TVar>.TVar> tVarToken = tokenContainer.new TVar("myVariable",
// "42").toToken();

// // Retrieve the inner TVar instance and access its parameters
// Token<TVar>.TVar tVarInstance = tVarToken
// .getValue();System.out.println("TVar Name:
// "+tVarInstance.name);System.out.println("TVar Value: "+tVarInstance.value);

public class Tokenizer {
    /**
     * Find the index of the enclosing character in a string.
     * 
     * @param line  The string to search in.
     * @param start The starting character.
     * @param end   The ending character.
     * @return
     */
    public static int findEnclosingCharIndex(String line, char start, char end) {
        int startCount = 0;
        int endCount = 0;
        boolean isStart = true;
        for (int i = 0; i < line.length(); i++) {
            if (start != end) {
                if (line.charAt(i) == start) {
                    startCount++;
                } else if (line.charAt(i) == end) {
                    endCount++;
                }
                if (startCount == endCount && startCount != 0) {
                    return i;
                }
            } else {
                if (line.charAt(i) == end) {
                    if (!isStart) {
                        return i;
                    }
                    isStart = !isStart;
                }
            }
        }
        return -1;
    }

    /**
     * Read a line and tokenize it.
     * May return an arraylist of tokens or a string to be used as the previosu
     * string of another readLine call.
     * 
     * @param line
     * @param previousLine
     * @return ArrayList<Token<?>> or String
     * @throws Exception
     */
    public static Object readLine(String line, String previousLine) throws Exception {
        ArrayList<Token<?>> tokens = new ArrayList<>();
        Token<?> tContainer = new Token<>(null);

        String[] lines = line.split("(?<!\\*)!");
        if (lines.length > 1 && !lines[1].isEmpty()) {
            System.out.println("Multiple lines detected!");
            // multiple lines.
            for (int i = 0; i < lines.length; i++) {
                System.out.println("Reading line " + i + "...");
                tokens.addAll((ArrayList<Token<?>>) readLine(lines[i] + "!", i == 0 ? previousLine : ""));
            }
            return tokens;
        }

        line = previousLine + line;

        // #STRING# is a line which contains something that needs to be tokenized.

        if (line.startsWith("maak")) {
            boolean isString = false;
            // #maak varuiablename = " value"!#
            // #maak varuiablename = 49!#
            // #maak varuiablename = true!#
            int stringStart = line.indexOf("\"");
            int stringEnd = findEnclosingCharIndex(line, '"', '"');
            if (stringStart != -1 && stringEnd != -1) {
                isString = true;
                String encasedString = line.substring(stringStart + 1, stringEnd);
                line = line.substring(0, stringStart - 1) + EscapeSequence.unescape(encasedString) + "!";
                // #maak varuiablename = "*svalue"!#
            }
            line = line.trim();
            line = line.substring(4, line.length());
            // #varuiablename = *svalue!#
            // #varuiablename = 49!#
            // #varuiablename = true!#

            String[] parts = line.split("<-");
            // parts = ["varuiablename ", " *svalue!"]
            // parts = ["varuiablename ", " 49!"]
            // parts = ["varuiablename ", " true!"]
            if (parts.length != 2 || !parts[1].endsWith("!")) {
                throw new Exception("Invalid syntax!");
            }
            // remove ! from parts[2] (at the end of the line)
            parts[1] = parts[1].substring(0, parts[1].length() - 1).trim();
            parts[0] = parts[0].trim();
            // parts = ["varuiablename", "*svalue"]
            // parts = ["varuiablename", "49"]
            // parts = ["varuiablename", "true"]

            if (isString) {
                tokens.add(tContainer.new TStringVar(parts[0], EscapeSequence.unescape(parts[1])).toToken());
            } else {
                try {

                    tokens.add(tContainer.new TIntVar(parts[0], Integer.parseInt(parts[1])).toToken());
                } catch (NumberFormatException e) {
                    if (parts[1].equals("true") || parts[1].equals("false") || parts[1].equals("yebo")
                            || parts[1].equals("aowa")) {
                        parts[1] = parts[1].replace("yebo", "true").replace("aowa", "false");
                        tokens.add(tContainer.new TBooleanVar(parts[0], Boolean.parseBoolean(parts[1])).toToken());
                    } else {
                        throw new Errors.TokenizerSyntaxError("Invalid variable declaration!");
                    }
                    // tokens.add(tContainer.new TBooleanVar(parts[0],
                    // Boolean.parseBoolean(parts[1])).toToken());
                }
            }

        }

        return tokens;
        // if (!line.endsWith("!")) {
        // return line;
        // }
    }
}
