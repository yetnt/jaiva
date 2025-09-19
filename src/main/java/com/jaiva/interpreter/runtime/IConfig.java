package com.jaiva.interpreter.runtime;

import java.nio.file.Path;
import java.util.ArrayList;

import com.jaiva.Config;
import com.jaiva.Main;

/**
 * The IConfig class provides configuration settings for the interpreter
 * runtime.
 * It allows customization of interpreter behavior through various flags and
 * options.
 */
public class IConfig<T extends Object> extends Config {

    public final T object;
    /**
     * The debug controller instance used to manage debugging features.
     */
    public DebugController dc = new DebugController();
    /**
     * This flag is used to print stack traces when an error occurs during
     * interpretation.
     */
    public boolean printStacks = false;
    /**
     * The command-line arguments passed to the Jaiva tokenizer and interpreter.
     * This array is used to tell the user what arguments were passed to the current
     * file.
     */
    public String[] args = new String[] {};
    /**
     * The sanitised arguments list stores the command-line arguments without
     * arguments used by jaiva. It removes the first argument (File path) and
     * possibly second argument (which is sometimes the debug flag).
     * This is useful for processing the arguments in a more user-friendly way.
     */
    public ArrayList<String> sanitisedArgs = new ArrayList<>();
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
    public Path JAIVA_SRC_PATH;

    // ...add more interpreter settings.

    /**
     * Constructs a new IConfig instance with the specified file path.
     * <p>
     * This constructor is used to set the path of the current file being
     * interpreted.
     *
     * @param args            The command-line arguments passed to the jaiva
     *                        command.
     * @param currentFilePath The path of the current file being interpreted.
     * @param jSrc            The path to the Jaiva source directory.
     * @param customObject A given custom object to sve into IConfig
     * @throws NullPointerException if {@code currentFilePath} or {@code jSrc} is
     */
    public IConfig(String[] args, String currentFilePath, String jSrc, T customObject) {
        super(jSrc);
        object = customObject;
        this.args = args;
        for (String arg : args) {
            if (arg.equals("-is") || arg.equals("--include-stacks"))
                printStacks = !printStacks;
            if (arg.equals("-d") || arg.equals("--debug")) {
                printStacks = !printStacks;
                dc.activate();
            }
            if (!arg.equals(currentFilePath) && !Main.tokenArgs.contains(arg)
            /* && !Main.replArgs.contains(arg) */) {
                // because if this overload is invoked, we're running a file, so we dont need to
                // check for REPL args.
                sanitisedArgs.add(arg);
            }
        }
        filePath = Path.of(currentFilePath != null ? currentFilePath : "");
        fileDirectory = Path.of(currentFilePath != null ? currentFilePath : "").getParent();
        JAIVA_SRC_PATH = Path.of(jSrc);
    }

    /**
     * Constructs a new IConfig instance with the specified file path and Jaiva
     * source directory.
     * <p>
     * This constructor is used when the current file path and Jaiva source
     * directory
     * are provided as arguments. Primarily used by the interpreter when importing
     * other files into the current context. This is so that we can just pass the
     * sanitized args already.
     *
     * @param args            The sanitized command-line arguments passed to the
     *                        jaiva
     *                        command.
     * @param currentFilePath The path of the current file being interpreted.
     * @param jSrc            The path to the Jaiva source directory.
     * @param customObject The custom object.
     */
    public IConfig(ArrayList<String> args, String currentFilePath, String jSrc, T customObject) {
        this(args.toArray(new String[0]), currentFilePath, jSrc, customObject);
    }

    /**
     * Constructs a new IConfig instance with the specified Jaiva source directory.
     * <p>
     * This constructor is used when only the Jaiva source directory is provided,
     * typically in a REPL context.
     *
     * @param jSrc         The path to the Jaiva source directory.
     * @param customObject The custom object.
     */
    public IConfig(String jSrc, T customObject) {
        super(jSrc);
        object = customObject;
        JAIVA_SRC_PATH = Path.of(jSrc);
    }
}