package com.jaiva.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class CCol {
    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private CCol() {
    }

    /**
     * Applies ANSI escape codes to a given string to style its output in a console.
     * The styling can include text color, background color, and font styles (e.g., bold, italic).
     *
     * @param input  The string to be styled.
     * @param styles An array of {@link Style} enums (e.g., {@link TEXT}, {@link BG}, {@link FONT})
     *               to apply to the input string. If no styles are provided, the input string
     *               is returned as is.
     * @return The styled string with ANSI escape codes, followed by a reset code to ensure
     * subsequent console output is not affected.
     */
    public static String print(String input, Style... styles) {
        if (styles.length == 0) return input;
        String prefix = Arrays.stream(styles)
                .map(Style::getCode)
                .collect(Collectors.joining());
        return prefix + input + TEXT.RESET.getCode();
    }

    /**
     * Applies ANSI escape codes to a given string to style its output in a console.
     * Unlike {@link #print(String, Style...)}, this method does not append a reset code,
     * allowing subsequent output to inherit the applied styles.
     * @param input The string to be styled.
     * @param styles An array of {@link Style} enums to apply.
     * @return The styled string with ANSI escape codes.
     */
    public static String printInline(String input, Style... styles) {
        if (styles.length == 0) return input;
        String prefix = Arrays.stream(styles)
                .map(Style::getCode)
                .collect(Collectors.joining());
        return prefix + input;
    }

    /**
     * An interface representing a style that can be applied to console output.
     * Implementations of this interface provide the specific ANSI escape code for a style.
     */
    public interface Style {
        String getCode();
    }

    public enum FONT implements Style {
        BOLD("\033[1m"),
        UNDERLINE("\u001B[4m"),
        ITALIC("\033[3m");


        private final String code;

        FONT(String code) {
            this.code = code;
        }

        @Override
        public String getCode() {
            return code;
        }
    }

    public enum TEXT implements Style {
        RESET("\u001B[0m"),
        RED("\u001B[31m"),
        BLACK("\u001B[30m"),
        GREEN("\u001B[32m"),
        YELLOW("\u001B[33m"),
        BLUE("\u001B[34m"),
        PURPLE("\u001B[35m"),
        CYAN("\u001B[36m"),
        WHITE("\u001B[37m"),

        // Bright/High-Intensity
        BRIGHT_BLACK("\u001B[90m"),
        BRIGHT_RED("\u001B[91m"),
        BRIGHT_GREEN("\u001B[92m"),
        BRIGHT_YELLOW("\u001B[93m"),
        BRIGHT_BLUE("\u001B[94m"),
        BRIGHT_PURPLE("\u001B[95m"),
        BRIGHT_CYAN("\u001B[96m"),
        BRIGHT_WHITE("\u001B[97m");

        private final String code;

        TEXT(String code) {
            this.code = code;
        }

        @Override
        public String getCode() {
            return code;
        }
    }

    public enum BG implements Style {
        BLACK("\u001B[40m"),
        RED("\u001B[41m"),
        GREEN("\u001B[42m"),
        YELLOW("\u001B[43m"),
        BLUE("\u001B[44m"),
        PURPLE("\u001B[45m"),
        CYAN("\u001B[46m"),
        WHITE("\u001B[47m"),

        // Bright/High-Intensity
        BRIGHT_BLACK("\u001B[100m"),
        BRIGHT_RED("\u001B[101m"),
        BRIGHT_GREEN("\u001B[102m"),
        BRIGHT_YELLOW("\u001B[103m"),
        BRIGHT_BLUE("\u001B[104m"),
        BRIGHT_PURPLE("\u001B[105m"),
        BRIGHT_CYAN("\u001B[106m"),
        BRIGHT_WHITE("\u001B[107m");

        private final String code;

        BG(String code) {
            this.code = code;
        }

        @Override
        public String getCode() {
            return code;
        }
    }
}
