package com.jaiva.full;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.jaiva.Main;
import com.jaiva.errors.InterpreterException.TStatementResolutionException;
import com.jaiva.errors.JaivaException.DebugException;
import com.jaiva.interpreter.Context;
import com.jaiva.interpreter.Interpreter;
import com.jaiva.interpreter.globals.Globals;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.tokenizer.Token;

public class IntTest {
    private static final Path FILE_JIV;
    private static final Path FILE2_JIV;

    static {
        try {
            FILE_JIV = Path.of(
                    Objects.requireNonNull(
                            IntTest.class.getClassLoader()
                                    .getResource("file.jiv"))
                            .toURI());

            FILE2_JIV = Path.of(
                    Objects.requireNonNull(
                            IntTest.class.getClassLoader()
                                    .getResource("file2.jiv"))
                            .toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void fileJiv() {
        ArrayList<Token<?>> tokens = new ArrayList<>();
        try {
            // run main directly
            // Main.main(new String[] { fileJIV, "-d" });

            // or invoke the required things so we can customize the environment.
            tokens = Main.parseTokens(FILE_JIV.toString(), false);
            IConfig c = new IConfig(new ArrayList<>(Arrays.asList(
                    FILE_JIV.toString())),
                    FILE_JIV.toString(),
                    Main.callJaivaSrc());
            Interpreter.interpret(tokens, Context.GLOBAL, new Globals(c).vfs, c);

            // DebugException was not thrown. Test failure.
            Assertions.fail("Expected DebugException, but no exception was thrown.");
        } catch (DebugException e) {
            // check that there is no error
            Assertions.assertNull(e.error, "An error ocurred and was saved into DebugException.error");

            // Then check if the interpreter has the string.
            Assertions.assertEquals("Hello World!", e.components.get(0),
                    "d_emit did not get \"Hello World\" as a component");
        } catch (Exception e) {
            // catch any other exception that we realistically don't want to catch
            Assertions.fail("Exception thrown: " + e.getMessage(), e);
        }
    }

    @Test
    void run2() {
        ArrayList<Token<?>> tokens = new ArrayList<>();
        try {
            // run main directly
            // Main.main(new String[] { fileJIV, "-d" });

            // or invoke the required things so we can customize the environment.
            tokens = Main.parseTokens(FILE2_JIV.toString(), false);
            IConfig c = new IConfig(new ArrayList<>(Arrays.asList(
                    FILE2_JIV.toString())),
                    FILE2_JIV.toString(),
                    Main.callJaivaSrc());
            Interpreter.interpret(tokens, Context.GLOBAL, new Globals(c).vfs, c);

            // DebugException was not thrown. Test failure.
            Assertions.fail("Expected DebugException, but no exception was thrown.");
        } catch (DebugException e) {
            // In this test, e.error will be populated, which is what we actually want to
            // test for
            Assertions.assertNotNull(e.error, "Code does not throw an error when it should.");

            // Make sure the error is of the right class, a TStatementResolutionException
            Assertions.assertInstanceOf(TStatementResolutionException.class, e.error);

            TStatementResolutionException error = (TStatementResolutionException) e.error;

            // The error is on line 14
            Assertions.assertTrue(error.getMessage().startsWith("[14]"), "Error is not on line 14.");
        } catch (Exception e) {
            // catch any other exception that we realistically don't want to catch
            Assertions.fail("Exception thrown: " + e.getMessage(), e);
        }
    }
}
