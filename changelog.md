# 6.0.0 (Alpha)

## BREAKING

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
    }
}
```

#### New

```java
import com.jaiva.interpreter.libs.annotation.PublicLibrary;

@PublicLibrary(path = "shii")
public class Shii extends BaseLibrary {
    // No Constructor needed, unless you need IConfig<Object>, still no super call required.
}
```

#### Container Libraries

For Container libraries, you explicitly do not annotate them. So you can remove all their constructor stuff
completely. It just needs to extends `BaseLibrary`

### `jaiva-libjson-plugin`

TODO: write

But basically a maven build plugin for external hosts to export their Jaiva custom APIs as JSON files
for the Jaiva VSCode Extension to discover

### Maven Coordinates

The Maven Coordinates have fundamentally changed as the project has been split into a `core` module
and a `jaiva-libjson-plugin` module.

## New Features

### Language Features

- `#` : XOR Operator
- `;` : Syntax sugar (`expr;` becomes `expr = true`)
- `?` : Syntax sugar (`expr?` becomes `expr = idk`)
- `a_forEach` function in `jaiva/arrays`
- `a_apply` function in `jaiva/arrays`
- Fix broken Long support from v5.0.4
- Fix broken string concat
- Change some `JBundler` API to be private