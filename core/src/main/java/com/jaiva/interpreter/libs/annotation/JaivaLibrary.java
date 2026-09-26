package com.jaiva.interpreter.libs.annotation;

import com.jaiva.interpreter.libs.LibraryType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Library(libType = LibraryType.BUILTIN)
public @interface JaivaLibrary {
    String path();
}
