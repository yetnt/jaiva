package com.jaiva.interpreter.libs;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.jaiva.interpreter.Vfs;
import com.jaiva.tokenizer.Token;
import com.jaiva.tokenizer.Token.*;
import com.jaiva.Main;
import com.jaiva.errors.InterpreterException;
import com.jaiva.errors.InterpreterException.WtfAreYouDoingException;
import com.jaiva.errors.JaivaException;
import com.jaiva.interpreter.Scope;
import com.jaiva.interpreter.MapValue;
import com.jaiva.interpreter.Primitives;
import com.jaiva.interpreter.libs.math.Math;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.*;
import com.jaiva.lang.Keywords;
import com.jaiva.tokenizer.jdoc.JDoc;

/**
 * Globals class holds all the global symbols that are injected into the
 * variable functions store.
 */
public class Globals extends BaseGlobals {
    // public Vfs vfs = new HashMap<>();

    public HashMap<String, Vfs> builtInGlobals = new HashMap<>();

    /**
     * Constructor to create and get the globals.
     * 
     * @throws InterpreterException
     */
    public Globals(IConfig<Object> config) throws InterpreterException {
        super(GlobalType.MAIN);
        vfs.put("getVarClass", new FGetVarClass());
        vfs.put("reservedKeywords", new VReservedKeywords());
        vfs.put("version", new VJaivaVersion());
        vfs.put("flat", new FFlat());
        vfs.put("sleep", new FSleep());
        vfs.put("typeOf", new FTypeOf());
        vfs.put("arrLit", new FArrayLiteral());
        vfs.putAll(new IOFunctions(config).vfs);

        Types c = new Types();
        builtInGlobals.put(c.path, c.vfs);
        Math m = new Math();
        builtInGlobals.put(m.path, m.vfs);
        IOFile f = new IOFile(config);
        builtInGlobals.put(f.path, f.vfs);
        Debug d = new Debug(config);
        builtInGlobals.put(d.path, d.vfs);

        if (!config.destroyLibraryCircularDependancy)
            builtInGlobals.putAll(new LibraryLoader().loadAllLibraries(new IConfig<Object>(true, null)));
    }


