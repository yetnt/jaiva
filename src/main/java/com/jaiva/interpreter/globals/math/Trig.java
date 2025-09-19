package com.jaiva.interpreter.globals.math;

import java.util.ArrayList;

import com.jaiva.interpreter.Scope;
import com.jaiva.interpreter.Primitives;
import com.jaiva.interpreter.globals.BaseGlobals;
import com.jaiva.interpreter.globals.GlobalType;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.BaseFunction;
import com.jaiva.tokenizer.Token.*;
import com.jaiva.errors.InterpreterException.FunctionParametersException;

public class Trig extends BaseGlobals {
    public Trig() {
        super(GlobalType.CONTAINER);
        // This is a container class for the Math class, so prefix everything with "m_"
        vfs.put("m_sin", new FSin());
        vfs.put("m_cos", new FCos());
        vfs.put("m_tan", new FTan());
        vfs.put("m_asin", new FAsin());
        vfs.put("m_acos", new FAcos());
        vfs.put("m_atan", new FAtan());
        vfs.put("m_toRad", new FToRad());
        vfs.put("m_toDeg", new FToDeg());
    }

    /**
     * A function that returns the sine of a number in radians.
     * <p>
     * Usage:
     * <ul>
     * <li>{@code m_sin(value)} - Returns the sine of the given value in
     * radians.</li>
     * </ul>
     * </p>
     */
    class FSin extends BaseFunction {
        FSin() {
            super("m_sin", new TFunction("m_sin", new String[] { "value" }, null, -1,
                    "Returns the sine of a number in radians."));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config,
                Scope scope)
                throws Exception {
            checkParams(tFuncCall, scope);
            Object v = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.getFirst()), false, config, scope);
            // Ensure the first parameter is a number
            if (!(v instanceof Number)) {
                throw new FunctionParametersException(scope, this, "1", v, Number.class, tFuncCall.lineNumber);
            }
            // Calculate the arctangent of the number
            double value = ((Number) v).doubleValue();
            return java.lang.Math.sin(value);
        }
    }

    /**
     * A function that returns the cosine of a number in radians.
     * <p>
     * Usage:
     * <ul>
     * <li>{@code m_cos(value)} - Returns the cosine of the given value in
     * radians.</li>
     * </ul>
     * </p>
     */
    class FCos extends BaseFunction {
        FCos() {
            super("m_cos", new TFunction("m_cos", new String[] { "value" }, null, -1,
                    "Returns the cosine of a number in radians."));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config,
                Scope scope)
                throws Exception {
            checkParams(tFuncCall, scope);
            Object v = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.getFirst()), false, config, scope);
            // Ensure the first parameter is a number
            if (!(v instanceof Number)) {
                throw new FunctionParametersException(scope, this, "1", v, Number.class, tFuncCall.lineNumber);
            }
            // Calculate the arctangent of the number
            double value = ((Number) v).doubleValue();
            return java.lang.Math.cos(value);
        }
    }

    /**
     * A function that returns the tangent of a number in radians.
     * <p>
     * Usage:
     * <ul>
     * <li>{@code m_tan(value)} - Returns the tangent of the given value in
     * radians.</li>
     * </ul>
     * </p>
     */
    class FTan extends BaseFunction {
        FTan() {
            super("m_tan", new TFunction("m_tan", new String[] { "value" }, null, -1,
                    "Returns the tangent of a number in radians."));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config,
                Scope scope)
                throws Exception {
            checkParams(tFuncCall, scope);
            Object v = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.getFirst()), false, config, scope);
            // Ensure the first parameter is a number
            if (!(v instanceof Number)) {
                throw new FunctionParametersException(scope, this, "1", v, Number.class, tFuncCall.lineNumber);
            }
            // Calculate the arctangent of the number
            double value = ((Number) v).doubleValue();
            return java.lang.Math.tan(value);
        }
    }

    /**
     * A function that returns the arcsine of a number in radians.
     * <p>
     * Usage:
     * <ul>
     * <li>{@code m_asin(value)} - Returns the arcsine of the given value in
     * radians.</li>
     * </ul>
     * </p>
     */
    class FAsin extends BaseFunction {
        FAsin() {
            super("m_asin", new TFunction("m_asin", new String[] { "value" }, null, -1,
                    "Returns the arcsine of a number in radians."));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config,
                Scope scope)
                throws Exception {
            checkParams(tFuncCall, scope);
            Object v = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.getFirst()), false, config, scope);
            // Ensure the first parameter is a number
            if (!(v instanceof Number)) {
                throw new FunctionParametersException(scope, this, "1", v, Number.class, tFuncCall.lineNumber);
            }
            // Calculate the arctangent of the number
            double value = ((Number) v).doubleValue();
            return java.lang.Math.asin(value);
        }
    }

    /**
     * A function that returns the arccosine of a number in radians.
     * <p>
     * Usage:
     * <ul>
     * <li>{@code m_acos(value)} - Returns the arccosine of the given value in
     * radians.</li>
     * </ul>
     * </p>
     */
    class FAcos extends BaseFunction {
        FAcos() {
            super("m_acos", new TFunction("m_acos", new String[] { "value" }, null, -1,
                    "Returns the arccosine of a number in radians."));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config,
                Scope scope)
                throws Exception {
            checkParams(tFuncCall, scope);
            Object v = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.getFirst()), false, config, scope);
            // Ensure the first parameter is a number
            if (!(v instanceof Number)) {
                throw new FunctionParametersException(scope, this, "1", v, Number.class, tFuncCall.lineNumber);
            }
            // Calculate the arctangent of the number
            double value = ((Number) v).doubleValue();
            return java.lang.Math.acos(value);
        }
    }

    /**
     * A function that returns the arctangent of a number in radians.
     * <p>
     * Usage:
     * <ul>
     * <li>{@code m_atan(value)} - Returns the arctangent of the given value in
     * radians.</li>
     * </ul>
     * </p>
     */
    class FAtan extends BaseFunction {
        FAtan() {
            super("m_atan", new TFunction("m_atan", new String[] { "value" }, null, -1,
                    "Returns the arctangent of a number in radians."));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config,
                Scope scope)
                throws Exception {
            checkParams(tFuncCall, scope);
            Object v = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.getFirst()), false, config, scope);
            // Ensure the first parameter is a number
            if (!(v instanceof Number)) {
                throw new FunctionParametersException(scope, this, "1", v, Number.class, tFuncCall.lineNumber);
            }
            // Calculate the arctangent of the number
            double value = ((Number) v).doubleValue();
            return java.lang.Math.atan(value);
        }
    }

    /**
     * A function that converts degrees to radians.
     * <p>
     * Usage:
     * <ul>
     * <li>{@code m_toRad(degrees)} - Converts the given degrees to radians.</li>
     * </ul>
     * </p>
     */
    class FToRad extends BaseFunction {
        FToRad() {
            super("m_toRad", new TFunction("m_toRad", new String[] { "degrees" }, null, -1,
                    "Converts degrees to radians."));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config,
                Scope scope)
                throws Exception {
            checkParams(tFuncCall, scope);
            Object value = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.getFirst()), false, config,
                    scope);
            // Ensure the first parameter is a number
            if (!(value instanceof Number)) {
                throw new FunctionParametersException(scope, this, "1", value, Number.class, tFuncCall.lineNumber);
            }
            // Convert degrees to radians
            double degrees = ((Number) value).doubleValue();
            return java.lang.Math.toRadians(degrees);
        }
    }

    /**
     * A function that converts radians to degrees.
     * <p>
     * Usage:
     * <ul>
     * <li>{@code m_toDeg(radians)} - Converts the given radians to degrees.</li>
     * </ul>
     * </p>
     */
    class FToDeg extends BaseFunction {
        FToDeg() {
            super("m_toDeg", new TFunction("m_toDeg", new String[] { "radians" }, null, -1,
                    "Converts radians to degrees."));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config,
                Scope scope)
                throws Exception {
            checkParams(tFuncCall, scope);
            Object value = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.getFirst()), false, config,
                    scope);
            // Ensure the first parameter is a number
            if (!(value instanceof Number)) {
                throw new FunctionParametersException(scope, this, "1", value, Number.class, tFuncCall.lineNumber);
            }
            // Convert radians to degrees
            double radians = ((Number) value).doubleValue();
            return java.lang.Math.toDegrees(radians);
        }
    }
}