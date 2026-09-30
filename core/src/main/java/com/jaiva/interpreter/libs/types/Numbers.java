package com.jaiva.interpreter.libs.types;

import com.jaiva.errors.InterpreterException;
import com.jaiva.interpreter.Primitives;
import com.jaiva.interpreter.Scope;
import com.jaiva.interpreter.libBuilders.func.Argument;
import com.jaiva.interpreter.libBuilders.func.Arguments;
import com.jaiva.interpreter.libBuilders.func.FunctionBuilder;
import com.jaiva.interpreter.libBuilders.func.arg.AArgument;
import com.jaiva.interpreter.libs.BaseLibrary;
import com.jaiva.interpreter.libs.annotation.JaivaLibrary;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.BaseFunction;
import com.jaiva.interpreter.symbol.SymbolConfig;
import com.jaiva.tokenizer.jdoc.JDoc;
import com.jaiva.tokenizer.jdoc.JDocBuilder;
import com.jaiva.tokenizer.tokens.specific.TFuncCall;

import java.util.ArrayList;

@JaivaLibrary(path="types/numbers", description = "More complicated converters")
public class Numbers extends BaseLibrary {
    public Numbers() {
        add(
                new FDoubleToIEE754Long(),
                new FLongToIEEE754Double()
        );
    }

    public static class FDoubleToIEE754Long extends BaseFunction {
        public FDoubleToIEE754Long() {
            super(
                    FunctionBuilder.start()
                            .name("t_dToIEEE754Long")
                            .arguments(
                                    Arguments.getInstance().add(
                                            new AArgument(
                                                    "double", "The double to get the bits off",
                                                    false, Argument.Type.NUMBER
                                            )
                                    )
                            )
                            .docs(
                                    JDoc.builder()
                                            .addDesc(
                                                    "Returns the representation of the given double, as a long, where it's bits "
                                                    + "correspond to the IEEE 754 floating-point \"double format\" bit layout."
                                            )
                                            .addReturns("the bits that represent the double")
                                            .sinceVersion("6.0.0-beta.5")
                            )
            );
            freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config, Scope scope) throws Exception {
            checkParams(tFuncCall, scope);

            Object val = Primitives.toPrimitive(params.getFirst(), false,
                    config, scope);

            if (!(val instanceof Double d))
                throw new InterpreterException.FunctionParametersException(scope, this, "1", val, Double.class,
                        tFuncCall.lineNumber);

            return Double.doubleToLongBits(d);
        }
    }

    public static class FLongToIEEE754Double extends BaseFunction {
        public FLongToIEEE754Double() {
            super(
                    FunctionBuilder.start()
                            .name("t_lToIEEE754Double")
                            .arguments(
                                    Arguments.getInstance().add(
                                            new AArgument(
                                                    "long", "The representation of the double as a long",
                                                    false, Argument.Type.NUMBER
                                            )
                                    )
                            )
                            .docs(
                                    JDoc.builder()
                                            .addDesc(
                                                    "Returns the double value of which this long is assumed to be a representation of "
                                                    + "the IEEE 754 floating-point \"double format\" bit layout."
                                            )
                                            .addReturns("the double this long's bits represent.")
                                            .sinceVersion("6.0.0-beta.5")
                            )
            );
            freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config, Scope scope) throws Exception {
            checkParams(tFuncCall, scope);

            Object val = Primitives.toPrimitive(params.getFirst(), false,
                    config, scope);

            if (!(val instanceof Long l))
                throw new InterpreterException.FunctionParametersException(scope, this, "1", val, Long.class,
                        tFuncCall.lineNumber);

            return Double.longBitsToDouble(l);
        }
    }
}
