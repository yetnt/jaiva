package com.jaiva.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.jaiva.utils.Find.LeastImportantOperator;

public class FindTest {
    @Test
    void testClosingCharIndex() {
        // Test with ()
        String line = "3211232 412 1312(239(()((())))12()3)";
        Assertions.assertEquals(line.lastIndexOf(")"), Find.closingCharIndex(line, '(', ')'));
        // Test with []
        String line2 = "([[]()])";
        Assertions.assertEquals(6, Find.closingCharIndex(line2, '[', ']'));
    }

    @Test
    void testClosingCharIndexML() {
        String[] lines = new String[3];
        lines[0] = "I>/";
        lines[1] = "V>//>";
        lines[2] = "G/>";
        MultipleLinesOutput m = null;
        for (int i = 0; i < lines.length; i++) {
            String string = lines[i];
            if (m == null)
                m = Find.closingCharIndexML(string, ">/", "/>", 0, 0, "", "test", null, null, i);
            else
                m = Find.closingCharIndexML(string, ">/", "/>", m.startCount, m.endCount, m.preLine, m.b_type,
                        m.b_args,
                        m.specialArg, i);
        }

        System.out.println(m);
    }

    @Test
    void testLastOutermostBracePair() {

    }

    @Test
    void testLeastImportantOperator() {
        /*
         * Where
         * 0 = Exponentiation
         * 1 = div/mult/mod
         * 2 = add/sub
         * 3 = boolean & comparisons
         */
        String statement = "(10 + 4) / 5 * 10";
        LeastImportantOperator l = Find.leastImportantOperator(statement);
        LeastImportantOperator expected = new LeastImportantOperator("*", 13, 1);
        Assertions.assertEquals(expected.toString(), l.toString());
        statement = "5 + 3 * (2 - 8)";
        l = Find.leastImportantOperator(statement);
        expected = new LeastImportantOperator("+", 2, 2);
        Assertions.assertEquals(expected.toString(), l.toString());

        statement = "10 / (2 + 3) - 4";
        l = Find.leastImportantOperator(statement);
        expected = new LeastImportantOperator("-", 13, 2);
        Assertions.assertEquals(expected.toString(), l.toString());

        statement = "7 * (6 - (3 + 2))";
        l = Find.leastImportantOperator(statement);
        expected = new LeastImportantOperator("*", 2, 1);
        Assertions.assertEquals(expected.toString(), l.toString());

        statement = "4 ^ 2 + 6 / 3";
        l = Find.leastImportantOperator(statement);
        expected = new LeastImportantOperator("+", 6, 2);
        Assertions.assertEquals(expected.toString(), l.toString());
    }

    @Test
    void testOutermostOperatorIndex() {

    }

    @Test
    void testQuotationPairs() {

    }

    @Test
    void testSanitizeStatement() {

    }
}
