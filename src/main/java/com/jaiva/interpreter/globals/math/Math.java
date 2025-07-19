package com.jaiva.interpreter.globals.math;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import com.jaiva.errors.InterpreterException.*;
import com.jaiva.interpreter.ContextTrace;
import com.jaiva.interpreter.MapValue;
import com.jaiva.interpreter.Primitives;
import com.jaiva.interpreter.globals.BaseGlobals;
import com.jaiva.interpreter.globals.GlobalType;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.BaseFunction;
import com.jaiva.tokenizer.Token;
import com.jaiva.tokenizer.Token.TFuncCall;

/**
 * Math functions ofc
 */
public class Math extends BaseGlobals {
    /**
     * Default Constructor
     */
    public Math() {
        super(GlobalType.LIB, "math");
        vfs.put("m_random", new MapValue(new FRandom(container)));
        vfs.put("m_round", new MapValue(new FRound(container)));
        vfs.put("m_abs", new MapValue(new FAbs(container)));
        vfs.put("m_sqrt", new MapValue(new FSqrt(container)));
        vfs.put("m_pow", new MapValue(new FPow(container)));
        vfs.put("m_floor", new MapValue(new FFloor(container)));
        vfs.put("m_ceil", new MapValue(new FCeil(container)));
        vfs.putAll(new Constants().vfs); // Add constants like m_pi, m_e, etc.
        vfs.putAll(new Trig().vfs); // Add trigonometric functions like m_sin, m_cos, etc.
    }

    /**
     * {@code m_abs(value)} -> returns the absolute value of the given number.
     *
     * <p>
     * Throws {@link WtfAreYouDoingException} if:
     * <ul>
     * <li>{@code value} is not a number (Integer or Double).</li>
     * </ul>
     * </p>
     */
    class FAbs extends BaseFunction {
        FAbs(Token<?> container) {
            super("m_abs", container.new TFunction("m_abs", new String[] { "value" }, null, -1,
                    "Returns the absolute value of a number"));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs, IConfig config,
                ContextTrace cTrace)
                throws Exception {
            checkParams(tFuncCall, cTrace);
            Object value = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(0)), vfs, false, config,
                    cTrace);

