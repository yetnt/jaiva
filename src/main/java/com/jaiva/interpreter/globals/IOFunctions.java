package com.jaiva.interpreter.globals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import com.jaiva.errors.IntErrs.WtfAreYouDoingException;
import com.jaiva.interpreter.GlobalResources;
import com.jaiva.interpreter.Interpreter;
import com.jaiva.interpreter.MapValue;
import com.jaiva.interpreter.Primitives;
import com.jaiva.interpreter.Interpreter.ThrowIfGlobalContext;
import com.jaiva.interpreter.symbol.BaseFunction;
import com.jaiva.tokenizer.EscapeSequence;
import com.jaiva.tokenizer.Token;
import com.jaiva.tokenizer.Token.TFuncCall;
import com.jaiva.tokenizer.Token.TFunction;
import com.jaiva.tokenizer.Token.TStatement;
import com.jaiva.tokenizer.Token.TVarRef;
import com.jaiva.tokenizer.TokenDefault;

public class IOFunctions extends BaseGlobals {

    public IOFunctions() {
        super();
        vfs.put("khuluma", new MapValue(new FKhuluma(container)));
        vfs.put("mamela", new MapValue(new FMamela(container)));
    }

    /**
     * khuluma("hello world")!
     * Will just print.
     */
    class FKhuluma extends BaseFunction {

        FKhuluma(Token<?> container) {
            super("khuluma", container.new TFunction("khuluma", new String[] { "msg", "removeNewLn" }, null, -1,
                    "Prints any given input to the console with a newline afterwards. \\n(It just uses System.out.println() lol) This function returns no value."));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs,
                GlobalResources resources)
                throws Exception {
            String output;
            Object o = params.get(0);
            Object v = params.size() > 1
                    ? Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(1)), vfs, false, resources)
                    : null;
            if (o instanceof Token<?> || (o instanceof TokenDefault && Interpreter.isVariableToken(o))) {
                TokenDefault token = ((Token<?>) o).getValue();
                // Only TokenDefault classes have the .toToken() method, but TokenDefualt itself
                // doesnt, so we kinda need to check for every possible case unfortunately.
                Object input = token instanceof TStatement || token instanceof TVarRef || token instanceof TFuncCall
                        ? o
                        : token;
                Object value = Interpreter.handleVariables(input, vfs, resources);
                // System.out.println(value);
                output = value.toString();
            } else if (o instanceof ThrowIfGlobalContext) {
                // we dont have context, so just alert the user.
                // that either, they tried using the "voetsek" or "nevermind" outside of a for
                // loop context or cima was called with a custom error.
                throw new WtfAreYouDoingException(
                        "Cannot use 'voetsek' or 'nevermind' outside of a loop context, or 'cima' was called with a custom error.");
            } else {
                output = o.toString();
            }
            if (v instanceof Boolean && (Boolean) v == true) {
                System.out.print(EscapeSequence.escape(output.toString()));
            } else {
                System.out.println(EscapeSequence.escape(output.toString()));
            }
            return void.class;
        }
    }

    class FMamela extends BaseFunction {
        FMamela(Token<?> container) {
            super("mamela", container.new TFunction("mamela", new String[] {}, null, -1,
                    "Listens for input from the console. Warning this will pause all execution until input is given."));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs,
                GlobalResources resources)
                throws Exception {

            String output = resources.consoleIn.nextLine();

            return output;
        }
    }
}
