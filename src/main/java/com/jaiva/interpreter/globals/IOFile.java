package com.jaiva.interpreter.globals;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import com.jaiva.errors.InterpreterException;
import com.jaiva.errors.InterpreterException.*;
import com.jaiva.interpreter.ContextTrace;
import com.jaiva.interpreter.MapValue;
import com.jaiva.interpreter.Primitives;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.BaseFunction;
import com.jaiva.interpreter.symbol.BaseVariable;
import com.jaiva.tokenizer.Token;
import com.jaiva.tokenizer.Token.TArrayVar;
import com.jaiva.tokenizer.Token.TFuncCall;

public class IOFile extends BaseGlobals {
    IOFile(IConfig config) throws InterpreterException {
        super(GlobalType.LIB, "file");
        vfs.put("f_name", new MapValue(new VFileName(container, config)));
        vfs.put("f_dir", new MapValue(new VDirectory(container, config)));
        vfs.put("f_bin", new MapValue(new VBinaryDirectory(container, config)));
        vfs.put("f_this", new MapValue(new VThis(container, config)));
        vfs.put("f_file", new MapValue(new FFile(container, config)));
        vfs.put("f_new", new MapValue(new FNew(container, config)));
    }

    /**
     * Represents a variable holding the file name extracted from a file path.
     * <p>
     * This class extends {@link BaseVariable} and initializes the variable with the
     * file name
     * obtained from the provided {@link IConfig} object's file path. The variable
     * is named "f_name"
     * and is frozen upon creation to prevent further modification.
     * </p>
     *
     * @param container The {@link Token} container associated with this variable.
     * @param config    The {@link IConfig} instance containing the file path
     *                  information.
     */
    public class VFileName extends BaseVariable {
        public VFileName(Token<?> container, IConfig config) {
            super("f_name",
                    container.new TStringVar("f_name",
                            config.filePath == null ? "REPL" : config.filePath.getFileName().toString(), -1,
                            "Variable that holds the current file's name"),
                    config.filePath == null ? "REPL" : config.filePath.getFileName().toString());
            freeze();

        }
    }

    /**
     * Represents a variable that holds the current file's directory.
     * <p>
     * The {@code VDirectory} class extends {@link BaseVariable} and initializes
     * itself with the absolute path of the current file's directory, as specified
     * in the provided {@link IConfig} object. This variable is frozen upon
     * creation,
     * making it immutable.
     * </p>
     *
     * @param container The token container used to create the directory variable.
     * @param config    The configuration object containing the file directory path.
     */
    public class VDirectory extends BaseVariable {
        public VDirectory(Token<?> container, IConfig config) {
            super("f_dir",
                    container.new TStringVar("f_dir", config.fileDirectory == null ? "REPL"
                            : config.fileDirectory.toAbsolutePath().toString(), -1,
                            "Variable that holds the current file's directory."),
                    config.fileDirectory == null ? "REPL" : config.fileDirectory.toAbsolutePath().toString());
            freeze();
        }
    }

    /**
     * Represents a variable that holds the directory path where the Jaiva binary
     * (jaiva.jar) can be found.
     * <p>
     * This class extends {@link BaseVariable} and initializes the variable with the
     * absolute path
     * to the Jaiva source directory, as specified in the provided {@link IConfig}
     * instance.
     * The variable is named "f_bin" and is intended to be immutable after
     * construction.
     * </p>
     *
     * @param container The token container used for variable creation.
     * @param config    The configuration object providing the Jaiva source
     *                  directory path.
     */
    public class VBinaryDirectory extends BaseVariable {
        public VBinaryDirectory(Token<?> container, IConfig config) {
            super("f_bin", container.new TStringVar("f_bin", config.JAIVA_SRC_PATH.toAbsolutePath().toString(), 0,
                    "Variable that holds the directory where you can find jaiva.jar"),

                    config.JAIVA_SRC_PATH.toAbsolutePath().toString());
            freeze();
        }
    }

