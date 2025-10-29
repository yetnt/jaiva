package com.jaiva.interpreter.libs;

import com.jaiva.errors.JaivaException;
import com.jaiva.interpreter.Vfs;
import com.jaiva.interpreter.symbol.Symbol;
import com.jaiva.tokenizer.tokens.Token;

/**
 * Base class for global holder classes.
 */
public class BaseLibrary {
    /**
     * Token container to createFunction tokens
     */
    public final Token<?> container = new Token<>(null);

    /**
     * The type of the BaseGlobal container.
     */
    public LibraryType type;
    /**
     * The path if this container is defined as {@link LibraryType}.LIB
     */
    public String path = null;
    /**
     * Variable functions store
     */
    public Vfs vfs = new Vfs();

    /**
     * Default Constructor.
     * 
     * @param value The type of this Globals holder.
     */
    public BaseLibrary(LibraryType value) {
        type = value;
    }

    /**
     * Constructor for holder classes that need to be imported.
     * 
     * @param value The type of this Globals holder.
     * @param p     The "filename" (without the extension)
     */
    public BaseLibrary(LibraryType value, String p) {
        type = value;
        path = p;
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
}
