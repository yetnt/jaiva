package com.jaiva.interpreter.runtime;

import java.nio.file.Path;

/**
 * The IConfig class provides configuration settings for the interpreter
 * runtime.
 * It allows customization of interpreter behavior through various flags and
 * options.
 *
 * <p>
 * Fields:
 * </p>
 * <ul>
 * <li><b>importVfs:</b> A boolean flag indicating whether the interpreter
 * should
 * import the virtual file system (VFS) from another file. When set to true, the
 * interpreter skips tokenizing other content and only imports exported
 * symbols.</li>
 * </ul>
 *
 * <p>
 * Usage:
 * </p>
 * 
 * <pre>
 * IConfig config = new IConfig();
 * config.importVfs = true; // Enable VFS import
 * </pre>
 */
public class IConfig {
    /**
     * This flag is used when the interpreter needs to import the vfs from another
     * file to use in the current file. (This means it will skip tokenizing other
     * stuff and only import exported symbols.)
     */
    public boolean importVfs = false;

    /**
     * * The {@code GlobalResources} instance provides access to global resources
     * used by the intepreter at run time.
     */
    public GlobalResources resources = new GlobalResources();
    /**
     * The path of the current file being interpreted.
     */
    public Path filePath;
    /**
     * The directory containing the file we're interpreting
     */
    public Path fileDirectory;

    // ...add more interpreter settings.

    public IConfig(String currentFilePath) {
        filePath = Path.of(currentFilePath != null ? currentFilePath : "");
        fileDirectory = Path.of(currentFilePath != null ? currentFilePath : "").getParent();
    }
}