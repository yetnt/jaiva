package com.jaiva.interpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.jaiva.errors.IntErrs.*;
import com.jaiva.interpreter.symbol.BaseVariable;
import com.jaiva.tokenizer.Keywords;
import com.jaiva.tokenizer.Token;
import com.jaiva.tokenizer.Token.TArrayVar;
import com.jaiva.tokenizer.Token.TBooleanVar;
import com.jaiva.tokenizer.Token.TFuncCall;
import com.jaiva.tokenizer.Token.TFuncReturn;
import com.jaiva.tokenizer.Token.TIfStatement;
import com.jaiva.tokenizer.Token.TLoopControl;
import com.jaiva.tokenizer.Token.TNumberVar;
import com.jaiva.tokenizer.Token.TStatement;
import com.jaiva.tokenizer.Token.TStringVar;
import com.jaiva.tokenizer.Token.TUnknownVar;
import com.jaiva.tokenizer.Token.TVarReassign;
import com.jaiva.tokenizer.Token.TVarRef;
import com.jaiva.tokenizer.Token.TVoidValue;
import com.jaiva.tokenizer.Token.TWhileLoop;
import com.jaiva.utils.Validate;
import com.jaiva.tokenizer.TokenDefault;
import com.jaiva.tokenizer.Keywords.LoopControl;

public class Interpreter {

    public static boolean isPrimitive(Object t) {
        return t instanceof Boolean || t instanceof Integer
                || t instanceof Double || t instanceof String;
    }

    public static boolean isVariableToken(Object t) {
        return t instanceof TStatement || t instanceof TNumberVar || t instanceof TBooleanVar || t instanceof TArrayVar
                || t instanceof TStringVar
                || t instanceof TUnknownVar || t instanceof TVarReassign || t instanceof TVarRef
                || t instanceof TFuncCall || t instanceof TStatement || isPrimitive(t);
    }

