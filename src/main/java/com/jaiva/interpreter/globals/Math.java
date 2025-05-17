package com.jaiva.interpreter.globals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.random.*;

import com.jaiva.errors.InterpreterException;
import com.jaiva.errors.InterpreterException.*;
import com.jaiva.interpreter.MapValue;
import com.jaiva.interpreter.Primitives;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.BaseFunction;
import com.jaiva.tokenizer.Token;
import com.jaiva.tokenizer.Token.TFuncCall;

public class Math extends BaseGlobals {
    public Math() {
        super(GlobalType.LIB, "math");
        vfs.put("random", new MapValue(new FRandom(container)));
        vfs.put("round", new MapValue(new FRound(container)));
    }

    class FRandom extends BaseFunction {
        FRandom(Token<?> container) {
            super("random", container.new TFunction("random", new String[] { "start", "end" }, null, -1,
                    "Returns a random number in the range of `start` and `end` both inclusive"));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs, IConfig config)
                throws Exception {
            checkParams(tFuncCall);
            Object startObject = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(0)), vfs, isFrozen,
                    config);
            Object endObject = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(1)), vfs, isFrozen,
                    config);

            if (!(startObject instanceof Integer start))
                throw new WtfAreYouDoingException(startObject, Integer.class, tFuncCall.lineNumber);

            if (!(endObject instanceof Integer end))
                throw new WtfAreYouDoingException(endObject, Integer.class, tFuncCall.lineNumber);

            if (start > end)
                throw new WtfAreYouDoingException("The start cannot be bigger than the end", tFuncCall.lineNumber);
            return ThreadLocalRandom.current().nextInt(start, end + 1);
        }
    }

    class FRound extends BaseFunction {
        FRound(Token<?> container) {
            super("round", container.new TFunction("round", new String[] { "value" }, null, -1,
                    "Rounds a double to the nearest integer, or returns the integer if already an integer"));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs, IConfig config)
                throws Exception {
            checkParams(tFuncCall);
            Object value = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(0)), vfs, isFrozen, config);

            if (value instanceof Integer) {
                return value;
            } else if (value instanceof Double d) {
                return (int) java.lang.Math.round(d);
            } else {
                throw new WtfAreYouDoingException(value, Number.class, tFuncCall.lineNumber);
            }
        }
    }
}
