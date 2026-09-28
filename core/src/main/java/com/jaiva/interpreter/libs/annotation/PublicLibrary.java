package com.jaiva.interpreter.libs.annotation;

import com.jaiva.JBundler;
import com.jaiva.interpreter.libs.BaseLibrary;
import com.jaiva.interpreter.libs.LibraryType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that marks an external {@link BaseLibrary} class as public.
 * <p>
 *     If using the Jaiva Tooling Plugin (Maven), this allows the class to be up for discovery.
 *     Otherwise, going through {@link JBundler} is where it is enforced that a class have this
 *     annotation.
 * </p>
 * @implSpec If, a {@link BaseLibrary} implementation is only used to encapsulate specific symbols
 * but is then aggregated into a bigger {@link BaseLibrary} class, or in pre Jaiva v6 terms, a {@code CONTAINER}
 * type, it should not have this annotation. As the class is purely used for organisation and isn't itself
 * a user facing and discoverable library
 * @see Library
 * @see JaivaLibrary
 * @see GlobalsLib
 * @see Exports
 * @author Lehlogonolo Poole
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Library(libType = LibraryType.LIB)
public @interface PublicLibrary {
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
