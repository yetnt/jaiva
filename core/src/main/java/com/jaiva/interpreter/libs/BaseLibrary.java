package com.jaiva.interpreter.libs;

import com.jaiva.Main;
import com.jaiva.errors.JaivaException;
import com.jaiva.interpreter.MapValue;
import com.jaiva.interpreter.Vfs;
import com.jaiva.interpreter.libs.annotation.Exports;
import com.jaiva.interpreter.libs.annotation.JaivaLibrary;
import com.jaiva.interpreter.libs.annotation.Library;
import com.jaiva.interpreter.libs.annotation.PublicLibrary;
import com.jaiva.interpreter.libs.global.Globals;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.Symbol;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Base class for global holder classes.
 */
public class BaseLibrary {

    private final ArrayList<Symbol> symbols = new ArrayList<>();

    /**
     * These are any symbols encountered via {@link #add(Symbol...)}.
     * This means this symbol is unique to this library. If this library's child class has
     * {@link Exports} annotation, then {@link #symbols} will hold more values from the exported symbols.
     */
    private final ArrayList<Symbol> uniqueSymbols = new ArrayList<>();

    /**
     * Default Constructor.
     */
    public BaseLibrary() {
    }

    public BaseLibrary(IConfig<Object> config) {
        // This is just for reflection purposes.
    }

    protected void add(Symbol ...syms) {
        for (Symbol symbol : syms) {
            symbols.add(symbol);
            uniqueSymbols.add(symbol);
        }
    }

    protected void add(ArrayList<Symbol> syms) {
        for (Symbol symbol : syms) {
            symbols.add(symbol);
            uniqueSymbols.add(symbol);
        }
    }

    protected void addFromExport(ArrayList<Symbol> syms) {
        symbols.addAll(syms);
    }
//
//    protected void add(String alias, Symbol symbol) {
//        symbols.add(symbol);
//        vfs.put(alias, symbol);
//        uniqueSymbols.add(symbol);
//    }

    public String toToolingJSON() {
        Vfs VFS = getVfs();
        StringBuilder string = new StringBuilder();
        string.append("{").append("\"version\":\"").append(Main.version).append("\",");
        string.append("\"tokens\":");
        string.append("[");
        VFS.forEach((name, vf) -> {
            Symbol symbol = (Symbol) ((MapValue) vf).getValue();
            try {
                string.append(symbol.token.toJson());
            } catch (JaivaException e) {
                throw new RuntimeException(e);
            }
            string.append(",");
        });
        string.deleteCharAt(string.length() - 1);
        string.append("]");
        string.append("}");
        return string.toString();
    }

    /**
     * Variable functions store
     */
    public Vfs getVfs() {
        Vfs vfs = new Vfs();
        symbols.forEach(vfs::putAsSymbolName);
        return vfs;
    }

    public Optional<String> getPath() {
        JaivaLibrary library = this.getClass().getAnnotation(JaivaLibrary.class);
        if (library != null) return Optional.of(library.path());

        PublicLibrary library2 = this.getClass().getAnnotation(PublicLibrary.class);
        if (library2 != null) return Optional.of(library2.path());

        return Optional.empty();
    }

    public ArrayList<Symbol> getUniqueSymbols() {
        return new ArrayList<>(uniqueSymbols);
    }

    protected static BaseLibrary instantiate(
            Class<?> clazz,
            IConfig<Object> config,
            Globals globals
    ) throws Exception {
        BaseLibrary libraryInstance;
        try {
            try {
                Constructor<? extends BaseLibrary> constructor = (Constructor<? extends BaseLibrary>) clazz.getConstructor(IConfig.class);
                constructor.setAccessible(true); // In case the constructor is not public
                libraryInstance = constructor.newInstance(config);
            } catch (Exception e) {
                try {
                    Constructor<? extends BaseLibrary> constructor = (Constructor<? extends BaseLibrary>) clazz.getConstructor();
                    constructor.setAccessible(true); // In case the constructor is not public
                    libraryInstance = constructor.newInstance();
                } catch (Exception ex) {
                    throw new RuntimeException("Failed to instantiate library: " + clazz.getName(), ex);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load library: " + clazz.getName(), e);
        }

        // check for Exports annotation

        Exports exportsAnnot = clazz.getAnnotation(Exports.class);
        if (exportsAnnot != null) {
            // get all the classes which matches the given export list
            ArrayList<Class<? extends BaseLibrary>> exportList = new ArrayList<>(List.of(exportsAnnot.value()));

            if (exportList.contains(clazz)) {
                throw new RuntimeException(clazz.getCanonicalName() + " attempts to export itself");
            }

            ArrayList<BaseLibrary> fromExportList = globals
                    .getAllClassLibraries()
                    .stream()
                    .filter(LibraryLike::hasClass)
                    .filter(libl -> exportList.contains(libl.getLibClass()))
                    .map(libl -> libl.loadClassLibrary(config, globals))
                    .collect(Collectors.toCollection(ArrayList::new));

            for (BaseLibrary baseLibrary : fromExportList) {
                libraryInstance.addFromExport(baseLibrary.getSymbols());
            }
        }

        return libraryInstance;
    }

    public ArrayList<Symbol> getSymbols() {
        return new ArrayList<>(symbols);
    }

    /**
     * Converts the contents of the vfs to a JSON array string.
     * Each entry in the vfs is expected to have a value containing a Symbol object,
     * whose token is serialized to JSON using its toJson() method.
     * The resulting JSON array contains the serialized tokens of all symbols in the
     * vfs.
     *
     * @return a JSON array string representing the tokens of all symbols in the vfs
     */
    public String toJson() {
        StringBuilder str = new StringBuilder();
        getVfs().forEach((key, value) -> {
            // Example: append key and value to the string builder
            Symbol sym = (Symbol) value.getValue();
            try {
                str.append(sym.token.toJson());
            } catch (JaivaException e) {
                // Handle the exception, e.g., log or append an error message
                throw new RuntimeException(e);
            }
            str.append(",");
        });
        // Remove trailing comma and space if needed
        return "[" + str.substring(0, str.toString().length() - 1) + "]";
    }

    /**
     * Uses reflection to get the public static String field named "path" from the given class.
     *
     * @param clazz the class to inspect
     * @return the value of the "path" field
     * @throws IllegalStateException if the field is not found, not public static String, or null/blank
     */
    public static String externalLibraryRequirements(Class<?> clazz) {
        boolean publicLibAnnotFound = false;
        boolean isPublic = false;
        for (Annotation annotation : clazz.getAnnotations()) {
            if (publicLibAnnotFound) break;
            Library j = annotation.annotationType().getAnnotation(Library.class);
            if (j != null) {
                publicLibAnnotFound = true;
                isPublic = j.libType() == LibraryType.LIB;
            }
        }

        if (!publicLibAnnotFound)
            throw new RuntimeException(clazz.getCanonicalName() + " has no library annotation (use @PublicLibrary)");
        if (!isPublic)
            throw new IllegalStateException("External classes cannot have library annotations which aren't @PublicLibrary!");

        PublicLibrary library = clazz.getAnnotation(PublicLibrary.class);
        if (library != null) {
            return library.path();
        }
        throw new IllegalStateException(clazz.getName()
                + " must have the PublicLibrary annotation!");
    }
}
