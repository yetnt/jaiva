package com.jaiva.interpreter.libs.annotation;

import com.jaiva.interpreter.libs.BaseLibrary;
import com.jaiva.interpreter.libs.LibraryType;

import java.lang.annotation.*;

/**
 * Annotation which marks an implementor of {@link BaseLibrary} to be an actual library.
 * <p>
 *     This annotation is usually inherited hence it has private access.
 * </p>
 * <p>
 *     As per Jaiva v6 which introduced annotative library metadata, Libraries which originally were simply containers
 *     and are aggregated into a specific library, should not have any annotation. Which means {@link Library}
 *     annotation never exists on a container library.
 * </p>
 * @see PublicLibrary
 * @see JaivaLibrary
 * @see GlobalsLib
 * @author Lehlogonolo Poole
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Library {
    /**
     * The library type of this library
     * @return The library type.
     */
    LibraryType libType();
}
