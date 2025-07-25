package com.jaiva.interpreter.globals;

import java.util.ArrayList;

import com.jaiva.errors.JaivaException.DebugException;
import com.jaiva.interpreter.ContextTrace;
import com.jaiva.interpreter.Primitives;
import com.jaiva.interpreter.Vfs;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.BaseFunction;
import com.jaiva.tokenizer.Token.*;

public class Debug extends BaseGlobals {
    Debug(IConfig config) {
        super(GlobalType.LIB, "debug");
        vfs.put("d_emit", new FEmit(config));
    }

    public class FEmit extends BaseFunction {
        FEmit(IConfig config) {
            super("d_emit", new TFunction("d_emit", new String[] { "arr?" }, null, -1,
                    "Throws an error which is expected to be caught by a debugger of some sorts."));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, Vfs vfs, IConfig config,
                           ContextTrace cTrace)
                throws DebugException {
            // Any implementation of normal interpreter functions which could throw such an
            // exception should be in the catch.
            ArrayList<Object> components = new ArrayList<>();
            try {
                checkParams(tFuncCall, cTrace); // throws if params aren't correct. So naturally we loose having
                                                // varargs, so its
                // fine.
                if (!params.isEmpty()) {
                    Object param = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.getFirst()), vfs, false,
                            config, cTrace);
                    if (param instanceof ArrayList<?> arr) {
                        components.addAll(arr);
                    } else {
                        components.add(param);
                    }
                }
            } catch (Exception e) {
                throw new DebugException(e);
            }
            throw new DebugException(components, vfs, config, tFuncCall.lineNumber);
        }
    }
}
