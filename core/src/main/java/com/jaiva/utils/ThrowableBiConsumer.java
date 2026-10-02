package com.jaiva.utils;

@FunctionalInterface
public interface ThrowableBiConsumer<V, T,  E extends Throwable> {
    void accept(V v, T t) throws E;
}
