package com.jaiva.interpreter.libs.threads;

import com.jaiva.errors.InterpreterException;
import com.jaiva.interpreter.Primitives;
import com.jaiva.interpreter.Scope;
import com.jaiva.interpreter.libBuilders.func.Arguments;
import com.jaiva.interpreter.libBuilders.func.FunctionBuilder;
import com.jaiva.interpreter.libBuilders.func.arg.AFuncArgument;
import com.jaiva.interpreter.libs.BaseLibrary;
import com.jaiva.interpreter.libs.annotation.JaivaLibrary;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.BaseFunction;
import com.jaiva.tokenizer.jdoc.JDoc;
import com.jaiva.tokenizer.jdoc.JDocBuilder;
import com.jaiva.tokenizer.tokens.Token;
import com.jaiva.tokenizer.tokens.specific.TFuncCall;
import com.jaiva.tokenizer.tokens.specific.TVoidValue;
import com.jaiva.utils.ThrowableRunnable;
import com.yetnt.utils.builders.AnsiColour;
import com.yetnt.utils.functional.consumer.ThrowableBiConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

@JaivaLibrary(
        path = "threads",
        description = "Enables threading via function scopes executing on their own thread. Don't ask me" +
                " about 2 threads mutating the same variable... That's ur problem bro."
)
public class ThreadsLib extends BaseLibrary {
    public ThreadsLib() {
        add(new FThread());
    }

    private static class FThread extends BaseFunction {
        public FThread() {
            super(
                    FunctionBuilder.start()
                            .name("thread")
                            .arguments(
                                    Arguments.getInstance()
                                            .add(
                                                    new AFuncArgument(
                                                            "f",
                                                            "The function to execute in it's own thread." +
                                                                    " A function that should take no args.",
                                                            false
                                                    )
                                            )
                                            .add(
                                                    new AFuncArgument(
                                                            "f2",
                                                            "The function to execute as cleanup. This" +
                                                                    " function is expected to take 2 string arguments",
                                                            true
                                                    )
                                            )
                            )
                            .docs(JDoc.builder().sinceVersion("6.1.0"))
            );
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config, Scope scope) throws Exception {
            checkParams(tFuncCall, scope);
            Object funcToCall = Primitives.toPrimitive(
                    params.getFirst(), false, config, scope
            );
            if (!(funcToCall instanceof BaseFunction function))
                throw new InterpreterException.FunctionParametersException(
                        scope, this, "1", funcToCall, BaseFunction.class, tFuncCall.lineNumber
                );

            BaseFunction cleanup;
            if (params.size() > 1) {
                Object funcForCleanup = Primitives.toPrimitive(
                        params.get(1), false, config, scope
                );
                if (!(funcForCleanup instanceof BaseFunction) && !(funcForCleanup instanceof TVoidValue))
                    throw new InterpreterException.FunctionParametersException(
                            scope, this, "1", funcForCleanup, BaseFunction.class, tFuncCall.lineNumber
                    );

                // if they put idk the value, then its the same as not putting a value.
                cleanup = funcForCleanup instanceof BaseFunction ? (BaseFunction) funcForCleanup : null;
            } else {
                cleanup = null;
            }

            ThrowableRunnable<Exception> runnable = () -> {
                function.call(
                        new ArrayList<>(), config, scope, tFuncCall
                );
            };

            ThrowableBiConsumer<ThreadedInterp.CleanupCode, Exception, Exception> cleanupHandler = (code, ex) -> {
                // check if clean-up is non-null firs,t otherwise just exit
                if (cleanup == null) return;

                // if the exception belongs to Jaiva remove colours.
                String exceptionMessage =
                        ex != null ?
                        AnsiColour.remove(ex.getMessage()) : "";

                cleanup.call(
                        new ArrayList<>(List.of(code.toString(), exceptionMessage)),
                        config, scope, tFuncCall
                );
            };

            ThreadedInterp thread = new ThreadedInterp(config, runnable, cleanupHandler);

            thread.start();

            return thread.threadId();

        }
    }
}
