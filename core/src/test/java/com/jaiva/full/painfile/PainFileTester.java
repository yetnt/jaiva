package com.jaiva.full.painfile;

import com.jaiva.Main;
import com.jaiva.interpreter.Interpreter;
import com.jaiva.interpreter.Scope;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.tokenizer.tokens.*;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PainFileTester {
    static final Path PAIN_JVA;

    static {
        try {
            PAIN_JVA = Path.of(
                    Objects.requireNonNull(
            PainFileTester .class.getClassLoader()
                    .getResource("pain.jva")).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    protected ArrayList<Token<?>> tokens;

    public PainFileTester() {
    }

    public ArrayList<Token<?>> tokenize() throws Exception {
        tokens = Main.parseTokens(PAIN_JVA.toString(), false);
        return tokens;
    }

    public void interpret() throws Exception {
        IConfig<Object> c = new IConfig<Object>(new ArrayList<>(List.of(
                PAIN_JVA.toString())),
                PAIN_JVA.toString()
        );
        Interpreter.interpret(tokens, new Scope(c), c);
    }

}
