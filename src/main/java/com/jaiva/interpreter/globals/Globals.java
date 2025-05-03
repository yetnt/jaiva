package com.jaiva.interpreter.globals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.jaiva.tokenizer.Token;
import com.jaiva.tokenizer.TokenDefault;
import com.jaiva.tokenizer.Token.TFuncCall;
import com.jaiva.tokenizer.Token.TFunction;
import com.jaiva.tokenizer.Token.TStatement;
import com.jaiva.tokenizer.Token.TVarRef;
import com.jaiva.Main;
import com.jaiva.errors.IntErrs.FrozenSymbolException;
import com.jaiva.errors.IntErrs.FunctionParametersException;
import com.jaiva.errors.IntErrs.UnknownVariableException;
import com.jaiva.errors.IntErrs.WtfAreYouDoingException;
import com.jaiva.interpreter.Interpreter;
import com.jaiva.interpreter.MapValue;
import com.jaiva.interpreter.Primitives;
import com.jaiva.interpreter.runtime.GlobalResources;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.*;
import com.jaiva.lang.EscapeSequence;
import com.jaiva.lang.Keywords;

public class Globals extends BaseGlobals {
    // public HashMap<String, MapValue> vfs = new HashMap<>();

    public Globals() {
        super();
        vfs.put("getVarClass", new MapValue(new FGetVarClass(container)));
        vfs.put("reservedKeywords", new MapValue(new VReservedKeywords(container)));
        vfs.put("version", new MapValue(new VJaivaVersion(container)));
        vfs.put("flat", new MapValue(new FFlat(container)));
        vfs.putAll(new IOFunctions().vfs);
    }

    public String returnGlobalsJSON(boolean removeTrailingComma) {
        StringBuilder string = new StringBuilder();
        vfs.forEach((name, vf) -> {
            Symbol symbol = (Symbol) vf.getValue();
            string.append(symbol.token.toJson());
            string.append(",");
        });
        return string.toString().substring(0, string.length() - (removeTrailingComma ? 1 : 0));
    }

    /**
     * getVarClass(variable)
     * Returns the .toString() class representation of a variable's token.
     */
    class FGetVarClass extends BaseFunction {
        FGetVarClass(Token<?> container) {
            super("getVarClass", container.new TFunction("getVarClass", new String[] { "var" }, null, -1,
                    "Attempts to return the symbol's corresponding Java class in string form. If you're using this then you def don't know what you're doing."));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs,
                IConfig config)
                throws Exception {
            String name;
            if (params.get(0) instanceof String) {
                name = (String) params.get(0);
            } else if (params.get(0) instanceof Token && ((Token) params.get(0)).getValue() instanceof TVarRef) {
                name = ((TVarRef) ((Token) params.get(0)).getValue()).varName.toString();
            } else {
                throw new WtfAreYouDoingException(
                        "getVarClass() only accepts a variable reference or a string, whatever you sent is disgusting.");
            }
            MapValue var = vfs.get(name);
            if (var == null) {
                throw new UnknownVariableException(name, tFuncCall.lineNumber);
            }
            if (!(var.getValue() instanceof Symbol)) {
                throw new WtfAreYouDoingException(
                        name + " is not a variable nor a function, wtf. this error shouldnt happen.");
            }
            Symbol symbol = (Symbol) var.getValue();
            // We need to convert the named token to a raw token so we can call .toString()
            // on it.
            return symbol.token.toString();
        }
    }

    /**
     * reservedKeywords variable.
     * This contains an array of the reserved keywords
     */
    class VReservedKeywords extends BaseVariable {
        VReservedKeywords(Token<?> container) {
            super("reservedKeywords",
                    container.new TArrayVar("reservedKeywords", new ArrayList<>(Arrays.asList(Keywords.all)), -1,
                            "An array containing jaiva's reserved keywords that you cannot use as symbol names."),
                    new ArrayList<>(Arrays.asList(Keywords.all)));
            this.freeze();
        }
    }

    class VJaivaVersion extends BaseVariable {
        VJaivaVersion(Token<?> container) {
            super("version", container.new TStringVar("version", Main.version, -1,
                    "What do you think this returns."), Main.version);
            this.freeze();
        }
    }

    /**
     * flat(array1, array2)
     * Takes in 2 arrays and returns and flattens it, then retruns that.
     */
    class FFlat extends BaseFunction {
        FFlat(Token<?> container) {
            super("flat", container.new TFunction("flat", new String[] { "array1", "array2" }, null, -1,
                    "Attempts to flatten (at the top level) the given arrays array1 and array2 into 1 single array. \\n**Note:** If there are any type mismatches in array1, it will be ignored and the same check is done to array2. Therefore this function will **always** return an array, whether or not it was successful."));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs,
                IConfig config)
                throws Exception {
            if (tFuncCall.args.size() != params.size())
                throw new FunctionParametersException(this, params.size());

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
                        parsed = Primitives.toPrimitive(Primitives.parseNonPrimitive(arg), vfs, false, config);
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

}
