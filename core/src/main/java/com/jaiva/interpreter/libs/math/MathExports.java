package com.jaiva.interpreter.libs.math;

import com.jaiva.interpreter.libs.BaseLibrary;
import com.jaiva.interpreter.libs.annotation.Exports;
import com.jaiva.interpreter.libs.annotation.JaivaLibrary;

@JaivaLibrary(path = "math", description = "All the math stuff, not really sure what else to tell you.")
@Exports({MathConst.class, MathBase.class, MathTrig.class})
public class MathExports extends BaseLibrary {

}
