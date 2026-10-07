package com.jaiva.interpreter.symbol.continuous;

import com.jaiva.errors.InterpreterException;
import com.jaiva.interpreter.Primitives;
import com.jaiva.interpreter.Scope;
import com.jaiva.interpreter.libBuilders.func.Argument;
import com.jaiva.interpreter.libBuilders.func.Arguments;
import com.jaiva.interpreter.libBuilders.func.FunctionBuilder;
import com.jaiva.interpreter.libBuilders.func.arg.AArgument;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.BaseFunction;
import com.jaiva.interpreter.symbol.inf.BFMethodToConsumer;
import com.jaiva.interpreter.symbol.inf.BFMethodToSupplier;
import com.jaiva.tokenizer.tokens.Token;
import com.jaiva.tokenizer.tokens.specific.TFuncCall;

import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class ContinuousFunction<T extends Closeable> extends BaseFunction {

    public enum ContinuousFunctionType {INPUT, OUTPUT}
    T closeable;
    ContinuousFunctionType continuousFunctionType;

    CFClosures.Input inputStreamClosure = null;
    CFClosures.Output outputStreamClosure = null;
    CFClosures.Close closeClosure;
    CFClosures.Flush flushClosure;


    private ContinuousFunction(
            IConfig<Object> config,
            T closeable,
            ContinuousFunctionType continuousFunctionType,
            BFMethodToSupplier<?> readStreamSupplier,
            BFMethodToConsumer<Object> writeStreamConsumer
    ) {
        super(
                // docs wont matter here, all the stuff here is purely to create
                // the TFunction token.
                FunctionBuilder.start()
                        .name("continuousFunction")
                        .arguments(Arguments.getInstance().add(
                                new AArgument(
                                        "string",
                                        "The input string ",
                                        true,
                                        Argument.Type.ANY
                                )
                        ))
        );
        this.closeable = closeable;
        this.continuousFunctionType = continuousFunctionType;

        inputStreamClosure = new CFClosures.Input(readStreamSupplier);
        outputStreamClosure = new CFClosures.Output(writeStreamConsumer);
        closeClosure = new CFClosures.Close(
                config.getGlobalResources().ofCurrentThread().addResource(closeable)
        );
        flushClosure = new CFClosures.Flush(closeable);
    }

    public static <T extends InputStream> ContinuousFunction<T> from(
            IConfig<Object> config, T inputStream,
            BFMethodToSupplier<?> inputStreamConsumer
    ) {
        return new ContinuousFunction<T>(config, inputStream, ContinuousFunctionType.INPUT, inputStreamConsumer, null);
    }

    public static <T extends OutputStream> ContinuousFunction<T> from(
            IConfig<Object> config, T outputStream,
            BFMethodToConsumer<Object> outputStreamConsumer
    ) {
        return new ContinuousFunction<T>(config, outputStream, ContinuousFunctionType.OUTPUT, null, outputStreamConsumer);
    }

    @Override
    public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config, Scope scope) throws Exception {
        // do all the handling logic dispatch and new function creation shenangians
        checkParams(tFuncCall, scope);
        if (!params.isEmpty()) {
            Object prot = Primitives.toPrimitive(params.getFirst(), false, config, scope);
            if (!(prot instanceof String protocol))
                throw new InterpreterException.FunctionParametersException(
                        scope, this, "1", prot, String.class, tFuncCall.lineNumber
                );

            protocol = protocol.toLowerCase();
            if (!CFuncInput.isValid(protocol))
                throw new InterpreterException.WtfAreYouDoingException(
                        scope, "A continious function can only take string inputs of: " +
                        " \"read\", \"write\", \"flush\" or \"close\"!", tFuncCall.lineNumber
                );

            // isValid allows this to pass.
            CFuncInput input = CFuncInput.fromChar(protocol.charAt(0));

            assert input != null; // it won't be null but java.
            return switch (input) {
                case READ -> inputStreamClosure;
                case WRITE -> outputStreamClosure;
                case CLOSE -> closeClosure;
                case FLUSH -> flushClosure;
            };
        }
        return Token.voidValue(tFuncCall.lineNumber);
    }

}
