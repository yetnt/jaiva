package com.jaiva.interpreter.symbol.inf;

import com.jaiva.interpreter.Scope;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.tokenizer.tokens.specific.TFuncCall;
import com.yetnt.utils.functional.consumer.ThrowableConsumer;
import com.yetnt.utils.functional.function.ThrowableQuadFunction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;

public interface BFMethodToConsumer<T> extends ThrowableQuadFunction<
        TFuncCall, ArrayList<Object>,
        IConfig<Object>, Scope,
        ThrowableConsumer<T, IOException>,
        Exception> {
}
