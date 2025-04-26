package com.jaiva.interpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.jaiva.errors.IntErrs.*;
import com.jaiva.interpreter.symbol.BaseFunction;
import com.jaiva.interpreter.symbol.BaseVariable;
import com.jaiva.interpreter.symbol.BaseVariable.VariableType;
import com.jaiva.tokenizer.Keywords;
import com.jaiva.tokenizer.Token;
import com.jaiva.tokenizer.Token.TArrayVar;
import com.jaiva.tokenizer.Token.TBooleanVar;
import com.jaiva.tokenizer.Token.TForLoop;
import com.jaiva.tokenizer.Token.TFuncCall;
import com.jaiva.tokenizer.Token.TFuncReturn;
import com.jaiva.tokenizer.Token.TFunction;
import com.jaiva.tokenizer.Token.TIfStatement;
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

public class Interpreter {

    public static class ThrowIfGlobalContext {
        public int lineNumber = 0;
        public Object c;

        public ThrowIfGlobalContext(Object cObject, int ln) {
            c = cObject;
            lineNumber = ln;
        }
    }

    public static ThrowIfGlobalContext throwIfGlobalContext(Context context, Object lc, int lineNumber)
            throws WtfAreYouDoingException, CimaException {
        if (lc instanceof ThrowIfGlobalContext) {
            lc = ((ThrowIfGlobalContext) lc).c;
        }
        if (lc instanceof Keywords.LoopControl) {
            if (context == Context.GLOBAL)
                throw new WtfAreYouDoingException("So. You tried using "
                        + (lc.toString().equals("BREAK") ? Keywords.LC_BREAK : Keywords.LC_CONTINUE) + " on line "
                        + lineNumber
                        + ". But like, we're not in a loop yknow? ");
        } else if (Primitives.isPrimitive(lc)) {
            // a function return thing then
            if (context == Context.GLOBAL)
                throw new WtfAreYouDoingException(
                        "What are you trying to return out of on line " + lineNumber
                                + " if we're not in a function??");

        } else if (lc instanceof TThrowError) {
            if (context == Context.GLOBAL)
                throw new CimaException(((TThrowError) lc).errorMessage, lineNumber);
        }
        return new ThrowIfGlobalContext(lc, lineNumber);
    }

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
     * @param t   The token representing the variable or operation to handle. This
     *            can be
     *            one of several types, including TNumberVar, TBooleanVar,
     *            TStringVar,
     *            TUnknownVar, TArrayVar, or TVarReassign.
     * @param vfs A map storing variable names as keys and their corresponding
     *            MapValue
     *            objects as values. This map is updated based on the operation
     *            performed.
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
    public static Object handleVariables(Object t, HashMap<String, MapValue> vfs, GlobalResources resources)
            throws Exception {
        if (t instanceof TNumberVar) {
            Object number = Primitives.toPrimitive(((TNumberVar) t).value, vfs, false, resources);
            BaseVariable var = BaseVariable.create(((TokenDefault) t).name, (TokenDefault) t,
                    new ArrayList<>(Arrays.asList(number)), false);
            vfs.put(((TNumberVar) t).name, new MapValue(var));
        } else if (t instanceof TBooleanVar) {
            Object bool = Primitives.toPrimitive(((TBooleanVar) t).value, vfs, false, resources);
            BaseVariable var = BaseVariable.create(((TokenDefault) t).name, (TokenDefault) t,
                    new ArrayList<>(Arrays.asList(bool)), false);
            vfs.put(((TBooleanVar) t).name, new MapValue(var));
        } else if (t instanceof TStringVar) {
            Object string = Primitives.toPrimitive(((TStringVar) t).value, vfs, false, resources);
            BaseVariable var = BaseVariable.create(((TokenDefault) t).name, (TokenDefault) t,
                    new ArrayList<>(Arrays.asList(string)), false);
            vfs.put(((TStringVar) t).name, new MapValue(var));
        } else if (t instanceof TUnknownVar) {
            Object something = Primitives.toPrimitive(((TUnknownVar) t).value, vfs, false, resources);
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
                        arr.add(Primitives.toPrimitive(Primitives.parseNonPrimitive(obj), vfs, false, resources));
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
            if (MapValue.isEmpty(mapValue))
                throw new UnknownVariableException((TVarReassign) t);
            if (mapValue.getValue() == null || !(mapValue.getValue() instanceof BaseVariable))
                throw new WtfAreYouDoingException(((TVarReassign) t).name + " is not a variable.");

            BaseVariable var = (BaseVariable) mapValue.getValue();
            if (var.isFrozen)
                throw new FrozenSymbolException(var, ((TVarReassign) t).lineNumber);
            // if (!isVariableToken(((TVarReassign) t).newValue))
            // throw new WtfAreYouDoingException(
            // ((TVarReassign) t).newValue + " isn't like a valid var thingy yknow??");

            Object o = Primitives.toPrimitive(Primitives.parseNonPrimitive(((TVarReassign) t).newValue), vfs, false,
                    resources);

            if (o instanceof ArrayList) {
                var.a_set((ArrayList) o);
            } else {
                var.s_set(o);
            }

            // so hopefully this chanegs the instance and yeah 👍
        } else {
            // here its a primitive being parsed or recursively called
            // or TVarRef or TFuncCall or TStatement
            return Primitives.toPrimitive(t, vfs, false, resources);
        }

        return null; // return null because we are not returning anything.
    }

