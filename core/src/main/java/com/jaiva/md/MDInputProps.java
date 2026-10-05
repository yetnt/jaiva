package com.jaiva.md;

import com.jaiva.interpreter.Vfs;
import com.jaiva.interpreter.libs.BaseLibrary;

/**
 * The input file or class we are documenting.
 * @param isLib Whether we're documenting an internal library or not.
 * @param type The type, its either a class (always internals) or a file (user file, or an internal jaiva lib thats itself a file)
 * @param vfs The VFS from the file/library (should always be non Null. In the case of baseLibrary this is {@link BaseLibrary#getUniqueVfs()})
 * @param libClass The library class if applicable (Nullable)
 */
public record MDInputProps(
        boolean isLib,
        Type type,
        Vfs vfs,
        BaseLibrary libClass
) {
    public enum Type {
        FILE, CLASS
    }
}
