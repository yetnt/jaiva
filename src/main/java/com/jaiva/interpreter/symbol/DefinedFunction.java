package com.jaiva.interpreter.symbol;

import com.jaiva.errors.InterpreterException;
import com.jaiva.interpreter.*;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.lang.EscapeSequence;
import com.jaiva.tokenizer.tokens.Token;
import com.jaiva.tokenizer.tokens.specific.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A Function that is defined by the user at runtime.
 */
public class DefinedFunction extends BaseFunction {
    DefinedFunction(String name, TFunction token) {
        super(name, token);
    }

    @Override
    public Object call(TFuncCall tFuncCall, ArrayList<Object> params,
                       IConfig<Object> config, Scope scope)
            throws Exception {
        // tFuncCall contains the the token, we pass it for any extra checks we need to
        // do later.
        // params contains the input for the function.
        // this DefinedFunction, contains a TFunction where we need to check the params
        // so we can make name value pairs.
        String[] paramNames = ((TFunction) this.token).args;
        Vfs newVfs = scope.vfs.clone();
        if (((TFunction) this.token).varArgs) {
            ArrayList<Object> varArgsArr = new ArrayList<>();
            for (Object o : params) {
                // Parse each param
                if (o instanceof String)
                    o = EscapeSequence.fromEscape((String) o, tFuncCall.lineNumber);
                varArgsArr.add(Primitives.toPrimitive(Primitives.parseNonPrimitive(o), false, config, scope));
            }
            newVfs.put(paramNames[0], new BaseVariable(paramNames[0],
                    new TArrayVar(paramNames[0], varArgsArr, tFuncCall.lineNumber),
                    varArgsArr));
        } else {
            checkParams(tFuncCall, scope);
            for (int i = 0; i < paramNames.length; i++) {
                String name = paramNames[i];
                Object value;
                try {
                    value = params.get(i);
                } catch (IndexOutOfBoundsException e) {
                    // optional as we checked params above, so set it to TVoidValue
                    value = Token.voidValue(tFuncCall.lineNumber);
                }
                if (value instanceof String)
                    value = EscapeSequence.fromEscape((String) value, tFuncCall.lineNumber);
                Object wrappedValue = Token.voidValue(tFuncCall.lineNumber);


                if (name.startsWith("F~")) {
                    // value is definitely a TVarRef, so look for the function in vfs, if not found
                    // throw an error
                    // if found, createFunction aq copy of that MapValue, and name it to instead this new
                    // name and add to the vfs.
                    if (value instanceof TLambda) wrappedValue = Primitives.toPrimitive(value, false, config, scope);
                    if ((value instanceof Token<?> && ((Token<?>) value).value() instanceof TVarRef tVarRef)) {
                        MapValue v = scope.vfs.get(tVarRef.varName);
                        if (v == null)
                            throw new InterpreterException.UnknownVariableException(scope, tVarRef);
                        if (!(v.getValue() instanceof BaseFunction))
                            throw new InterpreterException.WtfAreYouDoingException(scope, v.getValue(), BaseFunction.class,
                                    tVarRef.lineNumber);
                        wrappedValue = v.getValue();
                    }


                }
                else if (name.startsWith("V~") || (value instanceof Token<?> && ((Token<?>) value).value() instanceof TVarRef)) {
                    // value is definitely a TVarRef, so look for the variable in vfs, if not found
                    // throw an error
                    // if found, createFunction aq copy of that MapValue, and name it to instead this new
                    // name and add to the vfs.
                    Object o = Primitives.toPrimitive(Primitives.parseNonPrimitive(value), false, config, scope);
                    // wrappedValue = new BaseVariable(name, tFuncCall, o);
                    wrappedValue = BaseVariable.create(name,
                            o instanceof ArrayList ? new TArrayVar(name, (ArrayList) o, tFuncCall.lineNumber)
                                    : new TUnknownScalar(name, o, tFuncCall.lineNumber),
                            o instanceof ArrayList ? (ArrayList) o : new ArrayList<>(Collections.singletonList(o)),
                            o instanceof ArrayList);
                } else if (Primitives.isPrimitive(value)) {
                    // primitivers ong
                    wrappedValue = BaseVariable.create(name,
                            new TUnknownScalar(name, value, tFuncCall.lineNumber),
                            new ArrayList<>(List.of(value)), false);

                } else {
                    // cacthes nested calls, operations and others
                    Object o = Primitives.toPrimitive(Primitives.parseNonPrimitive(value), false, config, scope);
                    wrappedValue = BaseVariable.create(name,
                            new TUnknownScalar(name, o, tFuncCall.lineNumber),
                            o instanceof ArrayList ? (ArrayList) o : new ArrayList<>(Collections.singletonList(o)), false);
                }
                newVfs.put(name.replace("F~", "").replace("V~", ""), (Symbol) wrappedValue);
            }
        }
        Object t = Interpreter.interpret((ArrayList<Token<?>>) ((TFunction) this.token).body.lines,
                new Scope(Context.FUNCTION, token, scope, newVfs), config);
        if (t instanceof Interpreter.ThrowIfGlobalContext) {
            return ((Interpreter.ThrowIfGlobalContext) t).c;
        } else {
            return Token.voidValue(tFuncCall.lineNumber);
        }
    }
}
