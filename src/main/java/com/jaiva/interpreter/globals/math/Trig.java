package com.jaiva.interpreter.globals.math;

import java.util.ArrayList;
import java.util.HashMap;

import com.jaiva.interpreter.ContextTrace;
import com.jaiva.interpreter.MapValue;
import com.jaiva.interpreter.Primitives;
import com.jaiva.interpreter.globals.BaseGlobals;
import com.jaiva.interpreter.globals.GlobalType;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.BaseFunction;
import com.jaiva.tokenizer.Token;
import com.jaiva.tokenizer.Token.TFuncCall;
import com.jaiva.errors.InterpreterException.FunctionParametersException;

public class Trig extends BaseGlobals {
    public Trig() {
        super(GlobalType.CONTAINER);
        // This is a container class for the Math class, so prefix everything with "m_"
        vfs.put("m_sin", new MapValue(new FSin(container)));
        vfs.put("m_cos", new MapValue(new FCos(container)));
        vfs.put("m_tan", new MapValue(new FTan(container)));
        vfs.put("m_asin", new MapValue(new FAsin(container)));
        vfs.put("m_acos", new MapValue(new FAcos(container)));
        vfs.put("m_atan", new MapValue(new FAtan(container)));
        vfs.put("m_toRad", new MapValue(new FToRad(container)));
        vfs.put("m_toDeg", new MapValue(new FToDeg(container)));
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
        FSin(Token<?> container) {
            super("m_sin", container.new TFunction("m_sin", new String[] { "value" }, null, -1,
                    "Returns the sine of a number in radians."));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs, IConfig config,
                ContextTrace cTrace)
                throws Exception {
            checkParams(tFuncCall, cTrace);
            Object v = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(0)), vfs, false, config, cTrace);
            // Ensure the first parameter is a number
            if (!(v instanceof Number)) {
                throw new FunctionParametersException(cTrace, this, "1", v, Number.class, tFuncCall.lineNumber);
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
        FCos(Token<?> container) {
            super("m_cos", container.new TFunction("m_cos", new String[] { "value" }, null, -1,
                    "Returns the cosine of a number in radians."));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs, IConfig config,
                ContextTrace cTrace)
                throws Exception {
            checkParams(tFuncCall, cTrace);
            Object v = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(0)), vfs, false, config, cTrace);
            // Ensure the first parameter is a number
            if (!(v instanceof Number)) {
                throw new FunctionParametersException(cTrace, this, "1", v, Number.class, tFuncCall.lineNumber);
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
        FTan(Token<?> container) {
            super("m_tan", container.new TFunction("m_tan", new String[] { "value" }, null, -1,
                    "Returns the tangent of a number in radians."));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs, IConfig config,
                ContextTrace cTrace)
                throws Exception {
            checkParams(tFuncCall, cTrace);
            Object v = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(0)), vfs, false, config, cTrace);
            // Ensure the first parameter is a number
            if (!(v instanceof Number)) {
                throw new FunctionParametersException(cTrace, this, "1", v, Number.class, tFuncCall.lineNumber);
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
        FAsin(Token<?> container) {
            super("m_asin", container.new TFunction("m_asin", new String[] { "value" }, null, -1,
                    "Returns the arcsine of a number in radians."));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs, IConfig config,
                ContextTrace cTrace)
                throws Exception {
            checkParams(tFuncCall, cTrace);
            Object v = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(0)), vfs, false, config, cTrace);
            // Ensure the first parameter is a number
            if (!(v instanceof Number)) {
                throw new FunctionParametersException(cTrace, this, "1", v, Number.class, tFuncCall.lineNumber);
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
        FAcos(Token<?> container) {
            super("m_acos", container.new TFunction("m_acos", new String[] { "value" }, null, -1,
                    "Returns the arccosine of a number in radians."));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs, IConfig config,
                ContextTrace cTrace)
                throws Exception {
            checkParams(tFuncCall, cTrace);
            Object v = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(0)), vfs, false, config, cTrace);
            // Ensure the first parameter is a number
            if (!(v instanceof Number)) {
                throw new FunctionParametersException(cTrace, this, "1", v, Number.class, tFuncCall.lineNumber);
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
        FAtan(Token<?> container) {
            super("m_atan", container.new TFunction("m_atan", new String[] { "value" }, null, -1,
                    "Returns the arctangent of a number in radians."));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs, IConfig config,
                ContextTrace cTrace)
                throws Exception {
            checkParams(tFuncCall, cTrace);
            Object v = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(0)), vfs, false, config, cTrace);
            // Ensure the first parameter is a number
            if (!(v instanceof Number)) {
                throw new FunctionParametersException(cTrace, this, "1", v, Number.class, tFuncCall.lineNumber);
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
        FToRad(Token<?> container) {
            super("m_toRad", container.new TFunction("m_toRad", new String[] { "degrees" }, null, -1,
                    "Converts degrees to radians."));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs, IConfig config,
                ContextTrace cTrace)
                throws Exception {
            checkParams(tFuncCall, cTrace);
            Object value = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(0)), vfs, false, config,
                    cTrace);
            // Ensure the first parameter is a number
            if (!(value instanceof Number)) {
                throw new FunctionParametersException(cTrace, this, "1", value, Number.class, tFuncCall.lineNumber);
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
        FToDeg(Token<?> container) {
            super("m_toDeg", container.new TFunction("m_toDeg", new String[] { "radians" }, null, -1,
                    "Converts radians to degrees."));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs, IConfig config,
                ContextTrace cTrace)
                throws Exception {
            checkParams(tFuncCall, cTrace);
            Object value = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(0)), vfs, false, config,
                    cTrace);
            // Ensure the first parameter is a number
            if (!(value instanceof Number)) {
                throw new FunctionParametersException(cTrace, this, "1", value, Number.class, tFuncCall.lineNumber);
            }
            // Convert radians to degrees
            double radians = ((Number) value).doubleValue();
            return java.lang.Math.toDegrees(radians);
        }
    }
}