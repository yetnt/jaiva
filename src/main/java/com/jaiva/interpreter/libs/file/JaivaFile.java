package com.jaiva.interpreter.libs.file;

import com.jaiva.errors.InterpreterException;
import com.jaiva.interpreter.Primitives;
import com.jaiva.interpreter.Scope;
import com.jaiva.interpreter.libs.BaseLibrary;
import com.jaiva.interpreter.libs.LibraryType;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.BaseFunction;
import com.jaiva.tokenizer.jdoc.JDoc;
import com.jaiva.tokenizer.tokens.specific.TFuncCall;
import com.jaiva.tokenizer.tokens.specific.TFunction;

import java.util.ArrayList;

/**
 * Holds helper functions (that are part of the "file" library) that operate on FileType instances or likewise.
 * <p>
 * It is used to group related functions together under the "file" library namespace.
 * It isn't directly related to FileType instances but provides functionality for them (within a jaiva script).
 * </p>
 * <p>
 * Although the functions that live here operate on FileType instances, this should not be known
 * to the jaiva script user. They only see files as arrays with a specific structure.
 * This class simply provides a way to organize these functions within the library system.
 * </p>
 */
public class JaivaFile extends BaseLibrary {
    public JaivaFile(IConfig<Object> config) {
        super(LibraryType.CONTAINER);

        vfs.put("f_nameOf", new FNameOf());
        vfs.put("f_dirOf", new FDirOf());
        vfs.put("f_contentOf", new FContentOf());
        vfs.put("f_permsOf", new FPermsOf());
    }

    public static class FNameOf extends BaseFunction {
        public FNameOf() {
            super("f_nameOf", new TFunction(
                    "f_nameOf", new String[]{"file"}, null, -1,
                    JDoc.builder()
                            .addDesc("Gets the name of the file from a file.")
                            .addParam("file", "[]", "The file array to get the name from.", false)
                            .addReturns("The name of the file as a string.")
                            .sinceVersion("5.0.0")
                            .addExample("""
                                    khuluma(f_nameOf(f_this))! @ Prints the name of the current file.
                                    """)
                            .build()
            ));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config, Scope scope) throws Exception {
            checkParams(tFuncCall, scope); // Ensure correct number of parameters
            Object fileObj = Primitives.toPrimitive(params.getFirst(), false, config, scope);
            if (!(fileObj instanceof ArrayList<?> arr))
                throw new InterpreterException.WtfAreYouDoingException(scope,
                        "The parameter 'file' needs to be an array like come on!", tFuncCall.lineNumber);
            FileType file = FileType.of((ArrayList<Object>) arr, scope, tFuncCall.lineNumber);
            return file.getFileName();
        }
    }

    public static class FDirOf extends BaseFunction {
        public FDirOf() {
            super("f_dirOf", new TFunction(
                    "f_dirOf", new String[]{"file"}, null, -1,
                    JDoc.builder()
                            .addDesc("Gets the directory path of the file from a file.")
                            .addParam("file", "[]", "The file array to get the directory path from.", false)
                            .addReturns("The directory path of the file as a string.")
                            .sinceVersion("5.0.0")
                            .addExample("""
                                    khuluma(f_dirOf(f_this))! @ Prints the directory path of the current file.
                                    """)
                            .build()
            ));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config, Scope scope) throws Exception {
            checkParams(tFuncCall, scope); // Ensure correct number of parameters
            Object fileObj = Primitives.toPrimitive(params.getFirst(), false, config, scope);
            if (!(fileObj instanceof ArrayList<?> arr))
                throw new InterpreterException.WtfAreYouDoingException(scope,
                        "The parameter 'file' needs to be an array like come on!", tFuncCall.lineNumber);
            FileType file = FileType.of((ArrayList<Object>) arr, scope, tFuncCall.lineNumber);
            return file.getFilePath();
        }
    }

    public static class FContentOf extends BaseFunction {
        public FContentOf() {
            super("f_contentOf", new TFunction(
                    "f_contentOf", new String[]{"file"}, null, -1,
                    JDoc.builder()
                            .addDesc("Gets the content of the file from a file.")
                            .addParam("file", "[]", "The file array to get the content from.", false)
                            .addReturns("The content of the file as an array of strings.")
                            .sinceVersion("5.0.0")
                            .addExample("""
                                    khuluma(f_contentOf(f_this))! @ Prints the content of the current file.
                                    """)
                            .build()
            ));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config, Scope scope) throws Exception {
            checkParams(tFuncCall, scope); // Ensure correct number of parameters
            Object fileObj = Primitives.toPrimitive(params.getFirst(), false, config, scope);
            if (!(fileObj instanceof ArrayList<?> arr))
                throw new InterpreterException.WtfAreYouDoingException(scope,
                        "The parameter 'file' needs to be an array like come on!", tFuncCall.lineNumber);
            FileType file = FileType.of((ArrayList<Object>) arr, scope, tFuncCall.lineNumber);
            return file.getFileContent();
        }
    }

