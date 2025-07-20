package com.jaiva.interpreter;

import java.nio.file.Path;
import java.util.*;

import com.jaiva.Main;
import com.jaiva.errors.InterpreterException.*;
import com.jaiva.errors.JaivaException;
import com.jaiva.errors.JaivaException.DebugException;
import com.jaiva.interpreter.globals.Globals;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.*;
import com.jaiva.interpreter.symbol.BaseVariable.VariableType;
import com.jaiva.lang.Keywords;
import com.jaiva.tokenizer.Token;
import com.jaiva.tokenizer.Token.*;
import com.jaiva.tokenizer.TokenDefault;
import com.jaiva.utils.Tuple2;

/**
 * The Interpreter class is one of the 3 main classes which handle Jaiva code.
 * <p>
 * It is responsible for interpreting Jaiva code and executing it.
 */
public class Interpreter {

    /**
     * The ThrowIfGlobalContext class is a helper class which is thrown when the
     * interpreter does not return a usual value, like it returns maybe an error or
     * it exits out of a loop.
     * <p>
     * It's the same idea as a software interrupt. Execution is halted to handle
     * whatever needs to be handled
     * <p>
     * For example, we could be deeply nested in a function and some other
     * constructs and it decides to break, this class helps to keep exiting out of
     * the nested calls until we are in a loop to break out if, UNLESS we're in the
     * global context then it throws an error.
     */
    public static class ThrowIfGlobalContext {
        /**
         * The line number of the token which caused the interuption.
         */
        public int lineNumber = 0;
        /**
         * The object which caused the interuption.
         * <p>
         * This can be a function return, a loop control, or an error.
         */
        public Object c;

        /**
         * The constructor for the ThrowIfGlobalContext class.
         * <p>
         * This is used to create a new instance of the class with the given object and
         * line number.
         *
         * @param cObject    The object which caused the interuption.
         * @param ln The line number of the token which caused the interuption.
         */
        public ThrowIfGlobalContext(Object cObject, int ln) {
            c = cObject;
            lineNumber = ln;
        }
    }

    /**
     * The throwIfGlobalContext method is used to check if the context is global and
     * if so, it throws an error.
     * <p>
     * This is used to handle the case where a function return or a loop control is
     * used in the global context.
     *
     * @param cTrace     The context of the code being interpreted.
     * @param lc         The object which caused the interuption.
     * @param lineNumber The line number of the token which caused the interuption.
     * @return Returns a new instance of the ThrowIfGlobalContext class with the
     *         given object and line number.
     * @throws WtfAreYouDoingException If the context is global
     *                                 and the object is a
     *                                 function return or a
     *                                 loop control.
     * @throws CimaException           If the context is global
     *                                 and the object is an
     *                                 error.
     */
    public static ThrowIfGlobalContext throwIfGlobalContext(ContextTrace cTrace, Object lc, int lineNumber)
            throws JaivaException {
        if (lc instanceof ThrowIfGlobalContext) {
            lc = ((ThrowIfGlobalContext) lc).c;
        }
        if (lc instanceof DebugException dlc)
            throw dlc; // user shouldnt catch it anywhere fr.
        else if (lc instanceof Keywords.LoopControl) {
            if (cTrace.current == Context.GLOBAL)
                throw new WtfAreYouDoingException(cTrace, "So. You tried using "
                        + (lc.toString().equals("BREAK") ? Keywords.LC_BREAK : Keywords.LC_CONTINUE)
                        + ". But like, we're not in a loop yknow? ", lineNumber);
        } else if (Primitives.isPrimitive(lc)) {
            // a function return thing then
            if (cTrace.current == Context.GLOBAL)
                throw new WtfAreYouDoingException(cTrace,
                        "What are you trying to return out of if we're not in a function??", lineNumber);

        } else if (lc instanceof TThrowError) {
            if (cTrace.current == Context.GLOBAL)
                throw new CimaException(cTrace, ((TThrowError) lc).errorMessage, lineNumber);
        }
        return new ThrowIfGlobalContext(lc, lineNumber);
    }

