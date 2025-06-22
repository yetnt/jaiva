package com.jaiva.lang;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.jaiva.errors.TokenizerException;

public class EscapeSequenceTest {
    @Test
    void testEscape() throws TokenizerException {
        String str = "hello\t\t!!$";
        String input = "hello$t$t$!$!$$";
        String g = EscapeSequence.fromEscape(input, 10);
        System.out.println();
        System.out.println(str);
        System.out.println(g);
        Assertions.assertEquals(str, g);
    }

    @Test
    void testUnescape() {

        String str = "hello\t\t!!$,$";
        String input = "hello$t$t$!$!$,$$";
        String g = EscapeSequence.toEscape(str);
        System.out.println();
        System.out.println(str);
        System.out.println(g);
        Assertions.assertEquals(input, g);
    }

    @Test
    void testEscapeAll() {
        // Test basic escaping inside a string literal
        {
            String input = "print(\"hello, world!\")";
            String expected = "print(\"hello$, world$!\")";
            String result = EscapeSequence.escapeAll(input);
            Assertions.assertEquals(expected, result, "Basic escaping inside string literal failed");
        }

        // Test escaping inside multiple string literals in one input
        {
            String input = "func(\"a,b\", \"c!d\", \"e@f\")";
            String expected = "func(\"a$,b\", \"c$!d\", \"e$@f\")";
            String result = EscapeSequence.escapeAll(input);
            Assertions.assertEquals(expected, result, "Escaping inside multiple string literals failed");
        }

        // Test input with no string literals (should remain unchanged)
        {
            String input = "maak y <- 10 + 20";
            String expected = "maak y <- 10 + 20";
            String result = EscapeSequence.escapeAll(input);
            Assertions.assertEquals(expected, result, "Input with no string literals should remain unchanged");
        }

        // Test escaping inside a string literal with escaped quotes
        {
            String input = "say(\"he said $\"hi!$\"\")";
            // The escapeAll method only escapes inside string literals, not escaped quotes
            String expected = "say(\"he said $\"hi$!$\"\")";
            String result = EscapeSequence.escapeAll(input);
            Assertions.assertEquals(expected, result, "Escaping inside string with escaped quotes failed");
        }

        // Test input with unclosed string literal (should remain unchanged)
        {
            String input = "print(\"hello, world!)";
            String expected = "print(\"hello, world!)";
            String result = EscapeSequence.escapeAll(input);
            Assertions.assertEquals(expected, result, "Input with unclosed string literal should remain unchanged");
        }

        // Test escaping special characters inside a string literal
        {
            String input = "echo(\"line1\nline2\tend\")";
            String expected = "echo(\"line1$nline2$tend\")";
            String result = EscapeSequence.escapeAll(input);
            Assertions.assertEquals(expected, result, "Escaping special characters inside string literal failed");
        }

        // Test escaping inside adjacent string literals
        {
            String input = "\"a,b\"\"c!d\"";
            String expected = "\"a$,b\"\"c$!d\"";
            String result = EscapeSequence.escapeAll(input);
            Assertions.assertEquals(expected, result, "Escaping inside adjacent string literals failed");
        }
    }
}
