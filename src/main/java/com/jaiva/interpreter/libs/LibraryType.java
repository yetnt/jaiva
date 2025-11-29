package com.jaiva.interpreter.libs;

import com.jaiva.interpreter.libs.global.Globals;
import com.jaiva.interpreter.libs.math.Math;
import com.jaiva.tokenizer.tokens.specific.TImport;
import com.jaiva.interpreter.libs.math.*;

/**
 * Library type describes the {@link BaseLibrary} instance where {@link LibraryType#GLOBAL}
 * means it's given to you everywhere, however {@link LibraryType#LIB}
 * requires you to
 * import it via {@link TImport}. If {@link LibraryType#MAIN} then it is the {@link Globals}
 * class which is the main input.
 * If {@link LibraryType#CONTAINER} then it is a class which will it's vfs will be taken and
 * used in another class. This is how the {@link Constants} class relates to the
 * {@link Math} class.
 */
public enum LibraryType {
    /**
     * Container is any container class which is used to encapsulate vfs for
     * some other lib class.
     */
    CONTAINER,
    /**
     * Lib is given to you anytime.
     */
    GLOBAL,
    /**
     * Lib here require importing
     */
    LIB,
    /**
     * Reserved for {@link Globals}
     */
    MAIN
}
