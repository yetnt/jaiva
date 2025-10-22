package com.jaiva.interpreter;

import com.jaiva.interpreter.libs.BaseLibrary;
import com.jaiva.interpreter.symbol.Symbol;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * "vfs" stands for Variable FunctionS. It's a class that extends HashMap and in term inherits all it's properties.
 * <p>
 *     A vfs instance, holds all the symbols defined in the specific context that the user can use.
 * </p>
 * <p>
 *     The {@link String} is the identifier of a specific symbol in the given scope. Like a nickname, it is not
 *     the symbol's actual name, but what the user has to refer to in order to call the function/use the variable.
 *     e.g.: The symbol named {@code "function"} could have the identifier {@code "foo"}. Calling {@code foo()} will call
 *     the said {@code "function"}. However many times (unless frozen, see {@link Symbol#isFrozen}) the identifier is changed,
 *     it still references the exact same function that it was defined as.
 * </p>
 * <p>
 *     A vfs instance, is usually created only by {@link Scope} as all symbols are scoped constructs.
 *     The only "exceptions" (haha get it), would be {@link BaseLibrary} which creates
 *     a vfs instance to put all the globals into.
 * </p>
 * <p>
 * So much documentation, might as well call this an accumulation. HMM, bars.
 */
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
