package com.jaiva.interpreter.runtime;

import java.nio.file.Path;

/**
 * The IConfig class provides configuration settings for the interpreter
 * runtime.
 * It allows customization of interpreter behavior through various flags and
 * options.
 */
public class IConfig {
    /**
     * This flag is used when the interpreter needs to import the vfs from another
     * file to use in the current file. (This means it will skip tokenizing other
     * stuff and only import exported symbols.)
     */
    public boolean importVfs = false;

    /**
     * Boolean flag indicating we're in the REPL.
     */
    public boolean REPL = false;

    /**
     * * The {@code GlobalResources} instance provides access to global resources
     * used by the intepreter at run time.
     */
    public GlobalResources resources = new GlobalResources();
    /**
     * The path of the current file being interpreted.
     */
    public Path filePath = null;
    /**
     * The directory containing the file we're interpreting
     */
    public Path fileDirectory = null;

    /**
     * The path to the source directory for Jaiva scripts.
     * This variable should point to the root directory containing Jaiva source
     * files.
     */
    public Path JAIVA_SRC;

    // ...add more interpreter settings.

    /**
     * Constructs a new IConfig instance with the specified file path.
     * <p>
     * This constructor is used to set the path of the current file being
     * interpreted.
     *
     * @param currentFilePath The path of the current file being interpreted.
     */
    public IConfig(String currentFilePath, String jSrc) {
        filePath = Path.of(currentFilePath != null ? currentFilePath : "");
        fileDirectory = Path.of(currentFilePath != null ? currentFilePath : "").getParent();
        JAIVA_SRC = Path.of(jSrc);
    }

    public IConfig(String jSrc) {
        JAIVA_SRC = Path.of(jSrc);
    }
}