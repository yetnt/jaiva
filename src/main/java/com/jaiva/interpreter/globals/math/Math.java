package com.jaiva.interpreter.globals.math;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import com.jaiva.errors.InterpreterException.*;
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
     * Represents a function that returns the absolute value of a number.
     * <p>
     * The {@code FAbs} class extends {@link BaseFunction} and implements a
     * function
     * named {@code m_abs} that takes one parameter: {@code value}.
     * It returns the absolute value of the provided number.
     * </p>
     *
     * <p>
     * Usage:
     * <ul>
     * <li>{@code m_abs(value)} - Returns the absolute value of the number
     * provided.</li>
     * </ul>
     * </p>
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
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs, IConfig config)
                throws Exception {
            checkParams(tFuncCall);
            Object value = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(0)), vfs, false, config);

            if (value instanceof Integer i)
                return java.lang.Math.abs(i);
            else if (value instanceof Double d)
                return java.lang.Math.abs(d);
            else
                throw new FunctionParametersException(this, "1", value, Number.class, tFuncCall.lineNumber);

        }
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
            Object lowerObject = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(0)), vfs, false,
                    config);
            Object upperObject = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(1)), vfs, false,
                    config);

            if (!(lowerObject instanceof Integer lower))
                throw new FunctionParametersException(this, "1", lowerObject, Integer.class, tFuncCall.lineNumber);

            if (!(upperObject instanceof Integer upper))
                throw new FunctionParametersException(this, "2", upperObject, Integer.class, tFuncCall.lineNumber);

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
            Object value = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(0)), vfs, false, config);

            if (value instanceof Integer)
                return value;
            else if (value instanceof Double d)
                return (int) java.lang.Math.round(d);
            else
                throw new FunctionParametersException(this, "1", value, Number.class, tFuncCall.lineNumber);

        }
    }

    /**
     * Represents a function that calculates the square root of a number.
     * <p>
     * The {@code FSqrt} class extends {@link BaseFunction} and implements a
     * function
     * named {@code m_sqrt} that takes one parameter: {@code value}.
     * It returns the square root of the provided number.
     * </p>
     *
     * <p>
     * Usage:
     * <ul>
     * <li>{@code m_sqrt(value)} - Returns the square root of the number
     * provided.</li>
     * </ul>
     * </p>
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
                    "Returns the square root of a number"));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs, IConfig config)
                throws Exception {
            checkParams(tFuncCall);
            Object value = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(0)), vfs, false, config);

            if (value instanceof Integer i)
                return java.lang.Math.sqrt(i);
            else if (value instanceof Double d)
                return java.lang.Math.sqrt(d);
            else
                throw new FunctionParametersException(this, "1", value, Number.class, tFuncCall.lineNumber);

        }
    }

    /**
     * Represents a function that raises a base to the power of an exponent.
     * <p>
     * The {@code FPow} class extends {@link BaseFunction} and implements a
     * function
     * named {@code m_pow} that takes two parameters: {@code base} and
     * {@code exponent}.
     * It returns the result of raising the base to the power of the exponent.
     * </p>
     *
     * <p>
     * Usage:
     * <ul>
     * <li>{@code m_pow(base, exponent)} - Returns the result of base raised to
     * the power of exponent.</li>
     * </ul>
     * </p>
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
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs, IConfig config)
                throws Exception {
            checkParams(tFuncCall);
            Object base = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(0)), vfs, false, config);
            Object exponent = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(1)), vfs, false,
                    config);

            if (!(base instanceof Number))
                throw new FunctionParametersException(this, "1", base, Number.class, tFuncCall.lineNumber);
            if (!(exponent instanceof Number))
                throw new FunctionParametersException(this, "2", exponent, Number.class, tFuncCall.lineNumber);

            return java.lang.Math.pow(((Number) base).doubleValue(), ((Number) exponent).doubleValue());

        }
    }

    /**
     * Represents a function that returns the largest integer less than or equal to
     * a given value.
     * <p>
     * The {@code FFloor} class extends {@link BaseFunction} and implements a
     * function
     * named {@code m_floor} that takes one parameter: {@code value}.
     * It returns the largest integer less than or equal to the provided value.
     * </p>
     *
     * <p>
     * Usage:
     * <ul>
     * <li>{@code m_floor(value)} - Returns the largest integer less than or equal
     * to the number provided.</li>
     * </ul>
     * </p>
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
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs, IConfig config)
                throws Exception {
            checkParams(tFuncCall);
            Object value = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(0)), vfs, false, config);

            if (value instanceof Integer i)
                return i;
            else if (value instanceof Double d)
                return (int) java.lang.Math.floor(d);
            else
                throw new FunctionParametersException(this, "1", value, Number.class, tFuncCall.lineNumber);
        }
    }

    /**
     * Represents a function that returns the smallest integer greater than or equal
     * to a given value.
     * <p>
     * The {@code FCeil} class extends {@link BaseFunction} and implements a
     * function
     * named {@code m_ceil} that takes one parameter: {@code value}.
     * It returns the smallest integer greater than or equal to the provided value.
     * </p>
     *
     * <p>
     * Usage:
     * <ul>
     * <li>{@code m_ceil(value)} - Returns the smallest integer greater than or
     * equal to the number provided.</li>
     * </ul>
     * </p>
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
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs, IConfig config)
                throws Exception {
            checkParams(tFuncCall);
            Object value = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(0)), vfs, false, config);

            if (value instanceof Integer i)
                return i;
            else if (value instanceof Double d)
                return (int) java.lang.Math.ceil(d);
            else
                throw new FunctionParametersException(this, "1", value, Number.class, tFuncCall.lineNumber);
        }
    }
}
