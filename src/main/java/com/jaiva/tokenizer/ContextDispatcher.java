package com.jaiva.tokenizer;

/**
 * The ContextDispatcher class is used to determine the context of a given line
 * <p>
 * If you dont get it. its life bro. Also there's a markdown table in this
 * folder
 */
public class ContextDispatcher {
    public boolean SE = false;
    // public boolean SB = false;
    public boolean EB = false;
    public boolean EO = false;
    public boolean BC = false;
    public boolean EIB = false;
    public int bits = 0; // 64 bit number. Refer to table.

    /**
     * Returns true if the string s has balanced parentheses.
     */
    private boolean isBalanced(String s) {
        int balance = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(' || c == '[') {
                balance++;
            } else if (c == ')' || c == ']') {
                balance--;
                if (balance < 0)
                    return false; // too many closing parentheses
            }
        }
        return balance == 0;
    }

    /**
     * Checks if a character is considered an operator.
     * Modify this method if you have a specific set of operator characters.
     */
    private boolean isOperator(char c) {
        // Define your operators here. For example:
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '|' || c == '>' || c == '<';
    }

    /**
     * Returns the index of the outermost operator (at top level, i.e. depth==0).
     * Returns -1 if there is no operator at the outer level.
     */
    private int findOutermostOperatorIndex(String s) {
        int depth = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(' || c == '[') {
                depth++;
            } else if (c == ')' || c == ']') {
                depth--;
            } else if (depth == 0 && isOperator(c)) {
                // We found an operator while at depth 0: that's our outermost operator.
                return i;
            }
        }
        return -1;
    }

    /**
     * Determines if the parentheses in the left-hand side of the OUTERMOST operator
     * are balanced.
     * This sets BC (Braces Closed) correctly.
     */
    private boolean checkBracesClosedBeforeOutermostOperand(String line) {
        int outerOpIndex = findOutermostOperatorIndex(line);
        // If there's no outer operator, then we cannot say the braces are “closed
        // before the operator.”
        if (outerOpIndex == -1)
            return false;
        // Return true if the substring from the start to the outer operator is balanced
        return isBalanced(line.substring(0, outerOpIndex));
    }

    public ContextDispatcher(String line) {
        line = line.trim();
        if (line.isEmpty()) {
            SE = true;
            bits |= 0b10000;
            return;
        }
        int opIndex = Lang.getOperatorIndex(line.toCharArray()) != -1 ? Lang.getOperatorIndex(line.toCharArray())
                : Integer.MAX_VALUE; // this is so EB can be handled properly.
        // SB = line.startsWith("(");
        EO = Lang.containsOperator(line.toCharArray()) != -1;
        EB = (line.indexOf("(") != -1) && ((opIndex > line.indexOf("("))) ||
                (line.indexOf(")") != -1) && (opIndex > line.indexOf(")")) ||
                (line.indexOf("[") != -1) && ((opIndex > line.indexOf("["))) ||
                (line.indexOf("]") != -1) && (opIndex > line.indexOf("]"));
        BC = checkBracesClosedBeforeOutermostOperand(line);

        EIB = line.charAt(line.length() - 1) == ')' || line.charAt(line.length() - 1) == ']';

        if (EB)
            bits |= 0b1000; // Add EB
        if (EO)
            bits |= 0b0100; // Add EO
        if (BC)
            bits |= 0b0010; // Add BC
        if (EIB)
            bits |= 0b0001; // Add EIB

        // // Add SB only if the other bits make 1101 or 1000
        // if (((bits == 0b1101) || (bits == 0b1000)) && SB) {
        // System.out.println("SB");
        // bits |= 0b10000;
        // } // Add SB as a fifth bit

        if (bits == 0b1001 && !(line.equals(")") || line.equals("]")))
            bits |= 0b0010; // if mistaken for 0b1001 (single brace) and its not a single brace, its a clean
                            // function call
                            // so bump brac es closed bit

    }

    public String toBitString() {
        return String.format("%5s", Integer.toBinaryString(bits)).replace(' ', '0');
    }

    public String printCase() {
        switch (bits) {
            case 6, 7, 12, 14, 15:
                return "TStatement";
            case 0, 11, 13:
                return "processContext";
            case 9, 8:
                return "single brace";
            case 16:
                return "empty string";
            default:
                return "ERROR";
        }
    }

    public enum To {
        TSTATEMENT, PROCESS_CONTENT, SINGLE_BRACE, EMPTY_STRING, ERROR
    }

    public To getDeligation() {
        switch (bits) {
            case 6, 7, 12, 14, 15:
                return To.TSTATEMENT;
            case 0, 11, 13:
                return To.PROCESS_CONTENT;
            case 9, 8:
                return To.SINGLE_BRACE;
            case 16:
                return To.EMPTY_STRING;
            default:
                return To.ERROR;
        }
    }
}