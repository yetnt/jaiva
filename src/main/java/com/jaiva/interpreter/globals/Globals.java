package com.jaiva.interpreter.globals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.jaiva.interpreter.Vfs;
import com.jaiva.tokenizer.Token;
import com.jaiva.tokenizer.Token.*;
import com.jaiva.Main;
import com.jaiva.errors.InterpreterException;
import com.jaiva.errors.InterpreterException.WtfAreYouDoingException;
import com.jaiva.errors.JaivaException;
import com.jaiva.interpreter.ContextTrace;
import com.jaiva.interpreter.MapValue;
import com.jaiva.interpreter.Primitives;
import com.jaiva.interpreter.globals.math.Math;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.*;
import com.jaiva.lang.Keywords;

/**
 * Globals class holds all the global symbols that are injected into the
 * variable functions store.
 */
public class Globals extends BaseGlobals {
    // public Vfs vfs = new HashMap<>();

    public HashMap<String, Vfs> builtInGlobals = new HashMap<>();

    /**
     * Constructor to create and get the globals.
     * 
     * @throws InterpreterException
     */
    public Globals(IConfig config) throws InterpreterException {
        super(GlobalType.MAIN);
        vfs.put("getVarClass", new FGetVarClass());
        vfs.put("reservedKeywords", new VReservedKeywords());
        vfs.put("version", new VJaivaVersion());
        vfs.put("flat", new FFlat());
        vfs.put("sleep", new FSleep());
        vfs.putAll(new IOFunctions(config).vfs);

        Types c = new Types();
        builtInGlobals.put(c.path, c.vfs);
        Math m = new Math();
        builtInGlobals.put(m.path, m.vfs);
        IOFile f = new IOFile(config);
        builtInGlobals.put(f.path, f.vfs);
        Debug d = new Debug(config);
        builtInGlobals.put(d.path, d.vfs);
    }

    /**
     * Returns the JSON of the globals
     * 
     * @param removeTrailingComma Remove the trailing comma
     * @return string with the JSON representation of the global tokens.
     */
    public String returnGlobalsJSON(boolean removeTrailingComma) {
        StringBuilder string = new StringBuilder();
        vfs.forEach((name, vf) -> {
            Symbol symbol = (Symbol) ((MapValue) vf).getValue();
            try {
                string.append(symbol.token.toJson());
            } catch (JaivaException e) {
                throw new RuntimeException(e);
            }
            string.append(",");
        });
        return string.substring(0, string.length() - (removeTrailingComma ? 1 : 0));
    }

    /**
     * getVarClass(variable)
     * Returns the .toString() class representation of a variable's token.
     */
    class FGetVarClass extends BaseFunction {
        FGetVarClass() {
            super("getVarClass", new TFunction("getVarClass", new String[] { "var" }, null, -1,
                    "Attempts to return the symbol's corresponding Java class in string form. If you're using this then you def don't know what you're doing."));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, Vfs vfs,
                IConfig config, ContextTrace cTrace)
                throws Exception {
            String name;
            if (params.getFirst() instanceof String) {
                name = (String) params.getFirst();
            } else if (params.getFirst() instanceof Token && ((Token<?>) params.getFirst()).value() instanceof TVarRef) {
                name = ((TVarRef) ((Token<?>) params.getFirst()).value()).varName.toString();
            } else {
                throw new InterpreterException.WtfAreYouDoingException(cTrace,
                        "getVarClass() only accepts a variable reference or a string, whatever you sent is disgusting.",
                        tFuncCall.lineNumber);
            }
            MapValue var = vfs.get(name);
            if (var == null) {
                throw new InterpreterException.UnknownVariableException(cTrace, name, tFuncCall.lineNumber);
            }
            if (!(var.getValue() instanceof Symbol symbol)) {
                throw new InterpreterException.WtfAreYouDoingException(cTrace,
                        name + " is not a variable nor a function, wtf. this error shouldnt happen.",
                        tFuncCall.lineNumber);
            }
            // We need to convert the named token to a raw token so we can call .toString()
            // on it.
            return symbol.token.getClass().getSimpleName();
        }
    }

