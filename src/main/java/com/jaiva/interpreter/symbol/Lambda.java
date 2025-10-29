package com.jaiva.interpreter.symbol;

import com.jaiva.interpreter.Primitives;
import com.jaiva.interpreter.Scope;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.tokenizer.tokens.specific.TFuncCall;
import com.jaiva.tokenizer.tokens.specific.TFunction;

import java.util.ArrayList;

public class Lambda extends DefinedFunction {
    private Scope lambdaScope;

    Lambda(String name, TFunction token, Scope scope) {
        super(name, token);
        lambdaScope = scope;
    }

    @Override
    public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config, Scope scope) throws Exception {
        // Because we're going into a closure now, m
        return super.call(
                tFuncCall,
                new ArrayList<>(
                        params.stream().map(p -> {
                            try {
                                return Primitives.toPrimitive(p, false, config, scope);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }).toList()
                ),
                config,
                lambdaScope
        );
    }
}
