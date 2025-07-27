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
     * @param scope     The context of the code being interpreted.
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
    public static ThrowIfGlobalContext throwIfGlobalContext(Scope scope, Object lc, int lineNumber)
            throws JaivaException {
        if (lc instanceof ThrowIfGlobalContext) {
            lc = ((ThrowIfGlobalContext) lc).c;
        }
        if (lc instanceof DebugException dlc)
            throw dlc; // user shouldnt catch it anywhere fr.
        else if (lc instanceof Keywords.LoopControl) {
            if (scope.current == Context.GLOBAL)
                throw new WtfAreYouDoingException(scope, "So. You tried using "
                        + (lc.toString().equals("BREAK") ? Keywords.LC_BREAK : Keywords.LC_CONTINUE)
                        + ". But like, we're not in a loop yknow? ", lineNumber);
        } else if (Primitives.isPrimitive(lc)) {
            // a function return thing then
            if (scope.current == Context.GLOBAL)
                throw new WtfAreYouDoingException(scope,
                        "What are you trying to return out of if we're not in a function??", lineNumber);

        } else if (lc instanceof TThrowError) {
            if (scope.current == Context.GLOBAL)
                throw new CimaException(scope, ((TThrowError) lc).errorMessage, lineNumber);
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
    public static Object handleVariables(Object t, IConfig config, Scope scope)
            throws Exception {
        switch (t) {
            case TNumberVar tNumberVar -> {
                Object number = Primitives.toPrimitive(tNumberVar.value, false, config, scope);
                BaseVariable var = BaseVariable.create(((TokenDefault) t).name, (TokenDefault) t,
                        new ArrayList<>(Collections.singletonList(number)), false);
                scope.vfs.put(tNumberVar.name, var);
            }
            case TBooleanVar tBooleanVar -> {
                Object bool = Primitives.toPrimitive(tBooleanVar.value, false, config, scope);
                BaseVariable var = BaseVariable.create(((TokenDefault) t).name, (TokenDefault) t,
                        new ArrayList<>(Collections.singletonList(bool)), false);
                scope.vfs.put(tBooleanVar.name, var);
            }
            case TStringVar tStringVar -> {
                Object string = Primitives.toPrimitive(tStringVar.value, false, config, scope);
                BaseVariable var = BaseVariable.create(((TokenDefault) t).name, (TokenDefault) t,
                        new ArrayList<>(Collections.singletonList(string)), false);
                scope.vfs.put(tStringVar.name, var);
            }
            case TUnknownVar tUnknownVar -> {
                Object something = Primitives.toPrimitive(tUnknownVar.value, false, config, scope);
                Symbol var;
                if (something instanceof BaseFunction)
                    // this assigns
                    var = (Symbol) something;
                else
                    var = BaseVariable.create(((TokenDefault) t).name, (TokenDefault) t,
                            something instanceof ArrayList ? (ArrayList) something
                                    : new ArrayList<>(Collections.singletonList(something)),
                            false);
                scope.vfs.put(tUnknownVar.name, var);
            }
            case TArrayVar tArrayVar -> {
                ArrayList<Object> arr = new ArrayList<>();
                tArrayVar.contents.forEach(obj -> {
                    try {
                        if (obj instanceof ArrayList) {
                            arr.add(obj);
                        } else {
                            arr.add(Primitives.toPrimitive(Primitives.parseNonPrimitive(obj), false, config, scope));
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                BaseVariable var = BaseVariable.create(((TokenDefault) t).name, (TokenDefault) t, arr, true);
                scope.vfs.put(tArrayVar.name, var);
            }
            case TFunction function -> {
                String name = function.name.replace("F~", "");
                BaseFunction func = BaseFunction.create(name, function);
                scope.vfs.put(name, func);
            }
            case TVarReassign tVarReassign -> {
                MapValue mapValue = scope.vfs.get(tVarReassign.name);
                if (MapValue.isEmpty(mapValue)
                        || (mapValue.getValue() == null || !(mapValue.getValue() instanceof BaseVariable
                        || mapValue.getValue() instanceof BaseFunction)))
                    throw new UnknownVariableException(scope, tVarReassign);

                Symbol var = (Symbol) mapValue.getValue();
                if (var.isFrozen)
                    throw new FrozenSymbolException(scope, var, tVarReassign.lineNumber);

                Object o = Primitives.toPrimitive(Primitives.parseNonPrimitive(tVarReassign.newValue), false,
                        config, scope);

                if (o instanceof BaseFunction func) {
                    mapValue.setValue(func);
                } else if (var instanceof BaseVariable v) {
                    if (o instanceof ArrayList a) {
                        v.a_set(a, scope);
                    } else {
                        v.s_set(o, scope);
                    }
                }

                // so hopefully this chanegs the instance and yeah 👍
            }
            case null, default -> {
                // here its a primitive being parsed or recursively called
                // or TVarRef or TFuncCall or TStatement
                return Primitives.toPrimitive(t, false, config, scope);
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
     * @param scope The current scope of the array list of tokens being interpreted
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
    public static Object interpret(ArrayList<Token<?>> tokens, Scope scope, IConfig config)
            throws Exception {
        // prepare a new vfs.
        scope.vfs = config.importVfs && scope.current == Context.GLOBAL ? new Vfs() : scope.vfs;

//        vfs = vfs != null ? vfs.clone() : vfs;

        // Step 2: go throguh eahc token
        for (Token<?> t : tokens) {
            TokenDefault token = t.value();
            // first check if we're in a debug environment
            if (config.dc.active) {
                if (config.dc.getBreakpoints().contains(token.lineNumber)) {
                    // if we are, then we pause the execution and wait for the user to continue.
                    config.dc.print(token.lineNumber, null, t,  scope);
                }
                if (config.dc.stepOver.equals(new Tuple2<>(true, false))) {
                    config.dc.print(token.lineNumber, null, t, scope);
                    continue;
                } else if (config.dc.stepOver.equals(new Tuple2<>(false, true))) {
                    config.dc.print(token.lineNumber, null, t, scope);
                }
            }
            if (token instanceof TVoidValue) {
                // Well, the user placed idk by itself, so i also don't know what to do with it.
                continue;
            } else if (token instanceof TImport tImport) {
                Globals g = new Globals(config);
                Path importPath = Path.of(tImport.filePath);

                Vfs vfsFromFile;

                if (g.builtInGlobals.containsKey(tImport.fileName))
                    vfsFromFile = (Vfs) g.builtInGlobals.get(tImport.fileName);
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

                    vfsFromFile = (Vfs) Interpreter.interpret(tks, scope, newConfig);

                    if ((vfsFromFile == null)) {
                        // error? maybe not yet but throw.
                        throw new CatchAllException(scope,
                                importPath.toString()
                                        + " either had nothing to export or somethign wqent horribly wrong.",
                                tImport.lineNumber);
                        // continue;
                    }
                }
                if (!tImport.symbols.isEmpty()) {
                    vfsFromFile.entrySet().removeIf(e -> !tImport.symbols.contains(e.getKey()));
                }
                scope.vfs.putAll(vfsFromFile);
            } else if (isVariableToken(token)) {
                if (token instanceof TFuncCall || token instanceof TVarRef)
                    handleVariables(t, config, scope);
                else
                    handleVariables(token, config, scope);
                // If it returns a meaningful value, then oh well, because in this case they
                // basically called a function that returned soemthing but dont use that value.
            } else if (token instanceof TFuncReturn tFuncReturn && !config.importVfs) {
                Object c = handleVariables(tFuncReturn.value, config, scope);
                return throwIfGlobalContext(scope, c, token.lineNumber);
            } else if (token instanceof TLoopControl loopControl && !config.importVfs) {
                Object lc = loopControl.type;
                return throwIfGlobalContext(scope, lc, loopControl.lineNumber);
            } else if (token instanceof TThrowError lc && !config.importVfs) {
                return throwIfGlobalContext(scope, lc, lc.lineNumber);
            } else if (token instanceof TWhileLoop whileLoop && !config.importVfs) {
                // while loop
                Object cond = Primitives.setCondition(whileLoop, config, scope);

                boolean terminate = false;

                while ((Boolean) cond && !terminate) {
                    Object out = Interpreter.interpret(whileLoop.body.lines,
                            new Scope(Context.WHILE, token, scope), config);
                    if (out instanceof ThrowIfGlobalContext old) {
                        if (old.c == Keywords.LoopControl.BREAK)
                            break;
                        ThrowIfGlobalContext checker = throwIfGlobalContext(scope, out, old.lineNumber);
                        if (checker.c == Keywords.LoopControl.BREAK)
                            break;
                        return checker;
                    }

                    cond = Primitives.setCondition(whileLoop, config, scope);
                }
                // while loop
            } else if (token instanceof TIfStatement ifStatement && !config.importVfs) {
                // if statement handling below
                if (!(ifStatement.condition instanceof TStatement))
                    throw new WtfAreYouDoingException(scope,
                            "Okay well idk how i will check for true in " + ifStatement,
                            token.lineNumber);
                Object cond = Primitives.setCondition(ifStatement, config, scope);

                // if if, lol i love coding
                if ((Boolean) cond) {
                    // run if code.
                    Object out = Interpreter.interpret(ifStatement.body.lines,
                            new Scope(Context.IF, token, scope), config);
                    if (out instanceof ThrowIfGlobalContext g) {
                        return throwIfGlobalContext(scope, out, g.lineNumber);
                    }
                } else {
                    // check for else branches first
                    boolean runElseBlock = true;
                    for (Object e : ifStatement.elseIfs) {
                        TIfStatement elseIf = (TIfStatement) e;
                        if (!(elseIf.condition instanceof TStatement))
                            throw new WtfAreYouDoingException(scope,
                                    "Okay well idk how i will check for true in " + elseIf, token.lineNumber);
                        Object cond2 = Primitives.setCondition(elseIf, config, scope);
                        if ((Boolean) cond2) {
                            // run else if code.
                            Object out = Interpreter.interpret(elseIf.body.lines,
                                    new Scope(Context.ELSE, token, scope), config);
                            if (out instanceof ThrowIfGlobalContext g) {
                                return throwIfGlobalContext(scope, out, g.lineNumber);
                            }
                            runElseBlock = false;
                            break;
                        }
                    }
                    if (runElseBlock && ifStatement.elseBody != null) {
                        // run else block.
                        Object out = Interpreter.interpret(ifStatement.elseBody.lines,
                                new Scope(Context.ELSE, token, scope), config);
                        if (out instanceof ThrowIfGlobalContext g) {
                            return throwIfGlobalContext(scope, out, g.lineNumber);
                        }
                    }
                }
                // if statement handling above
            } else if (token instanceof TForLoop tForLoop && !config.importVfs) {
                // for loop
                handleVariables(tForLoop.variable, config, scope);
                Object vObject = scope.vfs.get(tForLoop.variable.name).getValue();
                if (!(vObject instanceof BaseVariable))
                    throw new WtfAreYouDoingException(scope, vObject, BaseVariable.class,
                            tForLoop.lineNumber);
                BaseVariable v = (BaseVariable) vObject;
                // if (!(v.s_get() instanceof Integer))
                // throw new WtfAreYouDoingException("");
                if (tForLoop.array != null && tForLoop.increment == null && tForLoop.condition == null) {
                    // for each
                    MapValue mapValue = scope.vfs.get(tForLoop.array.varName);
                    if (mapValue == null || !((mapValue.getValue()) instanceof BaseVariable))
                        throw new UnknownVariableException(scope, tForLoop.array);
                    BaseVariable arr = (BaseVariable) mapValue.getValue();

                    if (arr.variableType != VariableType.ARRAY
                            && arr.variableType != VariableType.A_FUCKING_AMALGAMATION
                            && !(arr.s_get() instanceof String))
                        throw new WtfAreYouDoingException(scope,
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
                        v.s_set(o, scope);
                        Object out = Interpreter.interpret(tForLoop.body.lines,
                                new Scope(Context.FOR, token, scope), config);
                        if (out instanceof ThrowIfGlobalContext g) {
                            if (g.c == Keywords.LoopControl.BREAK)
                                break;
                            if (g.c == Keywords.LoopControl.CONTINUE)
                                continue;
                            return throwIfGlobalContext(scope, out, g.lineNumber);
                        }
                    }
                } else {
                    // normal for loop.
                    assert tForLoop.increment != null;
                    char increment = tForLoop.increment.charAt(0);
                    Object cond = Primitives.setCondition(tForLoop, config, scope);
                    while ((Boolean) cond) {

                        Object out = Interpreter.interpret(tForLoop.body.lines,
                                new Scope(Context.FOR, token, scope), config);
                        if (out instanceof ThrowIfGlobalContext g) {
                            if (g.c == Keywords.LoopControl.BREAK)
                                break;
                            if (g.c == Keywords.LoopControl.CONTINUE) {
                                v.s_set((Integer) v.s_get() + (increment == '+' ? 1 : -1), scope);
                                cond = Primitives.setCondition(tForLoop, config, scope);
                                continue;
                            }
                            return throwIfGlobalContext(scope, out, g.lineNumber);
                        }
                        v.s_set((Integer) v.s_get() + (increment == '+' ? 1 : -1), scope);
                        cond = Primitives.setCondition(tForLoop, config, scope);
                    }
                }

                scope.vfs.remove(v.name);
                // for loop
            } else if (token instanceof TTryCatchStatement throwError && !config.importVfs) {

                Vfs errorVfs = ((Vfs) scope.vfs.clone());
                errorVfs.entrySet().removeIf(vf -> !vf.getKey().contains("error"));

                String varName = "error" + (!errorVfs.isEmpty() ? errorVfs.size() : "");

                try {
                    Object out = Interpreter.interpret(throwError.tryBlock.lines,
                            new Scope(Context.TRY, token, scope), config);
                    if (out instanceof ThrowIfGlobalContext g) {
                        if (g.c instanceof Keywords
                                || isVariableToken(g.c)) {
                            return throwIfGlobalContext(scope, out, g.lineNumber);
                        } else if (g.c instanceof TThrowError) {
                            Token<?> tContainer = new Token<>(null);
                            TThrowError err = (TThrowError) g.c;
                            scope.vfs.put(varName,
                                    BaseVariable.create(
                                            varName,
                                            new TStringVar(
                                                    varName, err.errorMessage,
                                                    err.lineNumber),
                                            new ArrayList<>(Collections.singletonList(
                                                    err.errorMessage)),
                                            false));
                            Object out2 = Interpreter.interpret(throwError.catchBlock.lines,
                                    new Scope(Context.CATCH, token, scope),
                                    config);
                            scope.vfs.remove(varName);
                            if (out2 instanceof ThrowIfGlobalContext g2) {
                                return throwIfGlobalContext(scope, out2, g2.lineNumber);
                            }

                            // return void.class;
                        }
                    }
                } catch (Exception e) {
                    // catch any exception, and call the catch block with the error.
                    Token<?> tContainer = new Token<>(null);

                    scope.vfs.put(varName,
                            BaseVariable.create(
                                    varName,
                                    new TStringVar(
                                            varName, e.getMessage(),
                                            throwError.catchBlock.lineNumber),
                                    new ArrayList<>(Arrays.asList(
                                            e.getMessage())),
                                    false));
                    Object out2 = Interpreter.interpret(throwError.catchBlock.lines,
                            new Scope(Context.CATCH, token, scope), config); // line
                    // 542
                    scope.vfs.remove(varName);
                    if (out2 instanceof ThrowIfGlobalContext g2) {
                        return throwIfGlobalContext(scope, out2, g2.lineNumber);
                    }

                    // return void.class;
                }
            }
            // return contextValue;
        }
        // System.out.println("heyy");

        if (config.dc.active && scope.current == Context.GLOBAL) {
            // if we're in the global context, then we end the debugging session.
            config.dc.endOfFile(scope);
        }
        return config.importVfs || config.REPL ? scope.vfs : void.class;
    }
}
