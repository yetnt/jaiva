package com.jaiva.interpreter.libs.types;

public final class TypeConverter {
    public static byte toByte(int i)  {
        if (i < 0 || i > 255) {
            throw new IllegalArgumentException(
                    "Expected a byte value between 0 and 255, got: " + i
            );
        }

        return (byte)i;
    }

    public static byte toByte(long i)  {
        if (i < 0 || i > 255) {
            throw new IllegalArgumentException(
                    "Expected a byte value between 0 and 255, got: " + i
            );
        }

        return (byte)i;
    }

    public static int[] byteArrToIntArr(byte[] arr) {
        int[] arr2 = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            arr2[i] = fromByte(arr[i]);
        }
        return arr2;
    }

    public static int fromByte(byte b) {
        return b & 0xFF;
    }
}
