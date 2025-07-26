package com.jaiva.interpreter;

import com.jaiva.interpreter.symbol.Symbol;

import java.util.ArrayList;
import java.util.HashMap;

public class Vfs extends HashMap<String, MapValue> {
    /**
     * This is used to keep track of every Vfs instance created and copied. Why? idk.
     */
    private static final ArrayList<Vfs> all = new ArrayList<>();

    /**
     * Calls {@link HashMap#HashMap()} creating an empty hash map for vfs.
     */
    public Vfs() {
        super();
        all.add(this);
    }

    /**
     * Creates a new Vfs Object with the given HashMap.
     * @param vfs Given Vfs.
     */
    private Vfs(HashMap<String, MapValue> vfs) {
        this.putAll(vfs);
        all.add(this);
    }

    /**
     * Same as {@link HashMap#put(Object, Object)} however this method wraps the Symbol in a MapValue for you.
     * @param key The key
     * @param sym The symbol
     * @return The previous value associated with the key or null. See documentation for {@link HashMap#put(Object, Object)}
     */
    public MapValue put(String key, Symbol sym) {
        return super.put(key, new MapValue(sym));
    }

    /**
     * Return the list of all the vfs.
     * @return An arraylist of vfs
     */
    public static ArrayList<Vfs> getAll() {
        return all;
    }

    @Override
    public Vfs clone() {
        Vfs vfs = new Vfs((HashMap<String, MapValue>) super.clone());
        all.add(vfs);
        return vfs;
    }

    /**
     * Unlike {@link HashMap#clone()} which creates a shallow copy of itself and not the contents.
     * This goes through the trouble by doing that for you. (Creates a full copy of every {@link MapValue} and
     * {@link com.jaiva.interpreter.symbol.Symbol} that's in it.
     * @return An exact copy of this instance with every element being a copy.
     */
    public Vfs fullCopy() {
        HashMap<String, MapValue> vfs = new HashMap<>();
        super.forEach((s, mv) -> {
            try {
                vfs.put(s, new MapValue(mv.getValue().clone()));
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        });

        return new Vfs(vfs);
    }
}
