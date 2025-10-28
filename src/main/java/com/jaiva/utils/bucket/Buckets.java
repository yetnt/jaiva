package com.jaiva.utils.bucket;

import java.util.ArrayList;

public abstract class Buckets {
    /**
     * Divides an {@code ArrayList} into multiple buckets based on the provided classes.
     * Each bucket will contain elements from the original list that are instances of the corresponding class.
     * The order of elements within each bucket is preserved from the original list.
     *
     * @param original The original {@code ArrayList} to be divided.
     * @param classes A variable number of {@code Class} objects, each representing a type
     *                for which a separate bucket will be created.
     * @return An {@code ArrayList} of {@code ArrayLists}, where each inner {@code ArrayList}
     *         is a bucket containing elements of a specific type from the original list.
     *         The order of buckets in the returned list corresponds to the order of {@code classes} provided.
     */
    public static <T> ArrayList<ArrayList<T>> of(ArrayList<T> original, Class<?> ...classes) {
        ArrayList<ArrayList<T>> buckets = new ArrayList<>(classes.length);
        for (Class<?> c : classes) {
            ArrayList<T> bucket = new ArrayList<>(original
                    .subList(0, original.size())
                            .stream()
                            .filter(c::isInstance)
                                    .toList());
            buckets.add(bucket);
        }
        return buckets;
    }

    /**
     * Divides an {@code ArrayList} into multiple buckets based on custom {@link Bucket} implementations.
     * Each bucket will contain elements from the original list that satisfy the condition defined by the
     * corresponding {@link Bucket}'s {@code shouldHold} method.
     * The order of elements within each bucket is preserved from the original list.
     *
     * @param original The original {@code ArrayList} to be divided.
     * @param buckets A variable number of {@link Bucket} objects, each defining a custom
     *                condition for elements to be placed into its respective bucket.
     * @return An {@code ArrayList} of {@code ArrayLists}, where each inner {@code ArrayList}
     *         is a bucket containing elements that satisfy the condition of the corresponding {@link Bucket}.
     *         The order of buckets in the returned list corresponds to the order of {@code buckets} provided.
     */
    public static <T> ArrayList<ArrayList<T>> of(ArrayList<T> original, Bucket ...buckets) {
        ArrayList<ArrayList<T>> bucketsList = new ArrayList<>(buckets.length);
        for (Bucket b : buckets) {
            ArrayList<T> bucket = new ArrayList<>(original
                    .subList(0, original.size())
                    .stream()
                    .filter(b::shouldHold)
                    .toList());
            bucketsList.add(bucket);
        }
        return bucketsList;
    }
}
