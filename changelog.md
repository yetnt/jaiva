# 6.0.0 (Library & Expressiveness overhaul)

idk the library and expressiveness overhaul?

# BREAKING CHANGES

For a quick list of things that broke
- Scripts
    - [deprecation stuff](#deprecation)
    - [`colonize` loops](#colonize-loops)
    - [library locations](#library-location-changes)
- Jaiva Internals
    - [maven coordinates](#maven-coords)
    - [BaseLibrary metadata](#baselibrary)

## Jaiva Scripts

### Deprecation

#### REMOVED

Remove functions which have been deprecated for quite some time now.

- `f_bin` from the files library. Deprecated since `v4.0.0`
- `neg` from the global scope. Deprecated since `v1.0.1`
- `ask` from the global scope. Deprecated since kingdom come (idk when)

#### Newly deprecated

- `a_push`, `a_pushAll` and `a_unshift` from arrays library. They've been
marked as deprecated as `arrLit(element, arr:::)` combinations are more
expressive syntax. It may be removed in a later version idk

### `colonize` loops

I compeltely broke them lol

OLD:

```jaiva
colonize (i <- 0 | i <= 10 | +) ->
<~
```

NEW:

```jaiva
colonize (i <- 0 <| i <= 10 <| +) ->
<~
```


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
tsea "jaiva/file/api"! @ For actual creating or deleting files

tsea "jaiva/file"! @ Aggregates both.
```

### `-md` output

The output no longer relies on the given input file and output folder
alone just to make the resultant `.md` file. Now you also need a
`path` variable with documentation. Although this is just the file name
not the fulll qualified path.

This is also trtue if you want a `description`

```jaiva
@* "path"
maak *path!

@* My lovely library shenanigans
maak *description!
```

## Internal Jaiva API

### Maven Coords

As the inclusion of [a new plugin](#jaiva-libjson-plugin), The actual `jaiva`
project is now the `core` module. The output target directory of importance
is now `.../jaiva/core/target/`.

Old `pom.xml` (assuming you already have `jitpack` as a repository)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.app</groupId>
    <artifactId>App</artifactId>
    <version>1.0.0</version>

    <dependencies>
        <dependency>
            <groupId>com.github.yetnt</groupId>
            <artifactId>jaiva</artifactId>
            <version>5.0.2</version>
        </dependency>
    </dependencies>

</project>
```

New `pom.xml`


```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.app</groupId>
    <artifactId>App</artifactId>
    <version>1.0.0</version>

    <dependencies>
        <dependency>
            <groupId>com.github.yetnt.jaiva</groupId>
            <artifactId>core</artifactId> 
            <!-- core is now the actual Jaiva types and stuff-->
            <version>5.0.2</version>
        </dependency>
    </dependencies>

</project>
```

### `BaseLibrary`

BaseLibrary, now no longer takes in any types in it's constructor at all, and instead fully relies on new
annotations, the only public annotation of interest being `PublicLibrary`. This also means the `path` static
field requirement is no more.

#### Old

```java
import com.jaiva.interpreter.libs.BaseLibrary;
import com.jaiva.interpreter.symbol.BaseFunction;

public class Shii extends BaseLibrary {
    public static String path = "shii"; // accesible in jaiva via jaiva/shii
    
    public Shii() {
        super(LibraryType.LIB, "shii");
        
        vfs.put("aliasName", new FFunction());
    }
    
    public class FFunction extends BaseFunction {}
}
```

#### New

```java
import com.jaiva.interpreter.libs.annotation.PublicLibrary;
import com.jaiva.interpreter.libs.BaseLibrary;
import com.jaiva.interpreter.symbol.BaseFunction;

@PublicLibrary(path = "shii", description = "The description")
public class Shii extends BaseLibrary {
    public Shii() {
        add(new FFunction());
    }

    public class FFunction extends BaseFunction {}
}
```

#### Aliases

Previously, due to having direct `vfs` access, aliases creation was quite verbose

```java
public class Shii extends BaseLibrary {
    public Shii() {
        vfs.put("alias1", new Symbol1());
        vfs.put("alias2", new Symbol());
    }
}
```

now its much cleaner

```java
public class Shii extends BaseLibrary {
    public Shii() {
        addWithAliases(new Symbol1(), "alias1", "alias2");
    }
}
```

> [!NOTE]
> Keep in mind, the symbol name itself, is still also an alias. But the class will add the symbol name as an alias
> for you if you didn't already.


#### Container Libraries

For Container libraries, you explicitly do not annotate them. So you can remove all their constructor stuff
completely. It just needs to extends `BaseLibrary`

## New Features

### Jaiva file metadata

For jaiva file's themselves as the Markdown was overhauled, they can
optionally export a `description` and `path` variable with Jaiva Documentation
to document itself

```jaiva

@* "arrays"
maak *path!

@* The arrays library
maak *description!
```

> [!NOTE]
> The value of the variable's is not taken into account. Only the actual
> documentation is whats of importance.

> [!NOTE]
> The path is purely metadata it does not mean the script can define
> it's own custom path. If a script is `lib.jiv`, then its best to just
> document the path as `"lib.jiv"`

### `jaiva-install` script

The `jaiva.zip` now comes pre-packed with a script (both windows and unix, however
unix might need to also apply a new alias for this script), which allows you to
Install newer AND older versions of jaiva.

And the script will stay in place, so you can install `1.0.0` then immediately
go back to `6.0.0` and see why theres so many java versions.

(all versions (without the `v` prefix), that expose a `jaiva.zip` archive work.)

See [jaiva-install on CLI](./docs/CLI.md#jaiva-installcmd-batch--jaiva-install-bash)

### Language Features

#### Argument Extensions

Since `()` and `[]` in jaiva have to be on the same line, problems arise when you are passing multiple arguments into a function.

For instance, take `a_apply` from `jaiva/arrays` which applies the given functions to an array:

```jaiva
tsea "jaiva/arrays"!

@ A very messy array
maak myArr <-| 10, 49, f~() : 2, "whats up", yebo, idk, arrLit(10), 81! 

maak out <- a_apply(myArr, f~(arr) : a_forEach(arr, khuluma), f~(arr) : a_map(arr, f~(el) : typeOf(el) = "function" => el() however el), f~(arr) : a_filter(arr, f~(el) : (el?)' )!
```

I can barely even read that!

To fix this, we can use syntax which allows you to extend a function call's arguments
into multiple lines (uses `|>`)

```jaiva
tsea "jaiva/arrays"!

@ A very messy array
maak myArr <-| 10, 49, f~() : 2, "whats up", yebo, idk, arrLit(10), 81! 

maak out <- a_apply(myArr)!
    |> f~(arr) : a_forEach(arr, khuluma)!
    |> f~(arr) : a_map(arr, f~(el) : typeOf(el) = "function" => el() however el)!
    |> f~(arr) : a_filter(arr, f~(el) : (el?)')!
```

which is functionally equivalent to calling the called function with all the values in `|>` appended to the call as independent arguments.

So it is literally just a way to split the argument list into multiple lines.

> [!NOTE]
> The statement itself still has to close it's brace and have `!` at the end. As in:
```diff
- WRONG
func(
    |> arg
    |> arg, arg2)!

+ CORRECT
func()!
|> arg!
|> (arg, arg2)!
```

This does come in handy for functional style usage but this is purely for making arguments readable, for exmaple
take `f_new` function from `jaiva/file`, which takes in conceptually `f_new(filePath, [content], canRead?, canWrite?, canExecute?)`

You can write the entire call in a single line

```jaiva
tsea "jaiva/file/api"!
f_new("C:/Users/PrivateLol/data.csv", arrLit("some,random,text,here","1,2,6,3","10,5,1,6"), true, true, true)!
```

or you can use argument extension to either pass one or multiple arguments at once vertically
```jaiva
f_new()!
    |> "C:/Users/PrivateLol/data.csv"!
    |> arrLit("some,random,text,here","1,2,6,3","10,5,1,6")! @ Passes a single array as the second parameter
    |> (true, true, true)! @ Passes true into the third, fourth and fifth slot.
```

id document more but come on man go test urself

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
a_apply(arr, f~(a) : a_map(a, f~(num) : num * 3))!
@ Returns [3, 6, 9, 12]
a_apply()!
    |> arr!                                 @ First argument
    |> a_reverse!                           @ Second argument
    |> f~(a) : a_map(a, f~(num) : num * 3)! @ Third argument
@ Returns [12, 9, 6, 3]
a_apply(arr, khuluma)!
@ Returns [1, 2, 3, 4] (khuluma returns idk)
```

#### Colonize Loops

instead of taking just `+` or `-`, now they can also take any parsable value! And i mean any.

```jiv
colonize (i <- 1 <| i <= 10 <| (f~(z) : i % 2 = 1 => 2 * z however z + 1)(i) ) ->
    khuluma(i)! @ 1, 2, 3, 6,  7
<~
```

`+` for increment and `-` for decrement are still supported.

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

(oh yeah theres also the `markdown` mojo for markdown documentation)

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
            <version>6.0.0</version>
            <executions>
              <execution>
                <id>json-gen</id>
                <goals><goal>json</goal></goals>
                <configuration>
                  <basePackage>com.j3d.jaiva.packs</basePackage>
                  <ifOutDirNotEmpty>OVERWRITE</ifOutDirNotEmpty>
                </configuration>
              </execution>
              <execution>
                <id>md-gen</id>
                <goals><goal>markdown</goal></goals>
                <configuration>
                  <basePackage>com.j3d.jaiva.packs</basePackage>
                  <allOutputOptionsTrue>true</allOutputOptionsTrue>
                  <ifOutDirNotEmpty>OVERWRITE</ifOutDirNotEmpty>
                </configuration>
              </execution>
            </executions>
          </plugin>

        </plugins>

    </build>

</project>

```

In this case, it's configured to look for your custom `BaseLibrary` implementations within
`com.app.baselib.package`, and generate the output JSON and MD in `(basedir)/jaiva/` such that
if the project is shared on Github or otherwise, the Jaiva VSCode extension can find the
appropriate JSON and provide autocomplete to the user.

## Fixes

- Fix broken Long support from v5.0.4
    - `types` now handles long values, parsing to and from strings.
      - Similarly, if an integer string is too large it will be parsed as a long
  instead
- Some random runtime exceptions have actual jaiva exceptions
- Fix broken string concat
- Refactor some fields in the interpreter to be encapsulated.