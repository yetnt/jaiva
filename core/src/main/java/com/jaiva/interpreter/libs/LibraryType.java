package com.jaiva.interpreter.libs;

import com.jaiva.interpreter.libs.global.Globals;
import com.jaiva.interpreter.libs.math.Math;
import com.jaiva.tokenizer.tokens.specific.TImport;
import com.jaiva.interpreter.libs.math.*;

/**
 * Library type describes the {@link BaseLibrary} instance where
 */
public enum LibraryType {
    /**
     * Lib is given to you anytime.
     */
    BUILTIN,
    /**
     * Lib here require importing
     */
    LIB
}
