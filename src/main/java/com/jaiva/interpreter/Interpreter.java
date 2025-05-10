package com.jaiva.interpreter;

import java.nio.file.Path;
import java.util.*;

import com.jaiva.Main;
import com.jaiva.errors.InterpreterException;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.BaseFunction;
import com.jaiva.interpreter.symbol.BaseVariable;
import com.jaiva.interpreter.symbol.BaseVariable.VariableType;
import com.jaiva.lang.Keywords;
import com.jaiva.tokenizer.Token;
import com.jaiva.tokenizer.Token.TArrayVar;
import com.jaiva.tokenizer.Token.TBooleanVar;
import com.jaiva.tokenizer.Token.TForLoop;
import com.jaiva.tokenizer.Token.TFuncCall;
import com.jaiva.tokenizer.Token.TFuncReturn;
import com.jaiva.tokenizer.Token.TFunction;
import com.jaiva.tokenizer.Token.TIfStatement;
import com.jaiva.tokenizer.Token.TImport;
import com.jaiva.tokenizer.Token.TLoopControl;
import com.jaiva.tokenizer.Token.TNumberVar;
import com.jaiva.tokenizer.Token.TStatement;
import com.jaiva.tokenizer.Token.TStringVar;
import com.jaiva.tokenizer.Token.TThrowError;
import com.jaiva.tokenizer.Token.TTryCatchStatement;
import com.jaiva.tokenizer.Token.TUnknownVar;
import com.jaiva.tokenizer.Token.TVarReassign;
import com.jaiva.tokenizer.Token.TVarRef;
import com.jaiva.tokenizer.Token.TVoidValue;
import com.jaiva.tokenizer.Token.TWhileLoop;
import com.jaiva.tokenizer.TokenDefault;

/**
 * The Interpreter class is one of the 3 main classes which handle Jaiva code.
 * <p>
 * It is responsible for interpreting Jaiva code and executing it.
 */
public class Interpreter {

