package com.jaiva.interpreter.globals;

import java.util.ArrayList;

import com.jaiva.errors.InterpreterException.FunctionParametersException;
import com.jaiva.errors.InterpreterException.WtfAreYouDoingException;
import com.jaiva.interpreter.Scope;
import com.jaiva.interpreter.Primitives;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.BaseFunction;
import com.jaiva.lang.Keywords;
import com.jaiva.tokenizer.Token;
import com.jaiva.tokenizer.Token.*;

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
public class Types extends BaseGlobals {
    public Types() {
        // the import will be "jaiva/types.jiv"
        super(GlobalType.LIB, "types");
        vfs.put("t_num", new FNum());
        vfs.put("t_str", new FStr());
        vfs.put("t_of", new FOf());
    }

    /**
     * 
     * t_num(string, radix?) -> number
     *
     */
    class FNum extends BaseFunction {
        FNum() {
            super("t_num", new TFunction("t_num", new String[] { "string", "radix?" }, null, -1,
                    "converts a given **string** to a number with an optional **radix**. (Jaiva integer prefixes [such as _0x_ or _0b_], are checked for first before the radix)"));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config,
                Scope scope)
                throws Exception {
            this.checkParams(tFuncCall, scope);
            Object val = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.getFirst()), false, config,
                    scope);

            if (!(val instanceof String value))
                throw new WtfAreYouDoingException(scope, params.getFirst() + " cannot become a number kau",
                        tFuncCall.lineNumber);

            int type = value.startsWith("0b") ? 2 : value.startsWith("0x") ? 16 : value.startsWith("0c") ? 8 : -1;
            value = type != -1 ? value.substring(2) : value;
            try {
                if (value.contains("."))
                    return Double.parseDouble(value);
                else {
                    if (params.size() == 1)
                        return Integer.parseInt(value, type != -1 ? type : 10);

                    Object r = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(1)), false, config,
                            scope);
                    if (!(r instanceof TVoidValue) && !(r instanceof Integer))
                        throw new FunctionParametersException(scope, this, "2", tFuncCall.lineNumber);
                    int radix = r instanceof TVoidValue ? -1 : (int) r;

                    return Integer.parseInt(value, type != -1 ? type : radix != -1 ? radix : 10);
                }
            } catch (NumberFormatException e) {
                throw new WtfAreYouDoingException(scope, params.getFirst() + " cannot become a number kau",
                        tFuncCall.lineNumber);
            }
        }
    }

    /**
     * t_str(input?, radix?) -> string
     * <p>
     * Input is marked as optional to allow passing of {@link TVoidValue} into it.
     */
    class FStr extends BaseFunction {
        FStr() {
            super("t_str", new TFunction("t_str", new String[] { "input?", "radix?" }, null, -1,
                    "converts any **input** of any given type to a string. With an optional **radix** input for converting integers."));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config,
                Scope scope)
                throws Exception {
            this.checkParams(tFuncCall, scope);
            Object val = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(0)), false, config,
                    scope);

            if (val instanceof Double || val instanceof BaseFunction) {
                return val.toString();
            } else if (val instanceof Boolean b)
                // makes it such that, true and false even though they are valid keywords, we
                // return Jaiva's true and false all the time.
                return b ? Keywords.TRUE : Keywords.FALSE;
            else if (val instanceof Integer integer) {
                if (params.size() == 1)
                    return val.toString();
                Object r = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(1)), false, config,
                        scope);
                if (!(r instanceof TVoidValue) && !(r instanceof Integer))
                    throw new FunctionParametersException(scope, this, "2", tFuncCall.lineNumber);
                int radix = r instanceof TVoidValue ? null : (int) r;
                return switch (radix) {
                    case 2 -> "0b" + Integer.toBinaryString(integer);
                    case 16 -> "0x" + Integer.toHexString(integer).toUpperCase();
                    case 8 -> "0c" + Integer.toOctalString(integer);
                    default -> Integer.toString(integer, radix);
                };
            } else if (val instanceof TVoidValue v) {
                return v.toString();
            } else {
                throw new WtfAreYouDoingException(scope, params.get(0) + " cannot become a string kau",
                        tFuncCall.lineNumber);
            }
        }
    }

    class FOf extends BaseFunction {
        FOf() {
            super("t_of", new TFunction("t_of", new String[] { "input?" }, null, -1,
                    "Returns the type of any given input. Which could be \"array\", \"string\", \"boolean\", \"number\", \"function\", or the primitive idk."));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config,
                Scope scope)
                throws Exception {

            this.checkParams(tFuncCall, scope);
            if (params.isEmpty())
                return Token.voidValue(tFuncCall.lineNumber);
            Object val = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.getFirst()), false, config,
                    scope);

            return switch (val) {
                case ArrayList arrayList -> "array";
                case Boolean b -> "boolean";
                case Number number -> "number";
                case BaseFunction baseFunction -> "function";
                case String s -> "string";
                case null, default ->
// there is no other type to possibly check for.
                        Token.voidValue(tFuncCall.lineNumber);
            };
        }
    }

}
