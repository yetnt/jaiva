package com.jaiva.interpreter.symbol;

import java.util.ArrayList;
import java.util.HashMap;

import com.jaiva.errors.IntErrs.FunctionParametersException;
import com.jaiva.interpreter.Interpreter;
import com.jaiva.interpreter.MapValue;
import com.jaiva.tokenizer.Token;
import com.jaiva.tokenizer.Token.TFuncCall;
import com.jaiva.tokenizer.Token.TFunction;

public class BaseFunction extends Symbol {

    @SuppressWarnings("rawtypes")
    public BaseFunction(TFunction token) {
        super(SymbolType.FUNCTION, token);
        this.token = token;
    }

    public BaseFunction(String name) {
        super(SymbolType.FUNCTION);
        this.name = name;
    }

    public BaseFunction(String name, TFunction token) {
        super(SymbolType.FUNCTION, token);
        this.token = token;
        this.name = name;
    }

    /**
     * Calls the specified function.
     * 
     * @return
     */
    public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs) throws Exception {
        return void.class;
    }

    /**
     * Creates a defined function. Literally nothing crazy.
     * 
     * @param name  Name of the function.
     * @param token the TFunction Declaration token.
     * @return
     */
    public static DefinedFunction create(String name, TFunction token) {
        return new DefinedFunction(name, token);
    }

    public static class DefinedFunction extends BaseFunction {
        DefinedFunction(String name, TFunction token) {
            super(name, token);
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs)
                throws Exception {
            // tFuncCall contains the the token, we pass it for any extra checks we need to
            // do later.
            // params contains the input for the function.
            // this DefinedFunction, contains a TFunction where qwe need to check the params
            // so we can make name value pairs.
            String[] paramNames = ((TFunction) this.token).args;
            HashMap<String, MapValue> newVfs = (HashMap) vfs.clone();
            if (paramNames.length != params.size())
                throw new FunctionParametersException(this, params.size());
            for (int i = 0; i < paramNames.length; i++) {
                String name = paramNames[i];
                newVfs.put(name, new MapValue(params.get(i)));
            }
            return Interpreter.interpret((ArrayList<Token<?>>) ((TFunction) this.token).body.lines, this, newVfs);
        }
    }
}