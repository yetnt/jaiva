package com.jaiva;

import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.tokenizer.TConfig;

/**
 * Configuration class that both the tokenizer and interpreter extend off to
 * have {@link IConfig} and {@link TConfig}
 */
public class Config {
    /**
     * The path to the Jaiva source code.
     */
    public String JAIVA_SRC;

    /**
     * Constructs a new {@code Config} instance with the specified JAIVA source
     * path.
     *
     * @param jSrc the source path for JAIVA configuration
     */
    public Config(String jSrc) {
        JAIVA_SRC = jSrc;
    }
}
