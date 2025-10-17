package com.jaiva.interpreter.libs;

import com.jaiva.interpreter.libs.math.Math;
import com.jaiva.tokenizer.Token.TImport;
import com.jaiva.interpreter.libs.math.*;

/**
 * Global type describes the {@link BaseGlobals} instance where {@link GlobalType#GLOBAL}
 * means it's given to you everywhere, however {@link GlobalType#LIB}
 * requires you to
 * import it via {@link TImport}. If {@link GlobalType#MAIN} then it is the {@link Globals}
 * class which is the main input.
 * If {@link GlobalType#CONTAINER} then it is a class which will it's vfs will be taken and
 * used in another class. This is how the {@link Constants} class relates to the
 * {@link Math} class.
 */
public enum GlobalType {
    /**
     * Container is any container class which is used to encapsulate globals for
     * some other global class.
     */
    CONTAINER,
    /**
     * Globals is given to you anytime.
     */
    GLOBAL,
    /**
     * Globals here require importing
     */
    LIB,
    /**
     * Reserved for {@link Globals}
     */
    MAIN
}
