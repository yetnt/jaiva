package com.jaiva.interpreter.symbol;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SymbolConfig {
    boolean experimental() default false;
    boolean deprecated() default false;
    boolean unsafe() default false;
}
