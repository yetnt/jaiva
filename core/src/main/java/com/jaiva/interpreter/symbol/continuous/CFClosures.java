package com.jaiva.interpreter.symbol.continuous;

import com.jaiva.interpreter.Primitives;
import com.jaiva.interpreter.Scope;
import com.jaiva.interpreter.libBuilders.func.FunctionBuilder;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.BaseFunction;
import com.jaiva.interpreter.symbol.inf.BFMethodToConsumer;
import com.jaiva.interpreter.symbol.inf.BFMethodToSupplier;
import com.jaiva.tokenizer.tokens.Token;
import com.jaiva.tokenizer.tokens.specific.TFuncCall;
import com.jaiva.utils.ThrowableSupplier;
import com.yetnt.utils.functional.consumer.ThrowableConsumer;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class CFClosures {
    public static class Input extends BaseFunction {
        BFMethodToSupplier<?> readStreamSupplier;

        public Input(BFMethodToSupplier<?> rss) {
            super(FunctionBuilder.start());
            this.readStreamSupplier = rss;
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config, Scope scope) throws Exception {
            // parse every object in params
            if (readStreamSupplier == null) return Token.voidValue(tFuncCall.lineNumber);
            ArrayList<Object> parsedParams = new ArrayList<>();
            if (!params.isEmpty() && (params.size() != 1 || params.getFirst() != null)) {
                for (Object p : params) {
                    parsedParams.add(Primitives.toPrimitive(p, false, config, scope));
                }
            }

            ThrowableSupplier<?, IOException> method
                    = readStreamSupplier.apply(tFuncCall, parsedParams, config, scope);

            // TODO: Handle types, for now if its String, Int, Long, Double, or Boolean, its fine.
            // TODO: If it's a byte, then convert to int.
            // TODO: Otherwise, we're fucked.

            Object out = method.get();

            if (out instanceof Byte b) {
                return TypeConverter.fromByte(b);
            } else {
                return out; // pray.
            }
        }
    }

    public static class Output extends BaseFunction {
        BFMethodToConsumer<Object> writeStreamConsumer;

        public Output(BFMethodToConsumer<Object> wsc) {
            super(FunctionBuilder.start());
            this.writeStreamConsumer = wsc;
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config, Scope scope) throws Exception {
            // parse every object in params
            if (writeStreamConsumer == null) return Token.voidValue(tFuncCall.lineNumber);
            ArrayList<Object> parsedParams = new ArrayList<>();
            for (Object p : params) {
                parsedParams.add(Primitives.toPrimitive(p, false, config, scope));
            }

            ThrowableConsumer<Object, IOException> method
                    = writeStreamConsumer.apply(tFuncCall, parsedParams, config, scope);

            // TODO: Handle types, for now if its String, Int, Long, Double, or Boolean, its fine.
            // TODO: If it's a byte, then convert to int.
            // TODO: Otherwise, we're fucked.
            // TODO: In this case, we just consume the first argument within paras. Caller is responbisle for mutating params

            method.apply(parsedParams.getFirst());
            return Token.voidValue(tFuncCall.lineNumber);
        }
    }

    public static class Flush extends BaseFunction {
        Closeable cls;
        public Flush(Closeable cls) {
            super(FunctionBuilder.start());
            this.cls = cls;
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config, Scope scope) throws Exception {
            // we dont care about args.
            if (!(cls instanceof OutputStream os)) return Token.voidValue(tFuncCall.lineNumber);
            os.flush();
            return Token.voidValue(tFuncCall.lineNumber);
        }
    }

    public static class Close extends BaseFunction {
        UUID resourceId;
        public Close(UUID resourceId) {
            super(FunctionBuilder.start());
            this.resourceId = resourceId;
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config, Scope scope) throws Exception {
            // dont care about args again
            config.getGlobalResources().ofCurrentThread().releaseResource(resourceId);
            return Token.voidValue(tFuncCall.lineNumber);
        }
    }
}
