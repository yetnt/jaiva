package com.jaiva.interpreter.symbol.continuous;

import com.jaiva.errors.InterpreterException;

public final class TypeConverter {
    public static byte toByte(int i)  {
        if (i < 0 || i > 255) {
            throw new IllegalArgumentException(
                    "Expected a byte value between 0 and 255, got: " + i
            );
        }

        return (byte)i;
    }

    public static int fromByte(byte b) {
        return b & 0xFF;
    }
}
