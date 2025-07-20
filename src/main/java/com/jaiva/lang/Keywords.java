package com.jaiva.lang;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The Keywords class contains a set of predefined string constants
 * that represent various keywords used in JAIVA.
 * <p>
 * These keywords are mapped to specific string values.
 */
public class Keywords {
    /**
     * The keyword for the while loop.
     */
    public static String WHILE = "nikhil";
    /**
     * The keyword for variable declaration.
     */
    public static String D_VAR = "maak";
    /**
     * The keyword for an if statement.
     */
    public static String IF = "if";
    /**
     * The keyword for an else statement.
     */
    public static String ELSE = "mara";
    /**
     * The alternative keyword for {@code false}
     */
    public static String FALSE = "aowa";
    /**
     * The alternative keyword for {@code true}
     */
    public static String TRUE = "yebo";
    /**
     * The keyword for a function declaration.
     */
    public static String D_FUNCTION = "kwenza";
    /**
     * The keyword for a function return statement.
     */
    public static String RETURN = "khutla";
    /**
     * The keyword for a for loop.
     */
    public static String FOR = "colonize";
    /**
     * The keyword used in a for-each loop.
     */
    public static String FOR_EACH = "with";
    /**
     * The keyword for a try block.
     */
    public static String TRY = "zama zama";
    /**
     * The keyword for a catch block.
     */
    public static String CATCH = "chaai";
    /**
     * The keyword for throwing an error.
     */
    public static String THROW = "cima";
    /**
     * The keyword for breaking out of a loop.
     */
    public static String LC_BREAK = "voetsek";
    /**
     * The keyword for continuing to the next iteration of a loop.
     */
    public static String LC_CONTINUE = "nevermind";
    /**
     * The keyword for an undefined value.
     */
    public static String UNDEFINED = "idk";
    /**
     * The keyword for an import statement.
     */
    public static ArrayList<String> IMPORT = new ArrayList<>(Arrays.asList("tsea", "tšea"));

    /**
     * The keyword used to define the else section in a ternary.
     */
    public static String TERNARY = "however";

    /*
     * All keywords in JAIVA.
     */
    public static String[] all = { D_VAR, WHILE, IF, ELSE, FALSE, TRUE, D_FUNCTION, RETURN, FOR,
            TRY, CATCH, THROW, LC_BREAK, LC_CONTINUE, UNDEFINED, IMPORT.getFirst(), IMPORT.getLast() };

    /**
     * LoopControl is an enum that represents the control flow of a loop.
     */
    public enum LoopControl {
        /**
         * Represents a break statement in a loop.
         */
        BREAK,
        /**
         * Represents a continue statement in a loop.
         */
        CONTINUE
    }
}