    /**
     * The ThrowIfGlobalContext class is a base class which is thrown when the
     * interpreter does not return a usual value, like it returns maybe an error or
     * it exits out of a loop.
     * <p>
     * It's the same idea as a software interrupt. Execution is halted to handle
     * whatever needs tobe handled.
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
         * @param lineNumber The line number of the token which caused the interuption.
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
     * @param context    The context of the code being interpreted.
     * @param lc         The object which caused the interuption.
     * @param lineNumber The line number of the token which caused the interuption.
     * @return Returns a new instance of the ThrowIfGlobalContext class with the
     *         given object and line number.
     * @throws InterpreterException.WtfAreYouDoingException If the context is global
     *                                                      and the object is a
     *                                                      function return or a
     *                                                      loop control.
     * @throws InterpreterException.CimaException           If the context is global
     *                                                      and the object is an
     *                                                      error.
     */
    public static ThrowIfGlobalContext throwIfGlobalContext(Context context, Object lc, int lineNumber)
            throws InterpreterException.WtfAreYouDoingException, InterpreterException.CimaException {
        if (lc instanceof ThrowIfGlobalContext) {
            lc = ((ThrowIfGlobalContext) lc).c;
        }
        if (lc instanceof Keywords.LoopControl) {
            if (context == Context.GLOBAL)
                throw new InterpreterException.WtfAreYouDoingException("So. You tried using "
                        + (lc.toString().equals("BREAK") ? Keywords.LC_BREAK : Keywords.LC_CONTINUE)
                        + ". But like, we're not in a loop yknow? ", lineNumber);
        } else if (Primitives.isPrimitive(lc)) {
            // a function return thing then
            if (context == Context.GLOBAL)
                throw new InterpreterException.WtfAreYouDoingException(
                        "What are you trying to return out of if we're not in a function??", lineNumber);

        } else if (lc instanceof TThrowError) {
            if (context == Context.GLOBAL)
                throw new InterpreterException.CimaException(((TThrowError) lc).errorMessage, lineNumber);
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
        return t instanceof TStatement || t instanceof TNumberVar || t instanceof TBooleanVar || t instanceof TArrayVar
                || t instanceof TStringVar
                || t instanceof TUnknownVar || t instanceof TVarReassign || t instanceof TVarRef
                || t instanceof TFuncCall || t instanceof TStatement || t instanceof TFunction
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
    public static Object handleVariables(Object t, HashMap<String, MapValue> vfs, IConfig config)
            throws Exception {
        if (t instanceof TNumberVar) {
            Object number = Primitives.toPrimitive(((TNumberVar) t).value, vfs, false, config);
            BaseVariable var = BaseVariable.create(((TokenDefault) t).name, (TokenDefault) t,
                    new ArrayList<>(Arrays.asList(number)), false);
            vfs.put(((TNumberVar) t).name, new MapValue(var));
        } else if (t instanceof TBooleanVar) {
            Object bool = Primitives.toPrimitive(((TBooleanVar) t).value, vfs, false, config);
            BaseVariable var = BaseVariable.create(((TokenDefault) t).name, (TokenDefault) t,
                    new ArrayList<>(Arrays.asList(bool)), false);
            vfs.put(((TBooleanVar) t).name, new MapValue(var));
        } else if (t instanceof TStringVar) {
            Object string = Primitives.toPrimitive(((TStringVar) t).value, vfs, false, config);
            BaseVariable var = BaseVariable.create(((TokenDefault) t).name, (TokenDefault) t,
                    new ArrayList<>(Arrays.asList(string)), false);
            vfs.put(((TStringVar) t).name, new MapValue(var));
        } else if (t instanceof TUnknownVar) {
            Object something = Primitives.toPrimitive(((TUnknownVar) t).value, vfs, false, config);
            BaseVariable var = BaseVariable.create(((TokenDefault) t).name, (TokenDefault) t,
                    something instanceof ArrayList ? (ArrayList) something : new ArrayList<>(Arrays.asList(something)),
                    false);
            vfs.put(((TUnknownVar) t).name, new MapValue(var));
        } else if (t instanceof TArrayVar) {
            ArrayList<Object> arr = new ArrayList<>();
            ((TArrayVar) t).contents.forEach(obj -> {
                try {
                    if (obj instanceof ArrayList) {
                        arr.add(obj);
                    } else {
                        arr.add(Primitives.toPrimitive(Primitives.parseNonPrimitive(obj), vfs, false, config));
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            BaseVariable var = BaseVariable.create(((TokenDefault) t).name, (TokenDefault) t, arr, true);
            vfs.put(((TArrayVar) t).name, new MapValue(var));
        } else if (t instanceof TFunction) {
            TFunction function = (TFunction) t;
            String name = function.name.replace("F~", "");
            BaseFunction func = BaseFunction.create(name, function);
            vfs.put(name, new MapValue(func));
        } else if (t instanceof TVarReassign) {
            MapValue mapValue = vfs.get(((TVarReassign) t).name);
            if (MapValue.isEmpty(mapValue)
                    || (mapValue.getValue() == null || !(mapValue.getValue() instanceof BaseVariable)))
                throw new InterpreterException.UnknownVariableException((TVarReassign) t);

            BaseVariable var = (BaseVariable) mapValue.getValue();
            if (var.isFrozen)
                throw new InterpreterException.FrozenSymbolException(var, ((TVarReassign) t).lineNumber);
            // if (!isVariableToken(((TVarReassign) t).newValue))
            // throw new WtfAreYouDoingException(
            // ((TVarReassign) t).newValue + " isn't like a valid var thingy yknow??");

            Object o = Primitives.toPrimitive(Primitives.parseNonPrimitive(((TVarReassign) t).newValue), vfs, false,
                    config);

            if (o instanceof ArrayList) {
                var.a_set((ArrayList) o);
            } else {
                var.s_set(o);
            }

            // so hopefully this chanegs the instance and yeah 👍
        } else {
            // here its a primitive being parsed or recursively called
            // or TVarRef or TFuncCall or TStatement
            return Primitives.toPrimitive(t, vfs, false, config);
        }

        return null; // return null because we are not returning anything.
    }

    /**
     * The interpret method is the main method which handles the interpretation of
     * Jaiva code.
     * <p>
     * It takes a list of tokens and a context and interprets the code accordingly.
     *
     * @param tokens  The list of tokens to interpret.
     * @param context The context of the code being interpreted.
     * @param vfs     A map storing variable names as keys and their corresponding
     *                MapValue
     *                objects as values. This map is updated based on the operation
     *                performed.
     * @param config  The configuration object containing settings for the
     *                interpreter,
     *                including the file directory and import settings.
     * @return Returns the result of the interpretation, which can be a value or a
     *         void class.
     * @throws Exception If an error occurs during interpretation, such as an
     *                   unknown
     *                   variable or an invalid operation.
     */
    public static Object interpret(ArrayList<Token<?>> tokens, Context context, HashMap<String, MapValue> vfs,
            IConfig config)
            throws Exception {
        // prepare a new vfs.
        vfs = config.importVfs && context == Context.GLOBAL ? new HashMap<>() : vfs;

        vfs = vfs != null ? ((HashMap<String, MapValue>) vfs.clone()) : vfs;

        // Step 2: go throguh eahc token
        for (Token<?> t : tokens) {
            TokenDefault token = t.getValue();
            if (token instanceof TImport) {
                TImport tImport = (TImport) token;// Create a Path instance from tImport.filePath
                Path importPath = Path.of(tImport.filePath);

                // Check if the path is not absolute
                if (!importPath.isAbsolute()) {
                    // Resolve it relative to the current file's directory,
                    // then normalize to tidy up any relative path elements.
                    importPath = config.fileDirectory.resolve(importPath).normalize();
                }

                importPath = importPath.toAbsolutePath();

                ArrayList<Token<?>> tks = Main.parseTokens(importPath.toString(), true);

                if (tks.size() == 0)
                    continue;

                IConfig newConfig = new IConfig(importPath.toString());

                newConfig.importVfs = true;

                HashMap<String, MapValue> vfsFromFile = (HashMap<String, MapValue>) Interpreter.interpret(tks, context,
                        vfs, newConfig);

                if (!(vfsFromFile instanceof HashMap) && (vfsFromFile == null)) {
                    // error? maybe not yet but print for debugging
                    System.out.println(
                            importPath.toString() + " either had nothing to export or somethign wqent horribly wrong.");
                    continue;
                }
                if (tImport.symbols.size() > 0) {
                    vfsFromFile.entrySet().removeIf(e -> !tImport.symbols.contains(e.getKey()));
                }
                vfs.putAll(vfsFromFile);
            } else if (isVariableToken(token)) {
                Object contextValue = null;
                // handles the following cases:
                // TNumberVar, TBooleanVar, TStringVar, TUnknownVar, TVarReassign, TArrayVar
                // including TStatement, TFuncCall and TVarRef. This also includes primitives.
                contextValue = (token instanceof TFuncCall || token instanceof TVarRef)
                        ? handleVariables(t, vfs,
                                config)
                        : handleVariables(token, vfs, config);
                // If it returns a meaningful value, then oh well, because in this case they
                // basically called a function that returned soemthing but dont use that value.
            } else if (token instanceof TVoidValue) {
                // void
                continue;
            } else if (token instanceof TFuncReturn && !config.importVfs) {
                Object c = handleVariables(((TFuncReturn) token).value, vfs, config);
                ThrowIfGlobalContext g = throwIfGlobalContext(context, c, token.lineNumber);
                return g;
            } else if (token instanceof TLoopControl && !config.importVfs) {
                TLoopControl loopControl = (TLoopControl) token;
                // if (loopControl.type == Keywords.LoopControl.CONTINUE && context !=
                // Context.FOR)
                // throw new WtfAreYouDoingException(
                // "kanti why is ther a nevermind on line " + loopControl.lineNumber);
                // if (loopControl.type == Keywords.LoopControl.BREAK
                // && (context != Context.WHILE && context != Context.FOR))
                // throw new WtfAreYouDoingException(
                // "kanti why is there a voetsek on line " + loopControl.lineNumber);

                Object lc = loopControl.type;
                ThrowIfGlobalContext g = throwIfGlobalContext(context, lc, loopControl.lineNumber);
                return g;

            } else if (token instanceof TThrowError && !config.importVfs) {
                TThrowError lc = (TThrowError) token;
                ThrowIfGlobalContext g = throwIfGlobalContext(context, lc, lc.lineNumber);
                return g;
            } else if (token instanceof TWhileLoop && !config.importVfs) {
                // while loop
                TWhileLoop whileLoop = (TWhileLoop) token;
                Object cond = Primitives.setCondition(whileLoop, vfs, config);

                boolean terminate = false;

                while (((Boolean) cond).booleanValue() && !terminate) {
                    Object out = Interpreter.interpret(whileLoop.body.lines, Context.WHILE, vfs, config);
                    if (out instanceof ThrowIfGlobalContext) {
                        ThrowIfGlobalContext g = (ThrowIfGlobalContext) out;
                        ThrowIfGlobalContext checker = throwIfGlobalContext(context, out, g.lineNumber);
                        // if (Primitives.isPrimitive(checker.c))
                        // return checker;

                        if (checker.c instanceof Keywords.LoopControl && checker.c == Keywords.LoopControl.BREAK)
                            break;
                        return checker;
                    }

                    cond = Primitives.setCondition(whileLoop, vfs, config);
                }
                // while loop
            } else if (token instanceof TIfStatement && !config.importVfs) {
                // if statement handling below
                TIfStatement ifStatement = (TIfStatement) token;
                if (!(ifStatement.condition instanceof TStatement))
                    throw new InterpreterException.WtfAreYouDoingException(
                            "Okay well idk how i will check for true in " + ifStatement,
                            token.lineNumber);
                Object cond = Primitives.setCondition(ifStatement, vfs, config);

                // if if, lol i love coding
                if (((Boolean) cond).booleanValue()) {
                    // run if code.
                    Object out = Interpreter.interpret(ifStatement.body.lines, Context.IF, vfs, config);
                    if (out instanceof ThrowIfGlobalContext) {
                        ThrowIfGlobalContext g = (ThrowIfGlobalContext) out;
                        ThrowIfGlobalContext checker = throwIfGlobalContext(context, out, g.lineNumber);
                        return checker;
                    }
                } else {
                    // check for else branches first
                    boolean runElseBlock = true;
                    for (Object e : ifStatement.elseIfs) {
                        TIfStatement elseIf = (TIfStatement) e;
                        if (!(elseIf.condition instanceof TStatement))
                            throw new InterpreterException.WtfAreYouDoingException(
                                    "Okay well idk how i will check for true in " + elseIf, token.lineNumber);
                        Object cond2 = Primitives.setCondition(elseIf, vfs, config);
                        if (((Boolean) cond2).booleanValue() == true) {
                            // run else if code.
                            Object out = Interpreter.interpret(elseIf.body.lines, Context.ELSE, vfs, config);
                            if (out instanceof ThrowIfGlobalContext) {
                                ThrowIfGlobalContext g = (ThrowIfGlobalContext) out;
                                ThrowIfGlobalContext checker = throwIfGlobalContext(context, out, g.lineNumber);
                                return checker;
                            }
                            runElseBlock = false;
                            break;
                        }
                    }
                    if (runElseBlock && ifStatement.elseBody != null) {
                        // run else block.
                        Object out = Interpreter.interpret(ifStatement.elseBody.lines, Context.ELSE, vfs, config);
                        if (out instanceof ThrowIfGlobalContext) {
                            ThrowIfGlobalContext g = (ThrowIfGlobalContext) out;
                            ThrowIfGlobalContext checker = throwIfGlobalContext(context, out, g.lineNumber);
                            return checker;
                        }
                    }
                }
                // if statement handling above
            } else if (token instanceof TForLoop && !config.importVfs) {
                // for loop
                TForLoop tForLoop = (TForLoop) token;
                handleVariables(tForLoop.variable, vfs, config);
                Object vObject = vfs.get(tForLoop.variable.name).getValue();
                if (!(vObject instanceof BaseVariable))
                    throw new InterpreterException.WtfAreYouDoingException(vObject, BaseVariable.class,
                            tForLoop.lineNumber);
                BaseVariable v = (BaseVariable) vObject;
                // if (!(v.s_get() instanceof Integer))
                // throw new WtfAreYouDoingException("");
                if (tForLoop.array != null && tForLoop.increment == null && tForLoop.condition == null) {
                    // for each
                    MapValue mapValue = vfs.get(tForLoop.array.varName);
                    if (mapValue == null || !((mapValue.getValue()) instanceof BaseVariable))
                        throw new InterpreterException.UnknownVariableException(tForLoop.array);
                    BaseVariable arr = (BaseVariable) mapValue.getValue();

                    if (arr.variableType != VariableType.ARRAY
                            && arr.variableType != VariableType.A_FUCKING_AMALGAMATION
                            && !(arr.s_get() instanceof String))
                        throw new InterpreterException.WtfAreYouDoingException(
                                "Ayo that variable argument on " + tForLoop.lineNumber + " gotta be an array.",
                                token.lineNumber);

                    List<Object> list = (arr.s_get() instanceof String)
                            ? Arrays.asList(((String) arr.s_get()).split(""))
                            : arr.a_getAll();

                    for (Object o : list) {
                        v.s_set(o);
                        Object out = Interpreter.interpret(tForLoop.body.lines, Context.FOR, vfs, config);
                        if (out instanceof ThrowIfGlobalContext) {
                            ThrowIfGlobalContext g = (ThrowIfGlobalContext) out;
                            // if (Primitives.isPrimitive(checker.c))
                            // return checker;

                            if (g.c instanceof Keywords.LoopControl
                                    && g.c == Keywords.LoopControl.BREAK)
                                break;
                            if (g.c instanceof Keywords.LoopControl
                                    && g.c == Keywords.LoopControl.CONTINUE)
                                continue;
                            ThrowIfGlobalContext checker = throwIfGlobalContext(context, out, g.lineNumber);
                            return checker;
                        }
                    }
                } else {
                    // normal for loop.
                    char increment = tForLoop.increment.charAt(0);
                    Object cond = Primitives.setCondition(tForLoop, vfs, config);
                    while (((Boolean) cond).booleanValue()) {

                        Object out = Interpreter.interpret(tForLoop.body.lines, Context.FOR, vfs, config);
                        if (out instanceof ThrowIfGlobalContext) {
                            ThrowIfGlobalContext g = (ThrowIfGlobalContext) out;
                            // if (Primitives.isPrimitive(checker.c))
                            // return checker;

                            if (g.c instanceof Keywords.LoopControl && g.c == Keywords.LoopControl.BREAK)
                                break;
                            if (g.c instanceof Keywords.LoopControl
                                    && g.c == Keywords.LoopControl.CONTINUE) {
                                v.s_set(((Integer) v.s_get()).intValue() + (increment == '+' ? 1 : -1));
                                cond = Primitives.setCondition(tForLoop, vfs, config);
                                continue;
                            }
                            ThrowIfGlobalContext checker = throwIfGlobalContext(context, out, g.lineNumber);
                            return checker;
                        }
                        v.s_set(((Integer) v.s_get()).intValue() + (increment == '+' ? 1 : -1));
                        cond = Primitives.setCondition(tForLoop, vfs, config);
                    }
                }

                vfs.remove(v.name);
                // for loop
            } else if (token instanceof TTryCatchStatement && !config.importVfs) {
                TTryCatchStatement throwError = (TTryCatchStatement) token;

                Object out = Interpreter.interpret(throwError.tryBlock.lines, Context.TRY, vfs, config);
                if (out instanceof ThrowIfGlobalContext) {
                    // if (Primitives.isPrimitive(checker.c))
                    // return checker;

                    if (((ThrowIfGlobalContext) out).c instanceof Keywords) {
                        ThrowIfGlobalContext g = (ThrowIfGlobalContext) out;
                        ThrowIfGlobalContext checker = throwIfGlobalContext(context, out, g.lineNumber);
                        return checker;
                    } else if (((ThrowIfGlobalContext) out).c instanceof TThrowError) {
                        Token<?> tContainer = new Token<>(null);
                        TThrowError err = (TThrowError) ((ThrowIfGlobalContext) out).c;
                        vfs.put("error",
                                new MapValue(BaseVariable.create("error",
                                        tContainer.new TStringVar("error", err.errorMessage,
                                                err.lineNumber),
                                        new ArrayList<>(Arrays.asList(
                                                err.errorMessage)),
                                        false)));
                        Object out2 = Interpreter.interpret(throwError.catchBlock.lines, Context.CATCH, vfs, config);
                        vfs.remove("error");
                        if (out2 instanceof ThrowIfGlobalContext) {
                            ThrowIfGlobalContext g2 = (ThrowIfGlobalContext) out2;
                            ThrowIfGlobalContext checker2 = throwIfGlobalContext(context, out2, g2.lineNumber);
                            return checker2;
                        }

                        return void.class;
                    }
                }
            }
            // return contextValue;
        }
        // System.out.println("heyy");

        return config.importVfs ? vfs : void.class;
    }
}
