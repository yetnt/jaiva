package com.jaiva.utils.generic;

/**
 * A generic pair class that holds two values of the same type.
 *
 * @param <T> the type of the values in the pair
 */
public class SamePair<T> extends Pair<T, T> {

    /**
     * Constructs a new pair with the specified values.
     *
     * @param first  the first value in the pair
     * @param second the second value in the pair
     */
    public SamePair(T first, T second) {
        super(first, second);
    }
}