    /**
     * Represents a special variable that encapsulates information about the current
     * file context
     * within the interpreter. The structure of this variable is as follows:
     * 
     * <pre>
     * [
     *   "fileName",                // Name of the current file, or "REPL" if not in a file context
     *   "fileDir",                 // Directory of the current file, or void value if not in a file context
     *   [contents],                // List of lines in the file, or a default list in REPL mode
     *   [canRead?, canWrite?, canExecute?] // File permission flags, or defaults in REPL mode
     * ]
     * </pre>
     * <p>
     * If the interpreter is running in REPL mode (no file context), default values
     * are used.
     * Otherwise, the file's name, directory, contents, and permissions are
     * extracted and stored.
     * The variable is frozen after initialization to prevent further modification.
     */
    public class VThis extends BaseVariable {
        /**
         * Constructs a VThis object representing the current file's structure and
         * metadata.
         * <p>
         * The structure of the array assigned to this variable is as follows:
         * 
         * <pre>
         * [
         *   "fileName",                // Name of the file or "REPL" if not in a file context
         *   "fileDir",                 // Directory of the file or void value if not in a file context
         *   [contents],                // List of file contents (lines) or sample data in REPL
         *   [canRead?, canWrite?, canExecute?] // List of booleans indicating file permissions
         * ]
         * </pre>
         * 
         * If the interpreter is running in REPL mode (no file context), default values
         * are used.
         * Otherwise, the file's name, directory, contents, and permissions are
         * extracted and stored.
         *
         * @param container The token container for this variable.
         * @param config    The interpreter configuration, including file path and
         *                  directory.
         * @throws InterpreterException If the file does not exist or cannot be read.
         */
        public VThis(Token<?> container, IConfig config) throws InterpreterException {
            super("f_this", container.new TArrayVar("f_this", new ArrayList<>(), -1,
                    "Returns an array containing the current file's properties \\n [fileName, fileDir, [contents], [canRead?, canWrite?, canExecute?]]"),
                    new ArrayList<>());
            // create the array containign this current file's structure
            // if we're in the file.
            /*
             * [
             * "fileName",
             * "fileDir",
             * [contents],
             * [canRead?, canWrite?, canExecute?]
             * ]
             */
            if (config.filePath == null) {
                ((TArrayVar) this.token).contents.addAll(Arrays.asList(
                        "REPL",
                        Token.voidValue(-1),
                        new ArrayList<>(Arrays.asList("fweah!", "seeyuh")),
                        new ArrayList<>(Arrays.asList(false, true, false))));
                return;
            }

            ArrayList<Object> file = new ArrayList<>();
            File f = config.filePath.toFile();
            Scanner fs;
            try {
                fs = new Scanner(f);
            } catch (FileNotFoundException e) {
                throw new InterpreterException.CatchAllException(new ContextTrace(),
                        "Well, the current file doesnt exist??...", -1);
            }
            ArrayList<String> contents = new ArrayList<>();
            while (fs.hasNextLine())
                contents.add(fs.nextLine());
            fs.close();

            file.add(f.getName());
            file.add(config.fileDirectory);
            file.add(contents);
            file.add(new ArrayList<>(Arrays.asList(f.canRead(), f.canWrite(), f.canExecute())));

            ((TArrayVar) this.token).contents = file; // set the token.
            a_set(file, new ContextTrace()); // set the interpreter variable.

            freeze(); // freeze this variable.
        }
    }

    /**
     * FFile is a function that retrieves properties and contents of a specified
     * file.
     * <p>
     * Usage: f_file(path)
     * </p>
     * <ul>
     * <li>Checks if the provided path is a string and resolves it relative to the
     * current configuration's file path if necessary.</li>
     * <li>Throws an exception if the file does not exist.</li>
     * <li>Reads the file contents line by line into a list.</li>
     * <li>Returns an ArrayList of the file properties
     * </ul>
     */
    public class FFile extends BaseFunction {
        // function looks for the file and returns its properties in the structure.
        public FFile(Token<?> container, IConfig config) {
            super("f_file", container.new TFunction("f_file", new String[] { "path" }, null, -1,
                    "Returns an array containing the properties of the file at the given `path` \\n [fileName, fileDir, [contents], [canRead?, canWrite?, canExecute?]]"));
            freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs, IConfig config,
                ContextTrace cTrace)
                throws Exception {
            checkParams(tFuncCall, cTrace);
            Object path = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(0)), vfs, false, config,
                    cTrace);
            if (!(path instanceof String))
                throw new WtfAreYouDoingException(cTrace, "Da path must be a string.",
                        tFuncCall.lineNumber);

            Path baseDir = (config.filePath != null) ? config.filePath.getParent() : null;
            Path filePath;
            if (baseDir != null) {
                filePath = Paths.get((String) path);
                if (!filePath.isAbsolute()) {
                    filePath = baseDir.resolve((String) path).normalize();
                }
            } else {
                filePath = Paths.get((String) path);
            }

            File file = filePath.toFile();
            if (!file.exists())
                throw new WtfAreYouDoingException(new ContextTrace(), "File does not exist: " + filePath,
                        tFuncCall.lineNumber);

