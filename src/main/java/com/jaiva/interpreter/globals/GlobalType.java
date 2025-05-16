package com.jaiva.interpreter.globals;

import com.jaiva.tokenizer.Token.TImport;

/**
 * Global type describes the {@link BaseGlobals} instance where {@link GLOBAL}
 * means it's given to you everywhere, however {@link LIB}
 * requires you to
 * import it via {@link TImport}. If {@link MAIN} then it is the {@link Globals}
 * class which is the main input.
 */
public enum GlobalType {
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
