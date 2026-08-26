package com.jaiva;

import com.jaiva.errors.JaivaException;
import com.jaiva.tokenizer.tokens.Token;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Streamer {
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private boolean run = true;

    public static String removeCCol(String rawMessage) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < rawMessage.length(); i++) {
            if (rawMessage.charAt(i) == '\u001B'
                    && i + 1 < rawMessage.length()
                    && rawMessage.charAt(i + 1) == '[') {

                i += 2;

                // Skip until the terminating 'm'
                while (i < rawMessage.length() && rawMessage.charAt(i) != 'm') {
                    i++;
                }

                continue;
            }

            result.append(rawMessage.charAt(i));
        }

        return result.toString();
    }

    Streamer() {
        try {
            while (run) {
                String line = reader.readLine();
                if (line == null || line.equals("EXIT") || line.isEmpty()) {
                    run = false;
                    System.out.println();
                    reader.close();
                } else {
                    // pray its just a file input
                    try {
                        ArrayList<Token<?>> tokens = Main.parseTokens(line, false);
                        System.out.print("[");
                        for (int i = 0; i < tokens.size(); i++) {
                            Token<?> token = tokens.get(i);
                            System.out.print(token.value().toJson());
                            if (i != tokens.size() - 1) {
                                System.out.print(",");
                            }
                        }
                        System.out.print("]");
                        System.out.println();
                    } catch (JaivaException e) {
                        System.out.println(toJsonError(e, ErrorType.JAIVA));
                        System.out.println();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(
                    toJsonError(e, ErrorType.OH_FUCK)
            );
            System.out.println();
            new Streamer(); // try again.
        }
    }

    enum ErrorType {
        JAIVA, OH_FUCK
    }

    private static String jsonEscape(String s) {
        return s
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    public String toJsonError(Exception e, ErrorType type) {
        return "{\"err\":\"" + jsonEscape(removeCCol(e.getMessage())) + "\", \"type\":\"" + type.toString() + "\"}";
    }
}
