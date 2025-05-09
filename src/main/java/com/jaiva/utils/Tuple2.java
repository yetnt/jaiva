package com.jaiva.utils;

/**
 * A generic tuple class that holds two values of potentially different types.
 *
 * @param <A> the type of the first value in the tuple
 * @param <B> the type of the second value in the tuple
 */
public class Tuple2<A, B> {
    /**
     * The first value in the tuple.
     */
    public final A first;
    /**
     * The second value in the tuple.
     */
    public final B second;

    /**
     * Constructs a new tuple with the specified values.
     *
     * @param first  the first value in the tuple
     * @param second the second value in the tuple
     */
    public Tuple2(A first, B second) {
        this.first = first;
        this.second = second;
    }
}