    /**
     * The isVariableToken method checks if the given token is a variable token.
     * <p>
     * This is used to determine if the token is a variable or not.
     *
     * @param t The token to check.
     * @return Returns true if the token is a variable token, false otherwise.
     */
    public static boolean isVariableToken(Object t) {
        return t instanceof TUnknownVar || t instanceof TVarReassign || t instanceof TVarRef
                || t instanceof  TArrayVar || t instanceof TFuncCall || t instanceof TStatement || t instanceof TFunction
                || Primitives.isPrimitive(t);
    }

    /**
     * Handles variable operations such as creation, reassignment, and primitive
     * parsing.
     * This method processes different types of tokens representing variables or
     * operations
     * on variables and updates the provided variable storage map accordingly.
     *
     * @param t      The token representing the variable or operation to handle.
     *               This
     *               can be
     *               one of several types, including TNumberVar, TBooleanVar,
     *               TStringVar,
     *               TUnknownVar, TArrayVar, or TVarReassign.
     * @param vfs    A map storing variable names as keys and their corresponding
     *               MapValue
     *               objects as values. This map is updated based on the operation
     *               performed.
     * @param config The configuration object containing settings for the
     *               interpreter,
     *               including the file directory and import settings.
     * @return Returns the primitive value of the token if it is not a
     *         variable-related
     *         token, or null if the operation involves variable creation or
     *         reassignment.
     * @throws Exception If an error occurs during variable handling, such as
     *                   referencing
     *                   an unknown variable, attempting to reassign a frozen
     *                   variable, or
     *                   performing an invalid operation.
     */
    public static Object handleVariables(Object t, HashMap<String, MapValue> vfs, IConfig config, ContextTrace cTrace)
            throws Exception {
        switch (t) {
            case TNumberVar tNumberVar -> {
                Object number = Primitives.toPrimitive(tNumberVar.value, vfs, false, config, cTrace);
                BaseVariable var = BaseVariable.create(((TokenDefault) t).name, (TokenDefault) t,
                        new ArrayList<>(Collections.singletonList(number)), false);
                vfs.put(tNumberVar.name, new MapValue(var));
            }
            case TBooleanVar tBooleanVar -> {
                Object bool = Primitives.toPrimitive(tBooleanVar.value, vfs, false, config, cTrace);
                BaseVariable var = BaseVariable.create(((TokenDefault) t).name, (TokenDefault) t,
                        new ArrayList<>(Collections.singletonList(bool)), false);
                vfs.put(tBooleanVar.name, new MapValue(var));
            }
            case TStringVar tStringVar -> {
                Object string = Primitives.toPrimitive(tStringVar.value, vfs, false, config, cTrace);
                BaseVariable var = BaseVariable.create(((TokenDefault) t).name, (TokenDefault) t,
                        new ArrayList<>(Collections.singletonList(string)), false);
                vfs.put(tStringVar.name, new MapValue(var));
            }
            case TUnknownVar tUnknownVar -> {
                Object something = Primitives.toPrimitive(tUnknownVar.value, vfs, false, config, cTrace);
                Symbol var;
                if (something instanceof BaseFunction)
                    // this assigns
                    var = (Symbol) something;
                else
                    var = BaseVariable.create(((TokenDefault) t).name, (TokenDefault) t,
                            something instanceof ArrayList ? (ArrayList) something
                                    : new ArrayList<>(Collections.singletonList(something)),
                            false);
                vfs.put(tUnknownVar.name, new MapValue(var));
            }
            case TArrayVar tArrayVar -> {
                ArrayList<Object> arr = new ArrayList<>();
                tArrayVar.contents.forEach(obj -> {
                    try {
                        if (obj instanceof ArrayList) {
                            arr.add(obj);
                        } else {
                            arr.add(Primitives.toPrimitive(Primitives.parseNonPrimitive(obj), vfs, false, config, cTrace));
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                BaseVariable var = BaseVariable.create(((TokenDefault) t).name, (TokenDefault) t, arr, true);
                vfs.put(tArrayVar.name, new MapValue(var));
            }
            case TFunction function -> {
                String name = function.name.replace("F~", "");
                BaseFunction func = BaseFunction.create(name, function);
                vfs.put(name, new MapValue(func));
            }
            case TVarReassign tVarReassign -> {
                MapValue mapValue = vfs.get(tVarReassign.name);
                if (MapValue.isEmpty(mapValue)
                        || (mapValue.getValue() == null || !(mapValue.getValue() instanceof BaseVariable
                        || mapValue.getValue() instanceof BaseFunction)))
                    throw new UnknownVariableException(cTrace, tVarReassign);

                Symbol var = (Symbol) mapValue.getValue();
                if (var.isFrozen)
                    throw new FrozenSymbolException(cTrace, var, tVarReassign.lineNumber);

                Object o = Primitives.toPrimitive(Primitives.parseNonPrimitive(tVarReassign.newValue), vfs, false,
                        config, cTrace);

                if (o instanceof BaseFunction) {
                    mapValue.setValue(o);
                } else if (var instanceof BaseVariable v) {
                    if (o instanceof ArrayList a) {
                        v.a_set(a, cTrace);
                    } else {
                        v.s_set(o, cTrace);
                    }
                }

                // so hopefully this chanegs the instance and yeah 👍
            }
            case null, default -> {
                // here its a primitive being parsed or recursively called
                // or TVarRef or TFuncCall or TStatement
                return Primitives.toPrimitive(t, vfs, false, config, cTrace);
            }
        }

        return null; // return null because we are not returning anything.
    }

    /**
     * The interpret method is the main method which handles the interpretation of
     * Jaiva code.
     * <p>
     * It takes a list of tokens and a context and interprets the code accordingly.
     *
     * @param tokens The list of tokens to interpret.
     * @param cTrace The context of the code being interpreted.
     * @param vfs    A map storing variable names as keys and their corresponding
     *               MapValue
     *               objects as values. This map is updated based on the operation
     *               performed.
     * @param config The configuration object containing settings for the
     *               interpreter,
     *               including the file directory and import settings.
     * @return Returns the result of the interpretation, which can be a value or a
     *         void class.
     * @throws Exception If an error occurs during interpretation, such
     *                   as an
     *                   unknown
     *                   variable or an invalid operation.
     */
    public static Object interpret(ArrayList<Token<?>> tokens, ContextTrace cTrace, HashMap<String, MapValue> vfs,
            IConfig config)
            throws Exception {
        // prepare a new vfs.
        vfs = config.importVfs && cTrace.current == Context.GLOBAL ? new HashMap<>() : vfs;

        vfs = vfs != null ? ((HashMap<String, MapValue>) vfs.clone()) : vfs;

        // Step 2: go throguh eahc token
        for (Token<?> t : tokens) {
            TokenDefault token = t.value();
            // first check if we're in a debug environment
            if (config.dc.active) {
                if (config.dc.getBreakpoints().contains(token.lineNumber)) {
                    // if we are, then we pause the execution and wait for the user to continue.
                    config.dc.print(token.lineNumber, null, t, vfs, cTrace);
                }
                if (config.dc.stepOver.equals(new Tuple2<>(true, false))) {
                    config.dc.print(token.lineNumber, null, t, vfs, cTrace);
                    continue;
                } else if (config.dc.stepOver.equals(new Tuple2<>(false, true))) {
                    config.dc.print(token.lineNumber, null, t, vfs, cTrace);
                }
            }
            if (token instanceof TVoidValue) {
                // Well, the user placed idk by itself, so i also don't know what to do with it.
                continue;
            } else if (token instanceof TImport tImport) {
                Globals g = new Globals(config);
                Path importPath = Path.of(tImport.filePath);

                HashMap<String, MapValue> vfsFromFile;

                if (g.builtInGlobals.containsKey(tImport.fileName))
                    vfsFromFile = (HashMap<String, MapValue>) g.builtInGlobals.get(tImport.fileName);
                else {

                    // Check if the path is not absolute
                    if (!importPath.isAbsolute()) {
                        // Resolve it relative to the current file's directory,
                        // then normalize to tidy up any relative path elements.
                        importPath = config.fileDirectory.resolve(importPath).normalize();
                    }

                    importPath = importPath.toAbsolutePath();

                    ArrayList<Token<?>> tks = Main.parseTokens(importPath.toString(), true);

                    if (tks.isEmpty())
                        continue; // Nohing to import.

                    IConfig newConfig = new IConfig(config.sanitisedArgs, importPath.toString(),
                            config.JAIVA_SRC_PATH.toString());

                    newConfig.importVfs = true; // This tells the interpreter to only parse exported symbols. (Functions
                                                // and variables)

                    vfsFromFile = (HashMap<String, MapValue>) Interpreter.interpret(tks, cTrace,
                            vfs, newConfig);

                    if ((vfsFromFile == null)) {
                        // error? maybe not yet but throw.
                        throw new CatchAllException(cTrace,
                                importPath.toString()
                                        + " either had nothing to export or somethign wqent horribly wrong.",
                                tImport.lineNumber);
                        // continue;
                    }
                }
                if (!tImport.symbols.isEmpty()) {
                    vfsFromFile.entrySet().removeIf(e -> !tImport.symbols.contains(e.getKey()));
                }
                assert vfs != null;
                vfs.putAll(vfsFromFile);
            } else if (isVariableToken(token)) {
                if (token instanceof TFuncCall || token instanceof TVarRef)
                    handleVariables(t, vfs, config, cTrace);
                else
                    handleVariables(token, vfs, config, cTrace);
                // If it returns a meaningful value, then oh well, because in this case they
                // basically called a function that returned soemthing but dont use that value.
            } else if (token instanceof TFuncReturn tFuncReturn && !config.importVfs) {
                Object c = handleVariables(tFuncReturn.value, vfs, config, cTrace);
                ThrowIfGlobalContext g = throwIfGlobalContext(cTrace, c, token.lineNumber);
                return g;
            } else if (token instanceof TLoopControl loopControl && !config.importVfs) {
                Object lc = loopControl.type;
                ThrowIfGlobalContext g = throwIfGlobalContext(cTrace, lc, loopControl.lineNumber);
                return g;
            } else if (token instanceof TThrowError lc && !config.importVfs) {
                ThrowIfGlobalContext g = throwIfGlobalContext(cTrace, lc, lc.lineNumber);
                return g;
            } else if (token instanceof TWhileLoop whileLoop && !config.importVfs) {
                // while loop
                Object cond = Primitives.setCondition(whileLoop, vfs, config, cTrace);

                boolean terminate = false;

                while ((Boolean) cond && !terminate) {
                    Object out = Interpreter.interpret(whileLoop.body.lines,
                            new ContextTrace(Context.WHILE, token, cTrace), vfs, config);
                    if (out instanceof ThrowIfGlobalContext old) {
                        if (old.c == Keywords.LoopControl.BREAK)
                            break;
                        ThrowIfGlobalContext checker = throwIfGlobalContext(cTrace, out, old.lineNumber);
                        if (checker.c == Keywords.LoopControl.BREAK)
                            break;
                        return checker;
                    }

                    cond = Primitives.setCondition(whileLoop, vfs, config, cTrace);
                }
                // while loop
            } else if (token instanceof TIfStatement ifStatement && !config.importVfs) {
                // if statement handling below
                if (!(ifStatement.condition instanceof TStatement))
                    throw new WtfAreYouDoingException(cTrace,
                            "Okay well idk how i will check for true in " + ifStatement,
                            token.lineNumber);
                Object cond = Primitives.setCondition(ifStatement, vfs, config, cTrace);

                // if if, lol i love coding
                if ((Boolean) cond) {
                    // run if code.
                    Object out = Interpreter.interpret(ifStatement.body.lines,
                            new ContextTrace(Context.IF, token, cTrace), vfs, config);
                    if (out instanceof ThrowIfGlobalContext g) {
                        return throwIfGlobalContext(cTrace, out, g.lineNumber);
                    }
                } else {
                    // check for else branches first
                    boolean runElseBlock = true;
                    for (Object e : ifStatement.elseIfs) {
                        TIfStatement elseIf = (TIfStatement) e;
                        if (!(elseIf.condition instanceof TStatement))
                            throw new WtfAreYouDoingException(cTrace,
                                    "Okay well idk how i will check for true in " + elseIf, token.lineNumber);
                        Object cond2 = Primitives.setCondition(elseIf, vfs, config, cTrace);
                        if ((Boolean) cond2) {
                            // run else if code.
                            Object out = Interpreter.interpret(elseIf.body.lines,
                                    new ContextTrace(Context.ELSE, token, cTrace), vfs, config);
                            if (out instanceof ThrowIfGlobalContext g) {
                                return throwIfGlobalContext(cTrace, out, g.lineNumber);
                            }
                            runElseBlock = false;
                            break;
                        }
                    }
                    if (runElseBlock && ifStatement.elseBody != null) {
                        // run else block.
                        Object out = Interpreter.interpret(ifStatement.elseBody.lines,
                                new ContextTrace(Context.ELSE, token, cTrace), vfs, config);
                        if (out instanceof ThrowIfGlobalContext g) {
                            return throwIfGlobalContext(cTrace, out, g.lineNumber);
                        }
                    }
                }
                // if statement handling above
            } else if (token instanceof TForLoop tForLoop && !config.importVfs) {
                // for loop
                handleVariables(tForLoop.variable, vfs, config, cTrace);
                assert vfs != null;
                Object vObject = vfs.get(tForLoop.variable.name).getValue();
                if (!(vObject instanceof BaseVariable))
                    throw new WtfAreYouDoingException(cTrace, vObject, BaseVariable.class,
                            tForLoop.lineNumber);
                BaseVariable v = (BaseVariable) vObject;
                // if (!(v.s_get() instanceof Integer))
                // throw new WtfAreYouDoingException("");
                if (tForLoop.array != null && tForLoop.increment == null && tForLoop.condition == null) {
                    // for each
                    MapValue mapValue = vfs.get(tForLoop.array.varName);
                    if (mapValue == null || !((mapValue.getValue()) instanceof BaseVariable))
                        throw new UnknownVariableException(cTrace, tForLoop.array);
                    BaseVariable arr = (BaseVariable) mapValue.getValue();

                    if (arr.variableType != VariableType.ARRAY
                            && arr.variableType != VariableType.A_FUCKING_AMALGAMATION
                            && !(arr.s_get() instanceof String))
                        throw new WtfAreYouDoingException(cTrace,
                                "Ayo that variable argument on " + tForLoop.lineNumber + " gotta be an array.",
                                token.lineNumber);

                    List<Object> list = (arr.s_get() instanceof String)
                            // Type String[] of the last argument to method asList(Object...) doesn't
                            // exactly match the vararg parameter type. Cast to Object[] to confirm the
                            // non-varargs invocation, or pass individual arguments of type Object for a
                            // varargs invocation.
                            ? Arrays.asList(((String) arr.s_get()).split(""))
                            : arr.a_getAll();

                    for (Object o : list) {
                        v.s_set(o, cTrace);
                        Object out = Interpreter.interpret(tForLoop.body.lines,
                                new ContextTrace(Context.FOR, token, cTrace), vfs, config);
                        if (out instanceof ThrowIfGlobalContext g) {
                            if (g.c == Keywords.LoopControl.BREAK)
                                break;
                            if (g.c == Keywords.LoopControl.CONTINUE)
                                continue;
                            return throwIfGlobalContext(cTrace, out, g.lineNumber);
                        }
                    }
                } else {
                    // normal for loop.
                    assert tForLoop.increment != null;
                    char increment = tForLoop.increment.charAt(0);
                    Object cond = Primitives.setCondition(tForLoop, vfs, config, cTrace);
                    while ((Boolean) cond) {

                        Object out = Interpreter.interpret(tForLoop.body.lines,
                                new ContextTrace(Context.FOR, token, cTrace), vfs, config);
                        if (out instanceof ThrowIfGlobalContext g) {
                            if (g.c == Keywords.LoopControl.BREAK)
                                break;
                            if (g.c == Keywords.LoopControl.CONTINUE) {
                                v.s_set((Integer) v.s_get() + (increment == '+' ? 1 : -1), cTrace);
                                cond = Primitives.setCondition(tForLoop, vfs, config, cTrace);
                                continue;
                            }
                            return throwIfGlobalContext(cTrace, out, g.lineNumber);
                        }
                        v.s_set((Integer) v.s_get() + (increment == '+' ? 1 : -1), cTrace);
                        cond = Primitives.setCondition(tForLoop, vfs, config, cTrace);
                    }
                }

                vfs.remove(v.name);
                // for loop
            } else if (token instanceof TTryCatchStatement throwError && !config.importVfs) {

                HashMap<String, MapValue> errorVfs = ((HashMap<String, MapValue>) vfs.clone());
                errorVfs.entrySet().removeIf(vf -> !vf.getKey().contains("error"));

                String varName = "error" + (!errorVfs.isEmpty() ? errorVfs.size() : "");

                try {
                    Object out = Interpreter.interpret(throwError.tryBlock.lines,
                            new ContextTrace(Context.TRY, token, cTrace), vfs, config);
                    if (out instanceof ThrowIfGlobalContext g) {
                        if (g.c instanceof Keywords
                                || isVariableToken(g.c)) {
                            return throwIfGlobalContext(cTrace, out, g.lineNumber);
                        } else if (g.c instanceof TThrowError) {
                            Token<?> tContainer = new Token<>(null);
                            TThrowError err = (TThrowError) g.c;
                            vfs.put(varName,
                                    new MapValue(BaseVariable.create(
                                            varName,
                                            new TStringVar(
                                                    varName, err.errorMessage,
                                                    err.lineNumber),
                                            new ArrayList<>(Collections.singletonList(
                                                    err.errorMessage)),
                                            false)));
                            Object out2 = Interpreter.interpret(throwError.catchBlock.lines,
                                    new ContextTrace(Context.CATCH, token, cTrace), vfs,
                                    config);
                            vfs.remove(varName);
                            if (out2 instanceof ThrowIfGlobalContext g2) {
                                return throwIfGlobalContext(cTrace, out2, g2.lineNumber);
                            }

                            // return void.class;
                        }
                    }
                } catch (Exception e) {
                    // catch any exception, and call the catch block with the error.
                    Token<?> tContainer = new Token<>(null);

                    vfs.put(varName,
                            new MapValue(BaseVariable.create(
                                    varName,
                                    new TStringVar(
                                            varName, e.getMessage(),
                                            throwError.catchBlock.lineNumber),
                                    new ArrayList<>(Arrays.asList(
                                            e.getMessage())),
                                    false)));
                    Object out2 = Interpreter.interpret(throwError.catchBlock.lines,
                            new ContextTrace(Context.CATCH, token, cTrace), vfs, config); // line
                    // 542
                    vfs.remove(varName);
                    if (out2 instanceof ThrowIfGlobalContext g2) {
                        return throwIfGlobalContext(cTrace, out2, g2.lineNumber);
                    }

                    // return void.class;
                }
            }
            // return contextValue;
        }
        // System.out.println("heyy");

        if (config.dc.active && cTrace.current == Context.GLOBAL) {
            // if we're in the global context, then we end the debugging session.
            config.dc.endOfFile(vfs);
        }
        return config.importVfs || config.REPL ? vfs : void.class;
    }
}
