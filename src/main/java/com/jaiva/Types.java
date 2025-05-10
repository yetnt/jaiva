package com.jaiva;

import com.jaiva.lang.Keywords;

/**
 * This is a class which will define Jaiva types.
 * <p>
 * This class does NOT define types which are already defined in Java, such as
 * string, int, double and boolean. This class's job is to make a class for
 * types which ar especial to Jaiva.
 * <p>
 * This is a container class.
 */
public class Types {
    /**
     * Type that represents nothingness. This class works directly with the value
     * defined in {@link Keywords#UNDEFINED}
     */
    public static class Idk {
        /**
         * Constructor to make a new Idk instance.
         */
        public Idk() {

        }

        @Override
        public String toString() {
            return Keywords.UNDEFINED;
        }
    }
}
