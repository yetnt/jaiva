package com.jaiva.interpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.jaiva.tokenizer.EscapeSequence;
import com.jaiva.tokenizer.Keywords;
import com.jaiva.tokenizer.Token;
import com.jaiva.tokenizer.TokenDefault;
import com.jaiva.tokenizer.Token.TFuncCall;
import com.jaiva.tokenizer.Token.TFunction;
import com.jaiva.tokenizer.Token.TStatement;
import com.jaiva.tokenizer.Token.TVarRef;
import com.jaiva.Main;
import com.jaiva.errors.IntErrs.FrozenSymbolException;
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
        vfs.put("version", new MapValue(new VJaivaVersion(container)));
    }

    /**
     * khuluma("hello world")!
     * Will just print.
     */
    class FKhuluma extends BaseFunction {

        FKhuluma(Token<?> container) {
            super("khuluma", container.new TFunction("khuluma", new String[] { "msg" }, null, -1));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs)
                throws Exception {
            String output;
            Object o = params.get(0);
            if (o instanceof Token<?> || (o instanceof TokenDefault && Interpreter.isVariableToken(o))) {
                TokenDefault token = ((Token<?>) o).getValue();
                // Only TokenDefault classes have the .toToken() method, but TokenDefualt itself
                // doesnt, so we kinda need to check for every possible case unfortunately.
                Object input = token instanceof TStatement || token instanceof TVarRef || token instanceof TFuncCall
                        ? o
                        : token;
                Object value = Interpreter.handleVariables(input, vfs);
                // System.out.println(value);
                output = value.toString();
            } else {
                output = o.toString();
            }
            System.out.println(EscapeSequence.escape(output.toString()));
            return void.class;
        }
    }

    /**
     * getVarClass(variable)
     * Returns the .toString() class representation of a variable's token.
     */
    class FGetVarClass extends BaseFunction {
        FGetVarClass(Token<?> container) {
            super("getVarClass", container.new TFunction("getVarClass", new String[] { "var" }, null, -1));
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
            super("reservedKeywords",
                    container.new TArrayVar("reservedKeywords", new ArrayList<>(Arrays.asList(Keywords.all)), -1));
            this.array.addAll(Arrays.asList(Keywords.all));
            this.freeze();
        }
    }

    class VJaivaVersion extends BaseVariable {
        VJaivaVersion(Token<?> container) {
            super("version", container.new TStringVar("version", Main.version, -1));
            try {
                this.setScalar(Main.version);
            } catch (FrozenSymbolException e) {
                // This should never happen, but catch it so java doesnt complain. Damn you
                // java.
                e.printStackTrace();
            }
            this.freeze();
        }
    }
}
