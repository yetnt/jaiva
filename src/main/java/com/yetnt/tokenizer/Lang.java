package com.yetnt.tokenizer;

import java.util.Set;

public class Lang {
    public static char COMMENT = '@';
    public static char COMMENT_OPEN = '{';
    public static char COMMENT_CLOSE = '}';
    public static String BLOCK_OPEN = "->";
    public static String BLOCK_CLOSE = "<~";
    public static String ASSIGNMENT = "<-";
    public static String ARRAY_ASSIGNMENT = "<-|";
    public static Set<String> ARITHMATIC_OPERATIONS = Set.of("+", "-", "*", "/", "=");
    public static Set<String> BOOLEAN_OPERATORS = Set.of("<", ">", "&", "|", "!", "&&", "||", ">=", "<=", "!=");

    public static int containsOperator(char[] string) {
        for (char c : string) {
            if (ARITHMATIC_OPERATIONS.contains(String.valueOf(c))) {
                return 1;
            }
            if (BOOLEAN_OPERATORS.contains(String.valueOf(c))) {
                return 0;
            }
        }
        return -1;
    }
}