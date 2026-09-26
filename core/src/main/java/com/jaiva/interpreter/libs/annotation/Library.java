package com.jaiva.interpreter.libs.annotation;

import com.jaiva.interpreter.libs.LibraryType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface Library {
    LibraryType libType();
}
