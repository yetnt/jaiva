package com.jaiva.full;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.jaiva.Main;
import com.jaiva.errors.JaivaException.DebugException;
import com.jaiva.interpreter.Context;
import com.jaiva.interpreter.Interpreter;
import com.jaiva.interpreter.globals.Globals;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.tokenizer.Token;
import com.jaiva.tokenizer.Token.TFuncCall;

public class ITTest {

    private static String fileJIV = "C:\\Users\\ACER\\Documents\\code\\jaiva\\target\\test-classes\\file.jiv";

    @Test
    void run1() {
        ArrayList<Token<?>> tokens = new ArrayList<>();
        try {
            // run main directly
            // Main.main(new String[] { fileJIV, "-d" });

            // or invoke the required things so we can customize the environment.
            tokens = Main.parseTokens(fileJIV, false);
            IConfig c = new IConfig(new ArrayList<>(Arrays.asList(fileJIV)), fileJIV,
                    Main.callJaivaSrc());
            Interpreter.interpret(tokens, Context.GLOBAL, new Globals(c).vfs, c);

            // DebugException was not thrown. Test failure.
            Assertions.fail("Expected DebugException, but no exception was thrown.");
        } catch (DebugException e) {
            // check that there is no error
            Assertions.assertNull(e.error, "An error ocurred and was saved into DebugException.error");
            // check that there are exactly 3 outer tokens.
            Assertions.assertEquals(3, tokens.size(), "Token size is NOT exactly 3.");

            // check that the last token is a TFuncCall
            Assertions.assertInstanceOf(Token.TFuncCall.class, ((Token) tokens.getLast()).getValue(),
                    "The last token is not a function call, thats weird thats literally what causes this DebugException.");
            // As this was emitted, the LAST token in the tokens list, should be a TFuncCall
            // and it's first parameter, should contain the exact same stirng.
            // Check if the tokenizer has the string.
            Assertions.assertEquals("Hello World", ((TFuncCall) ((Token) tokens.getLast()).getValue()).args.get(0),
                    "d_emit did not receive the exact stirng hello world (Token)");
            // This is the exception we want to catch and debug. Check if the components has
            // the string hello world
            // Then check if the interpreter has the string.
            Assertions.assertEquals("Hello World", e.components.get(0),
                    "d_emit did not get \"Hello World\" as a component");
        } catch (Exception e) {
            // catch any other exception that we realistically don't want to catch
            Assertions.fail("Exception thrown: " + e.getMessage(), e);
        }
    }
}
