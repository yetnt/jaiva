package com.jaiva.utils;

/**
 * A generic pair class that holds two values of the same type.
 *
 * @param <T> the type of the values in the pair
 */
public class Pair<T> extends Tuple2<T, T> {

    /**
     * Constructs a new pair with the specified values.
     *
     * @param first  the first value in the pair
     * @param second the second value in the pair
     */
    public Pair(T first, T second) {
        super(first, second);
    }
}