    /**
     * Constructor to create and get the globals with external globals too.
     *
     * @throws InterpreterException
     */
    public Globals(IConfig<Object> config, List<Class<? extends BaseGlobals>> external) throws InterpreterException {
        super(GlobalType.MAIN);
        vfs.put("getVarClass", new FGetVarClass());
        vfs.put("reservedKeywords", new VReservedKeywords());
        vfs.put("version", new VJaivaVersion());
        vfs.put("flat", new FFlat());
        vfs.put("sleep", new FSleep());
        vfs.put("neg", new FNeg());
        vfs.putAll(new IOFunctions(config).vfs);

        if (!config.destroyLibraryCircularDependancy)
            builtInGlobals.putAll(new LibraryLoader().loadAllLibraries(new IConfig<Object>(true, null)));

        Types c = new Types();
        builtInGlobals.put(c.path, c.vfs);
        Math m = new Math();
        builtInGlobals.put(m.path, m.vfs);
        IOFile f = new IOFile(config);
        builtInGlobals.put(f.path, f.vfs);
        Debug d = new Debug(config);
        builtInGlobals.put(d.path, d.vfs);

        for (Class<? extends BaseGlobals> ext : external) {
            try {
                Constructor<? extends BaseGlobals> constructor = ext.getDeclaredConstructor(IConfig.class);
                constructor.setAccessible(true); // 👈 This bypasses Java's access checks
                BaseGlobals n = constructor.newInstance(config);

                builtInGlobals.put(n.path, n.vfs);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Returns the JSON of the globals
     * 
     * @param removeTrailingComma Remove the trailing comma
     * @return string with the JSON representation of the global tokens.
     */
    public String returnGlobalsJSON(boolean removeTrailingComma) {
        StringBuilder string = new StringBuilder();
        vfs.forEach((name, vf) -> {
            Symbol symbol = (Symbol) ((MapValue) vf).getValue();
            try {
                string.append(symbol.token.toJson());
            } catch (JaivaException e) {
                throw new RuntimeException(e);
            }
            string.append(",");
        });
        return string.substring(0, string.length() - (removeTrailingComma ? 1 : 0));
    }

    /**
     * getVarClass(variable)
     * Returns the .toString() class representation of a variable's token.
     */
    class FGetVarClass extends BaseFunction {
        FGetVarClass() {
            super("getVarClass", new TFunction("getVarClass", new String[] { "var" }, null, -1,
                    JDoc.builder()
                            .addDesc("Attempts to return the symbol's corresponding Java class in string form. If you're using this then you def don't know what you're doing.")
                            .addParam("var", "idk", "The value to return it's token symbol for", false)
                            .addReturns("The .toString() class representation of the given variable's token")
                            .sinceVersion("1.0.0-beta.0")
                            .build()

            ));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params,
                           IConfig<Object> config, Scope scope)
                throws Exception {
            String name;
            if (params.getFirst() instanceof String) {
                name = (String) params.getFirst();
            } else if (params.getFirst() instanceof Token && ((Token<?>) params.getFirst()).value() instanceof TVarRef) {
                name = ((TVarRef) ((Token<?>) params.getFirst()).value()).varName.toString();
            } else {
                throw new InterpreterException.WtfAreYouDoingException(scope,
                        "getVarClass() only accepts a variable reference or a string, whatever you sent is disgusting.",
                        tFuncCall.lineNumber);
            }
            MapValue var = scope.vfs.get(name);
            if (var == null) {
                throw new InterpreterException.UnknownVariableException(scope, name, tFuncCall.lineNumber);
            }
            if (!(var.getValue() instanceof Symbol symbol)) {
                throw new InterpreterException.WtfAreYouDoingException(scope,
                        name + " is not a variable nor a function, wtf. this error shouldnt happen.",
                        tFuncCall.lineNumber);
            }
            // We need to convert the named token to a raw token so we can call .toString()
            // on it.
            return symbol.token.getClass().getSimpleName();
        }
    }

    /**
     * reservedKeywords (array) variable.
     * This contains an array of the reserved keywords
     */
    class VReservedKeywords extends BaseVariable {
        VReservedKeywords() {
            super("reservedKeywords",
                    new TArrayVar("reservedKeywords", new ArrayList<>(Arrays.asList(Keywords.all)), -1,
                            JDoc.builder()
                                    .sinceVersion("1.0.0-beta.0")
                                    .addDesc("An array containing jaiva's reserved keywords that you cannot use as symbol names.").build()),
                    new ArrayList<>(Arrays.asList(Keywords.all)));
            this.freeze();
        }
    }

    /**
     * version variable.
     * This holds the current version of jaiva in {@link Main#version}
     */
    class VJaivaVersion extends BaseVariable {
        VJaivaVersion() {
            super("version", new TStringVar("version", Main.version, -1,
                    JDoc.builder()
                            .addDesc("What do you think this returns.")
                            .sinceVersion("1.0.0-beta.0").build()), Main.version);
            this.freeze();
        }
    }

    /**
     * flat(array1, array2, array3, array4, ...)
     * flat(<-arrays)
     * Takes in 2 or more arrays and flattens them into a singular array.
     */
    class FFlat extends BaseFunction {
        FFlat() {
            super("flat", new TFunction("flat", new String[] { "<-arrays" }, null, -1,
                    JDoc.builder()
                            .addDesc( "Attempts to flatten (at the top level) the given arrays array1 and array2 into 1 single array.")
                            .addParam("arrays", "[]", "Variable amount of arrays to input", true)
                            .addNote("If there are any type mismatches in array1, it will be ignored and the same check is done to array2 and so on. Therefore this function will **always** return an array, whether or not it was successful.")
                            .sinceVersion("1.0.0-beta.2")
                            .build()
            ));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params,
                           IConfig<Object> config, Scope scope)
                throws Exception {

            ArrayList<Object> returned = new ArrayList<>();
            tFuncCall.args.forEach(arg -> {
                if (arg instanceof TVarRef && ((TVarRef) arg).index == null) {
                    String name = ((TVarRef) arg).name;
                    MapValue v = scope.vfs.get(name);
                    if (v == null)
                        return;
                    if (!(v.getValue() instanceof BaseVariable))
                        return;
                    if (((BaseVariable) v.getValue()).a_size() <= 0)
                        return; // technically in this case, it will concat the arrays because this array has
                                // nothing to concat lol.
                    returned.addAll(((BaseVariable) v.getValue()).a_getAll());
                } else {
                    // stuff that need be parsed, parse and pray arraylist is returned.
                    Object parsed = null;
                    try {
                        parsed = Primitives.toPrimitive(Primitives.parseNonPrimitive(arg),  false, config, scope);
                    } catch (Exception e) {
                        // do nothing.
                    }
                    if (!(parsed instanceof ArrayList))
                        return;

                    returned.addAll((ArrayList) parsed);
                }
            });
            return returned;
        }
    }

    /**
     * Represents a built-in function that pauses the execution of the current
     * thread for a specified number of milliseconds.
     * <p>
     * The function expects a single integer parameter representing the duration to
     * sleep in milliseconds.
     * If the parameter is not an integer, a {@link WtfAreYouDoingException} is
     * thrown.
     * </p>
     * 
     * <p>
     * Usage: <code>sleep(milliseconds)</code>
     * </p>
     * 
     */
    class FSleep extends BaseFunction {
        FSleep() {
            super("sleep", new TFunction("sleep", new String[] { "milliseconds" }, null, -1,
                    JDoc.builder()
                            .addDesc("Pause execution of the interpreter for n amount of milliseconds.")
                            .addParam("milliseconds", "number", "The amount of milliseconds to sleep for", false)
                            .addNote("(Keep in mind this function still has to take your value and turn it into a Java primitive and other things, so the delay might not be exact. If you're looking for accuracy maybe remove x amount of ms till it's accurate.)")
                            .sinceVersion("1.0.0")
                            .build()
            ));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params,  IConfig<Object> config,
                Scope scope)
                throws Exception {
            checkParams(tFuncCall, scope);
            Object val = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.getFirst()),  false, config,
                    scope);
            if (!(val instanceof Integer integer))
                throw new WtfAreYouDoingException(scope, "Bruv, you can't just like, pls put number",
                        tFuncCall.lineNumber);

            Thread.sleep(integer);

            return Token.voidValue(tFuncCall.lineNumber);
        }
    }


    class FNeg extends BaseFunction {
        FNeg() {
            super("neg", new TFunction("neg", new String[] { "input" }, null, -1,
                    JDoc.builder()
                            .addDesc("Negates the given number. This is because my unary minus shit isnt working so i had to make this.")
                            .addParam("input", "number", "The input to negate", false)
                            .markDeprecated("Unary minus works now. Use it or multiply by -1")
                            .build()
            ));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params,  IConfig<Object> config,
                           Scope scope)
                throws Exception {
            checkParams(tFuncCall, scope);
            Object val = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.getFirst()),  false, config,
                    scope);
            if (!(val instanceof Number num))
                throw new WtfAreYouDoingException(scope, "Bruv, you can't just like, pls put number",
                        tFuncCall.lineNumber);

            return switch (num) {
                case Integer i -> -i;
                case Double d -> -d;
                default -> throw new InterpreterException.CatchAllException(scope, "we checked for number but didnt receive a number?", tFuncCall.lineNumber);
            };

        }
    }


    class FTypeOf extends BaseFunction {
        FTypeOf() {
            super("typeOf", new TFunction("typeOf", new String[] { "input?" }, null, -1,
                    JDoc.builder()
                            .addDesc("Returns the type of any given input.")
                            .addParam("input", "idk", "The input to check the type against", true)
                            .addReturns("Returns the string form of the typ, which could be \"array\", \"string\", \"boolean\", \"number\", \"function\", or the primitive idk.")
                            .sinceVersion("3.0.0")
                            .build()
            ));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config,
                           Scope scope)
                throws Exception {

            this.checkParams(tFuncCall, scope);
            if (params.isEmpty())
                return Token.voidValue(tFuncCall.lineNumber);
            Object val = Primitives.toPrimitive(Primitives.parseNonPrimitive(params.getFirst()), false, config,
                    scope);

            return switch (val) {
                case ArrayList arrayList -> "array";
                case Boolean b -> "boolean";
                case Number number -> "number";
                case BaseFunction baseFunction -> "function";
                case String s -> "string";
                case null, default ->
// there is no other type to possibly check for.
                        Token.voidValue(tFuncCall.lineNumber);
            };
        }
    }

    class FArrayLiteral extends BaseFunction {
        FArrayLiteral() {
            super("arrLit", new TFunction("arrLit", new String[] { "<-elements" }, null, -1,
                    JDoc.builder()
                            .addDesc("Creates an array literal from the given elements. This is useful if you want to create an array without declaring it to a variable. For example, `arrLit(1, 2, 3)` will return `[1, 2, 3]`. This is needed as Jaiva doesnt have square bracket syntax")
                            .addParam("elements", "[]", "Variable amount of elements to take in and turn into a single array.", true)
                            .addReturns("The input given, as an array")
                            .sinceVersion("3.0.0")
                            .build()
            ));
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params,
                           IConfig<Object> config, Scope scope)
                throws Exception {

            ArrayList<Object> returned = new ArrayList<>();
            tFuncCall.args.forEach(arg -> {
                try {
                    returned.add(Primitives.toPrimitive(Primitives.parseNonPrimitive(arg), false, config, scope));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            return returned;
        }
    }

}
