package com.jaiva.interpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.jaiva.tokenizer.EscapeSequence;
import com.jaiva.tokenizer.Token;
import com.jaiva.tokenizer.Token.TFuncCall;
import com.jaiva.tokenizer.Token.TFunction;
import com.jaiva.tokenizer.Token.TVarRef;
import com.jaiva.errors.IntErrs.UnknownVariableException;
import com.jaiva.errors.IntErrs.WtfAreYouDoingException;
import com.jaiva.interpreter.symbol.*;

public class Globals {
    public HashMap<String, MapValue> vfs = new HashMap<>();

    public Globals() {
        Token<?> container = new Token<>(null);
        vfs.put("khuluma", new MapValue(new FKhuluma(container)));
        vfs.put("getVarClass", new MapValue(new FGetVarClass(container)));
        vfs.put("reservedKeywords", new MapValue(new VReservedKeywords(container)));
    }

    /**
     * khuluma("hello world")!
     * Will just print.
     */
    class FKhuluma extends BaseFunction {

        FKhuluma(Token<?> container) {
            super("khuluma", container.new TFunction("khuluma", new String[] { "msg" }, null));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs) {
            System.out.println(EscapeSequence.escape(params.get(0).toString()));
            return void.class;
        }
    }

    /**
     * getVarClass(variable)
     * Returns the .toString() class representation of a variable's token.
     */
    class FGetVarClass extends BaseFunction {
        FGetVarClass(Token<?> container) {
            super("getVarClass", container.new TFunction("getVarClass", new String[] { "var" }, null));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs)
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
                throw new UnknownVariableException(name);
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
            super("reservedKeywords", container.new TArrayVar("reservedKeywords", new ArrayList<>(Arrays.asList(
                    "maak", "if", "mara", "zama zama",
                    "kwenza", "cima", "colonize", "chaai", "khutla", "with",
                    "nikhil", "kota", "voetsek", "nevermind"))));
            this.array.addAll(Arrays.asList(
                    "maak", "if", "mara", "zama zama",
                    "kwenza", "cima", "colonize", "chaai", "khutla", "with",
                    "nikhil", "kota", "voetsek", "nevermind"));
            this.freeze();
        }
    }
}
