package com.jaiva.interpreter.libs.time;

import com.jaiva.interpreter.libs.BaseLibrary;
import com.jaiva.interpreter.libs.annotation.Exports;
import com.jaiva.interpreter.libs.annotation.JaivaLibrary;

@Exports({TimeApi.class, TimeZone.class})
@JaivaLibrary(path = "time", description = "Contains every single function and variable related to working with time that's available")
public class TimeExports extends BaseLibrary {
}
