package com.jaiva.interpreter.runtime;

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

    // ...add more interpreter settings.

    public IConfig() {
    }
}