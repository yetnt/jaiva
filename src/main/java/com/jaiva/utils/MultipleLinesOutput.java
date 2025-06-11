package com.jaiva.utils;

import java.util.Arrays;

import com.jaiva.tokenizer.Token;

/**
 * 
 * 
 * 
 * Class that represents that the tokenizer needs to call readline again for
 * some reason.
 * <p>
 * For {@code Find.closingCharIndexML()}
 * <ul>
 * <li>{@code this.startCount == this.endCount} : if their equal then the method
 * ahs found the closing brace and doesnt need to read another line.
 * <li>{@code this.startCount != this.endCount} : if their not equal, no closing
 * brace has been found yet and we should continue
 * <li>{@code this.startCount == this.endCount == -1}: if their both equal to
 * -1, something went wrong.
 * <li>{@code (this.startCount == this.endCount) && this.startCount < 0} : if
 * their lower than -1, this output isnt to be used for this method but rather
 * another method.
 * </ul>
 * 
 * Okay now to talk about the {@code type} field and the {@code args} field.
 * <p>
 * The {@code type} field is used to determine which block it is between an if
 * statement "if", a loop "colonize" or a function "kwenza" or a while loop
 * Keywords.WHILE.
 * <p>
 * The {@code args} field is used to store the arguments for the block.
 * <ul>
 * <li>For an if statement, the first argument is the condition. More blocks
 * will be handled later on in develpment
 * <li>For a loop, the first argument is the variable declaration, second is the
 * condition, third is the increment.
 * <li>For a function, the first argument is the function name, second is the
 * arguments.
 * <li>For a while loop, the first argument is the function name, second is the
 * arguments.
 * </ul>
 */
public class MultipleLinesOutput {
    /**
     * The start count of the line.
     */
    public int startCount = 0;
    /**
     * The end count of the line.
     */
    public int endCount = 0;
    /**
     * Boolean to check if the line is a comment or not.
     */
    public boolean isComment = false;
    /**
     * The previous line of code before the current line.
     */
    public String preLine = "";

    /**
     * The block type of the line. Either "kwenza" or "if" or "colonize".
     * <p>
     * "kwenza" is for function definitions, "if" is for if statements and
     * "colonize" is for loops.
     */
    public String b_type = ""; // either "kwenza" or "if" or "colonize"
    /**
     * The arguments for the block. It depends on the type of the block, however it
     * only needs to be 3 or less.
     */
    public String[] b_args = new String[3]; // the arguments for it.
    /**
     * The special argument for the block. This is used for else if chains, where
     * this is the original if statement.
     */
    public Token<?> specialArg = null; // these are for else if chains. This represents the original if

    /**
     * The line number of the line. This is used for error reporting.
     */
    public int lineNumber = -1;

    /**
     * The constructor for the MultipleLinesOutput class. (Specifically, for blocks
     * that contain code.)
     * <p>
     * This constructor is used for the {@code Find.closingCharIndexML()} method.
     * <p>
     * The {@code startCount} and {@code endCount} are used to determine if the
     * braces are closed or not.
     * <p>
     * The {@code preLine} is used to store the previous line of code before the
     * current line.
     * <p>
     * The {@code type} and {@code args} are used to determine which block it is
     * between an if statement "if", a loop "colonize" or a function "kwenza" or a
     * while loop Keywords.WHILE.
     *
     * @param start   The start count of the line.
     * @param end     The end count of the line.
     * @param pString The previous line of code before the current line.
     * @param t       The block type of the line. Either "kwenza" or "if" or
     *                "colonize".
     * @param a       The arguments for the block. It depends on the type of the
     *                block, however it only needs to be 3 or less.
     * @param sArg    The special argument for the block. This is used for else if
     *                chains, where this is the original if statement.
     * @param ln      The line number of the line. This is used for error reporting.
     */
    public MultipleLinesOutput(int start, int end, String pString, String t,
            String[] a, Token<?> sArg,
            int ln) {
        startCount = start;
        endCount = end;
        preLine = pString;
        b_type = t;
        b_args = a;
        specialArg = sArg;
        lineNumber = ln;
    }

    /**
     * The constructor for the MultipleLinesOutput class. (Specifically, for
     * multiple line
     * comments.)
     * <p>
     * This constructor is used for the {@code Find.closingCharIndexML()} method.
     * <p>
     * The {@code startCount} and {@code endCount} are used to determine if the
     * braces are closed or not.
     * <p>
     * The {@code isComment} is used to determine if the line is a comment or not.
     *
     * @param start          The start count of the line.
     * @param end            The end count of the line.
     * @param inBlockComment Boolean to check if the line is a comment or not.
     */
    public MultipleLinesOutput(int start, int end, boolean inBlockComment) {
        isComment = inBlockComment;
        startCount = start;
        endCount = end;
    }

    @Override
    public String toString() {
        return "MultipleLinesOutput{" +
                "startCount=" + startCount +
                ", endCount=" + endCount +
                ", isComment=" + isComment +
                ", preLine='" + preLine + '\'' +
                ", b_type='" + b_type + '\'' +
                ", b_args=" + (b_args != null ? String.join(", ", b_args) : "null") +
                ", specialArg=" + specialArg +
                ", lineNumber=" + lineNumber +
                '}';
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + startCount;
        result = prime * result + endCount;
        result = prime * result + (isComment ? 1231 : 1237);
        result = prime * result + ((preLine == null) ? 0 : preLine.hashCode());
        result = prime * result + ((b_type == null) ? 0 : b_type.hashCode());
        result = prime * result + Arrays.hashCode(b_args);
        result = prime * result + ((specialArg == null) ? 0 : specialArg.hashCode());
        result = prime * result + lineNumber;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MultipleLinesOutput other = (MultipleLinesOutput) obj;
        if (startCount != other.startCount)
            return false;
        if (endCount != other.endCount)
            return false;
        if (isComment != other.isComment)
            return false;
        if (preLine == null) {
            if (other.preLine != null)
                return false;
        } else if (!preLine.equals(other.preLine))
            return false;
        if (b_type == null) {
            if (other.b_type != null)
                return false;
        } else if (!b_type.equals(other.b_type))
            return false;
        if (!Arrays.equals(b_args, other.b_args))
            return false;
        if (specialArg == null) {
            if (other.specialArg != null)
                return false;
        } else if (!specialArg.equals(other.specialArg))
            return false;
        if (lineNumber != other.lineNumber)
            return false;
        return true;
    }
}