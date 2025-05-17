package com.jaiva.interpreter.globals;

import com.jaiva.interpreter.MapValue;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.BaseVariable;
import com.jaiva.tokenizer.Token;

public class IOFile extends BaseGlobals {
    IOFile(IConfig config) {
        super(GlobalType.LIB, "file");
        vfs.put("f_name", new MapValue(new VFileName(container, config)));
        vfs.put("f_dir", new MapValue(new VDirectory(container, config)));
        vfs.put("f_bin", new MapValue(new VBinaryDirectory(container, config)));
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
                    container.new TStringVar("f_name", config.filePath.getFileName().toString(), -1,
                            "Variable that holds the current file's name"),
                    config.filePath.getFileName().toString());
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
                    container.new TStringVar("f_dir", config.fileDirectory.toAbsolutePath().toString(), -1,
                            "Variable that holds the current file's directory."),
                    config.fileDirectory.toAbsolutePath().toString());
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
            super("f_bin", container.new TStringVar("f_bin", config.JAIVA_SRC.toAbsolutePath().toString(), 0,
                    "Variable that holds the directory where you can find jaiva.jar"),

                    config.JAIVA_SRC.toAbsolutePath().toString());
            freeze();
        }
    }
}
