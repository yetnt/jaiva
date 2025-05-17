package com.jaiva.interpreter.globals;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import com.jaiva.errors.InterpreterException;
import com.jaiva.interpreter.Interpreter;
import com.jaiva.interpreter.MapValue;
import com.jaiva.interpreter.Primitives;
import com.jaiva.interpreter.Interpreter.ThrowIfGlobalContext;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.BaseFunction;
import com.jaiva.lang.EscapeSequence;
import com.jaiva.tokenizer.Token;
import com.jaiva.tokenizer.Token.TFuncCall;
import com.jaiva.tokenizer.Token.TStatement;
import com.jaiva.tokenizer.Token.TVarRef;
import com.jaiva.tokenizer.TokenDefault;

/**
 * IOFunctions class holds the functions that are used for input and output in
 * the console.
 */
public class IOFunctions extends BaseGlobals {

    /**
     * Constructs the IOFunctions with the Input/Output functions.
     */
    public IOFunctions() {
        super(GlobalType.GLOBAL);
        vfs.put("khuluma", new MapValue(new FKhuluma(container)));
        vfs.put("mamela", new MapValue(new FMamela(container)));
        vfs.put("ask", new MapValue(new FAsk(container)));
        vfs.put("clear", new MapValue(new FClear(container)));
    }

    /**
     * khuluma("hello world")!
     * This will print the given input to the console.
     */
    class FKhuluma extends BaseFunction {

        FKhuluma(Token<?> container) {
            super("khuluma", container.new TFunction("khuluma", new String[] { "msg", "removeNewLn?" }, null, -1,
                    "Prints any given input to the console with a newline afterwards. \\n(It just uses System.out.println() lol) This function returns no value."));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs,
                IConfig config)
                throws Exception {
            String output;
            Object o = params.get(0);
            Object v = params.size() > 1
                    ? Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(1)), vfs, false, config)
                    : null;
            if (o instanceof Token<?> || (o instanceof TokenDefault && Interpreter.isVariableToken(o))) {
                TokenDefault token = ((Token<?>) o).getValue();
                // Only TokenDefault classes have the .toToken() method, but TokenDefualt itself
                // doesnt, so we kinda need to check for every possible case unfortunately.
                Object input = token instanceof TStatement || token instanceof TVarRef || token instanceof TFuncCall
                        ? o
                        : token;
                Object value = Interpreter.handleVariables(input, vfs, config);
                // System.out.println(value);
                output = value.toString();
            } else if (o instanceof ThrowIfGlobalContext) {
                // we dont have context, so just alert the user.
                // that either, they tried using the "voetsek" or "nevermind" outside of a for
                // loop context or cima was called with a custom error.
                throw new InterpreterException.WtfAreYouDoingException(
                        "Cannot use 'voetsek' or 'nevermind' outside of a loop context, or 'cima' was called with a custom error.",
                        tFuncCall.lineNumber);
            } else {
                output = o.toString();
            }
            if (v instanceof Boolean && (Boolean) v == true) {
                System.out.print(EscapeSequence.fromEscape(output.toString(), tFuncCall.lineNumber));
            } else {
                System.out.println(EscapeSequence.fromEscape(output.toString(), tFuncCall.lineNumber));
            }
            return void.class;
        }
    }

    /**
     * maak n <- mamela()!
     * Listens for input form the console and returns a string.
     */
    class FMamela extends BaseFunction {
        FMamela(Token<?> container) {
            super("mamela", container.new TFunction("mamela", new String[] {}, null, -1,
                    "Listens for input from the console. Warning this will pause all execution until input is given."));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs,
                IConfig config)
                throws Exception {

            String output = config.resources.consoleIn.nextLine();

            return output;
        }
    }

    /**
     * Represents the "ask" function, which prompts the user for input via a UI
     * dialog.
     * <p>
     * This function displays a dialog box with a message provided as a parameter
     * and
     * returns the user's input as a string.
     * </p>
     *
     * <p>
     * Usage: <code>ask("Enter your name:")</code>
     * </p>
     *
     * @see javax.swing.JOptionPane#showInputDialog(Object)
     */
    class FAsk extends BaseFunction {
        FAsk(Token<?> container) {
            super("ask", container.new TFunction("ask", new String[] { "message" }, null, -1,
                    "Prompts the user for input via a UI."));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs, IConfig config)
                throws Exception {
            checkParams(tFuncCall);
            return JOptionPane.showInputDialog(params.get(0));
        }
    }

    /**
     * FClear is a built-in function that clears the console output.
     * <p>
     * This function uses ANSI escape codes to clear the terminal screen and move
     * the cursor to the home position.
     * It does not take any parameters and returns void.
     * </p>
     *
     * Usage:
     * 
     * <pre>
     * clear();
     * </pre>
     *
     * Note: The effectiveness of this function depends on the terminal's support
     * for ANSI escape codes.
     */
    class FClear extends BaseFunction {

        FClear(Token<?> container) {
            // Define the function without any parameters.
            super("clear",
                    container.new TFunction("clear", new String[] {}, null, -1, "Clears the console."));
            freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs, IConfig config)
                throws InterpreterException {
            // Clear the console using ANSI escape codes.
            System.out.print("\033[H\033[2J");
            System.out.flush();
            return Token.voidValue(tFuncCall.lineNumber);
        }
    }

}
