package com.jaiva.utils.bucket;


/**
 * A utility class that acts as a container for a set of `Class` objects.
 * It provides a method to check if a given object is an instance of the
 * classes held within the bucket.
 */
public class Bucket {
    private Class<?>[] cls;
    public Bucket(Class<?> ...classes) {
        cls = classes;
    }
    
    public boolean shouldHold(Object t) {
        for (Class<?> c : cls) {
            if (c.isInstance(t)) return true;
        }
        return false;
    }
}
