package com.jaiva.interpreter.globals;

import java.util.ArrayList;

import com.jaiva.errors.JaivaException.DebugException;
import com.jaiva.interpreter.Scope;
import com.jaiva.interpreter.Primitives;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.BaseFunction;
import com.jaiva.tokenizer.Token.*;

public class Debug extends BaseGlobals {
    Debug(IConfig<Object> config) {
        super(GlobalType.LIB, "debug");
        vfs.put("d_emit", new FEmit(config));
    }

    public class FEmit extends BaseFunction {
        FEmit(IConfig<Object> config) {
            super("d_emit", new TFunction("d_emit", new String[] { "arr?" }, null, -1,
                    "Throws an error which is expected to be caught by a debugger of some sorts."));
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
