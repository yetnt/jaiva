package com.jaiva.md;

import com.jaiva.interpreter.Vfs;
import com.jaiva.interpreter.libs.BaseLibrary;

/**
 *
 * @param isLib Whether we're documenting a library or just a file
 * @param type The type.
 * @param vfs The VFS from the file/library
 * @param libClass The library class if applicable
 */
public record MarkDownProps(
        boolean isLib,
        Type type,
        Vfs vfs,
        Class<? extends BaseLibrary> libClass
) {
    public enum Type {
        FILE, CLASS
    }
}
