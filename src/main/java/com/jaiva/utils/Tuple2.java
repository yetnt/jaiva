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

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }

    /**
     * Returns a string representation of this Tuple2 object.
     * The string includes the values of the first and second elements.
     *
     * @return a string in the format "Tuple2 [first=..., second=...]"
     */
    @Override
    public String toString() {
        return "Tuple2 [first=" + first + ", second=" + second + "]";
    }

    /**
     * Computes the hash code for this Tuple2 object.
     * The hash code is calculated based on the hash codes of the {@code first} and
     * {@code second} elements.
     * If either element is {@code null}, its hash code is considered as 0.
     *
     * @return the computed hash code value for this object
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((first == null) ? 0 : first.hashCode());
        result = prime * result + ((second == null) ? 0 : second.hashCode());
        return result;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * Two {@code Tuple2} objects are considered equal if both their {@code first}
     * and {@code second}
     * elements are equal (or both are null).
     *
     * @param obj the reference object with which to compare
     * @return {@code true} if this object is the same as the obj argument;
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Tuple2 other = (Tuple2) obj;
        if (first == null) {
            if (other.first != null)
                return false;
        } else if (!first.equals(other.first))
            return false;
        if (second == null) {
            if (other.second != null)
                return false;
        } else return second.equals(other.second);
        return true;
    }
}