    // private static Object interpretWithoutContext(ArrayList<Token<?>> tokens,
    // HashMap<String, MapValue> vfs) {
    // return void.class;
    // }

    public static Object interpret(ArrayList<Token<?>> tokens, Context context, HashMap<String, MapValue> vfs,
            GlobalResources resources)
            throws Exception {
        resources = resources == null ? new GlobalResources() : resources;
        vfs = vfs != null ? ((HashMap<String, MapValue>) vfs.clone()) : vfs;

        // Step 2: go throguh eahc token
        for (Token<?> t : tokens) {
            TokenDefault token = t.getValue();
            if (isVariableToken(token)) {
                Object contextValue = null;
                // handles the following cases:
                // TNumberVar, TBooleanVar, TStringVar, TUnknownVar, TVarReassign, TArrayVar
                // including TStatement, TFuncCall and TVarRef. This also includes primitives.
                contextValue = token instanceof TFuncCall || token instanceof TVarRef
                        ? handleVariables(t, vfs, resources)
                        : handleVariables(token, vfs, resources);
                // If it returns a meaningful value, then oh well, because in this case they
                // basically called a function that returned soemthing but dont use that value.
            } else if (token instanceof TVoidValue) {
                // void
                continue;
            } else if (token instanceof TFuncReturn) {
                Object c = handleVariables(((TFuncReturn) token).value, vfs, resources);
                ThrowIfGlobalContext g = throwIfGlobalContext(context, c, token.lineNumber);
                return g;
            } else if (token instanceof TLoopControl) {
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

            } else if (token instanceof TThrowError) {
                TThrowError lc = (TThrowError) token;
                ThrowIfGlobalContext g = throwIfGlobalContext(context, lc, lc.lineNumber);
                return g;
            } else if (token instanceof TWhileLoop) {
                // while loop
                TWhileLoop whileLoop = (TWhileLoop) token;
                Object cond = Primitives.setCondition(whileLoop, vfs, resources);

                boolean terminate = false;

                while (((Boolean) cond).booleanValue() && !terminate) {
                    Object out = Interpreter.interpret(whileLoop.body.lines, Context.WHILE, vfs, resources);
                    if (out instanceof ThrowIfGlobalContext) {
                        ThrowIfGlobalContext g = (ThrowIfGlobalContext) out;
                        ThrowIfGlobalContext checker = throwIfGlobalContext(context, out, g.lineNumber);
                        // if (Primitives.isPrimitive(checker.c))
                        // return checker;

                        if (checker.c instanceof Keywords.LoopControl && checker.c == Keywords.LoopControl.BREAK)
                            break;
                        return checker;
                    }

                    cond = Primitives.setCondition(whileLoop, vfs, resources);
                }
                // while loop
            } else if (token instanceof TIfStatement) {
                // if statement handling below
                TIfStatement ifStatement = (TIfStatement) token;
                if (!(ifStatement.condition instanceof TStatement))
                    throw new WtfAreYouDoingException("Okay well idk how i will check for true in " + ifStatement);
                Object cond = Primitives.setCondition(ifStatement, vfs, resources);

                // if if, lol i love coding
                if (((Boolean) cond).booleanValue()) {
                    // run if code.
                    Object out = Interpreter.interpret(ifStatement.body.lines, Context.IF, vfs, resources);
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
                            throw new WtfAreYouDoingException(
                                    "Okay well idk how i will check for true in " + elseIf);
                        Object cond2 = Primitives.setCondition(elseIf, vfs, resources);
                        if (((Boolean) cond2).booleanValue() == true) {
                            // run else if code.
                            Object out = Interpreter.interpret(elseIf.body.lines, Context.ELSE, vfs, resources);
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
                        Object out = Interpreter.interpret(ifStatement.elseBody.lines, Context.ELSE, vfs, resources);
                        if (out instanceof ThrowIfGlobalContext) {
                            ThrowIfGlobalContext g = (ThrowIfGlobalContext) out;
                            ThrowIfGlobalContext checker = throwIfGlobalContext(context, out, g.lineNumber);
                            return checker;
                        }
                    }
                }
                // if statement handling above
            } else if (token instanceof TForLoop) {
                // for loop
                TForLoop tForLoop = (TForLoop) token;
                handleVariables(tForLoop.variable, vfs, resources);
                Object vObject = vfs.get(tForLoop.variable.name).getValue();
                if (!(vObject instanceof BaseVariable))
                    throw new WtfAreYouDoingException(vObject, BaseVariable.class, tForLoop.lineNumber);
                BaseVariable v = (BaseVariable) vObject;
                // if (!(v.s_get() instanceof Integer))
                // throw new WtfAreYouDoingException("");
                if (tForLoop.array != null && tForLoop.increment == null && tForLoop.condition == null) {
                    // for each
                    MapValue mapValue = vfs.get(tForLoop.array.varName);
                    if (mapValue == null || !((mapValue.getValue()) instanceof BaseVariable))
                        throw new UnknownVariableException(tForLoop.array);
                    BaseVariable arr = (BaseVariable) mapValue.getValue();

                    if (arr.variableType != VariableType.ARRAY
                            && arr.variableType != VariableType.A_FUCKING_AMALGAMATION
                            && !(arr.s_get() instanceof String))
                        throw new WtfAreYouDoingException(
                                "Ayo that variable argument on " + tForLoop.lineNumber + " gotta be an array.");

                    List<Object> list = (arr.s_get() instanceof String)
                            ? Arrays.asList(((String) arr.s_get()).split(""))
                            : arr.a_getAll();

                    for (Object o : list) {
                        v.s_set(o);
                        Object out = Interpreter.interpret(tForLoop.body.lines, Context.FOR, vfs, resources);
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
                    Object cond = Primitives.setCondition(tForLoop, vfs, resources);
                    while (((Boolean) cond).booleanValue()) {

                        Object out = Interpreter.interpret(tForLoop.body.lines, Context.FOR, vfs, resources);
                        if (out instanceof ThrowIfGlobalContext) {
                            ThrowIfGlobalContext g = (ThrowIfGlobalContext) out;
                            // if (Primitives.isPrimitive(checker.c))
                            // return checker;

                            if (g.c instanceof Keywords.LoopControl && g.c == Keywords.LoopControl.BREAK)
                                break;
                            if (g.c instanceof Keywords.LoopControl
                                    && g.c == Keywords.LoopControl.CONTINUE) {
                                v.s_set(((Integer) v.s_get()).intValue() + (increment == '+' ? 1 : -1));
                                cond = Primitives.setCondition(tForLoop, vfs, resources);
                                continue;
                            }
                            ThrowIfGlobalContext checker = throwIfGlobalContext(context, out, g.lineNumber);
                            return checker;
                        }
                        v.s_set(((Integer) v.s_get()).intValue() + (increment == '+' ? 1 : -1));
                        cond = Primitives.setCondition(tForLoop, vfs, resources);
                    }
                }

                vfs.remove(v.name);
                // for loop
            } else if (token instanceof TTryCatchStatement) {
                TTryCatchStatement throwError = (TTryCatchStatement) token;

                Object out = Interpreter.interpret(throwError.tryBlock.lines, Context.TRY, vfs, resources);
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
                        Object out2 = Interpreter.interpret(throwError.catchBlock.lines, Context.CATCH, vfs, resources);
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

        return void.class;
    }
}
