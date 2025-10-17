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
import com.jaiva.interpreter.Scope;
import com.jaiva.interpreter.Interpreter;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.BaseFunction;
import com.jaiva.interpreter.symbol.Symbol;
import com.jaiva.tokenizer.Token;

public class IntTest {
    private static final Path FILE_JIV;
    private static final Path FILE2_JIV;
    private static final Path IMPORT_JIV;

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
            IMPORT_JIV = Path.of(
                    Objects.requireNonNull(
                            IntTest.class.getClassLoader()
                                    .getResource("import.jiv"))
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
            IConfig<Object> c = new IConfig<Object>(new ArrayList<>(Arrays.asList(
                    FILE_JIV.toString())),
                    FILE_JIV.toString(),
                    null);
            Interpreter.interpret(tokens, new Scope(c), c);

            // DebugException was not thrown. Test failure.
            Assertions.fail("Expected DebugException, but no exception was thrown.");
        } catch (DebugException e) {
            // check that there is no error
            Assertions.assertNull(e.error, "An error ocurred and was saved into DebugException.error");

            // Then check if the interpreter has the string.
            Assertions.assertEquals("Hello World!", e.components.getFirst(),
                    "d_emit did not get \"Hello World\" as a component");
        } catch (Exception e) {
            // catch any other exception that we realistically don't want to catch
            Assertions.fail("Exception thrown: " + e.getMessage(), e);
        }
    }

    @Test
    void file2Jiv() {
        ArrayList<Token<?>> tokens = new ArrayList<>();
        try {
            // run main directly
            // Main.main(new String[] { fileJIV, "-d" });

            // or invoke the required things so we can customize the environment.
            tokens = Main.parseTokens(FILE2_JIV.toString(), false);
            IConfig<Object> c = new IConfig<Object>(new ArrayList<>(Arrays.asList(
                    FILE2_JIV.toString())),
                    FILE2_JIV.toString(),
                    null);
            Interpreter.interpret(tokens, new Scope(c), c);

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

    @Test
    void importJiv() {
        ArrayList<Token<?>> tokens = new ArrayList<>();
        try {
            // run main directly
            // Main.main(new String[] { fileJIV, "-d" });

            // or invoke the required things so we can customize the environment.
            tokens = Main.parseTokens(IMPORT_JIV.toString(), false);
            IConfig<Object> c = new IConfig<Object>(new ArrayList<>(Arrays.asList(
                    IMPORT_JIV.toString())),
                    IMPORT_JIV.toString(),
                    null);
            Interpreter.interpret(tokens, new Scope(c), c);

            // DebugException was not thrown. Test failure.
            Assertions.fail("Expected DebugException, but no exception was thrown.");
        } catch (DebugException e) {
            // check that there is no error
            Assertions.assertNull(e.error, "An error ocurred and was saved into DebugException.error");

            // Then check the components
            e.components.forEach(comp -> {
                // "m_pi" gets parsed as a doubled and returned.
                if (comp instanceof Symbol s) {
                    Assertions.assertInstanceOf(BaseFunction.class, comp, s + " is not a BaseFunction");

                    Assertions.assertTrue(s.isFrozen, s.name + " is not frozen.");
                } else {
                    Assertions.assertInstanceOf(Double.class, comp,
                            comp + " which is not a symbol, is also not a double.");
                    Double pi = (Double) comp;

                    Assertions.assertEquals(Math.PI, pi, "Pi values do not match");
                }

            });
        } catch (Exception e) {
            // catch any other exception that we realistically don't want to catch
            Assertions.fail("Exception thrown: " + e.getMessage(), e);
        }
    }
}
