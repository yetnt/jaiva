package com.jaiva.interpreter.libs;

import com.jaiva.errors.InterpreterException;
import com.jaiva.interpreter.Context;
import com.jaiva.interpreter.Interpreter;
import com.jaiva.interpreter.Scope;
import com.jaiva.interpreter.Vfs;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.runtime.ResourceLoader;
import com.jaiva.tokenizer.TConfig;
import com.jaiva.tokenizer.Token;
import com.jaiva.tokenizer.Tokenizer;

import java.util.*;

public class LibraryLoader {
    private static final String LIB_DIR = "lib/";

    public HashMap<String, Vfs> loadAllLibraries(IConfig<Object> config) throws InterpreterException.LibImportException {
        String[] libraryFiles = {
                "arrays.jiv",
                // and more, probably not but whatever.
        };

        HashMap<String, Vfs> imported = new HashMap<>();

        for (String libFile : libraryFiles) {
            try {
                String fullPath = LIB_DIR + libFile;
                String content = ResourceLoader.getResourceAsString(fullPath);
                Vfs vfs = loadLibrary(content, libFile, fullPath, config);
                imported.put(libFile.substring(0, libFile.indexOf('.')), vfs);
//                System.out.println("✓ Loaded library: " + libFile);
            } catch (Exception e) {
                throw new InterpreterException.LibImportException(new Scope(config), libFile);
            }
        }

        return imported;

    }

    private Vfs loadLibrary(String content, String name, String path, IConfig<Object> config) throws Exception {
//        System.out.println("Loading " + name + " (" + content.length() + " chars)");

        ArrayList<Token<?>> tks = (ArrayList<Token<?>>) Tokenizer.readLine(content, "", null, null, -1, new TConfig());

        IConfig<Object> newConfig = new IConfig<Object>(config.sanitisedArgs, path,
                null);

        newConfig.importVfs = true; // This tells the interpreter to only parse exported symbols. (Functions
        // and variables)

        Vfs vfsFromFile = ((Vfs) Interpreter.interpret(tks, new Scope(Context.IMPORT,
                new Token.TImport(path, name, true, -1), new Scope(config)), newConfig));

        return vfsFromFile;

    }
}
