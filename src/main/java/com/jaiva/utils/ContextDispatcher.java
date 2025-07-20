package com.jaiva.utils;

import com.jaiva.tokenizer.Token.TStatement;
import com.jaiva.tokenizer.Token;
import com.jaiva.lang.Chars;
import com.jaiva.lang.Keywords;

/**
 * This class is used to determine the context of a line of code in Jaiva.
 * It checks for various conditions such as whether the line is empty, contains
 * balanced parentheses, and whether it is a single brace or a statement.
 * <p>
 * The class uses bitwise operations to represent the context of the line in a
 * compact form.
 * <p>
 * If you dont get it. its life bro. Also there's a markdown table in this
 * folder
 * <p>
 * But to put it simply, here are example inputs and outputs:
 * <p>
 * <code> "a + b" -> TStatement </code>
 * <p>
 * <code> "a + b == c" -> TStatement </code>
 * <p>
 * <code> "c" -> processContext() </code>
 * <p>
 * <code> "func()" -> processContext() </code>
 * <p>
 * <code> "arr[]" -> processContext() </code>
 * <p>
 * <code> "arr[]() + 3 == 10" -> TStatement </code>
 * <p>
 * 
 * And further more, this class handles also more complex cases.
 * 
 */
public class ContextDispatcher {
    private final String line;
    private boolean EIB = false;
    private boolean SE = false;
    private boolean EB = false;
    private boolean EO = false;
    private boolean BC = false;
    /**
     * The bits variable is a 64-bit number that represents the context of the line.
     * Refer to the table in the same directory for the meaning of each bit.
     */
    public int bits = 0; // 64 bit number. Refer to table.

    /**
     * Checks if the parentheses in the given string are balanced.
     * This is used to determine if the braces are closed before the operator.
     * <p>
     * A string is considered balanced if every opening parenthesis has a matching
     * closing parenthesis and they are properly nested.
     *
     * @param s The string to check for balanced parentheses.
     * @return true if the parentheses are balanced, false otherwise.
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
     * Checks if the braces are closed before the outermost operator in the given
     * line.
     * <p>
     * This is used to determine if the braces are closed before the operator.
     * <p>
     *
     * @param line The line to check for braces closed before the outermost
     *             operator.
     * @return true if the braces are closed before the outermost operator, false
     *         otherwise.
     */
    private boolean checkBracesClosedBeforeOutermostOperand(String line) {
        int outerOpIndex = Find.leastImportantOperator(line).index;
        // int outerOpIndex = Find.outermostOperatorIndex(line);
        // If there's no outer operator, then we cannot say the braces are “closed
        // before the operator.”
        if (outerOpIndex == -1)
            return false;
        // Return true if the substring from the start to the outer operator is balanced
        return isBalanced(line.substring(0, outerOpIndex));
    }

    /**
     * Constructor for ContextDispatcher.
     * 
     * @param line The line to check against.
     */
    public ContextDispatcher(String line) {
        this.line = line;
        // public boolean SB = false;
        line = line.trim();
        // Extra special case, encased in quotes, so go to processContext
        if (line.startsWith("\"") && line.endsWith("\"") && Find.quotationPairs(line).size() == 1) {
            bits = 0b10001;
            return;
        }
        if (line.isEmpty()) {
            SE = true;
            bits |= 0b10000;
            return;
        }
        int opIndex = Find.operatorIndex(line.toCharArray()) != -1
                ? Find.operatorIndex(line.toCharArray())
                : Integer.MAX_VALUE; // this is so EB can be handled properly.
        EO = Validate.containsOperator(line.toCharArray()) != -1;
        EB = (line.contains("(")) && ((opIndex > line.indexOf("("))) ||
                (line.contains(")")) && (opIndex > line.indexOf(")")) ||
                (line.contains("[")) && ((opIndex > line.indexOf("["))) ||
                (line.contains("]")) && (opIndex > line.indexOf("]"));
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

        if (bits == 0b1001 && !(line.equals(")") || line.equals("]")))
            bits |= 0b0010; // if mistaken for 0b1001 (single brace) and its not a single brace, its a clean
                            // function call
                            // so bump brac es closed bit

        // hard coded value. If we find => in the outermost pair of braces, it's 100% a
        // ternary
        // Only do this if it contains both the characters and the keyword
        if (line.contains(Chars.TERNARY) && line.contains(Keywords.TERNARY)) {
            // go thru the whole line, make sure the ternary is not in some nested shi
            int count = 0;
            for (int i = 0; i < line.length(); i++) {
                char next = i != line.length() - 1 ? line.charAt(i + 1) : 0;
                char c = line.charAt(i);
                // char previous = i != 0 ? line.charAt(i - 1) : 0;
                if (c == '(' || c == '[')
                    count++;
                if (c == ')' || c == ']')
                    count--;
                if (count == 0) {
                    if (c == Chars.TERNARY.charAt(0) && next == Chars.TERNARY.charAt(1)) {
                        bits = 0b10010;
                        break;
                    }
                }
            }
        }
    }

    /**
     * Returns the bits as a string of 0s and 1s.
     * <p>
     * The bits are represented as a 5-bit binary string, with leading zeros added
     * if necessary.
     *
     * @return the bits as a string of 0s and 1s.
     */
    public String toBitString() {
        return String.format("%5s", Integer.toBinaryString(bits)).replace(' ', '0');
    }

    /**
     * Returns the bits as the string of the output that should be used, as stated
     * in the table.
     * 
     * @return string representing the case to use.
     */
    public String printCase() {
        return switch (bits) {
            case 6, 7, 12, 14, 15 -> "TStatement";
            case 0, 11, 13, 17, 18 -> "processContext";
            case 9, 8 -> "single brace";
            case 16 -> "empty string";
            default -> "ERROR";
        };

    }

    /**
     * enum to represent the possible delegation targets and also error cases.
     * <p>
     * This enum is only available via ContextDispatcher class.
     */
    public enum To {
        /**
         * The statement parsed should be handled by {@link TStatement}
         */
        TSTATEMENT,
        /**
         * The statement parsed should be handled by {@link Token#processContext}
         */
        PROCESS_CONTENT,
        /**
         * The statement is a singular brace
         */
        SINGLE_BRACE,
        /**
         * The statement is empty.
         */
        EMPTY_STRING,
        /**
         * The statement just doesnt work
         */
        ERROR

    }

    /**
     * Returns the delegation target based on the bits.
     * <p>
     * The method uses a switch statement to determine the delegation target based
     * on
     * the bits.
     *
     * @return the delegation target as a To enum value.
     */
    public To getDeligation() {
        return switch (bits) {
            case 6, 7, 12, 14, 15 -> To.TSTATEMENT;
            case 0, 11, 13, 17, 18 -> To.PROCESS_CONTENT;
            case 9, 8 -> To.SINGLE_BRACE;
            case 16 -> To.EMPTY_STRING;
            default -> To.ERROR;
        };
    }

    @Override
    public String toString() {
        return "ContextDispatcher [line=" + line + ", EIB=" + EIB + ", SE=" + SE + ", EB=" + EB + ", EO=" + EO + ", BC="
                + BC + ", bits=" + bits + ", printCase()=" + printCase() + ", getDeligation()=" + getDeligation() + "]";
    }

}