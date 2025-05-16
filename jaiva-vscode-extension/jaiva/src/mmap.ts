/**
 * A `MultiMap` is a specialized map-like data structure that allows multiple values
 * to be associated with a single key. It provides methods for adding, retrieving,
 * filtering, and removing key-value associations, as well as iterating over the entries.
 *
 * @typeParam K - The type of the keys in the map.
 * @typeParam V - The type of the values associated with each key.
 *
 * ### Example
 * ```typescript
 * const multiMap = new MultiMap<string, number>();
 * multiMap.add("a", 1, 2, 3);
 * multiMap.add("b", 4);
 * console.log(multiMap.get("a")); // Output: [1, 2, 3]
 * console.log(multiMap.has("b")); // Output: true
 * multiMap.remove("a", 2);
 * console.log(multiMap.get("a")); // Output: [1, 3]
 * ```
 */
export class MultiMap<K, V> {
    private data: Map<K, V[]> = new Map();

    /**
     * Filters the entries of the MultiMap based on a provided predicate function.
     *
     * @param predicate - A function that takes a key and its associated array of values,
     * and returns a boolean indicating whether the entry should be included in the result.
     *
     * @returns A new MultiMap containing only the entries that satisfy the predicate.
     */
    public filter(predicate: (key: K, value: V[]) => boolean): MultiMap<K, V> {
        let m: MultiMap<K, V> = new MultiMap();
        for (let entry of this.entries()) {
            if (predicate(entry[0], entry[1])) m.add(entry[0], ...entry[1]);
        }

        return m;
    }
    /**
     * Sets the values for the specified key, replacing any existing values.
     * If the key does not exist, it will be created with the provided values.
     *
     * @param key - The key to associate the values with.
     * @param values - An array of values to set for the key.
     */
    public set(key: K, ...values: V[]): void {
        this.data.set(key, values);
    }
    /**
     * Adds all entries from the provided `MultiMap` to the current map.
     *
     * @param map - The `MultiMap` containing the entries to be added. Each key-value pair
     *              in the provided map will be added to the current map.
     */
    public addAll(map: MultiMap<K, V>) {
        for (let entry of map.entries()) {
            this.add(entry[0], ...entry[1]);
        }
    }

    /**
     * Adds one or more values to the collection associated with the specified key.
     * If the key already exists, the values are appended to the existing collection.
     * If the key does not exist, a new collection is created with the provided values.
     *
     * @param key - The key to associate the values with.
     * @param values - One or more values to add to the collection.
     */
    public add(key: K, ...values: V[]): void {
        if (this.data.has(key)) {
            // '!' is safe here because we know the key exists.
            this.data.get(key)!.push(...values);
        } else {
            this.data.set(key, values);
        }
    }

    // Retrieves the array of values for a given key.
    public get(key: K): V[] {
        return this.data.get(key) || [];
    }

    // Checks whether a key exists.
    public has(key: K): boolean {
        return this.data.has(key);
    }

    // Removes a specific value for a given key.
    // If the value is the last one for that key, the key is removed entirely.
    public remove(key: K, value: V): boolean {
        const values = this.data.get(key);
        if (values) {
            const index = values.indexOf(value);
            if (index !== -1) {
                values.splice(index, 1);
                if (values.length === 0) {
                    this.data.delete(key);
                }
                return true;
            }
        }
        return false;
    }

    // Removes an entire key (and all its associated values) from the map.
    public delete(key: K): boolean {
        return this.data.delete(key);
    }

    // Clears all entries.
    public clear(): void {
        this.data.clear();
    }

    // Iterators for convenience.
    public keys(): IterableIterator<K> {
        return this.data.keys();
    }

    public values(): IterableIterator<V[]> {
        return this.data.values();
    }

    public entries(): IterableIterator<[K, V[]]> {
        return this.data.entries();
    }

    /**
     * Converts the map data into a JSON string representation.
     *
     * @param parseV - A callback function that takes an array of values (`V[]`)
     * and returns a string representation of those values.
     *
     * @returns A JSON string representing the map, where each key is mapped to
     * its corresponding array of values processed by the `parseV` function.
     */
    public toJson(parseV: (el: V[]) => string[]): string {
        let string = [];
        for (const [name, data] of this.entries()) {
            string.push(`"${name}":[${parseV(data).join(",")}]`);
        }

        return "{" + string.join(",") + "}";
    }

    /**
     * Creates a new `MultiMap` instance from a JSON-like object.
     *
     * @template K - The type of the keys in the `MultiMap`.
     * @template V - The type of the values in the `MultiMap`.
     *
     * @param data - An object where each key is a string and the value is an array of strings.
     * @param parseV - A function that takes a string representation of the value tokens
     *                 and returns an array of values of type `V`.
     *
     * @returns A `MultiMap` instance populated with the parsed keys and values.
     */
    static fromJson<K, V>(
        data: { [key: string]: string[] },
        parseV: (hTokens: string[]) => V[]
    ): MultiMap<K, V> {
        const m = new MultiMap<K, V>();
        for (const key in data) {
            m.set(key as unknown as K, ...parseV(data[key]));
        }
        return m;
    }

    /**
     * Removes entries with empty values from a given MultiMap and returns a new MultiMap instance.
     *
     * @template K - The type of the keys in the MultiMap.
     * @template V - The type of the values in the MultiMap.
     * @param mmap - The MultiMap instance to process.
     * @returns A new MultiMap instance containing only the entries with non-empty values.
     */
    static clearEmptyKeys<K, V>(mmap: MultiMap<K, V>): MultiMap<K, V> {
        const newMMap = new MultiMap<K, V>();
        for (const [name, value] of mmap.entries()) {
            if (value.length != 0) newMMap.add(name, ...value);
        }
        return newMMap;
    }
}
