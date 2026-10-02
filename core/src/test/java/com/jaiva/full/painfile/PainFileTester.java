package com.jaiva.full.painfile;

import com.jaiva.Main;
import com.jaiva.interpreter.Interpreter;
import com.jaiva.interpreter.Scope;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.tokenizer.tokens.TConstruct;
import com.jaiva.tokenizer.tokens.Token;
import com.jaiva.tokenizer.tokens.TokenDefault;
import com.jaiva.tokenizer.tokens.specific.TCodeblock;
import com.jaiva.tokenizer.tokens.specific.TFunction;
import com.yetnt.utils.tuple.Pair;
import org.junit.jupiter.api.Assertions;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

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
                PAIN_JVA.toString(),
                null);
        Interpreter.interpret(tokens, new Scope(c), c);
    }

    public <T extends TokenDefault<T>> T assertAndCast(Token<?> token, Class<T> clz) {
        Assertions.assertNotNull(token);
        Assertions.assertInstanceOf(
                clz, token.value(),
                "Token was expected to be " + clz.getSimpleName() + ". Was instead " + token.value().getClass().getSimpleName()
                );
        return (T) token.value();
    }

    public void assertLineNumber(TokenDefault<?> token, int expectedLineNumber) {
        Assertions.assertNotNull(token);
        Assertions.assertEquals(
                expectedLineNumber,
                token.lineNumber,
                "The " + token.name + " token (" + token.getClass().getSimpleName() + ") "
                + "was expected to be on line number " + expectedLineNumber
        );
    }

    public ArrayList<TCodeblock> assertConstruct(TConstruct construct, int codeblockAmount) {
        Assertions.assertNotNull(construct);

        Assertions.assertEquals(
                codeblockAmount,
                construct.getCodeBlocks().size()
        );

        return construct.getCodeBlocks();
    }

    public void assertFunctionDefinition(
            TFunction function, String[] expectedArgs,
            ArrayList<Boolean> expectedOptionalityArr,
            boolean expectedVarArgs,
            Predicate<String> defintionStringPredicate
    ) {
        // Arguments
        Assertions.assertArrayEquals(expectedArgs, function.args);
        // Argumnts are all required
        Assertions.assertEquals(expectedOptionalityArr, function.isArgOptional);
        // Not Var Args
        Assertions.assertEquals(expectedVarArgs, function.varArgs);
        // Defintiion string matches
        Assertions.assertTrue(defintionStringPredicate.test(function.name));
    }
}
