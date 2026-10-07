package com.jaiva.interpreter.libs.file;

import com.jaiva.errors.InterpreterException;
import com.jaiva.interpreter.Primitives;
import com.jaiva.interpreter.Scope;
import com.jaiva.interpreter.libBuilders.func.Argument;
import com.jaiva.interpreter.libBuilders.func.Arguments;
import com.jaiva.interpreter.libBuilders.func.FunctionBuilder;
import com.jaiva.interpreter.libBuilders.func.arg.AArgument;
import com.jaiva.interpreter.libs.BaseLibrary;
import com.jaiva.interpreter.libs.annotation.JaivaLibrary;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.BaseFunction;
import com.jaiva.interpreter.symbol.continuous.ContinuousFunction;
import com.jaiva.tokenizer.jdoc.JDoc;
import com.jaiva.tokenizer.tokens.specific.TFuncCall;
import com.jaiva.utils.ThrowableSupplier;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;

@JaivaLibrary(
        path = "file/io",
        description = "experimental"
)
public class FileBytes extends BaseLibrary {
    public FileBytes() {
        add(new FByteReader());
    }
    static class FByteReader extends BaseFunction {
        public FByteReader() {
            super(
                    FunctionBuilder.start()
                            .name("f_read")
                            .arguments(Arguments.getInstance()
                                    .add(
                                            new AArgument(
                                                    "filePath",
                                                    "Absolute path",
                                                    false,
                                                    Argument.Type.STRING
                                            )
                                    ))
                            .docs(JDoc.builder().sinceVersion("6.1.0"))
            );
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config, Scope scope) throws Exception {
            checkParams(tFuncCall, scope);

            Object p = Primitives.toPrimitive(params.getFirst(), false, config, scope);
            if (!(p instanceof String path))
                throw new InterpreterException.FunctionParametersException(
                        scope, this, "1", p, String.class, tFuncCall.lineNumber
                );

            File f = new File(path);

            if (!f.exists())
                throw new InterpreterException.WtfAreYouDoingException(
                       scope,  "File no existy", tFuncCall.lineNumber
                );

            DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(f)));

            return ContinuousFunction.from(
                    config,
                    dis,
                    (tf, pr, cf, sc) -> {
                        try {
                            byte b = dis.readByte();
                            return () -> b;
                        } catch (EOFException eof) {
                            return this::returnEOF;
                        }
                    }
            );
        }

        private boolean returnEOF() throws IOException {
            return false;
        }
    }
}
