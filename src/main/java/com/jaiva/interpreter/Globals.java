package com.jaiva.interpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.jaiva.interpreter.symbol.BaseFunction;
import com.jaiva.tokenizer.Token;
import com.jaiva.tokenizer.Token.TFunction;

import com.jaiva.interpreter.symbol.*;

public class Globals {
    public HashMap<String, MapValue> vfs;

    Globals() {
        Token<?> container = new Token<>(null);
        vfs.put("khuluma", new MapValue(new FKhuluma(container)));
        vfs.put("logToken", new MapValue(new FLogToken(container)));
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
        public Object call(Object[] params) {
            System.out.println(params[0]);
            return void.class;
        }
    }

    /**
     * logToken(variable)
     * Possibly logs the Token to the console for debugging purpioses.
     */
    class FLogToken extends BaseFunction {
        FLogToken(Token<?> container) {
            super("logToken", container.new TFunction("logToken", new String[] { "var" }, null));
            this.freeze();
        }

        @Override
        public Object call(Object[] params) {
            System.out.println();
            return void.class;
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
