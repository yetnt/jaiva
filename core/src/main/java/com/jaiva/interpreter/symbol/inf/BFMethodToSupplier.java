package com.jaiva.interpreter.symbol.inf;


import com.jaiva.interpreter.Scope;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.tokenizer.tokens.specific.TFuncCall;
import com.yetnt.utils.functional.function.ThrowableQuadFunction;
import com.yetnt.utils.functional.generic.ThrowableSupplier;

import java.io.IOException;
import java.util.ArrayList;

public interface BFMethodToSupplier<T> extends ThrowableQuadFunction<
        TFuncCall, ArrayList<Object>,
        IConfig<Object>, Scope,
        ThrowableSupplier<T, IOException>,
        Exception> {
}
