package com.jaiva.interpreter.libs;

import com.jaiva.interpreter.Vfs;
import com.jaiva.interpreter.runtime.IConfig;

import java.lang.reflect.Constructor;

/**
 * A class that represents either a built-in library class or an external library by name.
 * It provides a method to hold a library reference to load only when needed. (lazy loading)
 * <p>
 *     This class can be instantiated with either a Class object representing a subclass of BaseLibrary
 *     for built-in libraries, or a String representing the name of an external library.
 * </p>
 */
public class LibraryLike {
    private final Object lib;
    private LibraryLike(Class<? extends BaseLibrary> lib) {
        this.lib = lib;
    }
    private LibraryLike(BaseLibrary lib) {
        this.lib = lib;
    }
    private LibraryLike(String libName) {
        this.lib = libName;
    }

    public static LibraryLike of(Class<? extends BaseLibrary> lib) {
        return new LibraryLike(lib);
    }

    public static LibraryLike of(BaseLibrary fuckYouReflection) {
        return new LibraryLike(fuckYouReflection);
    }

    public static LibraryLike of(String libName) {
        return new LibraryLike(libName);
    }

    public BaseLibrary loadClassLibrary(IConfig<Object> i) {
        if (lib instanceof BaseLibrary b) return b;
        if (!(lib instanceof Class<?> c)) throw new RuntimeException(
                "The object stored by this lazily initializer is not a class."
        );

        try {
            BaseLibrary libraryInstance;
            try {
                Constructor<? extends BaseLibrary> constructor = (Constructor<? extends BaseLibrary>) c.getConstructor(IConfig.class);
                constructor.setAccessible(true); // In case the constructor is not public
                libraryInstance = constructor.newInstance(i);
            } catch (Exception e) {
                try {
                    Constructor<? extends BaseLibrary> constructor = (Constructor<? extends BaseLibrary>) c.getConstructor();
                    constructor.setAccessible(true); // In case the constructor is not public
                    libraryInstance = constructor.newInstance();
                } catch (Exception ex) {
                    throw new RuntimeException("Failed to instantiate library: " + c.getName(), ex);
                }
            }
            return libraryInstance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load library: " + c.getName(), e);
        }
    }

    public Vfs load(IConfig<Object> i) {
        switch (lib) {
            case Class<?> ignored -> {
                return loadClassLibrary(i).vfs;
            }
            case BaseLibrary r -> {
                return r.vfs;
            }
            case String s -> {
                try {
                    ExternalLibraryLoader loader = new ExternalLibraryLoader();
                    return loader.loadLibrary(s, i);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to load library: " + s, e);
                }
            }
            default -> throw new IllegalStateException("Invalid library type: " + lib.getClass().getName());
        }
    }
}
