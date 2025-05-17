package com.jaiva.interpreter.globals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import com.jaiva.errors.InterpreterException.*;
import com.jaiva.interpreter.MapValue;
import com.jaiva.interpreter.Primitives;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.BaseFunction;
import com.jaiva.tokenizer.Token;
import com.jaiva.tokenizer.Token.TFuncCall;

/**
 * Math functions ofc
 */
public class Math extends BaseGlobals {
    public Math() {
        super(GlobalType.LIB, "math");
        vfs.put("m_random", new MapValue(new FRandom(container)));
        vfs.put("m_round", new MapValue(new FRound(container)));
    }

    /**
     * Represents a function that generates a random integer within a specified
     * range.
     * <p>
     * The {@code FRandom} class extends {@link BaseFunction} and implements a
     * function
     * named {@code m_random} that takes two parameters: {@code start} and
     * {@code end}.
     * It returns a random integer between {@code start} and {@code end}, both
     * inclusive.
     * </p>
     *
     * <p>
     * Usage:
     * <ul>
     * <li>{@code m_random(start, end)} - Returns a random integer in the range
     * [start, end].</li>
     * </ul>
     * </p>
     *
     * <p>
     * Throws {@link WtfAreYouDoingException} if:
     * <ul>
     * <li>Either {@code start} or {@code end} is not an integer.</li>
     * <li>{@code start} is greater than {@code end}.</li>
     * </ul>
     * </p>
     */
    class FRandom extends BaseFunction {
        FRandom(Token<?> container) {
            super("m_random", container.new TFunction("m_random", new String[] { "start", "end" }, null, -1,
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

    /**
     * Represents a function that rounds a double value to the nearest integer.
     * <p>
     * If the input value is already an integer, it is returned as-is.
     * If the input value is a double, it is rounded to the nearest integer using
     * {@link java.lang.Math#round(double)}.
     * If the input is neither an integer nor a double, a
     * {@link WtfAreYouDoingException} is thrown.
     * </p>
     *
     * <p>
     * Function signature: <code>m_round(value)</code>
     * </p>
     *
     */
    class FRound extends BaseFunction {
        FRound(Token<?> container) {
            super("m_round", container.new TFunction("m_round", new String[] { "value" }, null, -1,
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
