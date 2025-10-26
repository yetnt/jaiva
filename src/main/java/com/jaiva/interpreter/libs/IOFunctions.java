package com.jaiva.interpreter.libs;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JOptionPane;

import com.jaiva.errors.InterpreterException;
import com.jaiva.interpreter.Scope;
import com.jaiva.interpreter.Interpreter;
import com.jaiva.interpreter.Primitives;
import com.jaiva.interpreter.Interpreter.ThrowIfGlobalContext;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.BaseFunction;
import com.jaiva.interpreter.symbol.BaseVariable;
import com.jaiva.lang.EscapeSequence;
import com.jaiva.tokenizer.tokens.specific.*;
import com.jaiva.tokenizer.tokens.Token;
import com.jaiva.tokenizer.tokens.TokenDefault;
import com.jaiva.tokenizer.jdoc.JDoc;

/**
 * IOFunctions class holds the functions that are used for input and output in
 * the console.
 */
public class IOFunctions extends BaseLibrary {

    /**
     * Constructs the IOFunctions with the Input/Output functions.
     */
    public IOFunctions(IConfig<Object> config) {
        super(LibraryType.GLOBAL);
        vfs.put("khuluma", new FKhuluma());
        vfs.put("mamela", new FMamela());
        vfs.put("ask", new FAsk());
        vfs.put("clear", new FClear());
        vfs.put("args", new VArgs(config));
        vfs.put("uargs", new VUArgs(config));
    }

    /**
     * khuluma("hello world")!
     * This will print the given input to the console.
     */
    class FKhuluma extends BaseFunction {

        FKhuluma() {
            super("khuluma", new TFunction("khuluma", new String[] { "msg?", "removeNewLn?" }, null, -1,
                    JDoc.builder()
                            .addDesc("Prints any given input to the console.")
                            .addParam("msg", "idk","The message to print.", true)
                            .addParam("removeNewLn", "boolean", "If true, no new line is printed after the message. Defaults to false.", true)
                            .addReturns("idk")
                            .sinceVersion("1.0.0-beta.2")
                            .addNote("For the nerds. This is just System.out.println as default, then if removeNewLn is true, System.out.print. Lol")
                            .build()
            ));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params,
                           IConfig<Object> config, Scope scope)
                throws Exception {
            checkParams(tFuncCall, scope);
            String output;
            Object o = !params.isEmpty() ? params.getFirst() : null;
            Object newO = o;
            if (o == null || o instanceof TVoidValue) {
                System.out.println();
                return Token.voidValue(tFuncCall.lineNumber);
            }
            Object v = params.size() > 1
                    ? Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(1)), false, config, scope)
                    : null;
            if (o instanceof Token<?> || (o instanceof TokenDefault && Interpreter.isVariableToken(o))) {
                assert o instanceof Token<?>;
                TokenDefault token = ((Token<?>) o).value();
                // Only TokenDefault classes have the .toToken() method, but TokenDefualt itself
                // doesnt, so we kinda need to check for every possible case unfortunately.
                Object input = token instanceof TStatement || token instanceof TVarRef || token instanceof TFuncCall
                        ? o
                        : token;
                Object value = Interpreter.handleVariables(input, config, scope);
                // System.out.println(value);
                assert value != null;
                output = value.toString();
                newO = value;
            } else if (o instanceof ThrowIfGlobalContext) {
                // we dont have context, so just alert the user.
                // that either, they tried using the "voetsek" or "nevermind" outside of a for
                // loop context or cima was called with a custom error.
                throw new InterpreterException.WtfAreYouDoingException(scope,
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
                    JDoc.builder()
                            .addDesc("Listens for input from the console.")
                            .addReturns("The input given from the console as a string")
                            .addNote("Warning: This will pause all execution until input is given.")
                            .sinceVersion("1.0.0-beta.3")
                    .build()
            ));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params,
                           IConfig<Object> config, Scope scope)
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
                    JDoc.builder()
                            .addDesc("Prompts the user for input via a UI dialog.")
                            .addParam("message", "string", "The message to display in the dialog.", false)
                            .addReturns("The user's input as a string.")
                            .sinceVersion("1.0.0")
                            .build()
            ));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params,  IConfig<Object> config,
                Scope scope)
                throws Exception {
            checkParams(tFuncCall, scope);
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
                    new TFunction("clear", new String[] {}, null, -1,
                            JDoc.builder()
                                    .addDesc("Clears the console.")
                                    .addReturns("idk")
                                    .addNote("The effectiveness of this function depends on the terminal's support for ANSI escape codes.")
                                    .sinceVersion("1.0.0")
                                    .build()
                    ));
            freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params,  IConfig<Object> config,
                Scope scope)
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
        VArgs(IConfig<Object> config) {
            super("args", new TArrayVar("args", new ArrayList<>(Arrays.asList(config.args)), -1,
                    JDoc.builder()
                            .addDesc("The command-line arguments passed to the Jaiva command.")
                            .addReturns("An array of strings, where each string is a command-line argument.")
                            .sinceVersion("1.0.2")
                            .build()
            ),
                    new ArrayList<>(Arrays.asList(config.args)));
            this.freeze();
        }
    }

    class VUArgs extends BaseVariable {
        VUArgs(IConfig<Object> config) {
            super("uArgs", new TArrayVar("uargs", (ArrayList) config.sanitisedArgs, -1,
                    JDoc.builder()
                            .addDesc("The command-line arguments without Jaiva-specific arguments.")
                            .addReturns("An array of strings, representing the sanitized command-line arguments.")
                            .sinceVersion("1.0.2")
                            .build()
            ),
                    config.sanitisedArgs);
            this.freeze();
        }
    }
}
