package com.jaiva.interpreter.symbol;

import com.jaiva.interpreter.Primitives;
import com.jaiva.interpreter.Scope;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.tokenizer.tokens.specific.TFuncCall;
import com.jaiva.tokenizer.tokens.specific.TFunction;

import java.util.ArrayList;

public class Lambda extends DefinedFunction {
    private final Scope lambdaScope;

    Lambda(String name, TFunction token, Scope scope) {
        super(name, token);
        lambdaScope = scope;
    }

    /**
     * Prepares parameters by converting them to their primitive forms.
     *
     * @param params The list of parameters to prepare.
     * @param config The configuration object.
     * @param scope  The current scope.
     * @return A list of prepared parameters.
     * @throws Exception If an error occurs during parameter preparation.
     */
    private ArrayList<Object> prepareParams(ArrayList<Object> params, IConfig<Object> config, Scope scope) throws Exception {
        ArrayList<Object> preparedParams = new ArrayList<>();
        for (Object param : params) {
            preparedParams.add(Primitives.toPrimitive(param, false, config, scope));
        }
        return preparedParams;
    }

    @Override
    public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config, Scope scope) throws Exception {
        // Because we're going into a closure now, m
        return super.call(
                tFuncCall,
                prepareParams(params, config, scope),
                config,
                lambdaScope
        );
    }
}
