package com.jaiva.interpreter.libs.math;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.jaiva.errors.InterpreterException;
import com.jaiva.errors.InterpreterException.*;
import com.jaiva.interpreter.Scope;
import com.jaiva.interpreter.Primitives;
import com.jaiva.interpreter.libs.BaseLibrary;
import com.jaiva.interpreter.libs.annotation.JaivaLibrary;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.BaseFunction;
import com.jaiva.tokenizer.jdoc.JDoc;
import com.jaiva.tokenizer.tokens.specific.TFuncCall;
import com.jaiva.tokenizer.tokens.specific.TFunction;

/**
 * MathBase functions ofc
 */
@JaivaLibrary(path = "math/base")
public class MathBase extends BaseLibrary {

    public MathBase() {
        vfs.put("m_random", new FRandom());
        vfs.put("m_round", new FRound());
        vfs.put("m_abs", new FAbs());
        vfs.put("m_sqrt", new FSqrt());
        vfs.put("m_floor", new FFloor());
        vfs.put("m_ceil", new FCeil());
        vfs.put("m_log", new FLog());
        vfs.put("m_gcd", new FGcd());
        vfs.put("m_lcm", new FLcm());
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
                            .addExample("""
                                    khuluma("The absolute value of -5 is: " + m_abs(-5))!
                                    """)
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
            Object value = Primitives.toPrimitive(params.getFirst(), false, config,
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
            super("m_random", new TFunction("m_random", new String[] { "a?", "b?" }, null, -1,
            JDoc.builder()
                    .addDesc("Returns a random number in the range of `a` and `b`, If both are omitted, returns a random (double) between 0 and 1.")
                    .addParam("a", "number", "The highest number possible between `a` and 0, otherwise the lowest between `a` and `b` (inclusive)", true)
                    .addParam("b", "number", "The highest number possible between a and b (inclusive). ", true)
                    .addNote("Unlike other functions, if you provide no arguments, this function returns a double between 0 and 1."
                            + " If you provide only one argument, it is treated as the upper bound, with the lower bound being 0.")
                    .addReturns("A random number.")
                    .addExample("""
                            khuluma("Random number between 1 and 10: " + m_random(1, 10))!
                            khuluma("Random number between 0 and 5: " + m_random(5))!
                            khuluma("Random number between 0 and 1: " + m_random())!
                            """)
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
            if (params.isEmpty())
                return ThreadLocalRandom.current().nextDouble();

            Object lowerObject = Primitives.toPrimitive(params.get(0), false,
                    config, scope);
            Object upperObject = params.size() > 1 ? Primitives.toPrimitive(params.get(1), false,
                    config, scope) : null;

            if (!(lowerObject instanceof Integer lower))
                throw new FunctionParametersException(scope, this, "1", lowerObject, Integer.class,
                        tFuncCall.lineNumber);

            if (upperObject == null) {
                // only one param was given, so treat lower as upper and 0 as lower.
                // check if a is negative
                if (lower < 0)
                    throw new WtfAreYouDoingException(scope,
                            "When only one argument is given, it is treated as the upper bound, and the lower bound is 0. Therefore, the upper bound cannot be negative.",
                            tFuncCall.lineNumber);
                return ThreadLocalRandom.current().nextInt(0, lower + 1);
            }

            if (!(upperObject instanceof Integer upper))
                throw new FunctionParametersException(scope, this, "2", upperObject, Integer.class,
                        tFuncCall.lineNumber);

            if (lower > upper)
                throw new WtfAreYouDoingException(scope, "The lower bound cannot be bigger than the upper bound.",
                        tFuncCall.lineNumber);

            if (lower == 6 && upper == 9)
                return 69;

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
                            .addExample("""
                                    khuluma("Rounded value of 4.6 is: " + m_round(4.6))!
                                    """)
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
            Object value = Primitives.toPrimitive(params.getFirst(), false, config,
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
                            .addDesc("Calculates the square root of the input value.")
                            .addParam("value", "number", "The number to find the square root of.", false)
                            .addReturns("Returns the (positive) square root of the number provided")
                            .addExample("""
                                    khuluma("The square root of 16 is: " + m_sqrt(16))!
                                    """)
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
            Object value = Primitives.toPrimitive(params.getFirst(), false, config,
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
            Object value = Primitives.toPrimitive(params.getFirst(), false, config,
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
            Object value = Primitives.toPrimitive(params.getFirst(), false, config,
                    scope);

            if (value instanceof Integer i)
                return i;
            else if (value instanceof Double d)
                return (int) java.lang.Math.ceil(d);
            else
                throw new FunctionParametersException(scope, this, "1", value, Number.class, tFuncCall.lineNumber);
        }
    }

    class FLog extends BaseFunction {
        FLog() {
            super("m_log", new TFunction("m_log", new String[] { "value", "base?" }, null, -1,
                    JDoc.builder()
                            .addDesc("Calculates the logarithm of a value with the specified base.")
                            .addParam("value", "number", "The value to calculate the logarithm for.", false)
                            .addParam("base", "number", "The base of the logarithm, otherwise 10 is used", true)
                            .addReturns("The logarithm of the value with the specified base.")
                            .addExample("""
                                    khuluma("Log base 10 of 1000 is: " + m_log(1000))!
                                    khuluma("Log base 2 of 1024 is: " + m_log(1024, 2))!
                                    """)
                            .sinceVersion("5.0.0")
                            .build()
            ));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config,
                Scope scope)
                throws Exception {
            checkParams(tFuncCall, scope);
            Object value = Primitives.toPrimitive(params.get(0), false, config,
                    scope);
            Object base = params.size() > 1
                    ? Primitives.toPrimitive(params.get(1), false, config, scope)
                    : 10;

            if (!(value instanceof Number))
                throw new FunctionParametersException(scope, this, "1", value, Number.class, tFuncCall.lineNumber);
            if (!(base instanceof Number))
                throw new FunctionParametersException(scope, this, "2", base, Number.class, tFuncCall.lineNumber);

            double val = ((Number) value).doubleValue();
            double bas = ((Number) base).doubleValue();


            return switch ((int) bas) {
                case 10 -> java.lang.Math.log10(val);
                case 2 -> java.lang.Math.log(val) / java.lang.Math.log(2);
                default -> java.lang.Math.log(val) / java.lang.Math.log(bas);
            };
        }
    }

    private ArrayList<Integer> toIntegerList(ArrayList<Object> params, String funcName, BaseFunction func, IConfig<Object> config, Scope scope, int lineNumber) throws Exception {
        ArrayList<Integer> nums = new ArrayList<>();
        for (int i = 0; i < params.size(); i++) {
            Object p = params.get(i);
            if (p instanceof Integer) {
                nums.add((Integer) p);
            } else {
                Object possible = Primitives.toPrimitive(p, false, config, scope);
                if (possible instanceof Integer possibleInt) {
                    if (possibleInt < 0) {
                        throw new InterpreterException.WtfAreYouDoingException(scope, funcName + " is only defined for non-negative integers", lineNumber);
                    }
                    nums.add(possibleInt);
                } else {
                    throw new InterpreterException.FunctionParametersException(scope, func, i + "", possible, Integer.class, lineNumber);
                }
            }
        }
        return nums;
    }


    /**
     * Computes the GCD of two integers using the Euclidean algorithm.
     *
     * @param a First integer
     * @param b Second integer
     * @return GCD of a and b
     */
    private static int computeGCD(int a, int b) {
        if (b == 0) {
            return a;
        }
        return computeGCD(b, a % b);
    }

    public class FGcd extends BaseFunction {
        public FGcd() {
            super("m_gcd",
                    new TFunction("m_gcd", new String[]{"<-nums"}, null, -1,
                            JDoc.builder()
                                    .addDesc("Calculates the greatest common divisor (GCD) of a list of numbers.")
                                    .addParam("nums", "array", "Variable amount of numbers to calculate the GCD for.", true)
                                    .addReturns("The greatest common divisor of the provided numbers.")
                                    .addExample("""
                                            khuluma(m_gcd(10, 20, 42, 20))! @ Outputs: 2
                                            khuluma(m_gcd(54, 24, 36))! @ Outputs: 6
                                            """)
                                    .sinceVersion("5.0.0")
                                    .build()
                    )
            );
        }


        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config, Scope scope) throws Exception {
            checkParams(tFuncCall, scope);
            if (params.size() < 2)
                throw new InterpreterException.FunctionParametersException(scope, this, params.size(), 2, tFuncCall.lineNumber);

            ArrayList<Integer> nums = toIntegerList(params, "gcd", this, config, scope, tFuncCall.lineNumber);

            int gcd = nums.get(0);
            for (int i = 1; i < nums.size(); i++) {
                gcd = computeGCD(gcd, nums.get(i));
                if (gcd == 1) {
                    break; // GCD is 1, no need to continue
                }
            }
            return gcd;

        }
    }

    public class FLcm extends BaseFunction {
        public FLcm() {
            super("m_lcm",
                    new TFunction("m_lcm", new String[]{"<-nums"}, null, -1,
                            JDoc.builder()
                                    .addDesc("Calculates the least common multiple (LCM) of a list of numbers.")
                                    .addParam("nums", "array", "Variable amount of numbers to calculate the LCM for.", true)
                                    .addReturns("The least common multiple of the provided numbers.")
                                    .addExample("""
                                            khuluma(m_lcm(4, 5, 6))! @ Outputs: 60
                                            khuluma(m_lcm(7, 3, 14))! @ Outputs: 42
                                            """)
                                    .sinceVersion("5.0.0")
                                    .build()
                    )
            );
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config, Scope scope) throws Exception {
            checkParams(tFuncCall, scope);
            if (params.size() < 2)
                throw new InterpreterException.FunctionParametersException(scope, this, params.size(), 2, tFuncCall.lineNumber);

            ArrayList<Integer> nums = toIntegerList(params, "lcm", this, config, scope, tFuncCall.lineNumber);

            int lcm = nums.get(0);
            for (int i = 1; i < nums.size(); i++) {
                int num = nums.get(i);
                lcm = (lcm * num) / computeGCD(lcm, num);
            }
            return lcm;
        }
    }

}
