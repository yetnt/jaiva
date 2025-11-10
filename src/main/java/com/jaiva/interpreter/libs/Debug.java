package com.jaiva.interpreter.libs;

import java.util.ArrayList;
import java.util.List;

import com.jaiva.errors.JaivaException.DebugException;
import com.jaiva.interpreter.Scope;
import com.jaiva.interpreter.Primitives;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.BaseFunction;
import com.jaiva.interpreter.symbol.BaseVariable;
import com.jaiva.interpreter.symbol.Symbol;
import com.jaiva.interpreter.symbol.SymbolConfig;
import com.jaiva.tokenizer.jdoc.JDoc;
import com.jaiva.tokenizer.tokens.specific.TFuncCall;
import com.jaiva.tokenizer.tokens.specific.TFunction;

public class Debug extends BaseLibrary {
    static String path = "debug";
    public Debug(IConfig<Object> config) {
        super(LibraryType.LIB, "debug");
        vfs.put("d_emit", new FEmit(config));
        vfs.put("d_vfs", new FVfs(config));
    }

    @SymbolConfig(experimental = true)
    public class FVfs extends BaseFunction {
        FVfs(IConfig<Object> config) {
            super("d_vfs", new TFunction("d_vfs", new String[]{}, null, -1,
                    JDoc.builder()
                            .addDesc("Returns the current context's vfs.")
                            .sinceVersion("4.1.0")
                            .addReturns("A 2d array, first array containing the keys, second array the values.")
                            .addNote("This function does not allow you to edit the current vfs, only to get" +
                                    " everything that is currently within the vfs as an array." +
                                    " Everytime this function is called a new array containing all the stuff is made.")
                            .build()
                    ));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config, Scope scope) throws Exception {
            ArrayList<String> names = new ArrayList<>();
            ArrayList<Object> symbols = new ArrayList<>();
            scope.vfs.forEach((v, f)-> {
                names.add(v);
                Symbol sym = f.getValue();
                if (sym instanceof BaseVariable var) {
                    if (var.variableType == BaseVariable.VariableType.ARRAY)
                        symbols.add(var.a_getAll());
                    else
                        symbols.add(var.s_get());
                } else if (sym instanceof BaseFunction function) {
                    symbols.add(function);
                }

            });
            return new ArrayList<>(List.of(names, symbols));
        }
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