            ArrayList<String> contents = new ArrayList<>();
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    contents.add(scanner.nextLine());
                }
            } catch (FileNotFoundException e) {
                throw new CatchAllException(new ContextTrace(),
                        "Cannot read file but we found it... " + filePath, tFuncCall.lineNumber);
            }

            ArrayList<Object> result = new ArrayList<>();
            result.add(file.getName());
            result.add(file.getParentFile() != null ? file.getParentFile().getAbsolutePath()
                    : Token.voidValue(tFuncCall.lineNumber));
            result.add(contents);
            result.add(new ArrayList<>(Arrays.asList(file.canRead(), file.canWrite(), file.canExecute())));
            return result;

        }
    }

    /**
     * FNew is a function that creates a new file with specified content and
     * permissions.
     * <p>
     * Usage: f_new(path, content, canRead?, canWrite?, canExecute?)
     * </p>
     * <ul>
     * <li>Checks if the provided path is a string.</li>
     * <li>Creates the file at the specified path with the given content.</li>
     * <li>Sets the file permissions based on the provided boolean flags.</li>
     * <li>Returns true if the file was created successfully, false otherwise.</li>
     * </ul>
     */
    public class FNew extends BaseFunction {
        public FNew(Token<?> container, IConfig config) {
            /*
             * [
             * "fileName",
             * "fileDir",
             * [contents],
             * [canRead?, canWrite?, canExecute?]
             * ]
             */
            super("f_new", container.new TFunction("f_new",
                    new String[] { "path", "content", "canRead?", "canWrite?", "canExecute?" }, null, -1,
                    "Creates a new file at the given `path` with the specified `content` and permissions. \\n Returns boolean indicating success."));
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs, IConfig config,
                ContextTrace cTrace)
                throws Exception {
            // TODO Auto-generated method stub
            checkParams(tFuncCall, cTrace);
            Object path = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(0)), vfs, false, config,
                    cTrace);
            if (!(path instanceof String))
                throw new FunctionParametersException(cTrace, this, "1", params.get(0), String.class,
                        tFuncCall.lineNumber);

            Object content = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(1)), vfs, false, config,
                    cTrace);
            StringBuilder outBuilder = new StringBuilder();
            String output = content instanceof String ? (String) content : "";
            if (!(content instanceof String) && !(content instanceof ArrayList))
                throw new FunctionParametersException(cTrace, this, "2", params.get(1), String.class,
                        tFuncCall.lineNumber);
            if (content instanceof ArrayList list) {
                if (list.size() == 0)
                    output = "";
                // if the list has only one element, we can just use that as the content
                else if (list.size() == 1)
                    output = list.get(0).toString();
                else {
                    list.stream()
                            .filter(Objects::nonNull)
                            .map(Object::toString)
                            .forEach(s -> outBuilder.append(s).append("\n"));
                    output = outBuilder.toString();
                }
            }

            boolean canRead = true, canWrite = true, canExecute = true;
            if (params.size() > 2) {
                Object cr = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(2)), vfs, false, config,
                        cTrace);
                if (!(cr instanceof Boolean))
                    throw new FunctionParametersException(cTrace, this, "3", params.get(2), boolean.class,
                            tFuncCall.lineNumber);
                canRead = cr.equals(Boolean.TRUE);
            }
            if (params.size() > 3) {
                Object cw = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(3)), vfs, false, config,
                        cTrace);
                if (!(cw instanceof Boolean))
                    throw new FunctionParametersException(cTrace, this, "4", params.get(3), boolean.class,
                            tFuncCall.lineNumber);
                canWrite = cw.equals(Boolean.TRUE);
            }
            if (params.size() > 4) {
                Object ce = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(4)), vfs, false, config,
                        cTrace);
                if (!(ce instanceof Boolean))
                    throw new FunctionParametersException(cTrace, this, "5", params.get(4), boolean.class,
                            tFuncCall.lineNumber);
                canExecute = ce.equals(Boolean.TRUE);
            }
            Path baseDir = (config.filePath != null) ? config.filePath.getParent() : null;
            Path newFilePath = Paths.get((String) path);

            if (newFilePath.toFile().exists())
                return false; // file already exists.

            if (baseDir != null && !newFilePath.isAbsolute()) {
                newFilePath = baseDir.resolve((String) path).normalize();
            }
            try {
                Files.createDirectories(newFilePath.getParent()); // ensure parent directories exist
                Files.writeString(newFilePath, output, StandardOpenOption.CREATE_NEW);
                File newFile = newFilePath.toFile();
                newFile.setReadable(canRead);
                newFile.setWritable(canWrite);
                newFile.setExecutable(canExecute);
            } catch (IOException e) {
                return false; // failed to create file
            }
            return true; // file created successfully
            // return super.call(tFuncCall, params, vfs, config);
        }
    }
}
