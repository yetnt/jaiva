package com.jaiva.interpreter.symbol;

import java.util.*;

import com.jaiva.errors.InterpreterException;
import com.jaiva.interpreter.*;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.lang.EscapeSequence;
import com.jaiva.tokenizer.tokens.TReference;
import com.jaiva.tokenizer.tokens.TokenDefault;
import com.jaiva.tokenizer.tokens.specific.*;
import com.jaiva.tokenizer.tokens.Token;

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
        return ((TFunction)token).toDefinitionString();
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
     * @param config    The Interpreter configuration.
     * @return The return value of the function, if not overridden it will return
     *         {@code void.class}
     * @throws Exception If an error occurs during the function call.
     */
    public Object call(TFuncCall tFuncCall, ArrayList<Object> params,
                       IConfig<Object> config, Scope scope) throws Exception {
        return Token.voidValue(tFuncCall.lineNumber);
    }

    /**
     * Resolves the parameters for a function call, handling spread operators.
     *
     * @param f          The function being called.
     * @param tFuncCall  The function call token containing the arguments.
     * @param config     The interpreter configuration.
     * @param scope      The current scope for error reporting.
     * @return A list of resolved parameters for the function call.
     * @throws Exception If an error occurs during parameter resolution.
     */
    public static ArrayList<Object> resolveParameters(BaseFunction f, TFuncCall tFuncCall, IConfig<Object> config, Scope scope) throws Exception {
        ArrayList<Object> newTokenArgs = new ArrayList<>();
        // Go through each arg.

        // fast path, filter out non spread args, if new size 0, then return.
        boolean hasSpread = tFuncCall.args.stream().map(
                arg -> {
                    if (arg instanceof Token<?>(Object value)) {
                        arg = value;
                    }
                    return arg;
                }
        ).anyMatch(
                arg -> arg instanceof TReference referenceToken && referenceToken.getSpreadArr()
        );
        if (!hasSpread)
            return tFuncCall.args;
        for (Object arg : tFuncCall.args) {
            if (arg instanceof Token<?>(Object value)) {
                arg = value;
            }
            if (!((TFunction)f.token).varArgs && newTokenArgs.size() > ((TFunction)f.token).isArgOptional.size())  {
                // We could hard fail, or be funnier and just cut off the rest of the data.
                // Cut off the rest of the data.
                while (newTokenArgs.size() > ((TFunction)f.token).isArgOptional.size())
                    newTokenArgs.removeLast();
                break;
            }
            if (arg instanceof TReference referenceToken && referenceToken.getSpreadArr()) {
                Object evaledToken = Primitives.toPrimitive(((TokenDefault<?>)referenceToken).toToken(), false, config, scope);
                if (evaledToken instanceof ArrayList<?> arr) {
                    newTokenArgs.addAll(arr);
                } else if (evaledToken instanceof String string) {
                    string = EscapeSequence.toEscape(string);
                    newTokenArgs.addAll(Arrays.asList(string.split("")));
                } else {
                    throw new InterpreterException.WtfAreYouDoingException(scope,
                            "Spread operator can only be used on arrays or strings.",
                            tFuncCall.lineNumber);
                }
            } else {
                newTokenArgs.add(arg);
            }
        }

        return newTokenArgs;
    }

    /**
     * Checks if the parameters passed to a function call match the function's definition.
     * This method validates the number of arguments and their optionality.
     *
     * @param tFuncCall The function call token containing the arguments.
     * @param scope     The current scope for error reporting.
     * @throws InterpreterException If there's a mismatch in parameters (e.g., missing required arguments).
     */
    protected void checkParams(TFuncCall tFuncCall, Scope scope) throws Exception {
        TFunction tFunc = (TFunction) this.token;

        // this if sanitizes tFuncCall, as if it has 1 singular entry and that entry is
        // null, we remove it so we have clear args.
        if (tFuncCall.args.size() == 1 && tFuncCall.args.getFirst() == null) {
            tFuncCall.args.clear();
        }

        // Check if this case
        if (tFunc.args.length == 1 && (tFunc.args[0] == null || tFunc.args[0].isEmpty())) {
            return;
        }

        // first check if we jsut dont have args.
        if (Collections.frequency(tFunc.isArgOptional, true) == tFunc.isArgOptional.size() && tFuncCall.args.isEmpty())
            return;
        if (tFuncCall.args.isEmpty()) {
            throw new InterpreterException.FunctionParametersException(scope, this,
                    Integer.toString(1),
                    tFuncCall.lineNumber);
        }
        for (int i = 0; i < tFunc.isArgOptional.size(); i++) {
            boolean isOptional = (boolean) tFunc.isArgOptional.get(i);
            // check if the current param was given an input of TVoidValue
            // if so, error as we dont want them to pass in idk to a paramter thats not
            // optional
            if (tFuncCall.args.size() != i) {
                if (tFuncCall.args.get(i) instanceof TVoidValue && !isOptional)
                    throw new InterpreterException.FunctionParametersException(scope, this, Integer.toString(i + 1),
                            tFuncCall.lineNumber);
            } else {
                // the function call has less arguments than required by the function.
                // check if all the remaining arguments are defined as optional.
                for (int j = i; j < tFunc.isArgOptional.size(); j++) {
                    if (!(boolean) tFunc.isArgOptional.get(j)) {
                        throw new InterpreterException.FunctionParametersException(scope, this,
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


    @Override
    public Symbol clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Creates a new user-defined function with the specified name and token.
     * 
     * @param name  Function name
     * @param token Function token
     * @return A User Defined Function.
     */
    public static DefinedFunction createFunction(String name, TFunction token) {
        return new DefinedFunction(name, token);
    }

    /**
     * Creates a new user-defined lambda
     * @param name The lambda name, not really needed
     * @param token The lambda token
     * @param scope It's scope
     * @return A Lambda
     */
    public static Lambda createLambda(String name, TFunction token, Scope scope) {return new Lambda(name, token, scope);}

}