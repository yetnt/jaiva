package com.jaiva.interpreter.symbol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import com.jaiva.errors.InterpreterException;
import com.jaiva.interpreter.Context;
import com.jaiva.interpreter.ContextTrace;
import com.jaiva.interpreter.Interpreter;
import com.jaiva.interpreter.MapValue;
import com.jaiva.interpreter.Primitives;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.lang.EscapeSequence;
import com.jaiva.tokenizer.Token;
import com.jaiva.tokenizer.Token.TFuncCall;
import com.jaiva.tokenizer.Token.TFunction;
import com.jaiva.tokenizer.Token.TVarRef;
import com.jaiva.tokenizer.Token.TVoidValue;

/**
 * BaseFunction class is a base class for all functions in Jaiva.
 */
public class BaseFunction extends Symbol {

    /**
     * Constructs a new BaseFunction instance with the specified name and token.
     * 
     * @param token the token associated with the function
     */
    public BaseFunction(TFunction token) {
        super(token.name, SymbolType.FUNCTION, token);
        this.token = token;
    }

    /**
     * Constructs a new BaseFunction instance with the specified name.
     * 
     * @param name the name of the function
     */
    public BaseFunction(String name) {
        super(name, SymbolType.FUNCTION);
        this.name = name;
    }

    /**
     * Constructs a new BaseFunction instance with the specified name and token.
     * 
     * @param name  the name of the function
     * @param token the token associated with the function
     */
    public BaseFunction(String name, TFunction token) {
        super(name, SymbolType.FUNCTION, token);
        this.token = token;
        this.name = name;
    }

    @Override
    public String toString() {
        return name + Arrays.asList(((TFunction) token).args).toString().replace("[", "(").replace("]", ")");
    }

    @Override
    public String toDebugString() {
        return this.toString();
    }

