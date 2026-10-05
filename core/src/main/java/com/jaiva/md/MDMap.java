package com.jaiva.md;

import java.util.ArrayList;
import java.util.HashMap;

public class MDMap extends HashMap<MDMap.Key, ArrayList<String>> {
    public boolean hasKey(Key key) {
        return this.containsKey(key);
    }
    public enum Key {
        HEADER,
        TOC,
        EXPORTS,
        FUNCTIONS,
        VARIABLES,
        /**
         * if a file is not split into functions and variables, then it has only a symbols category.
         */
        SYMBOLS,
    }
}
