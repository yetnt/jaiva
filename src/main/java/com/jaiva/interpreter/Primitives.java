package com.jaiva.interpreter;

import java.util.HashMap;

import com.jaiva.errors.IntErrs.TStatementResolutionException;
import com.jaiva.errors.IntErrs.UnknownVariableException;
import com.jaiva.errors.IntErrs.WeirdAhhFunctionException;
import com.jaiva.errors.IntErrs.WtfAreYouDoingException;
import com.jaiva.interpreter.symbol.BaseFunction;
import com.jaiva.interpreter.symbol.BaseVariable;
import com.jaiva.tokenizer.EscapeSequence;
import com.jaiva.tokenizer.Token;
import com.jaiva.tokenizer.Token.TFuncCall;
import com.jaiva.tokenizer.Token.TStatement;
import com.jaiva.tokenizer.Token.TVarRef;

/**
 * Class to han dle primitives.
 * The name may change.
 */
public class Primitives {

    /**
     * The bane of my existance
     * The method that turns scrambled TStatement, TFuncCall, TVarRef and primitives
     * into... primitives.
     * <p>
     * This throws {@link UnknownVariableException} when the TVarRef cannot be
     * found.
     * <p>
     * This throws {@link TStatementResolutionException} when one of the sides of a
     * {@link TStatement} cannot be resolved to a primitive.
     * <p>
     * This throws {@link WtfAreYouDoingException} when you try use a function as a
     * variable or a variable as a function.
     * <p>
     * This throws {@link WeirdAhhFunctionException} when the function name cannot
     * be a string.
     * 
     * @param token the token or primitive in question
     * @param vfs   The variable functions store
     * @return
     * @throws Exception
     */
    public static Object toPrimitive(Object token, HashMap<String, MapValue> vfs)
            throws Exception {
        if (token instanceof Token<?> && ((Token<?>) token).getValue() instanceof TStatement) {
            // If the input is a TStatement, resolve the lhs and rhs.
            TStatement tStatement = (TStatement) ((Token<?>) token).getValue();
            Object lhs = toPrimitive(tStatement.lHandSide, vfs);
            String op = tStatement.op;
            Object rhs = toPrimitive(tStatement.rHandSide, vfs);

            // Check the input type, where input 1 is arithmatic, and 0 is boolean.
            if (tStatement.statementType == 1) {
                // check input first of all
                if (!(lhs instanceof Integer) && !(lhs instanceof Double))
                    throw new TStatementResolutionException(tStatement, "lhs", lhs.toString());
                if (!(rhs instanceof Integer) && !(rhs instanceof Double))
                    throw new TStatementResolutionException(tStatement, "rhs", rhs.toString());

                // For the following if, thanks to the above condition
                // if one is an integer then the other is an integer too
                if (lhs instanceof Integer) {
                    // Because the ^ returns a double, we use 2 variables, so that if yopu dont use
                    // the ^ operator you dont receive a double output.
                    switch (op) {
                        case "+":
                            return ((Integer) lhs) + ((Integer) rhs);
                        case "-":
                            return ((Integer) lhs) - ((Integer) rhs);
                        case "*":
                            return ((Integer) lhs) * ((Integer) rhs);
                        case "/":
                            return ((Integer) lhs) / ((Integer) rhs);
                        case "%":
                            return ((Integer) lhs) % ((Integer) rhs);
                        case "^":
                            return Math.pow(((Integer) lhs), ((Integer) rhs));

                    }
                } else {
                    switch (op) {
                        case "+":
                            return ((Double) lhs) + ((Double) rhs);
                        case "-":
                            return ((Double) lhs) - ((Double) rhs);
                        case "*":
                            return ((Double) lhs) * ((Double) rhs);
                        case "/":
                            return ((Double) lhs) / ((Double) rhs);
                        case "%":
                            return ((Double) lhs) % ((Double) rhs);
                        case "^":
                            return Math.pow(((Double) lhs), ((Double) rhs));

                    }
                }
            } else {
                // In this else branch, the type is boolean logic
                if (op.equals("&&") || op.equals("&") || op.equals("||") || op.equals("|")) {
                    // if the logic operator is one of these naturally, the input has to be boolean
                    // check input first of all
                    if (!(lhs instanceof Boolean))
                        throw new TStatementResolutionException(tStatement, "lhs", lhs.toString());
                    if (!(rhs instanceof Boolean))
                        throw new TStatementResolutionException(tStatement, "rhs", rhs.toString());

                    switch (op) {
                        case "&&", "&":
                            return ((Boolean) lhs) && ((Boolean) rhs);
                        case "||", "|":
                            return ((Boolean) lhs) || ((Boolean) rhs);
                    }
                } else if (op.equals(">=") || op.equals("<=") || op.equals("<") || op.equals(">")) {
                    // however if its these the input is a number or a double
                    // check input first of all
                    if (!(lhs instanceof Integer) && !(lhs instanceof Double))
                        throw new TStatementResolutionException(tStatement, "lhs", lhs.toString());
                    if (!(rhs instanceof Integer) && !(rhs instanceof Double))
                        throw new TStatementResolutionException(tStatement, "rhs", rhs.toString());

                    if (lhs instanceof Integer) {
                        // handle ints
                        switch (op) {
                            case ">=":
                                return ((Integer) lhs) >= ((Integer) rhs);
                            case "<=":
                                return ((Integer) lhs) <= ((Integer) rhs);
                            case "<":
                                return ((Integer) lhs) < ((Integer) rhs);
                            case ">":
                                return ((Integer) lhs) > ((Integer) rhs);
                        }
                    } else {
                        // handle doubles
                        switch (op) {
                            case ">=":
                                return ((Double) lhs) >= ((Double) rhs);
                            case "<=":
                                return ((Double) lhs) <= ((Double) rhs);
                            case "<":
                                return ((Double) lhs) < ((Double) rhs);
                            case ">":
                                return ((Double) lhs) > ((Double) rhs);
                        }
                    }

                } else if (op.equals("=") || op.equals("!=")) {
                    // here, the inputs can be either a
                    // int, double, boolean or string.
                    // check input first of all
                    if (!(lhs instanceof Integer) && !(lhs instanceof Double) && !(lhs instanceof Boolean)
                            && !(lhs instanceof String))
                        throw new TStatementResolutionException(tStatement, "lhs", lhs.toString());
                    if (!(rhs instanceof Integer) && !(rhs instanceof Double) && !(rhs instanceof Boolean)
                            && !(rhs instanceof String))
                        throw new TStatementResolutionException(tStatement, "rhs", rhs.toString());

                    if (lhs instanceof Integer) {
                        // handle ints
                        switch (op) {
                            case "=":
                                return ((Integer) lhs).equals((Integer) rhs);
                            case "!=":
                                return !((Integer) lhs).equals((Integer) rhs);
                        }
                    } else if (lhs instanceof Double) {
                        // handle doubles
                        switch (op) {
                            case "=":
                                return ((Double) lhs).equals((Double) rhs);
                            case "!=":
                                return !((Double) lhs).equals((Double) rhs);
                        }
                    } else if (lhs instanceof Boolean) {
                        // handle booleans
                        switch (op) {
                            case "=":
                                return ((Boolean) lhs).equals((Boolean) rhs);
                            case "!=":
                                return !((Boolean) lhs).equals((Boolean) rhs);
                        }
                    } else if (lhs instanceof String) {
                        // handle strings
                        switch (op) {
                            case "=":
                                return ((String) lhs).equals((String) rhs);
                            case "!=":
                                return !((String) lhs).equals((String) rhs);
                        }
                    }
                }

            }

        } else if (token instanceof Token<?> && ((Token<?>) token).getValue() instanceof TVarRef) {
            // just find the reference in the table and return whatever it is
            TVarRef tVarRef = (TVarRef) ((Token<?>) token).getValue();
            MapValue v = vfs.get((tVarRef).varName);
            if (v == null)
                throw new UnknownVariableException(tVarRef);
            if (!(v.getValue() instanceof BaseVariable))
                throw new WtfAreYouDoingException(v.getValue(), BaseVariable.class);
            BaseVariable variable = (BaseVariable) v.getValue();
            Object index = (tVarRef).index == null ? null : toPrimitive(tVarRef.index, vfs);
            if (index != null) {
                // it's an array ref, where we have an index
                if (!(index instanceof Integer) && index != null)
                    throw new WtfAreYouDoingException(tVarRef, tVarRef.getClass());
                if ((Integer) index <= -1)
                    return new WtfAreYouDoingException("Now tell me, how do you access negative data in ana array?");
                if (variable.array.size() <= (Integer) index)
                    return new WtfAreYouDoingException(
                            "Bro you're tryna access more data than there is in " + variable.name);
                return variable.array.get((Integer) index);
            } else {
                // normal variable ref, return it
                return variable.getScalar();
            }

        } else if (token instanceof Token<?> && ((Token<?>) token).getValue() instanceof TFuncCall) {
            TFuncCall tFuncCall = (TFuncCall) ((Token<?>) token).getValue();
            Object funcName = toPrimitive(tFuncCall.functionName, vfs);
            if (!(funcName instanceof String))
                throw new WeirdAhhFunctionException(tFuncCall);
            String name = (String) funcName;
            MapValue v = vfs.get(name);
            if (v == null)
                throw new UnknownVariableException(tFuncCall);
            if (!(v.getValue() instanceof BaseFunction))
                throw new WtfAreYouDoingException(v.getValue(), BaseFunction.class);
            BaseFunction function = (BaseFunction) v.getValue();
            Object returnValue = function.call(tFuncCall, tFuncCall.args, vfs);
            return returnValue instanceof String ? EscapeSequence.escape((String) returnValue) : returnValue;
            // MapValue mapValue = vfs.get();
        } else if (token instanceof Integer || token instanceof Double || token instanceof Boolean
                || token instanceof String) {
            // its not a token so its def jus a primitive, so we wanna parse it as a
            // primitive.
            // also for the above recursive call where it may already be a primitive.
            return token;
        }
        return void.class;
    }

}
