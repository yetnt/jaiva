package com.jaiva.interpreter.libs.time;

import com.jaiva.interpreter.libs.BaseLibrary;
import com.jaiva.interpreter.libs.LibraryType;
import com.jaiva.interpreter.symbol.BaseVariable;
import com.jaiva.tokenizer.jdoc.JDoc;
import com.jaiva.tokenizer.tokens.specific.TArrayVar;
import com.jaiva.tokenizer.tokens.specific.TStringVar;
import com.jaiva.utils.Pair;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TimeZone extends BaseLibrary {

    public static String path = "time/zone";
    private static ArrayList<Pair<String>> zoneIds = mapZoneIds();

    public TimeZone() {
        super(LibraryType.LIB, "time/zone");

        BaseVariable getAll = new BaseVariable(
                "tz_getAll",
                new TArrayVar(
                        "tz_getAll",
                        new ArrayList<>(zoneIds.stream().map(Pair::getSecond).toList()),
                        -1,
                        JDoc.builder()
                                .addDesc("The complete list of IANA format timezone constants.")
                                .sinceVersion("5.0.0")
                                .addExample("""
                                        tsea "jaiva/timezone"!
                                        
                                        @ Get all timezone constants
                                        maak list <- tz_getAll!
                                        @ Print everything with Etc prefix
                                        colonize item with list ->
                                            if (item ? "Etc") ->
                                                khuluma(item)!
                                            <~
                                        <~
                                        """)
                                .build()
                ), new ArrayList<>(zoneIds.stream().map(Pair::getSecond).toList()));
        getAll.freeze();
        vfs.put("tz_getAll", getAll);

        zoneIds.stream().map(TimeZone::createVariable).forEach(var -> vfs.put(var.name, var));

    }

    protected static ArrayList<Pair<String>> mapZoneIds() {
        ArrayList<Pair<String>> out = new ArrayList<>();
        ZoneId.getAvailableZoneIds().forEach(zoneId -> {
            StringBuilder name = new StringBuilder();
            name.append("TZ_");
            if (!zoneId.contains("/")) {
                out.add(new Pair<>(name.append(zoneId).toString(), zoneId));
            } else if (zoneId.contains("Etc/")) {
                String st = zoneId.replace("Etc/","");
                if (zoneId.length() == 7) {
                    // "Etc/GMT" or "Etc/UTC" to "TZ_GMT" and "TZ_UTC"
                    out.add(new Pair<>(name.append(st).toString(), zoneId));
                } else {
                    out.add(new Pair<>(
                            name.append(st).toString().replace("+", "Behind").replace("-", "After"),
                            zoneId
                    ));
                }
            } else {
                String st = Arrays.stream(zoneId.split(Pattern.quote("/"))).map(
                        str -> String.join("", str.split(Pattern.quote("_")))
                ).collect(Collectors.joining());
                out.add(new Pair<>(
                        name.append(st).toString(),
                        zoneId
                ));
            }
        });
        return out;
    }

    protected static BaseVariable createVariable(Pair<String> tuple) {
        String varName = tuple.first;
        String value = tuple.second;
        BaseVariable var = new BaseVariable(varName, new TStringVar(varName, value, -1,
                JDoc.builder()
                        .addDesc(value + " zone constant.")
                        .sinceVersion("5.0.0")
                        .build()
        ), value);
        var.freeze();
        return var;
    }
}
