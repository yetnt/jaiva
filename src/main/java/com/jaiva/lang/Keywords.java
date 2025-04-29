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

    public static String WHILE = "nikhil";
    public static String D_VAR = "maak";
    public static String IF = "if";
    public static String ELSE = "mara";
    public static String FALSE = "aowa";
    public static String TRUE = "yebo";
    public static String D_FUNCTION = "kwenza";
    public static String RETURN = "khutla";
    public static String FOR = "colonize";
    public static String FOR_EACH = "with";
    public static String TRY = "zama zama";
    public static String CATCH = "chaai";
    public static String THROW = "cima";
    public static String LC_BREAK = "voetsek";
    public static String LC_CONTINUE = "nevermind";
    public static String UNDEFINED = "idk";
    public static ArrayList<String> IMPORT = new ArrayList<>(Arrays.asList("tsea", "tšea"));

    public static String[] all = { D_VAR, WHILE, IF, ELSE, FALSE, TRUE, D_FUNCTION, RETURN, FOR,
            TRY, CATCH, THROW, LC_BREAK, LC_CONTINUE, UNDEFINED, IMPORT.get(0), IMPORT.get(1) };

    public enum LoopControl {
        BREAK, CONTINUE
    }
}