    /**
     * Parses a non-primitive object and converts it to a token representation if
     * applicable.
     * 
     * @param t The object to be parsed. It can be an instance of TStatement,
     *          TVarRef, TFuncCall,
     *          or any other type.
     * @return If the object is an instance of TStatement, TVarRef, or TFuncCall, it
     *         returns the
     *         result of their respective `toToken()` method. Otherwise, it returns
     *         the object itself.
     */
    public static Object parseNonPrimitive(Object t) {
        return t instanceof TStatement
                ? ((TStatement) t).toToken()
                : t instanceof TVarRef ? ((TVarRef) t).toToken()
                        : t instanceof TFuncCall ? ((TFuncCall) t).toToken()
                                : t;
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
    public static Object handleVariables(Object t, HashMap<String, MapValue> vfs)
            throws Exception {
        if (t instanceof TNumberVar) {
            Object number = Primitives.toPrimitive(((TNumberVar) t).value, vfs);
            BaseVariable var = BaseVariable.create(((TokenDefault) t).name, (TokenDefault) t,
                    new ArrayList<>(Arrays.asList(number)));
            vfs.put(((TNumberVar) t).name, new MapValue(var));
        } else if (t instanceof TBooleanVar) {
            Object bool = Primitives.toPrimitive(((TBooleanVar) t).value, vfs);
            BaseVariable var = BaseVariable.create(((TokenDefault) t).name, (TokenDefault) t,
                    new ArrayList<>(Arrays.asList(bool)));
            vfs.put(((TBooleanVar) t).name, new MapValue(var));
        } else if (t instanceof TStringVar) {
            BaseVariable var = BaseVariable.create(((TokenDefault) t).name, (TokenDefault) t,
                    new ArrayList<>(Arrays.asList(((TStringVar) t).value)));
            vfs.put(((TStringVar) t).name, new MapValue(var));
        } else if (t instanceof TUnknownVar) {
            BaseVariable var = BaseVariable.create(((TokenDefault) t).name, (TokenDefault) t,
                    new ArrayList<>(Arrays.asList(((TUnknownVar) t).value)));
            vfs.put(((TUnknownVar) t).name, new MapValue(var));
        } else if (t instanceof TArrayVar) {
            BaseVariable var = BaseVariable.create(((TokenDefault) t).name, (TokenDefault) t,
                    new ArrayList<Object>(((TArrayVar) t).contents));
            vfs.put(((TArrayVar) t).name, new MapValue(var));
        } else if (t instanceof TVarReassign) {
            // TODO: Handle array reassignment. like arr[0] <- 1 or arr[2][3][4] <- 1
            MapValue mapValue = vfs.get(((TVarReassign) t).name);
            if (MapValue.isEmpty(mapValue))
                throw new UnknownVariableException((TVarReassign) t);
            if (mapValue.getValue() == null || !(mapValue.getValue() instanceof BaseVariable))
                throw new WtfAreYouDoingException(((TVarReassign) t).name + " is not a variable.");

            BaseVariable var = (BaseVariable) mapValue.getValue();
            if (var.isFrozen)
                throw new FrozenSymbolException(var);
            // if (!isVariableToken(((TVarReassign) t).newValue))
            // throw new WtfAreYouDoingException(
            // ((TVarReassign) t).newValue + " isn't like a valid var thingy yknow??");

            var.setScalar(Primitives.toPrimitive(((TVarReassign) t).newValue, vfs));
            // so hopefully this chanegs the instance and yeah 👍
        } else {
            // here its a primitive being parsed or recursively called
            // or TVarRef or TFuncCall or TStatement
            return Primitives.toPrimitive(t, vfs);
        }

        return null; // return null because we are not returning anything.
    }

    // private static Object interpretWithoutContext(ArrayList<Token<?>> tokens,
    // HashMap<String, MapValue> vfs) {
    // return void.class;
    // }

    public static Object interpret(ArrayList<Token<?>> tokens, Context context, HashMap<String, MapValue> vfs)
            throws Exception {
        // The idea is that this method is context aware
        // and it will call another method which isnt context aware that will just
        // execute the tokens.
        // So this method will go through each token via a for loop, and depending on
        // waht context it is
        // will do it
        // So if it were a "if statement" it will try evaulte the condition, (after it
        // gets parsed to actual runnable values)
        // then yeah whatevs, i'll see along the way thats the fun part.

        // Step 1: Define our own vfs in this new context.
        // Unless vfs is undefined we will just use it as is.

        vfs = vfs != null ? ((HashMap<String, MapValue>) vfs.clone()) : vfs;

        // Step 2: go throguh eahc token
        for (Token<?> t : tokens) {
            TokenDefault token = t.getValue();
            if (isVariableToken(token)) {
                Object contextValue = null;
                // handles the following cases:
                // TNumberVar, TBooleanVar, TStringVar, TUnknownVar, TVarReassign, TArrayVar
                // including TStatement, TFuncCall and TVarRef. This also includes primitives.
                contextValue = token instanceof TFuncCall || token instanceof TVarRef ? handleVariables(t, vfs)
                        : handleVariables(token, vfs);
                // If it returns a meaningful value, then oh well, because in this case they
                // basically called a function that returned soemthing but dont use that value.
            } else if (token instanceof TFuncReturn) {
                if (context != Context.FUNCTION)
                    throw new WtfAreYouDoingException(
                            "What are you trying to return out of if we're not in a function??");
                return handleVariables(((TFuncReturn) token).value, vfs);
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

                return loopControl.type;

            } else if (token instanceof TVoidValue) {
                // void
                continue;
            } else if (token instanceof TWhileLoop) {
                // while loop
                TWhileLoop whileLoop = (TWhileLoop) token;
                Object cond = handleVariables(parseNonPrimitive(whileLoop.condition), vfs);
                if (!(cond instanceof Boolean))
                    throw new TStatementResolutionException(
                            whileLoop, ((TStatement) whileLoop.condition),
                            "boolean", cond.getClass().getName());

                boolean terminate = false;

                while (((Boolean) cond).booleanValue() && !terminate) {
                    Object out = Interpreter.interpret(whileLoop.body.lines, Context.WHILE, vfs);
                    if (out instanceof Keywords.LoopControl && (Keywords.LoopControl) out == Keywords.LoopControl.BREAK)
                        break;

                    // they may re assign it midway, so lets check for dat fr
                    // TODO: comeacvkl

                    cond = handleVariables(parseNonPrimitive(whileLoop.condition), vfs);
                    if (!(cond instanceof Boolean))
                        throw new TStatementResolutionException(
                                whileLoop, ((TStatement) whileLoop.condition),
                                "boolean", cond.getClass().getName());
                }
                // while loop
            } else if (token instanceof TIfStatement) {
                // if statement handling below
                TIfStatement ifStatement = (TIfStatement) token;
                if (!(ifStatement.condition instanceof TStatement))
                    throw new WtfAreYouDoingException("Okay well idk how i will check for true in " + ifStatement);
                Object cond = handleVariables(parseNonPrimitive(ifStatement.condition), vfs);
                if (!(cond instanceof Boolean))
                    throw new TStatementResolutionException(ifStatement, ((TStatement) ifStatement.condition),
                            "boolean", cond.getClass().getName());

                // if if, lol i love coding
                if (((Boolean) cond).booleanValue()) {
                    // run if code.
                    Object out = Interpreter.interpret(ifStatement.body.lines, Context.IF, vfs);
                    if (out instanceof Keywords.LoopControl || isPrimitive(out))
                        return out;
                } else {
                    // check for else branches first
                    boolean runElseBlock = true;
                    for (Object e : ifStatement.elseIfs) {
                        TIfStatement elseIf = (TIfStatement) e;
                        if (!(elseIf.condition instanceof TStatement))
                            throw new WtfAreYouDoingException(
                                    "Okay well idk how i will check for true in " + elseIf);
                        Object cond2 = handleVariables(
                                parseNonPrimitive(elseIf.condition), vfs);
                        if (!(cond2 instanceof Boolean))
                            throw new TStatementResolutionException(
                                    elseIf, ((TStatement) elseIf.condition),
                                    "boolean", cond2.getClass().getName());
                        if (((Boolean) cond2).booleanValue() == true) {
                            // run else if code.
                            Object out = Interpreter.interpret(elseIf.body.lines, Context.ELSE, vfs);
                            if (out instanceof Keywords.LoopControl || isPrimitive(out))
                                return out;
                            runElseBlock = false;
                            break;
                        }
                    }
                    if (runElseBlock && ifStatement.elseBody != null) {
                        // run else block.
                        Object out = Interpreter.interpret(ifStatement.elseBody.lines, Context.ELSE, vfs);
                        if (out instanceof Keywords.LoopControl || isPrimitive(out))
                            return out;
                    }
                }
                // if statement handling above
            }
            // return contextValue;
        }
        // System.out.println("heyy");

        return void.class;
    }
}
