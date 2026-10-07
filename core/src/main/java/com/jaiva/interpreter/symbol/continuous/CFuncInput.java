package com.jaiva.interpreter.symbol.continuous;

public enum CFuncInput {
    READ("read"),
    WRITE("write"),
    FLUSH("flush"), // only if the stream being closed is a OutputStream
    CLOSE("close");

    private final String key;
    private final char keyChar;
    private CFuncInput(String key) {
        this.key = key;
        this.keyChar = key.charAt(0);
    }

    public char getKeyChar() {
        return keyChar;
    }

    public String getKey() {
        return key;
    }

    public static CFuncInput fromChar(char c) {
        for (CFuncInput input : CFuncInput.values()) {
            if (input.keyChar == c) {
                return input;
            }
        }
        return null;
    }

    public static boolean isValid(String input) {
        for (CFuncInput input1 : CFuncInput.values()) {
            if (input.equals(input1.getKey()) || (input.charAt(0) == input1.getKeyChar())) {
                return true;
            }
        }
        return false;
    }

}
