package com.jaiva.interpreter.libs.annotation;

import com.jaiva.JBundler;
import com.jaiva.interpreter.libs.BaseLibrary;
import com.jaiva.interpreter.libs.LibraryType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Annotation that marks an external {@link BaseLibrary} class as one within the actual core
 * Jaiva classes. e.g. It's part of Jaiva.
 * <p>
 *     This annotation should not be used by external tooling.
 * </p>
 * @see Library
 * @see PublicLibrary
 * @see GlobalsLib
 * @see Exports
 * @author Lehlogonolo Poole
 */
@Retention(RetentionPolicy.RUNTIME)
@Library(libType = LibraryType.BUILTIN)
@Target(ElementType.TYPE)
public @interface JaivaLibrary {
    /**
     * The path of this library without the {@code jaiva/} or {@code jaiva\} suffix.
     * @implSpec Library paths in Jaiva are just strings and not hierarchical themselves. Here are some
     * examples of path definitions versus uses.
     * <p>
     *     <pre>{@code
     *          @PublicLibrary(path = "time/zone")
     *          public TimeZoneConstants extends BaseLibrary {
     *              ...
     *          }
     *
     *          @PublicLibrary(path = "time")
     *          pblic TimeLibrary extends BaseLibrary {
     *              ...
     *          }
     *          public
     *
     *          // use in Jaiva
     *
     *          tsea "jaiva/time/zone"! // for the TimeZoneConstants Library
     *          tsea "jaiva/time"! // for the TimeLibrary Library
     *     }</pre>
     *     If hierarchical paths is wanted, e.g. you want "jaiva/time" to export "jaiva/time/zone", use {@link Exports}
     *     annotation.
     * </p>
     * @return The string path
     */
    String path();
}
