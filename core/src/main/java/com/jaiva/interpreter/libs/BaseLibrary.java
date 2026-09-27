package com.jaiva.interpreter.libs;

import com.jaiva.Main;
import com.jaiva.errors.JaivaException;
import com.jaiva.interpreter.MapValue;
import com.jaiva.interpreter.Vfs;
import com.jaiva.interpreter.libs.annotation.PublicLibrary;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.Symbol;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Base class for global holder classes.
 */
public class BaseLibrary {

    /**
     * The type of the BaseGlobal container.
     */
    public LibraryType type;

    /**
     * Variable functions store
     */
    public Vfs vfs = new Vfs();

    /**
     * Default Constructor.
     */
    public BaseLibrary() {
    }

    /**
     * Constructor for holder classes that need to be imported.
     *
     * @param p The "filename" (without the extension)
     */
    public BaseLibrary(String p) {
    }

    public BaseLibrary(IConfig<Object> config) {
        // This is just for reflection purposes.
        // Libraries should not use this constructor.
    }

    public static String toolingJSONof(BaseLibrary baseLibrary) {
        Vfs VFS = baseLibrary.vfs;
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
        vfs.forEach((key, value) -> {
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
    public static String requirePublicAnnotation(Class<?> clazz) {
            PublicLibrary library = clazz.getAnnotation(PublicLibrary.class);
            if (library != null) {
                return library.path();
            }
            throw new IllegalStateException(clazz.getName()
                    + " must have the PublicLibrary annotation!");

    }
}
