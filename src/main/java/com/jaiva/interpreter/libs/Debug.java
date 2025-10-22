package com.jaiva.interpreter.libs;

import java.util.ArrayList;

import com.jaiva.errors.JaivaException.DebugException;
import com.jaiva.interpreter.Scope;
import com.jaiva.interpreter.Primitives;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.BaseFunction;
import com.jaiva.tokenizer.Token.*;
import com.jaiva.tokenizer.jdoc.JDoc;

public class Debug extends BaseLibrary {
    Debug(IConfig<Object> config) {
        super(LibraryType.LIB, "debug");
        vfs.put("d_emit", new FEmit(config));
    }

    public class FEmit extends BaseFunction {
        FEmit(IConfig<Object> config) {
            super("d_emit", new TFunction("d_emit", new String[] { "arr?" }, null, -1,
                    JDoc.builder()
                            .addDesc("Throws a DebugException to be caught by a Java test class and emits the given variables")
                            .addParam("arr", "[]", "The array of values to pass to the exception", true)
                            .addReturns("Physically can't return. As it always throws an error")
                            .addNote(
                                    "If you aren't familiar with Java, the language Jaiva is developed in. " +
                                            "This will essentially forcefully stop the execution of the interpreter, with the intent" +
                                            " for said error to be caught and dealt with by another Java class. This serves 0 purpose if " +
                                            " you're just running a Jaiva file."
                            )
                            .sinceVersion("1.0.2")
                            .build()
            ));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config,
                           Scope scope)
                throws DebugException {
            // Any implementation of normal interpreter functions which could throw such an
            // exception should be in the catch.
            ArrayList<Object> components = new ArrayList<>();
            try {
                checkParams(tFuncCall, scope); // throws if params aren't correct. So naturally we loose having
                                                // varargs, so its
                // fine.
                if (!params.isEmpty()) {
                    Object param = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.getFirst()), false,
                            config, scope);
                    if (param instanceof ArrayList<?> arr) {
                        components.addAll(arr);
                    } else {
                        components.add(param);
                    }
                }
            } catch (Exception e) {
                throw new DebugException(e);
            }
            throw new DebugException(components, scope, config, tFuncCall.lineNumber);
        }
    }
}
