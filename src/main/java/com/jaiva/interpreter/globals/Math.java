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
     * named {@code m_random} that takes two parameters: {@code lower} and
     * {@code upper}.
     * It returns a random integer between {@code lower} and {@code upper}, both
     * inclusive.
     * </p>
     *
     * <p>
     * Usage:
     * <ul>
     * <li>{@code m_random(lower, upper)} - Returns a random integer in the range
     * [lower, upper].</li>
     * </ul>
     * </p>
     *
     * <p>
     * Throws {@link WtfAreYouDoingException} if:
     * <ul>
     * <li>Either {@code lower} or {@code upper} is not an integer.</li>
     * <li>{@code lower} is greater than {@code upper}.</li>
     * </ul>
     * </p>
     */
    class FRandom extends BaseFunction {
        FRandom(Token<?> container) {
            super("m_random", container.new TFunction("m_random", new String[] { "lower", "upper" }, null, -1,
                    "Returns a random number (integer) in the range of `lower` and `upper`\\\n" +
                            " > ***lower***:lower bound (inclusive)\\\n" +
                            " > ***upper***:upper bound (inclusive)\n"));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs, IConfig config)
                throws Exception {
            checkParams(tFuncCall);
            Object lowerObject = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(0)), vfs, isFrozen,
                    config);
            Object upperObject = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(1)), vfs, isFrozen,
                    config);

            if (!(lowerObject instanceof Integer lower))
                throw new WtfAreYouDoingException(lowerObject, Integer.class, tFuncCall.lineNumber);

            if (!(upperObject instanceof Integer upper))
                throw new WtfAreYouDoingException(upperObject, Integer.class, tFuncCall.lineNumber);

            if (lower > upper)
                throw new WtfAreYouDoingException("The lower bound cannot be bigger than the upper bound.",
                        tFuncCall.lineNumber);
            return ThreadLocalRandom.current().nextInt(lower, upper + 1);
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
