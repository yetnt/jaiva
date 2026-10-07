package com.jaiva.utils;

@FunctionalInterface
public interface ThrowableSupplier<T, U extends Throwable> {
    T get() throws U;
}
