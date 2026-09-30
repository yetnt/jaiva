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
import com.jaiva.interpreter.symbol.SymbolType;
import com.yetnt.utils.collection.HashMultiMap;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * A library. im not documenting bro this has so many shits
 */
public class BaseLibrary {

    // this base library was imported from something else
    private ImportChain importChain;

    // Promise that this vbase library will import something else
    private final ArrayList<ImportPromise> importPromises = new ArrayList<>();

    private final ArrayList<LibrarySymbol> symbols = new ArrayList<>();

    /**
     * Default Constructor.
     */
    public BaseLibrary() {
    }

    public BaseLibrary(IConfig<Object> config) {
        // This is just for reflection purposes.
    }

    private void addToImportChain(ImportChain existing, Class<? extends BaseLibrary> incident) {
        importChain = new ImportChain(incident, importChain == null ? Optional.empty() : Optional.of(existing));
    }

    private void addImportPromise(ImportPromise importPromise/*, ImportChain importChain*/) {
        importPromises.add(importPromise);
    }

    protected void add(Symbol ...syms) {
        for (Symbol symbol : syms) {
            LibrarySymbol ls = new LibrarySymbol(symbol);
            symbols.add(ls);
        }
    }

    protected void add(BaseLibrary bis) {
        for (LibrarySymbol ls : bis.getSymbols()) {
            symbols.add(ls);
        }
    }

//    protected void addFromExport(ArrayList<LibrarySymbol> syms) {
//        symbols.addAll(syms);
//    }

    protected void addWithAliases(Symbol symbol, String... aliases) {
        LibrarySymbol ls = new LibrarySymbol(symbol, aliases);
        symbols.add(ls);
    }

    /**
     * Variable functions store
     */
    public Vfs getVfs(IConfig<Object> config, Globals globals) {
        Vfs vfs = new Vfs();
        symbols.forEach(vfs::putAsSymbolName);
        if (importPromises.isEmpty()) {
            return vfs;
        }

        HashMultiMap<Class<? extends BaseLibrary>, ImportPromise> exportClaims = new HashMultiMap<>();

        exportClaims.put(this.getClass(), importPromises);

        for (ImportPromise importPromise : importPromises) {
            // Get the promised import class to import
            Class<? extends BaseLibrary> toImport = importPromise.classToImportFrom();
            // check this current if the lib is in there
            if (importChain != null && importChain.getClassesInChain().contains(toImport))
                throw new RuntimeException(
                        "Circular dependency caught within " + toImport.getSimpleName() + " and " + this.getClass().getSimpleName()
                );

            BaseLibrary lib;
            try {
                lib =  instantiate(toImport, config, globals);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            ArrayList<ImportPromise> libImportPromises = lib.getImportPromises();
            AtomicReference<HashSet<Class<? extends BaseLibrary>>> intersected = new AtomicReference<>();

            Class<? extends BaseLibrary> alreadyExported = exportClaims.findFirstKeyWhere(
                    (ArrayList<ImportPromise> values) -> {
                        HashSet<Class<? extends BaseLibrary>> i
                                = ImportPromise.intersect(this.importPromises, libImportPromises);
                        if (i.isEmpty()) return false;
                        intersected.set(i);
                        return true;
                    }
            );

            if (alreadyExported != null) {
                HashSet<Class<? extends BaseLibrary>> interseciton = intersected.get();
                throw new RuntimeException(
                        toImport.getSimpleName() + " attempted to export " +  interseciton.toString()
                        + " however it has already been exported by " + alreadyExported.getSimpleName()
                );
            }

            exportClaims.put(toImport, libImportPromises);

            lib.addToImportChain(importChain, this.getClass());
            // otherwise, get the vfs
            vfs.putAll(lib.getVfs(config, globals));
        }

        return vfs;
    }

    protected static BaseLibrary instantiate(
            Class<?> clazz,
            IConfig<Object> config,
            Globals globals
//            ,
//            ClassLoader cl
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

            ArrayList<Class<? extends BaseLibrary>> fromExportList = globals
                    .getAllClassLibraries()
                    .stream()
                    .filter(LibraryLike::hasClass)
                    .map(LibraryLike::getLibClass)
                    .filter(exportList::contains)
                    .collect(Collectors.toCollection(ArrayList::new));

            for (Class<? extends BaseLibrary> importFrom : fromExportList) {
                libraryInstance.addImportPromise(new ImportPromise(importFrom));
            }
        }

        return libraryInstance;
    }

    public String toToolingJSON(IConfig<Object> config, Globals globals) {
        Vfs VFS = getVfs(config, globals);
        StringBuilder string = new StringBuilder();
        string.append("{").append("\"version\":\"").append(Main.version).append("\",");
        string.append("\"tokens\":");
        string.append("[");
        HashMap<Symbol, String> jsonMap = new HashMap<>();
        VFS.forEach((name, vf) -> {
            Symbol symbol = (Symbol) ((MapValue) vf).getValue();
            try {
                if (jsonMap.containsKey(symbol)) {
                    String symJson = jsonMap.get(symbol);
                    if (symbol.symbolType == SymbolType.FUNCTION) {
                        // TFunction token adds F~ syntax to name
                        symJson = symJson.replace("\"name\": \""+symbol.token.name+"\"", "\"name\": \"F~"+name+"\"");
                    } else {
                        symJson = symJson.replace("\"name\": \""+symbol.token.name+"\"", "\"name\": \""+name+"\"");
                    }
                    string.append(symJson);
                }
                else {
                    String json = symbol.token.toJson();
                    string.append(json);
                    jsonMap.put(symbol, json);
                }
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

    public Optional<String> getPath() {
        JaivaLibrary library = this.getClass().getAnnotation(JaivaLibrary.class);
        if (library != null) return Optional.of(library.path());

        PublicLibrary library2 = this.getClass().getAnnotation(PublicLibrary.class);
        if (library2 != null) return Optional.of(library2.path());

        return Optional.empty();
    }

    public ArrayList<LibrarySymbol> getSymbols() {
        return new ArrayList<>(symbols);
    }

    public ArrayList<ImportPromise> getImportPromises() {
        return importPromises;
    }

//    /**
//     * Converts the contents of the vfs to a JSON array string.
//     * Each entry in the vfs is expected to have a value containing a Symbol object,
//     * whose token is serialized to JSON using its toJson() method.
//     * The resulting JSON array contains the serialized tokens of all symbols in the
//     * vfs.
//     *
//     * @return a JSON array string representing the tokens of all symbols in the vfs
//     */
//    public String toJson(IConfig<Object> config, Globals globals) {
//        StringBuilder str = new StringBuilder();
//        getVfs(config, globals).forEach((key, value) -> {
//            // Example: append key and value to the string builder
//            Symbol sym = (Symbol) value.getValue();
//            try {
//                str.append(sym.token.toJson());
//            } catch (JaivaException e) {
//                // Handle the exception, e.g., log or append an error message
//                throw new RuntimeException(e);
//            }
//            str.append(",");
//        });
//        // Remove trailing comma and space if needed
//        return "[" + str.substring(0, str.toString().length() - 1) + "]";
//    }

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
