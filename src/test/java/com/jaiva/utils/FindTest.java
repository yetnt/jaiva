package com.jaiva.utils;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.jaiva.utils.Find.LeastImportantOperator;

public class FindTest {

//    public static

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
        String statement = "([])[]";
        int actual = Find.lastOutermostBracePair(statement);
        int expected = 4;
        Assertions.assertEquals(expected, actual);

        statement = "[(([])())()]";
        actual = Find.lastOutermostBracePair(statement);
        expected = 0;
        Assertions.assertEquals(expected, actual);
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
        Assertions.assertEquals(expected, l);
        statement = "5 + 3 * (2 - 8)";
        l = Find.leastImportantOperator(statement);
        expected = new LeastImportantOperator("+", 2, 2);
        Assertions.assertEquals(expected, l);

        statement = "10 / (2 + 3) - 4";
        l = Find.leastImportantOperator(statement);
        expected = new LeastImportantOperator("-", 13, 2);
        Assertions.assertEquals(expected, l);

        statement = "7 * (6 - (3 + 2))";
        l = Find.leastImportantOperator(statement);
        expected = new LeastImportantOperator("*", 2, 1);
        Assertions.assertEquals(expected, l);

        statement = "4 ^ 2 + 6 / 3";
        l = Find.leastImportantOperator(statement);
        expected = new LeastImportantOperator("+", 6, 2);
        Assertions.assertEquals(expected, l);
    }

    @Test
    void testQuotationPairs() {
        // test to mkae sure $" is escaped.
        String inputStr = "\"dd\" - \"$\"32\"";
        ArrayList<Pair<Integer>> actual = Find.quotationPairs(inputStr);
        ArrayList<Pair<Integer>> expected = new ArrayList<>(Arrays.asList(
                new Pair<>(0, 3),
                new Pair<>(7, 12)));
        Assertions.assertEquals(expected, actual);

        // test to make sure double $$ doesnt trigger escaping the " character.
        inputStr = "\"dd - $$\"\" $\" \"";
        actual = Find.quotationPairs(inputStr);
        expected = new ArrayList<>(Arrays.asList(
                new Pair<>(inputStr.indexOf('"'), 8),
                new Pair<>(9, inputStr.lastIndexOf('"'))));
        Assertions.assertEquals(expected, actual);
    }

    public static String inputString = "function([function])[2 + 4] - (\"hi hi(\")' + -2-2-2-2";

    public static ArrayList<Pair<Integer>> braces = new ArrayList<>(
            Arrays.asList(
                    new Pair<>(9, 18),
                    new Pair<>(8, 19),
                    new Pair<>(20, 26),
                    new Pair<>(30, 39)));
    public static ArrayList<Pair<Integer>> quotes = new ArrayList<>(
            Arrays.asList(
                    new Pair<>(31, 38)));

    @Test
    void testBracePairs() {
        ArrayList<Pair<Integer>> actual = Find.bracePairs(inputString).first;
        for (Pair<Integer> pair : actual) {
            Assertions.assertNotEquals(-1, braces.indexOf(pair), pair + " does not exist in expected array");
        }
    }

    @Test
    void testLastIndexOf() {
        Assertions.assertEquals(0, Find.lastIndexOf(inputString, "function"));
        Assertions.assertEquals(-1, Find.lastIndexOf(inputString, "hi"));
        Assertions.assertEquals(49, Find.lastIndexOf(inputString, "2-"));
    }

    @Test
    void testSanitizeStatement() {
        String statement = "-5*-3/4--4";
        ArrayList<Integer> opIndexes = new ArrayList<>(Arrays.asList(0, 2, 3, 5, 7, 8));
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(2, 5, 7));
        ArrayList<Integer> actual = Find.sanitizeStatement(statement, opIndexes);

        Assertions.assertEquals(expected, actual);
    }
}
