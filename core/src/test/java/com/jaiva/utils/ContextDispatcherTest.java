package com.jaiva.utils;

import java.util.ArrayList;
import java.util.Arrays;

import com.jaiva.utils.cd.ContextDispatcher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.jaiva.utils.cd.ContextDispatcher.To;

public class ContextDispatcherTest {
    public static final ArrayList<String> inputs = new ArrayList<>(
            Arrays.asList("a", "1 + a", "a + func()", "1 + a - (a / b)", "(", ")", "func()", "(1 - c) + n",
                    "func(a + b)", "func(a + (b - a))", "func(func(a - b))", "func(func(a) | b)",
                    "func((1 > 1) | func(10))", "func(a) + b", "func(func(a) - b) / 5", "func() < funcf()", "",
                    "func(1 + 3 + 4 / 2 / 2)", "func(func(func(10) - 1) + 334) / 2 = (2 - 4)"));
    public static final ArrayList<To> expected = new ArrayList<>(
            Arrays.asList(To.PROCESS_CONTENT, To.TEXPRESSION, To.TEXPRESSION, To.TEXPRESSION, To.SINGLE_BRACE,
                    To.SINGLE_BRACE, To.PROCESS_CONTENT, To.TEXPRESSION, To.PROCESS_CONTENT, To.PROCESS_CONTENT,
                    To.PROCESS_CONTENT, To.PROCESS_CONTENT, To.PROCESS_CONTENT, To.TEXPRESSION, To.TEXPRESSION,
                    To.TEXPRESSION, To.EMPTY_STRING, To.PROCESS_CONTENT, To.TEXPRESSION));

    @Test
    void testGetDeligation() {

        for (int i = 0; i < inputs.size(); i++) {
            String input = inputs.get(i);
            To actual = new ContextDispatcher(input).getDeligation();
            Assertions.assertEquals(expected.get(i), actual);
        }

    }
}
