# 6.0.0 (Alpha)

# BREAKING CHANGES

## Jaiva Scripts

### Deprecated removal

Remove functions which have been deprecated for quite some time now.

- `f_bin` from the files library. Deprecated since `v4.0.0`
- `neg` from the global scope. Deprecated since `v1.0.1`
- `ask` from the global scope. Deprecated since kingdom come (idk when)

### Library Location Changes

Due to internal changes [See nerd corner block](#baselibrary-metadata-as-annotations)
some libraries don't point to the same thing as before.

Although other libraries may have changed due to other concerns. The following changes
have been listed:

#### time libraries

**before**

```jaiva
tsea "jaiva/time"! @ For stuff like t_now or t_parseDate
tsea "jaiva/time/zone" @ For all IANA timezone constants
```

**now**

```jaiva
tsea "jaiva/time/api"! @ For stuff like t_now or t_parseDate
tsea "jaiva/time/zone"! @ FOr all the IANA timezone constants

@ Or

tsea "jaiva/time"! @ Which aggregates from both

{
Technically, this means importing from jaiva/time should function
the same, however this means also accepting literally EVERY single
IANA timezone constant. Rather change to /api
}
```

#### math libraries

**Before**

```jaiva
tsea "jaiva/math"! @ For trig, m_sqrt, m_log, constants etc
tsea "jaiva/math/utils"! @ For m_gcd and m_lcm
```

**New**

```jaiva
tsea "jaiva/math/trig"! @ For everything trigonometry related
                        @ So m_sin, m_cos, m_atan2, etc...
tsea "jaiva/math/const"! @ For every mathematical constant
                         @ m_e, m_pi, m_phi, etc...
tsea "jaiva/math/base"! @ For generic functions like sqrt, floor, ceil
                        @ math/utils is now part of base and no longer exists.

tsea "jaiva/math"! @ For EVERYTHING
```

#### file libraries

Technically unchanged, but its been split

**Before**

```jaiva
tsea "jaiva/file"! @ For all file related things
```

**Now**

```jaiva
tsea "jaiva/file/query"! @ For all stuff that query a file array
                        @ e.g. f_getName, f_getPermissions, etc...
tsea "jaiva/file/io"! @ For actuall creating or deleting files

tsea "jaiva/file"! @ Aggregates both.
```

## Internal Jaiva API

### Maven Coords

As the inclusion of [a new plugin](#jaiva-libjson-plugin), The actual `jaiva`
project is now the `core` module. The output target directory of importance
is now `.../jaiva/core/target/`.

### `BaseLibrary`

BaseLibrary, now no longer takes in any types in it's constructor at all, and instead fully relies on new
annotations, the only public annotation of interest being `PublicLibrary`. This also means the `path` static
field requirement is no more.

#### Old

```java
public class Shii extends BaseLibrary {
    public static String path = "shii"; // accesible in jaiva via jaiva/shii
    
    public Shii() {
        super(LibraryType.LIB, "shii");
        
        vfs.put("aliasName", new FFunction());
    }
    
    public class FFunction extends BaseFunction {...}
}
```

#### New

```java
import com.jaiva.interpreter.libs.annotation.PublicLibrary;

@PublicLibrary(path = "shii")
public class Shii extends BaseLibrary {
    public Shii() {
        add(new FFunction());
    }
}
```

#### Container Libraries

For Container libraries, you explicitly do not annotate them. So you can remove all their constructor stuff
completely. It just needs to extends `BaseLibrary`

## New Features

### Language Features

#### Hierarchical Libraries

Due to an internal change [See nerd corner block](#baselibrary-metadata-as-annotations), now
jaiva supports hierarchical libraries! (Although the library has to implement it)

Examples can be found from [Library Location Changes block](#library-location-changes)

#### New Operators

- `#` : XOR Operator

```jaiva
maak result <- 2 # 6!
khuluma(result)! @ 4
```

- `;` : Syntax sugar (`expr;` becomes `expr = true`/`expr = yebo`)
- `?` : Syntax sugar (`expr?` becomes `expr = idk`)

```jaiva
@ Stuff like ifs or loops take an expression and not just a boolean
@ The above syntax sugar is an expression to the interprter hence allowing:

maak dont <- aowa! @ As you know the same as saying false.

if (dont;) ->
    khuluma("Dont is true!")!
<~

kwenza a(param?) ->
    khuluma(param? => "Param not given!" however param)!
<~

a()! @ Prints not given
a("Hello World")! @ Prints hello world

maak b <- idk!
maak c!

b? @ True, as b = idk, idk = idk, which is true
c? @ True, as c is set to idk, idk = idk, which is true.
```

- `a_forEach` function in `jaiva/arrays`
- `a_apply` function in `jaiva/arrays`
- `m_atan2` function in `jaiva/math/trig`

```jaiva
tsea "jaiva/arrays" <- a_forEach! @ only need for a_forEach

maak arr <-| 1, 2, 3, 4!
maak square <- f~(num) : khuluma(num * num)! @ A lambda function to print the square a number
a_forEach(arr, square)! @ Will print the squares.
```

```jaiva
tsea "jaiva/arrays" <- a_map, a_reverse, a_apply! @ only need these 3
maak arr <-| 1, 2, 3, 4!

@ You can pass a function reference which is defined 
@ with an explicit body

kwenza func(array) ->
    kwenza add(element) ->
        khutla element + 1!
    <~
    maak arr <- a_map(array, add)!
    khutla arr!
<~

@ But just use lambdas for quick stuff, most of the examples
@ will use lambdas

a_apply(arr)!
@ Returns [1, 2, 3, 4]
a_apply(arr, func)!
@ Returns [2, 3, 4, 5]
a_apply(arr, f~(a) : a_map(a, f~(num) : num * 3))
@ Returns [3, 6, 9, 12]
a_apply(arr, a_reverse, f~(a) : a_map(a, f~(num) : num * 3))
@ Returns [12, 9, 6, 3]
a_apply(arr, khuluma)
@ Returns [1, 2, 3, 4] (khuluma returns idk)
```

- Fix broken Long support from v5.0.4
- Fix broken string concat

### Nerd Corner (API Features)

#### `BaseLibrary` metadata as Annotations

As seen in [BaseLibrary](#baselibrary), the actual metadata of a library being it's path
is now defined in the `PublicLibrary` annotation.

It's one of 2 which can be applied to a `BaseLibrary` subclass, but is the only one
available to any external Jaiva API implementors. (e.g. A Java app using Jaiva as a scripting
language may want to define it's own global libraries, they can only use `PublicLibrary`)

Along with that comes `Exports` interface, which allows `BaseLibrary` to aggregate
symbols from other implementors of `BaseLibrary`. This is how the new [Hierarchal Libraries](#hierarchical-libraries)
feature is supported

#### `Plugin` class

Living in `com.jaiva` in `core`, a New Plugin class which is just for [lib-tooling-plugin](#lib-tooling-plugin)
to instantiate the given classes and provide output.

ALl im going to say is. Class loader issues showed me what hell looks like.

#### `lib-tooling-plugin`

A maven plugin which allows external Jaiva Hosted Java apps, to
export tooling metadata such as `(basedir)/jaiva/*.json` for the VSCode Extension to
discover External APIs and provide support for them

(and soon) `*.md` output

An example pom is as follows which uses this:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.app</groupId>
    <artifactId>AppName</artifactId>
    <version>1.0.0-SNAPSHOT</version>

    <pluginRepositories>
        <pluginRepository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </pluginRepository>
    </pluginRepositories>

    <build>

        <plugins>

            <plugin>
                <groupId>com.github.yetnt.jaiva</groupId>
                <artifactId>lib-tooling-plugin</artifactId>
                <version>VERSION</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>generate-lib-json</goal>
                        </goals>
                        <configuration>
                            <toolingType>JSON</toolingType>
                            <basePackage>com.app.baselib.package</basePackage>
                        </configuration>
                    </execution>
                </executions>
            </plugin>

        </plugins>

    </build>

</project>

```

In this case, it's configured to look for your custom `BaseLibrary` implementations within
`com.app.baselib.package`, and generate the output JSON in `(basedir)/jaiva/` such that
if the project is shared on Github or otherwise, the Jaiva VSCode extension can find the
appropriate JSON and provide autocomplete to the user.