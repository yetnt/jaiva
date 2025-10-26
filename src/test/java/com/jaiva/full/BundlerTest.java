package com.jaiva.full;

import com.jaiva.JBundler;
import com.jaiva.errors.JaivaException;
import com.jaiva.interpreter.Scope;
import com.jaiva.interpreter.libs.BaseLibrary;
import com.jaiva.interpreter.libs.LibraryType;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.BaseFunction;
import com.jaiva.interpreter.symbol.BaseVariable;
import com.jaiva.tokenizer.tokens.Token;
import com.jaiva.tokenizer.jdoc.JDoc;
import com.jaiva.tokenizer.tokens.specific.TFuncCall;
import com.jaiva.tokenizer.tokens.specific.TFunction;
import com.jaiva.tokenizer.tokens.specific.TStringVar;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;

class CustomLib extends BaseLibrary {
    public CustomLib(IConfig<Object> config) {
        super(LibraryType.LIB, "customPath");
        vfs.put("echo", new FEcho());
        vfs.put("poop", new VVar());
    }

    static class FEcho extends BaseFunction {
        public FEcho() {
            super("echo", new TFunction("echo", new String[] {"arg1"}, null, -1, JDoc.fromString("d")));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config, Scope scope) throws Exception {
            this.checkParams(tFuncCall, scope);
            return params.getFirst();
        }
    }

    static class VVar extends BaseVariable {
        public VVar() {
            super("poop", new TStringVar("poop", "factuality", -1, JDoc.fromString("The best string")), "facuality");
            this.freeze();
        }
    }
}

public class BundlerTest {
    private static final Path BUNDLER_JIV;

    static {
        try {
            BUNDLER_JIV = Path.of(
                    Objects.requireNonNull(
                                    IntTest.class.getClassLoader()
                                            .getResource("bundler.jiv"))
                            .toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void test1() {
        ArrayList<Token<?>> tokens = new ArrayList<>();
        try {
            JBundler jb = new JBundler(BUNDLER_JIV.toString(), CustomLib.class);
            jb.run(null);

            // DebugException was not thrown. Test failure.
            Assertions.fail("Expected DebugException, but no exception was thrown.");
        } catch (JaivaException.DebugException e) {
            // check that there is no error
            Assertions.assertNull(e.error, "An error ocurred and was saved into DebugException.error");

            System.out.println(e.components);
        } catch (Exception e) {
            // catch any other exception that we realistically don't want to catch
            Assertions.fail("Exception thrown: " + e.getMessage(), e);
        }
    }
}