    /**
     * Calls the function with the given parameters and variable functions store.
     * * This method is called by the interpreter when the function is called.
     * Other functions should override this method to implement their own logic.
     * 
     * @param tFuncCall The function call token.
     * @param params    The parameters passed to the function. (taken from the
     *                  parameters in tFuncCall)
     * @param vfs       The variable functions store.
     * @param config    The Interpreter configuration.
     * @return The return value of the function, if not overriden it will return
     *         void.class
     * @throws Exception If an error occurs during the function call.
     */
    public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs,
            IConfig config, ContextTrace cTrace) throws Exception {
        return Token.voidValue(tFuncCall.lineNumber);
    }

    /**
     * Check the parameters
     * 
     * @param tFuncCall the function call
     * @throws InterpreterException when args are wrong.
     */
    protected void checkParams(TFuncCall tFuncCall, ContextTrace cTrace) throws InterpreterException {
        TFunction tFunc = (TFunction) this.token;

        // this if sanitizes tFuncCall, as if it has 1 singular entry and that entry is
        // null, we remove it so we have clear args.
        if (tFuncCall.args.size() == 1 && tFuncCall.args.get(0) == null) {
            tFuncCall.args.clear();
        }

        // Check if this case
        if (tFunc.args.length == 1 && tFunc.args[0].equals("")) {
            return;
        }

        // first check if we jsut dont have args.
        if (Collections.frequency(tFunc.isArgOptional, true) == tFunc.isArgOptional.size() && tFuncCall.args.isEmpty())
            return;
        if (tFuncCall.args.isEmpty() && (tFunc.isArgOptional.size() > 0 ||
                tFunc.isArgOptional.isEmpty())) {
            throw new InterpreterException.FunctionParametersException(cTrace, this,
                    Integer.toString(1),
                    tFuncCall.lineNumber);
        }
        for (int i = 0; i < tFunc.isArgOptional.size(); i++) {
            boolean isOptional = (boolean) tFunc.isArgOptional.get(i);
            // check if the current param was given an input of TVoidValue
            // if so, error as we dont want them to pass in idk to a paramter thats not
            // optional
            if ((!tFuncCall.args.isEmpty() && tFuncCall.args.size() != i)) {
                if (tFuncCall.args.get(i) instanceof TVoidValue && !isOptional)
                    throw new InterpreterException.FunctionParametersException(cTrace, this, Integer.toString(i + 1),
                            tFuncCall.lineNumber);
            } else {
                // the function call has less arguments than required by the function.
                // check if all the remaining arguments are defined as optional.
                for (int j = i; j < tFunc.isArgOptional.size(); j++) {
                    if (!(boolean) tFunc.isArgOptional.get(j) && j != 0) {
                        throw new InterpreterException.FunctionParametersException(cTrace, this,
                                Integer.toString(j + 1),
                                tFuncCall.lineNumber);
                    }
                }

                // if we made it here, we've checked everything, so we can break out the outer
                // loop
                break;
            }
        }
    }

    /**
     * Creates a new user-defined function with the specified name and token.
     * 
     * @param name  Function name
     * @param token Function token
     * @return
     */
    public static DefinedFunction create(String name, TFunction token) {
        return new DefinedFunction(name, token);
    }

    /**
     * A Function that is defined by the user at runtime.
     */
    public static class DefinedFunction extends BaseFunction {
        DefinedFunction(String name, TFunction token) {
            super(name, token);
        }

        @Override
        /**
         * Calls the function with the given parameters and variable functions store.
         * <p>
         * This method is called by the interpreter when the function is called.
         * Other functions should override this method to implement their own logic.
         * 
         * @param tFuncCall The function call token.
         * @param params    The parameters passed to the function. (taken from the
         *                  parameters in tFuncCall)
         * @param vfs       The variable functions store.
         * @param config    The Interpreter configuration.
         * @return The return value of the function, if not overriden it will return
         *         void.class
         * @throws Exception If an error occurs during the function call.
         */
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs,
                IConfig config, ContextTrace cTrace)
                throws Exception {
            Token<?> tContainer = new Token<>(null);
            // tFuncCall contains the the token, we pass it for any extra checks we need to
            // do later.
            // params contains the input for the function.
            // this DefinedFunction, contains a TFunction where qwe need to check the params
            // so we can make name value pairs.
            String[] paramNames = ((TFunction) this.token).args;
            HashMap<String, MapValue> newVfs = (HashMap) vfs.clone();
            checkParams(tFuncCall, cTrace);
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

                if (name.startsWith("F~")
                        && (value instanceof Token<?> && ((Token<?>) value).getValue() instanceof TVarRef)) {
                    // value is definitely a TVarRef, so look for the function in vfs, if not found
                    // throw an error
                    // if found, create aq copy of that MapValue, and name it to instead this new
                    // name and add to the vfs.
                    TVarRef tVarRef = (TVarRef) ((Token<?>) value).getValue();
                    MapValue v = vfs.get(tVarRef.varName);
                    if (v == null)
                        throw new InterpreterException.UnknownVariableException(cTrace, tVarRef);
                    if (!(v.getValue() instanceof BaseFunction))
                        throw new InterpreterException.WtfAreYouDoingException(cTrace, v.getValue(), BaseFunction.class,
                                tVarRef.lineNumber);

                    wrappedValue = v.getValue();

                } else if (name.startsWith("V~")
                        || (value instanceof Token<?> && ((Token<?>) value).getValue() instanceof TVarRef)) {
                    // value is definitely a TVarRef, so look for the variable in vfs, if not found
                    // throw an error
                    // if found, create aq copy of that MapValue, and name it to instead this new
                    // name and add to the vfs.
                    Object o = Primitives.toPrimitive(Primitives.parseNonPrimitive(value), vfs, false, config, cTrace);
                    // wrappedValue = new BaseVariable(name, tFuncCall, o);
                    wrappedValue = BaseVariable.create(name,
                            o instanceof ArrayList ? tContainer.new TArrayVar(name, (ArrayList) o, tFuncCall.lineNumber)
                                    : tContainer.new TUnknownVar<Object>(name, o, tFuncCall.lineNumber),
                            o instanceof ArrayList ? (ArrayList) o : new ArrayList<>(Arrays.asList(o)),
                            o instanceof ArrayList);
                } else if (Primitives.isPrimitive(value)) {
                    // primitivers ong
                    wrappedValue = BaseVariable.create(name,
                            tContainer.new TUnknownVar<Object>(name, value, tFuncCall.lineNumber),
                            new ArrayList<>(Arrays.asList(value)), false);

                } else {
                    // cacthes nested calls, operations and others
                    Object o = Primitives.toPrimitive(Primitives.parseNonPrimitive(value), vfs, false, config, cTrace);
                    wrappedValue = BaseVariable.create(name,
                            tContainer.new TUnknownVar<Object>(name, o, tFuncCall.lineNumber),
                            o instanceof ArrayList ? (ArrayList) o : new ArrayList<>(Arrays.asList(o)), false);
                }
                newVfs.put(name.replace("F~", "").replace("V~", ""), new MapValue(wrappedValue));
            }
            Object t = Interpreter.interpret((ArrayList<Token<?>>) ((TFunction) this.token).body.lines,
                    new ContextTrace(Context.FUNCTION, token, cTrace),
                    newVfs, config);
            if (t instanceof Interpreter.ThrowIfGlobalContext) {
                return ((Interpreter.ThrowIfGlobalContext) t).c;
            } else {
                return Token.voidValue(tFuncCall.lineNumber);
            }
        }
    }
}