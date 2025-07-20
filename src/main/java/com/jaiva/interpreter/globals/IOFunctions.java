package com.jaiva.interpreter.globals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JOptionPane;

import com.jaiva.errors.InterpreterException;
import com.jaiva.interpreter.ContextTrace;
import com.jaiva.interpreter.Interpreter;
import com.jaiva.interpreter.MapValue;
import com.jaiva.interpreter.Primitives;
import com.jaiva.interpreter.Interpreter.ThrowIfGlobalContext;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.BaseFunction;
import com.jaiva.interpreter.symbol.BaseVariable;
import com.jaiva.lang.EscapeSequence;
import com.jaiva.tokenizer.Token;
import com.jaiva.tokenizer.Token.*;
import com.jaiva.tokenizer.TokenDefault;

/**
 * IOFunctions class holds the functions that are used for input and output in
 * the console.
 */
public class IOFunctions extends BaseGlobals {

    /**
     * Constructs the IOFunctions with the Input/Output functions.
     */
    public IOFunctions(IConfig config) {
        super(GlobalType.GLOBAL);
        vfs.put("khuluma", new MapValue(new FKhuluma()));
        vfs.put("mamela", new MapValue(new FMamela()));
        vfs.put("ask", new MapValue(new FAsk()));
        vfs.put("clear", new MapValue(new FClear()));
        vfs.put("args", new MapValue(new VArgs(config)));
        vfs.put("uargs", new MapValue(new VUArgs(config)));
    }

    /**
     * khuluma("hello world")!
     * This will print the given input to the console.
     */
    class FKhuluma extends BaseFunction {

        FKhuluma() {
            super("khuluma", new TFunction("khuluma", new String[] { "msg?", "removeNewLn?" }, null, -1,
                    "Prints any given input to the console with a newline afterwards. \\n(It just uses System.out.println() lol) This function returns no value."));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs,
                IConfig config, ContextTrace cTrace)
                throws Exception {
            checkParams(tFuncCall, cTrace);
            String output;
            Object o = !params.isEmpty() ? params.getFirst() : null;
            Object newO = o;
            if (o == null || o instanceof TVoidValue) {
                System.out.println();
                return Token.voidValue(tFuncCall.lineNumber);
            }
            Object v = params.size() > 1
                    ? Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(1)), vfs, false, config, cTrace)
                    : null;
            if (o instanceof Token<?> || (o instanceof TokenDefault && Interpreter.isVariableToken(o))) {
                assert o instanceof Token<?>;
                TokenDefault token = ((Token<?>) o).value();
                // Only TokenDefault classes have the .toToken() method, but TokenDefualt itself
                // doesnt, so we kinda need to check for every possible case unfortunately.
                Object input = token instanceof TStatement || token instanceof TVarRef || token instanceof TFuncCall
                        ? o
                        : token;
                Object value = Interpreter.handleVariables(input, vfs, config, cTrace);
                // System.out.println(value);
                assert value != null;
                output = value.toString();
                newO = value;
            } else if (o instanceof ThrowIfGlobalContext) {
                // we dont have context, so just alert the user.
                // that either, they tried using the "voetsek" or "nevermind" outside of a for
                // loop context or cima was called with a custom error.
                throw new InterpreterException.WtfAreYouDoingException(cTrace,
                        "Cannot use 'voetsek' or 'nevermind' outside of a loop context, or 'cima' was called with a custom error.",
                        tFuncCall.lineNumber);
            } else {
                output = o.toString();
            }

            String isJustStr = newO instanceof String && config.printStacks ? "\"" : "";

            if (v instanceof Boolean && (Boolean) v) {
                System.out.print(
                        isJustStr + EscapeSequence.fromEscape(output, tFuncCall.lineNumber) + isJustStr);
            } else {
                System.out.println(
                        isJustStr + EscapeSequence.fromEscape(output, tFuncCall.lineNumber) + isJustStr);
            }
            return Token.voidValue(tFuncCall.lineNumber);
        }
    }

    /**
     * maak n <- mamela()!
     * Listens for input form the console and returns a string.
     */
    class FMamela extends BaseFunction {
        FMamela() {
            super("mamela", new TFunction("mamela", new String[] {}, null, -1,
                    "Listens for input from the console. Warning this will pause all execution until input is given."));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs,
                IConfig config, ContextTrace cTrace)
                throws Exception {

            return config.resources.consoleIn.nextLine();
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
        FAsk() {
            super("ask", new TFunction("ask", new String[] { "message" }, null, -1,
                    "Prompts the user for input via a UI."));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs, IConfig config,
                ContextTrace cTrace)
                throws Exception {
            checkParams(tFuncCall, cTrace);
            return JOptionPane.showInputDialog(params.getFirst());
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

        FClear() {
            // Define the function without any parameters.
            super("clear",
                    new TFunction("clear", new String[] {}, null, -1, "Clears the console."));
            freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, HashMap<String, MapValue> vfs, IConfig config,
                ContextTrace cTrace)
                throws InterpreterException {
            // Clear the console using ANSI escape codes.
            System.out.print("\033[H\033[2J");
            System.out.flush();
            return Token.voidValue(tFuncCall.lineNumber);
        }
    }

    /**
     * Represents the "args" variable, which contains the command-line arguments
     * passed to the Jaiva interpreter.
     * <p>
     * This variable is automatically populated with the arguments provided when
     * running the Jaiva command.
     * </p>
     */
    class VArgs extends BaseVariable {
        VArgs( IConfig config) {
            super("args", new TArrayVar("args", new ArrayList<>(Arrays.asList(config.args)), -1,
                    "The command-line arguments passed to the jaiva command"),
                    new ArrayList<>(Arrays.asList(config.args)));
            this.freeze();
        }
    }

    class VUArgs extends BaseVariable {
        VUArgs( IConfig config) {
            super("uArgs", new TArrayVar("uargs", (ArrayList) config.sanitisedArgs, -1,
                    "The command-line arguments without jaiva specific arguments. "),
                    config.sanitisedArgs);
            this.freeze();
        }
    }
}
