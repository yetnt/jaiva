package com.jaiva.interpreter.globals;

import java.util.ArrayList;
import java.util.HashMap;

import com.jaiva.errors.InterpreterException.WtfAreYouDoingException;
import com.jaiva.interpreter.MapValue;
import com.jaiva.interpreter.Primitives;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.BaseFunction;
import com.jaiva.tokenizer.Token;
import com.jaiva.tokenizer.Token.TFuncCall;

/**
 * The {@code Conversions} class provides global conversion functions for the
 * Jaiva interpreter.
 * <p>
 * It extends {@link BaseGlobals} and registers utility functions for converting
 * between
 * strings and numbers, such as {@code stringToNum} and {@code numToString},
 * into the vfs.
 * </p>
 *
 * <ul>
 * <li>{@code stringToNum}: Converts a string representation of a number into an
 * actual numeric type (Integer or Double).</li>
 * <li>{@code numToString}: Converts a numeric value or boolean into its string
 * representation.</li>
 * </ul>
 *
 * <p>
 * If conversion fails, these functions throw a {@link WtfAreYouDoingException}
 * with a descriptive error message.
 * </p>
 *
 * @see BaseGlobals
 * @see WtfAreYouDoingException
 */
@SuppressWarnings("rawtypes")
public class Conversions extends BaseGlobals {
    @SuppressWarnings("unchecked")
    public Conversions() {
        // the import will be "jaiva/convert.jiv"
        super(GlobalType.LIB, "convert");
        vfs.put("stringToNum", new MapValue(new FStringToNum(container)));
    }

    /**
     * A function that converts a string representation of a number into its numeric
     * value.
     * <p>
     * If the string contains a decimal point, it is parsed as a {@code Double};
     * otherwise,
     * it is parsed as an {@code Integer}. If the input cannot be parsed as a
     * number,
     * a {@link WtfAreYouDoingException} is thrown.
     * </p>
     *
     * <p>
     * Usage: {@code stringToNum("123")} returns {@code 123} (Integer),
     * {@code stringToNum("123.45")} returns {@code 123.45} (Double).
     * </p>
     *
     * @throws WtfAreYouDoingException if the input cannot be converted to a number
     */
    class FStringToNum extends BaseFunction {
        FStringToNum(Token<?> tContainer) {
            super("stringToNum", tContainer.new TFunction("stringToNum", new String[] { "number" }, null, -1,
                    "converts a string to a number"));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs, IConfig config)
                throws Exception {
            this.checkParams(tFuncCall);
            Object val = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(0)), vfs, false, config);

            if (!(val instanceof String value))
                throw new WtfAreYouDoingException(params.get(0) + " cannot become a number kau", tFuncCall.lineNumber);

            try {
                if (value.contains("."))
                    return Double.parseDouble(value);
                else
                    return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new WtfAreYouDoingException(params.get(0) + " cannot become a number kau", tFuncCall.lineNumber);
            }
            // }
        }
    }

    /**
     * FNumToString is a function that converts a numeric or boolean value to its
     * string representation.
     * <p>
     * This function is registered as "numToString" and expects a single parameter.
     * If the parameter is a {@link Number} or {@link Boolean}, it returns the
     * string representation of the value.
     * Otherwise, it throws a {@link WtfAreYouDoingException}.
     * </p>
     *
     */
    class FNumToString extends BaseFunction {
        FNumToString(Token<?> tContainer) {
            super("numToString", tContainer.new TFunction("numToString", new String[] { "string" }, null, -1,
                    "converts a number to a string"));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs, IConfig config)
                throws Exception {
            this.checkParams(tFuncCall);
            Object val = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(0)), vfs, false, config);

            if (val instanceof Number || val instanceof Boolean) {
                return val.toString();
            } else {
                throw new WtfAreYouDoingException(params.get(0) + " cannot become a string kau", tFuncCall.lineNumber);
            }
        }
    }

    {
        vfs.put("numToString", new MapValue(new FNumToString(container)));
    }
}
