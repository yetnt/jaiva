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
    private LibraryLike(String libName) {
        this.lib = libName;
    }

    public static LibraryLike of(Class<? extends BaseLibrary> lib) {
        return new LibraryLike(lib);
    }

    public static LibraryLike of(String libName) {
        return new LibraryLike(libName);
    }

    public Vfs load(IConfig<Object> i) {
        if (lib instanceof Class<?> c) {
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
                return libraryInstance.vfs;
            } catch (Exception e) {
                throw new RuntimeException("Failed to load library: " + c.getName(), e);
            }
        } else if (lib instanceof String s) {
            try {
                ExternalLibraryLoader loader = new ExternalLibraryLoader();
                return loader.loadLibrary(s, i);
            } catch (Exception e) {
                throw new RuntimeException("Failed to load library: " + s, e);
            }
        } else {
            throw new IllegalStateException("Invalid library type: " + lib.getClass().getName());
        }
    }
}
