package com.jaiva.full.painfile;

import com.jaiva.tokenizer.tokens.Token;
import com.jaiva.tokenizer.tokens.specific.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class PainFileTokenizerTests{

    static PainFileTester pft;
    static ArrayList<Token<?>> tokens;

    @BeforeAll
    static void setUp() throws Exception{
        pft = new PainFileTester();
        tokens = pft.tokenize();
    }

    @Test
    void structureTest() {
        Assertions.assertNotNull(tokens);

        // The file should contain 11 top level tokens
        Assertions.assertEquals(11, tokens.size(),
                "Tokenizer emitted more than the expected 11 tokens ("+tokens.size()+")."
        );
    }

    @Test
    void importsTest() {
        TImport arraysImport = pft.assertAndCast(tokens.getFirst(), TImport.class);
        TImport debugImport = pft.assertAndCast(tokens.get(1), TImport.class);

        pft.assertLineNumber(arraysImport, 26);
        pft.assertLineNumber(debugImport, 27);

        Assertions.assertNotNull(arraysImport);
        Assertions.assertNotNull(debugImport);

        // Imports are normalised to the user's directory even if library so check for the name
        // contained in the import

        Assertions.assertTrue(arraysImport.fileName.contains("arrays"));
        Assertions.assertTrue(debugImport.fileName.contains("debug"));

        Assertions.assertTrue(arraysImport.isLib);
        Assertions.assertTrue(debugImport.isLib);

        Assertions.assertEquals(new ArrayList<>(), arraysImport.symbols);
        Assertions.assertEquals(new ArrayList<>(List.of("d_emit")), debugImport.symbols);
    }

    @Test
    void replaceOddChar_functionDefintion() {
        TFunction replaceOddChar =  pft.assertAndCast(tokens.get(2), TFunction.class);
        pft.assertLineNumber(replaceOddChar, 34);
        // name with F~ prepended to it
        Assertions.assertEquals("F~replaceOddChar", replaceOddChar.name);

        // Function is exportedc with * syntax
        Assertions.assertTrue(replaceOddChar.exportSymbol);

        pft.assertFunctionDefinition(
                replaceOddChar, new String[]{"original", "c"},
                new ArrayList<>(List.of(false, false)),
                false,
                (actual) -> actual.equals("F~replaceOddChar(original, c)")
        );

        // Function has only a single body
        TCodeblock body = pft.assertConstruct(replaceOddChar, 1).getFirst();

        // the body goes to line number 45 and has 5 tokens
        Assertions.assertEquals(45, body.lineNumberEnd);
        Assertions.assertEquals(5, body.lines.size());
    }

    @Test
    void replaceOddChar_Body() {
        TFunction replaceOddChar =  pft.assertAndCast(tokens.get(2), TFunction.class);
        TCodeblock body = replaceOddChar.getCodeBlocks().getFirst();
        ArrayList<Token<?>> lines =  body.lines;

        // 5 tokens
        Assertions.assertEquals(5, lines.size());

        // maak originalAsCharArray <- arrLit(original:::)!
        {
            TUnknownScalar<?, ?> originalAsCharArray = pft.assertAndCast(lines.getFirst(), TUnknownScalar.class);
            pft.assertLineNumber(originalAsCharArray, 35);
            Assertions.assertEquals("originalAsCharArray", originalAsCharArray.name);
            Assertions.assertInstanceOf(Token.class, originalAsCharArray.value);
            TFuncCall value = pft.assertAndCast((Token<?>) originalAsCharArray.value, TFuncCall.class);
            pft.assertLineNumber(originalAsCharArray, 35);
        }

    }
}
