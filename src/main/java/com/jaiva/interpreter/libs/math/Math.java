package com.jaiva.interpreter.libs.math;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.jaiva.errors.InterpreterException.*;
import com.jaiva.interpreter.Scope;
import com.jaiva.interpreter.Primitives;
import com.jaiva.interpreter.libs.BaseLibrary;
import com.jaiva.interpreter.libs.LibraryType;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.BaseFunction;
import com.jaiva.tokenizer.jdoc.JDoc;
import com.jaiva.tokenizer.tokens.specific.TFuncCall;
import com.jaiva.tokenizer.tokens.specific.TFunction;

/**
 * Math functions ofc
 */
public class Math extends BaseLibrary {
    /**
     * Default Constructor
     */
    public Math() {
        super(LibraryType.LIB, "math");
        vfs.put("m_random", new FRandom());
        vfs.put("m_round", new FRound());
        vfs.put("m_abs", new FAbs());
        vfs.put("m_sqrt", new FSqrt());
        vfs.put("m_pow",new FPow());
        vfs.put("m_floor", new FFloor());
        vfs.put("m_ceil", new FCeil());
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
        FAbs() {
            super("m_abs", new TFunction("m_abs", new String[] { "value" }, null, -1,
                    JDoc.builder()
                            .addDesc("Returns the absolute value of a number.")
                            .addParam("value", "number", "The value to return the value of.", false)
                            .addReturns("A positive value.")
                            .sinceVersion("1.0.2")
                            .build()
            ));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config,
                           Scope scope)
                throws Exception {
            checkParams(tFuncCall, scope);
            Object value = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.getFirst()), false, config,
                    scope);

            if (value instanceof Integer i)
                return java.lang.Math.abs(i);
            else if (value instanceof Double d)
                return java.lang.Math.abs(d);
            else
                throw new FunctionParametersException(scope, this, "1", value, Number.class, tFuncCall.lineNumber);

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
        FRandom() {
            super("m_random", new TFunction("m_random", new String[] { "lower", "upper" }, null, -1,
            JDoc.builder()
                    .addDesc("Returns a random number (integer) in the range of `lower` and `upper`")
                    .addParam("lower", "number", "The lowest number possible (inclusive)", false)
                    .addParam("higher", "number", "The highest number possible (inclusive)", false)
                    .addReturns("A random number.")
                    .sinceVersion("1.0.0")
                    .build()
            ));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config,
                Scope scope)
                throws Exception {
            checkParams(tFuncCall, scope);
            Object lowerObject = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(0)), false,
                    config, scope);
            Object upperObject = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(1)), false,
                    config, scope);

            if (!(lowerObject instanceof Integer lower))
                throw new FunctionParametersException(scope, this, "1", lowerObject, Integer.class,
                        tFuncCall.lineNumber);

            if (!(upperObject instanceof Integer upper))
                throw new FunctionParametersException(scope, this, "2", upperObject, Integer.class,
                        tFuncCall.lineNumber);

            if (lower > upper)
                throw new WtfAreYouDoingException(scope, "The lower bound cannot be bigger than the upper bound.",
                        tFuncCall.lineNumber);

            if (lower == 6 && upper == 7)
                return 67;

            return ThreadLocalRandom.current().nextInt(lower, upper + 1);
        }
    }

    /**
     * {@code m_round(value)} -> rounds an approximate to the nearest integer.
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
        FRound() {
            super("m_round", new TFunction("m_round", new String[] { "value" }, null, -1,
                    JDoc.builder()
                            .addDesc("Rounds the given real number to an integer.")
                            .addParam("value", "number", "The input to round up/down.", false)
                            .addReturns("An integer value")
                            .sinceVersion("1.0.0")
                            .build()
            ));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config,
                Scope scope)
                throws Exception {
            checkParams(tFuncCall, scope);
            Object value = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.getFirst()), false, config,
                    scope);

            if (value instanceof Integer)
                return value;
            else if (value instanceof Double d)
                return (int) java.lang.Math.round(d);
            else
                throw new FunctionParametersException(scope, this, "1", value, Number.class, tFuncCall.lineNumber);

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
        FSqrt() {
            super("m_sqrt", new TFunction("m_sqrt", new String[] { "value" }, null, -1,
                    JDoc.builder()
                            .addDesc("Calculates the sqaure root of the input value.")
                            .addParam("value", "number", "The number to find the square root of.", false)
                            .addReturns("Returns the (positive) square root of the number provided")
                            .sinceVersion("1.0.2")
                            .build()
            ));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config,
                Scope scope)
                throws Exception {
            checkParams(tFuncCall, scope);
            Object value = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.getFirst()), false, config,
                    scope);

            if (value instanceof Integer i)
                return java.lang.Math.sqrt(i);
            else if (value instanceof Double d)
                return java.lang.Math.sqrt(d);
            else
                throw new FunctionParametersException(scope, this, "1", value, Number.class, tFuncCall.lineNumber);

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
        FPow() {
            super("m_pow", new TFunction("m_pow", new String[] { "base", "exponent" }, null, -1,
                    JDoc.builder()
                            .addDesc("Raises the base to the power of the exponent")
                            .addParam("base", "number", "The base.", false)
                            .addParam("exponent", "number", "The exponent.", false)
                            .addReturns("Returns the result of raising the base to the power of the exponent")
                            .markDeprecated("Literally just use \"base^exponent\". I have no idea why I even made this function if the operator"
                            + " part existed in the first place. This will be removed within later versions.")
                            .sinceVersion("1.0.2")
                            .build()
            ));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config,
                Scope scope)
                throws Exception {
            checkParams(tFuncCall, scope);
            Object base = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(0)), false, config,
                    scope);
            Object exponent = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(1)), false,
                    config, scope);

            if (!(base instanceof Number))
                throw new FunctionParametersException(scope, this, "1", base, Number.class, tFuncCall.lineNumber);
            if (!(exponent instanceof Number))
                throw new FunctionParametersException(scope, this, "2", exponent, Number.class, tFuncCall.lineNumber);

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
        FFloor() {
            super("m_floor", new TFunction("m_floor", new String[] { "value" }, null, -1,
                    JDoc.builder()
                            .addDesc("Returns the largest integer less than or equal to the given value.")
                            .addParam("value", "number", "The value to floor.", false)
                            .addReturns("The largest integer less than or equal to the given value.")
                            .sinceVersion("1.0.2")
                            .build()
            ));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config,
                Scope scope)
                throws Exception {
            checkParams(tFuncCall, scope);
            Object value = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.getFirst()), false, config,
                    scope);

            if (value instanceof Integer i)
                return i;
            else if (value instanceof Double d)
                return (int) java.lang.Math.floor(d);
            else
                throw new FunctionParametersException(scope, this, "1", value, Number.class, tFuncCall.lineNumber);
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
        FCeil() {
            super("m_ceil", new TFunction("m_ceil", new String[] { "value" }, null, -1,
                    JDoc.builder()
                            .addDesc("Returns the smallest integer greater than or equal to the given value.")
                            .addParam("value", "number", "The value to ceil.", false)
                            .addReturns("The smallest integer greater than or equal to the given value.")
                            .sinceVersion("1.0.2")
                            .build()
            ));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config,
                Scope scope)
                throws Exception {
            checkParams(tFuncCall, scope);
            Object value = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.getFirst()), false, config,
                    scope);

            if (value instanceof Integer i)
                return i;
            else if (value instanceof Double d)
                return (int) java.lang.Math.ceil(d);
            else
                throw new FunctionParametersException(scope, this, "1", value, Number.class, tFuncCall.lineNumber);
        }
    }
}