    /**
     * reservedKeywords (array) variable.
     * This contains an array of the reserved keywords
     */
    class VReservedKeywords extends BaseVariable {
        VReservedKeywords() {
            super("reservedKeywords",
                    new TArrayVar("reservedKeywords", new ArrayList<>(Arrays.asList(Keywords.all)), -1,
                            "An array containing jaiva's reserved keywords that you cannot use as symbol names."),
                    new ArrayList<>(Arrays.asList(Keywords.all)));
            this.freeze();
        }
    }

    /**
     * version variable.
     * This holds the current version of jaiva in {@link Main#version}
     */
    class VJaivaVersion extends BaseVariable {
        VJaivaVersion() {
            super("version", new TStringVar("version", Main.version, -1,
                    "What do you think this returns."), Main.version);
            this.freeze();
        }
    }

    /**
     * flat(array1, array2)
     * Takes in 2 arrays and returns and flattens it, then retruns that.
     */
    class FFlat extends BaseFunction {
        FFlat() {
            super("flat", new TFunction("flat", new String[] { "array1", "array2" }, null, -1,
                    "Attempts to flatten (at the top level) the given arrays array1 and array2 into 1 single array. \\n**Note:** If there are any type mismatches in array1, it will be ignored and the same check is done to array2. Therefore this function will **always** return an array, whether or not it was successful."));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, Vfs vfs,
                IConfig config, ContextTrace cTrace)
                throws Exception {
            if (tFuncCall.args.size() != params.size())
                throw new InterpreterException.FunctionParametersException(cTrace, this, params.size());

            ArrayList<Object> returned = new ArrayList<>();
            tFuncCall.args.forEach(arg -> {
                if (arg instanceof TVarRef && ((TVarRef) arg).index == null) {
                    String name = ((TVarRef) arg).name;
                    MapValue v = vfs.get(name);
                    if (v == null)
                        return;
                    if (!(v.getValue() instanceof BaseVariable))
                        return;
                    if (((BaseVariable) v.getValue()).a_size() <= 0)
                        return; // technically in this case, it will concat the arrays because this array has
                                // nothing to concat lol.
                    returned.addAll(((BaseVariable) v.getValue()).a_getAll());
                } else {
                    // stuff that need be parsed, parse and pray arraylist is returned.
                    Object parsed = null;
                    try {
                        parsed = Primitives.toPrimitive(Primitives.parseNonPrimitive(arg), vfs, false, config, cTrace);
                    } catch (Exception e) {
                        // do nothing.
                    }
                    if (!(parsed instanceof ArrayList))
                        return;

                    returned.addAll((ArrayList) parsed);
                }
            });
            return returned;
        }
    }

    /**
     * Represents a built-in function that pauses the execution of the current
     * thread for a specified number of milliseconds.
     * <p>
     * The function expects a single integer parameter representing the duration to
     * sleep in milliseconds.
     * If the parameter is not an integer, a {@link WtfAreYouDoingException} is
     * thrown.
     * </p>
     * 
     * <p>
     * Usage: <code>sleep(milliseconds)</code>
     * </p>
     * 
     */
    class FSleep extends BaseFunction {
        FSleep() {
            super("sleep", new TFunction("sleep", new String[] { "milliseconds" }, null, -1,
                    "Sleep for `ms` amount of milliseconds. (Keep in mind this function still has to take your value and turn it into a Java primitive and other things, so the delay might not be exact. If you're looking for accuracy maybe remove x amount of ms till it's accurate.)"));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, Vfs vfs, IConfig config,
                ContextTrace cTrace)
                throws Exception {
            checkParams(tFuncCall, cTrace);
            Object val = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.getFirst()), vfs, false, config,
                    cTrace);
            if (!(val instanceof Integer integer))
                throw new WtfAreYouDoingException(cTrace, "Bruv, you can't just like, pls put number",
                        tFuncCall.lineNumber);

            Thread.sleep(integer);

            return Token.voidValue(tFuncCall.lineNumber);
        }
    }

}
