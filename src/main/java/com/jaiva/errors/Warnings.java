package com.jaiva.errors;

import com.jaiva.interpreter.Scope;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.utils.generator.CCol;

public final class Warnings {

    public static class Warning {
        Scope scope;
        String message;
        int lineNumber;
        public Warning(Scope scope, String message, int lineNumber) {
            this.scope = scope;
            this.message = message;
            this.lineNumber = lineNumber;
        }

        public Scope getScope() {
            return scope;
        }

        public int getLineNumber() {
            return lineNumber;
        }

        public String getMessage() {
            return message;
        }
    }

    private Warnings() {

    }

    public static void println(int ln, String message, Scope scope, IConfig<Object> config) throws InterpreterException.NoWarningsException {
        if (scope.config.suppressWarnings()) return;
        if (scope.config.elevateWarnings())
            throw new InterpreterException.NoWarningsException(scope, ln, message);
        else {
            if (!config.isStreamer()) {
                System.out.println(CCol.print("[WARNING: line " + ln + "]", CCol.FONT.BOLD, CCol.BG.BRIGHT_WHITE, CCol.TEXT.YELLOW) + " " + CCol.print(message, CCol.TEXT.YELLOW));
            }
            config.addWarning(
                    new Warning(
                            scope, message, ln
                    )
            );
        }
    }
}
