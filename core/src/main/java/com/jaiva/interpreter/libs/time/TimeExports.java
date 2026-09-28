package com.jaiva.interpreter.libs.time;

import com.jaiva.interpreter.libs.BaseLibrary;
import com.jaiva.interpreter.libs.annotation.Exports;
import com.jaiva.interpreter.libs.annotation.JaivaLibrary;

@Exports({TimeApi.class, TimeZone.class})
@JaivaLibrary(path = "time")
public class TimeExports extends BaseLibrary {
}
