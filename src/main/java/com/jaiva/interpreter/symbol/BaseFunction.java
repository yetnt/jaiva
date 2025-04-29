package com.jaiva.interpreter.symbol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.jaiva.errors.IntErrs.FunctionParametersException;
import com.jaiva.errors.IntErrs.UnknownVariableException;
import com.jaiva.errors.IntErrs.WtfAreYouDoingException;
import com.jaiva.interpreter.Context;
import com.jaiva.interpreter.Interpreter;
import com.jaiva.interpreter.MapValue;
import com.jaiva.interpreter.Primitives;
import com.jaiva.interpreter.runtime.GlobalResources;
import com.jaiva.lang.EscapeSequence;
import com.jaiva.tokenizer.Token;
import com.jaiva.tokenizer.Token.TFuncCall;
import com.jaiva.tokenizer.Token.TFunction;
import com.jaiva.tokenizer.Token.TVarRef;

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

    @Override
    public String toString() {
        return name + Arrays.asList(((TFunction) token).args).toString().replace("[", "(").replace("]", ")");
    }

    /**
     * Calls the specified function.
     * 
     * @return
     */
    public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs,
            GlobalResources resources) throws Exception {
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
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs,
                GlobalResources resources)
                throws Exception {
            Token<?> tContainer = new Token<>(null);
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
                Object value = params.get(i);
                if (value instanceof String)
                    value = EscapeSequence.escape((String) value);
                Object wrappedValue = null;

                if (name.startsWith("F~")
                        && (value instanceof Token<?> && ((Token<?>) value).getValue() instanceof TVarRef)) {
                    // value is definitely a TVarRef, so look for the function in vfs, if not found
                    // throw an error
                    // if found, create aq copy of that MapValue, and name it to instead this new
                    // name and add to the vfs.
                    TVarRef tVarRef = (TVarRef) ((Token<?>) value).getValue();
                    MapValue v = vfs.get(tVarRef.varName);
                    if (v == null)
                        throw new UnknownVariableException(tVarRef);
                    if (!(v.getValue() instanceof BaseFunction))
                        throw new WtfAreYouDoingException(v.getValue(), BaseFunction.class, tVarRef.lineNumber);

                    wrappedValue = v.getValue();

                } else if (name.startsWith("V~")
                        || (value instanceof Token<?> && ((Token<?>) value).getValue() instanceof TVarRef)) {
                    // value is definitely a TVarRef, so look for the variable in vfs, if not found
                    // throw an error
                    // if found, create aq copy of that MapValue, and name it to instead this new
                    // name and add to the vfs.
                    TVarRef tVarRef = (TVarRef) ((Token<?>) value).getValue();
                    if (tVarRef.index == null && tVarRef.name instanceof String) {
                        MapValue v = vfs.get(tVarRef.varName);
                        if (v == null)
                            throw new UnknownVariableException(tVarRef);
                        if (!(v.getValue() instanceof BaseVariable))
                            throw new WtfAreYouDoingException(v.getValue(), BaseVariable.class, tVarRef.lineNumber);

                        wrappedValue = v.getValue();
                    } else {
                        Object o = Primitives.toPrimitive(Primitives.parseNonPrimitive(tVarRef), vfs, false, resources);
                        wrappedValue = BaseVariable.create(name,
                                tContainer.new TUnknownVar(name, o, tFuncCall.lineNumber),
                                o instanceof ArrayList ? (ArrayList) o : new ArrayList<>(Arrays.asList(o)), false);
                    }
                } else if (Primitives.isPrimitive(value)) {
                    // primitivers ong
                    wrappedValue = BaseVariable.create(name,
                            tContainer.new TUnknownVar(name, value, tFuncCall.lineNumber),
                            new ArrayList<>(Arrays.asList(value)), false);

                } else {
                    // cacthes nested calls, operations and others
                    Object o = Primitives.toPrimitive(Primitives.parseNonPrimitive(value), vfs, false, resources);
                    wrappedValue = BaseVariable.create(name,
                            tContainer.new TUnknownVar(name, o, tFuncCall.lineNumber),
                            o instanceof ArrayList ? (ArrayList) o : new ArrayList<>(Arrays.asList(o)), false);
                }
                newVfs.put(name.replace("F~", "").replace("V~", ""), new MapValue(wrappedValue));
            }
            Object t = Interpreter.interpret((ArrayList<Token<?>>) ((TFunction) this.token).body.lines,
                    Context.FUNCTION,
                    newVfs, resources);
            if (t instanceof Interpreter.ThrowIfGlobalContext) {
                return ((Interpreter.ThrowIfGlobalContext) t).c;
            } else {
                return void.class;
            }
        }
    }
}