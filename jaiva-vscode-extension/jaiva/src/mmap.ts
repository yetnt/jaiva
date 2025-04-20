export class MultiMap<K, V> {
    private data: Map<K, V[]> = new Map();

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
}
