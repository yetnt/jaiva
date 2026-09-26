package com.jaiva;

import com.jaiva.interpreter.MapValue;
import com.jaiva.interpreter.Vfs;
import com.jaiva.interpreter.libs.time.TimeZone;
import com.jaiva.interpreter.symbol.BaseVariable;
import com.yetnt.utils.builders.MarkDownLiteral;

import java.util.ArrayList;
import java.util.List;

public class playground {
    public static void main(String[] args) {
        Vfs vfs = new TimeZone().vfs;
        ArrayList<MapValue> sorted = vfs.sortKeys(
                (a, b) -> a.substring(3).compareTo(b.substring(3)),
                true
        );
        for (String row : MarkDownLiteral.asTable(new ArrayList<>(List.of("Variable Name", "IANA Constant Value", "Link")),
                new ArrayList<>(
                        sorted.stream()
                                .map(MapValue::getValue)
                                .map(sym -> (BaseVariable)sym)
                                .filter(var -> var.name.startsWith("TZ_"))
                                .map(var -> new ArrayList<>(
                                        List.of(
                                                var.name,
                                                (String) var.s_get(),
                                                new MarkDownLiteral("Link").linkTo("#" + var.name.toLowerCase() + "---string").toString()
                                        )
                                ))
                                .toList()
                ))) {
            System.out.println(row);
        }
    }
}
