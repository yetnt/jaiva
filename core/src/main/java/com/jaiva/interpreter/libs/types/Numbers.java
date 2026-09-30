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

@JaivaLibrary(path="types/numbers")
public class Numbers extends BaseLibrary {
    public Numbers() {
        add(
                new FDoubleToIEE754Long(),
                new FLongToIEEE754Double()
        );
    }

    @SymbolConfig(experimental = true)
    public static class FDoubleToIEE754Long extends BaseFunction {
        public FDoubleToIEE754Long() {
            super(
                    FunctionBuilder.start()
                            .name("t_dToIEEE754Long")
                            .arguments(
                                    Arguments.getInstance()
                                            .add(
                                                    new AArgument(
                                                            "double",
                                                            "The double to convert",
                                                            false,
                                                            Argument.Type.NUMBER
                                                    )
                                            )
                            )
                            .docs(
                                    JDoc.builder()
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

    @SymbolConfig(experimental = true)
    public static class FLongToIEEE754Double extends BaseFunction {
        public FLongToIEEE754Double() {
            super(
                    FunctionBuilder.start()
                            .name("t_lToIEEE754Double")
                            .arguments(
                                    Arguments.getInstance()
                                            .add(
                                                    new AArgument(
                                                            "long",
                                                            "The long integer to convert",
                                                            false,
                                                            Argument.Type.NUMBER
                                                    )
                                            )
                            )
                            .docs(
                                    JDoc.builder()
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