            if (value instanceof Integer i)
                return java.lang.Math.abs(i);
            else if (value instanceof Double d)
                return java.lang.Math.abs(d);
            else
                throw new FunctionParametersException(cTrace, this, "1", value, Number.class, tFuncCall.lineNumber);

        }
    }

    /**
     * {@code m_random(lower, upper)} -> returns a random integer with [lower,
     * upper]
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
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs, IConfig config,
                ContextTrace cTrace)
                throws Exception {
            checkParams(tFuncCall, cTrace);
            Object lowerObject = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(0)), vfs, false,
                    config, cTrace);
            Object upperObject = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(1)), vfs, false,
                    config, cTrace);

            if (!(lowerObject instanceof Integer lower))
                throw new FunctionParametersException(cTrace, this, "1", lowerObject, Integer.class,
                        tFuncCall.lineNumber);

            if (!(upperObject instanceof Integer upper))
                throw new FunctionParametersException(cTrace, this, "2", upperObject, Integer.class,
                        tFuncCall.lineNumber);

            if (lower > upper)
                throw new WtfAreYouDoingException(cTrace, "The lower bound cannot be bigger than the upper bound.",
                        tFuncCall.lineNumber);
            return ThreadLocalRandom.current().nextInt(lower, upper + 1);
        }
    }

    /**
     * {@code m_round(value)} -> rounds an approximatio to the nearest integer.
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
                    "Rounds the given real nmber to an integer. This is for the real ones who hate working with precision."));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs, IConfig config,
                ContextTrace cTrace)
                throws Exception {
            checkParams(tFuncCall, cTrace);
            Object value = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(0)), vfs, false, config,
                    cTrace);

            if (value instanceof Integer)
                return value;
            else if (value instanceof Double d)
                return (int) java.lang.Math.round(d);
            else
                throw new FunctionParametersException(cTrace, this, "1", value, Number.class, tFuncCall.lineNumber);

        }
    }

    /**
     * {@code m_sqrt(value)} -> Returns the (positive) square root of the number
     * provided
     *
     * <p>
     * Throws {@link WtfAreYouDoingException} if:
     * <ul>
     * <li>{@code value} is not a number (Integer or Double).</li>
     * </ul>
     * </p>
     */
    class FSqrt extends BaseFunction {
        FSqrt(Token<?> container) {
            super("m_sqrt", container.new TFunction("m_sqrt", new String[] { "value" }, null, -1,
                    "Returns the (positive) square root of the number provided"));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs, IConfig config,
                ContextTrace cTrace)
                throws Exception {
            checkParams(tFuncCall, cTrace);
            Object value = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(0)), vfs, false, config,
                    cTrace);

            if (value instanceof Integer i)
                return java.lang.Math.sqrt(i);
            else if (value instanceof Double d)
                return java.lang.Math.sqrt(d);
            else
                throw new FunctionParametersException(cTrace, this, "1", value, Number.class, tFuncCall.lineNumber);

        }
    }

    /**
     * {@code m_pow(base, exponent)} <- Returns the result of base raised to
     * the power of exponent.
     *
     * <p>
     * Throws {@link WtfAreYouDoingException} if:
     * <ul>
     * <li>{@code base} or {@code exponent} is not a number (Integer or
     * Double).</li>
     * </ul>
     * </p>
     */
    class FPow extends BaseFunction {
        FPow(Token<?> container) {
            super("m_pow", container.new TFunction("m_pow", new String[] { "base", "exponent" }, null, -1,
                    "Returns the result of raising the base to the power of the exponent"));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs, IConfig config,
                ContextTrace cTrace)
                throws Exception {
            checkParams(tFuncCall, cTrace);
            Object base = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(0)), vfs, false, config,
                    cTrace);
            Object exponent = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(1)), vfs, false,
                    config, cTrace);

            if (!(base instanceof Number))
                throw new FunctionParametersException(cTrace, this, "1", base, Number.class, tFuncCall.lineNumber);
            if (!(exponent instanceof Number))
                throw new FunctionParametersException(cTrace, this, "2", exponent, Number.class, tFuncCall.lineNumber);

            return java.lang.Math.pow(((Number) base).doubleValue(), ((Number) exponent).doubleValue());

        }
    }

    /**
     * {@code m_floor(value)} - Returns the largest integer less than or equal
     * to the number provided.
     *
     * <p>
     * Throws {@link WtfAreYouDoingException} if:
     * <ul>
     * <li>{@code value} is not a number (Integer or Double).</li>
     * </ul>
     * </p>
     */
    class FFloor extends BaseFunction {
        FFloor(Token<?> container) {
            super("m_floor", container.new TFunction("m_floor", new String[] { "value" }, null, -1,
                    "Returns the largest integer less than or equal to the given value"));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs, IConfig config,
                ContextTrace cTrace)
                throws Exception {
            checkParams(tFuncCall, cTrace);
            Object value = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(0)), vfs, false, config,
                    cTrace);

            if (value instanceof Integer i)
                return i;
            else if (value instanceof Double d)
                return (int) java.lang.Math.floor(d);
            else
                throw new FunctionParametersException(cTrace, this, "1", value, Number.class, tFuncCall.lineNumber);
        }
    }

    /**
     * {@code m_ceil(value)} - Returns the smallest integer greater than or
     * equal to the number provided.
     *
     * <p>
     * Throws {@link WtfAreYouDoingException} if:
     * <ul>
     * <li>{@code value} is not a number (Integer or Double).</li>
     * </ul>
     * </p>
     */
    class FCeil extends BaseFunction {
        FCeil(Token<?> container) {
            super("m_ceil", container.new TFunction("m_ceil", new String[] { "value" }, null, -1,
                    "Returns the smallest integer greater than or equal to the given value"));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs, IConfig config,
                ContextTrace cTrace)
                throws Exception {
            checkParams(tFuncCall, cTrace);
            Object value = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(0)), vfs, false, config,
                    cTrace);

            if (value instanceof Integer i)
                return i;
            else if (value instanceof Double d)
                return (int) java.lang.Math.ceil(d);
            else
                throw new FunctionParametersException(cTrace, this, "1", value, Number.class, tFuncCall.lineNumber);
        }
    }
}
