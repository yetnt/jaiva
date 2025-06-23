package com.jaiva.tokenizer;

import com.jaiva.Config;

/**
 * TConfig class is a configuration class for the Jaiva tokenizer that holds the
 * path to the Jaiva source
 * code.
 * <p>
 * This class is used to store the path to the Jaiva source code, which is used
 * by the interpreter.
 * <p>
 * The path is stored in the {@link TConfig#JAIVA_SRC} field.
 */
public class TConfig extends Config {

    /**
     * The constructor for the TConfig class.
     * <p>
     * This constructor is used to set the path to the Jaiva source code.
     *
     * @param jaiva_src The path to the Jaiva source code.
     */
    public TConfig(String jaiva_src) {
        super(jaiva_src);
    }
}
