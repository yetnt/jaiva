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
}
