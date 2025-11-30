package com.jaiva.interpreter.libs.time;

import com.jaiva.errors.InterpreterException;
import com.jaiva.interpreter.Primitives;
import com.jaiva.interpreter.Scope;
import com.jaiva.interpreter.libs.BaseLibrary;
import com.jaiva.interpreter.libs.LibraryType;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.BaseFunction;
import com.jaiva.interpreter.symbol.BaseVariable;
import com.jaiva.tokenizer.jdoc.JDoc;
import com.jaiva.tokenizer.tokens.specific.TFuncCall;
import com.jaiva.tokenizer.tokens.specific.TFunction;
import com.jaiva.tokenizer.tokens.specific.TNumberVar;
import com.jaiva.tokenizer.tokens.specific.TVoidValue;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Time extends BaseLibrary {
    public static String path = "time";
    public Time(IConfig<Object> config) {
        super(LibraryType.LIB, "time");
        vfs.put("t_now", new FNow());
        vfs.put("t_msToSec", new FMsToSec());
        vfs.put("t_parseDate", new FParseDate());

    }

    public static class FNow extends BaseFunction {
        public FNow() {
            super("t_now", new TFunction("t_now", new String[]{}, null, -1,
                    JDoc.builder()
                            .addDesc("Returns the current time in milliseconds since the Unix epoch.")
                            .addReturns("A number representing the current time in milliseconds.")
                            .sinceVersion("5.0.0")
                            .build()
            ));
            freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config, Scope scope) throws Exception {
            checkParams(tFuncCall, scope);
            return System.currentTimeMillis();
        }
    }

    public static class FMsToSec extends BaseFunction {
        public FMsToSec() {
            super("t_msToSec", new TFunction("t_msToSec", new String[]{"milliseconds"}, null, -1,
                    JDoc.builder()
                            .addDesc("Converts milliseconds to seconds.")
                            .addParam("milliseconds", "number", "The number of milliseconds to convert.", false)
                            .addReturns("A number representing the converted seconds.")
                            .sinceVersion("5.0.0")
                            .build()
            ));
            freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config, Scope scope) throws Exception {
            checkParams(tFuncCall, scope);
            Object val = Primitives.toPrimitive(com.jaiva.interpreter.Primitives.parseNonPrimitive(params.getFirst()), false, config, scope);
            if (!(val instanceof Number num))
                throw new InterpreterException.WtfAreYouDoingException(scope, "t_msToSec() only accepts a number.", tFuncCall.lineNumber);

            return num.doubleValue() / 1000.0;
        }

    }

    public static class FParseDate extends BaseFunction {
        public FParseDate() {
            super("t_parseDate", new TFunction("t_parseDate", new String[]{"dateString", "format?", "timezone?"}, null, -1,
                    JDoc.builder()
                            .addDesc("Parses a date string into milliseconds since the Unix epoch.")
                            .addParam("dateString", "string", "The date string to parse.", false)
                            .addParam("format", "string", "The format of the date string (e.g., \"yyyy-MM-dd HH:mm:ss\"). Defaults to ISO_LOCAL_DATE_TIME.", true)
                            .addParam("timezone", "string", "The timezone to parse this date into. You can either put a magic string yourself or use the constants within \"jaiva/timezone\"", true)
                            .addReturns("A number representing the parsed date in milliseconds.")
                            .addNote("If no timezone is provided, a default timezone of UTC is used.")
                            .addExample("""
                                    @ Import jaiva/time/zone
                                    tsea "jaiva/time/zone" <- TZ_AfricaJohannesburg!
                                    maak ms <- t_parseDate("2023-10-05 14:30:00", "yyyy-MM-dd HH:mm:ss", TZ_AfricaJohannesburg)!
                                    khuluma(ms)! @ Outputs the milliseconds since epoch for the given date in the specified timezone.
                                    """)
                            .sinceVersion("5.0.0")
                            .build()
            ));
            freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config, Scope scope) throws Exception {
            checkParams(tFuncCall, scope);
            Object dateStringObj = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.getFirst()), false, config, scope);
            if (!(dateStringObj instanceof String dateString))
                throw new InterpreterException.WtfAreYouDoingException(scope, "t_parseDate() expects a date string as the first argument.", tFuncCall.lineNumber);

            String format = null;
            if (params.size() > 1) {
                Object formatObj = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(1)), false, config, scope);
                if (!(formatObj instanceof String) && !(formatObj instanceof TVoidValue))
                    throw new InterpreterException.WtfAreYouDoingException(scope, "t_parseDate() expects a format string as the second argument.", tFuncCall.lineNumber);
                format = formatObj instanceof TVoidValue ? null : (String) formatObj;
            }

            DateTimeFormatter formatter;
            if (format != null) {
                try {
                    formatter = DateTimeFormatter.ofPattern(format);
                } catch (IllegalArgumentException e) {
                    throw new InterpreterException.WtfAreYouDoingException(scope, "Pls put valid format for date gng",  tFuncCall.lineNumber);
                }
            } else
                formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

            LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);

            String timezone = "UTC";
            if (params.size() > 2) {
                Object formatObj = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.get(2)), false, config, scope);
                if (!(formatObj instanceof String))
                    throw new InterpreterException.FunctionParametersException(scope, this, "3", formatObj, String.class, tFuncCall.lineNumber);
//                    throw new InterpreterException.WtfAreYouDoingException(scope, "t_parseDate() needs the 3rd param to be a string zawg.", tFuncCall.lineNumber);
                timezone = (String) formatObj;
            }

            ZoneId timeZoneId;
            try {
                timeZoneId = ZoneId.of(timezone);
            } catch (DateTimeException f) {
                throw new InterpreterException.WtfAreYouDoingException(scope, "If you parse a zoneID it has to be valid zawlf", tFuncCall.lineNumber);
            }

            ZonedDateTime zonedDateTime = dateTime.atZone(timeZoneId);
            return zonedDateTime.toInstant().toEpochMilli();
        }
    }

    public static class FMaxTime extends BaseVariable {
        public FMaxTime() {
            super("t_maxTime", new TNumberVar("t_maxTime", Long.MAX_VALUE, -1,
                    JDoc.builder()
                            .addDesc("Returns the maximum possible value for a time in milliseconds (Long.MAX_VALUE).")
                            .sinceVersion("5.0.0")
                            .build()
            ), Long.MAX_VALUE);
            freeze();
        }

    }
}