    public static class FPermsOf extends BaseFunction {
        public FPermsOf() {
            super("f_permsOf", new TFunction(
                    "f_permsOf", new String[]{"file"}, null, -1,
                    JDoc.builder()
                            .addDesc("Gets the permissions of the file from a file.")
                            .addParam("file", "[]", "The file array to get the permissions from.", false)
                            .addReturns("A function that allows you to return a specific permission or all permissions as an array.")
                            .addNote("Unlike other functions, this one may require a bit of functional thinking. It returns a function that you can call to get specific permissions or all permissions at once.")
                            .sinceVersion("5.0.0")
                            .addExample("""
                                    maak permissions <- f_permsOf(f_this)!
                                    khuluma(permissions())! @ Prints all permissions as an array [canRead, canWrite, canExecute]
                                    khuluma(permissions("read"))! @ Prints whether the file can be read (true/false)
                                    khuluma(permissions("r"))! @ Prints whether the file can be read (true/false)
                                    khuluma(permissions(0))! @ Prints whether the file can be read (true/false)
                                    @ Similarly for "write"/"w"/1 and "execute"/"x"/2
                                    """)
                            .build()
            ));
            this.freeze();
        }

        /**
         * Function returned by f_permsOf to get specific permissions.
         * This function doesn't actually require to have a name as it's created and immediately returned by f_permsOf.
         */
        public class FPermissions extends BaseFunction {
            private final FileType file;

            public FPermissions(FileType file) {
                super("perms", new TFunction(
                        "perms",
                        new String[]{"perm?"},
                        null,
                        -1,
                        JDoc.builder()
                                .addDesc("Gets specific permissions or all permission of the specified file.")
                                .addParam("perm", "idk", "The permission to get ('read'/'r'/0, 'write'/'w'/1, 'execute'/'x'/2). If omitted, returns all permissions as an array.", true)
                                .addReturns("The requested permission as a boolean, or all permissions as an array if no parameter is given.")
                                .sinceVersion("5.0.0")
                                .addExample("""
                                        maak permissions <- f_permsOf(f_this)!
                                        khuluma(permissions())! @ Prints all permissions as an array [canRead, canWrite, canExecute]
                                        khuluma(permissions("read"))! @ Prints whether the file can be read (true/false)
                                        khuluma(permissions("r"))! @ Prints whether the file can be read (true/false)
                                        khuluma(permissions(0))! @ Prints whether the file can be read (true/false)
                                        @ Similarly for "write"/"w"/1 and "execute"/"x"/2
                                        """)
                                .build()
                ));
                this.file = file;
                this.freeze();
            }

            @Override
            public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config, Scope scope) throws Exception {
                checkParams(tFuncCall, scope);
                if (params.isEmpty()) {
                    // Return all permissions as an array
                    return file.get(3);
                } else {
                    Object permObj = params.getFirst();
                    if (permObj instanceof String permStr) {
                        switch (permStr.toLowerCase()) {
                            case "read", "r" -> {
                                return file.canRead();
                            }
                            case "write", "w" -> {
                                return file.canWrite();
                            }
                            case "execute", "x" -> {
                                return file.canExecute();
                            }
                            default -> throw new InterpreterException.WtfAreYouDoingException(scope,
                                    "Invalid permission string. Use 'read'/'r', 'write'/'w', or 'execute'/'x'.", tFuncCall.lineNumber);
                        }
                    } else if (permObj instanceof Number permNum) {
                        int index = permNum.intValue();
                        switch (index) {
                            case 0 -> {
                                return file.canRead();
                            }
                            case 1 -> {
                                return file.canWrite();
                            }
                            case 2 -> {
                                return file.canExecute();
                            }
                            default -> throw new InterpreterException.WtfAreYouDoingException(scope,
                                    "Invalid permission index. Use 0 for read, 1 for write, or 2 for execute.", tFuncCall.lineNumber);
                        }
                    } else {
                        throw new InterpreterException.WtfAreYouDoingException(scope,
                                "Permission parameter must be a string or number.", tFuncCall.lineNumber);
                    }
                }
            }
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config, Scope scope) throws Exception {
            checkParams(tFuncCall, scope); // Ensure correct number of parameters
            Object fileObj = Primitives.toPrimitive(params.getFirst(), false, config, scope);
            if (!(fileObj instanceof ArrayList<?> arr))
                throw new InterpreterException.WtfAreYouDoingException(scope,
                        "The parameter 'file' needs to be an array like come on!", tFuncCall.lineNumber);
            FileType file = FileType.of((ArrayList<Object>) arr, scope, tFuncCall.lineNumber);
            return new FPermsOf.FPermissions(file);
        }
    }
}
