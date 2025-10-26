package com.jaiva.errors;

import com.jaiva.utils.CCol;

public final class Warnings {
    private Warnings() {

    }

    public static void println(int ln, String message) {
        System.out.println(CCol.print("[WARNING: line " + ln + "]", CCol.FONT.BOLD, CCol.BG.BRIGHT_WHITE, CCol.TEXT.YELLOW) + " " + CCol.print(message, CCol.TEXT.YELLOW));
    }
}
