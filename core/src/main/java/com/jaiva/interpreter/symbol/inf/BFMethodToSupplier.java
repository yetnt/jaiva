package com.jaiva.interpreter.symbol.inf;


import com.jaiva.interpreter.Scope;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.tokenizer.tokens.specific.TFuncCall;
import com.jaiva.utils.ThrowableSupplier;
import com.yetnt.utils.functional.function.ThrowableQuadFunction;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Supplier;

public interface BFMethodToSupplier<T> extends ThrowableQuadFunction<
        TFuncCall, ArrayList<Object>,
        IConfig<Object>, Scope,
        ThrowableSupplier<T, IOException>,
        Exception> {

//    public static BFMethodToSupplier<?> example() {
//        DataInputStream ds = new DataInputStream();
//        return (func, arr, i, scope) ->
//                ds::readBoolean;
//    }
}
