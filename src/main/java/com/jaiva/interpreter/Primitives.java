package com.jaiva.interpreter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.jaiva.errors.IntErrs.StringCalcException;
import com.jaiva.errors.IntErrs.TStatementResolutionException;
import com.jaiva.errors.IntErrs.UnknownVariableException;
import com.jaiva.errors.IntErrs.WeirdAhhFunctionException;
import com.jaiva.errors.IntErrs.WtfAreYouDoingException;
import com.jaiva.interpreter.symbol.BaseFunction;
import com.jaiva.interpreter.symbol.BaseVariable;
import com.jaiva.interpreter.symbol.BaseVariable.VariableType;
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

    public static Object resolveStringOperations(Object lhs, Object rhs, String op, TStatement ts)
            throws StringCalcException {
        String I = Integer.class.getSimpleName().substring(0, 1);
        String S = String.class.getSimpleName().substring(0, 1);
        String D = Double.class.getSimpleName().substring(0, 1);
        String B = Boolean.class.getSimpleName().substring(0, 1);

        String leftHandSide = lhs instanceof String ? S
                : lhs instanceof Integer ? I
                        : lhs instanceof Double ? D
                                : lhs instanceof Boolean ? B : "idk";

        String rightHandSide = rhs instanceof String ? S
                : rhs instanceof Integer ? I
                        : rhs instanceof Double ? D
                                : rhs instanceof Boolean ? B : "idk";
        String switchTing = leftHandSide + rightHandSide;

        ArrayList<String> IS = new ArrayList<>(Arrays.asList("+", "-", "*", "/", "=", "!="));
        ArrayList<String> SS = new ArrayList<>(Arrays.asList("+", "-", "=", "!="));

        try {
            switch (switchTing) {
                case "IS":
                    if (!IS.contains(op))
                        throw new StringCalcException(ts);
                    return op.equals("+") ? ((Integer) lhs) + ((String) rhs)
                            : op.equals("-") ? ((String) rhs).substring(
                                    ((Integer) lhs))
                                    : op.equals("*") ? ((String) rhs).repeat((Integer) lhs)
                                            : op.equals("/")
                                                    ? ((String) rhs)
                                                            .substring(((String) rhs).length() / ((Integer) lhs))
                                                    : op.equals("=") ? false : op.equals("!=") ? true : ((String) rhs);
                case "SI":
                    if (!IS.contains(op))
                        throw new StringCalcException(ts);
                    return op.equals("+") ? ((String) lhs) + ((Integer) rhs)
                            : op.equals("-") ? ((String) lhs).substring(0, ((String) lhs).length() - ((Integer) rhs))
                                    : op.equals("*") ? ((String) lhs).repeat((Integer) rhs)
                                            : op.equals("/")
                                                    ? ((String) lhs)
                                                            .substring(((String) lhs).length() / ((Integer) rhs))
                                                    : op.equals("=") ? false : op.equals("!=") ? true : ((String) lhs);
                case "SS":
                    if (!SS.contains(op))
                        throw new StringCalcException(ts);
                    return op.equals("+") ? (String) lhs + (String) rhs
                            : op.equals("-") ? ((String) lhs).replaceAll((String) rhs, "")
                                    : op.equals("=") ? ((String) lhs).equals((String) rhs)
                                            : op.equals("!=") ? !((String) lhs).equals((String) rhs) : ((String) lhs);
            }
        } catch (StringIndexOutOfBoundsException e) {
            // too big or too small of a number
            throw new StringCalcException(ts, e);
        } catch (IllegalArgumentException e) {
            // something received a negative number, when it shouldn't have.
            throw new StringCalcException(ts, e);
        }
        return void.class;
    }

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
    public static Object toPrimitive(Object token, HashMap<String, MapValue> vfs, boolean returnName)
            throws Exception {
        if (token instanceof Token<?> && ((Token<?>) token).getValue() instanceof TStatement) {
            // If the input is a TStatement, resolve the lhs and rhs.
            TStatement tStatement = (TStatement) ((Token<?>) token).getValue();
            Object lhs = toPrimitive(tStatement.lHandSide, vfs, false);
            String op = tStatement.op;
            Object rhs = toPrimitive(tStatement.rHandSide, vfs, false);

            Object sTuff = resolveStringOperations(lhs, rhs, op, tStatement);
            if (sTuff != void.class)
                return sTuff;

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
            if (returnName) {
                Object t = Primitives.toPrimitive(tVarRef.varName, vfs, returnName);
                if (t instanceof String) {
                    return t;
                }
            }
            MapValue v = vfs.get(tVarRef.varName instanceof Token<?>
                    ? Primitives.toPrimitive(tVarRef.varName, vfs, true)
                    : (tVarRef).varName);
            Object index = (tVarRef).index == null ? null : toPrimitive(tVarRef.index, vfs, false);
            if (index != null && (Integer) index <= -1)
                return new WtfAreYouDoingException("Now tell me, how do you access negative data in ana array?");
            if (v == null)
                throw new UnknownVariableException(tVarRef);
            if (!(v.getValue() instanceof BaseVariable)) {
                if (v.getValue() instanceof BaseFunction) {
                    if (index == null || !(index instanceof Integer))
                        return v.getValue();
                    // in this case, index is something so we need to call the function and it
                    // SHOULD return an array.
                    // therefore, we want to call toPrimitive on it again
                    // If we got BaseFunction, that means tVarRef.varName is a TFuncCall.
                    Object ret = toPrimitive(parseNonPrimitive(tVarRef.varName), vfs, false);
                    if (!(ret instanceof ArrayList))
                        throw new WtfAreYouDoingException("On line " + tVarRef.lineNumber
                                + " right, The function you used there did not return an array, and you expect to be able to index into that?");
                    return ((ArrayList) ret).get((Integer) index);
                } else {
                    throw new WtfAreYouDoingException(v.getValue(), BaseVariable.class, tVarRef.lineNumber);
                }
            }
            BaseVariable variable = (BaseVariable) v.getValue();
            if (index != null && (variable.variableType == VariableType.ARRAY
                    || variable.variableType == VariableType.A_FUCKING_AMALGAMATION
                    || tVarRef.varName instanceof TVarRef)) {
                // it's an array ref, where we have an index
                ArrayList<Object> arr = variable.a_getAll();
                if (!(index instanceof Integer) && index != null)
                    throw new WtfAreYouDoingException(tVarRef, tVarRef.getClass());
                if (tVarRef.varName instanceof Token) {
                    Object t = Primitives.toPrimitive(Primitives.parseNonPrimitive(tVarRef.varName), vfs, returnName);
                    if (t instanceof ArrayList) {
                        arr = (ArrayList) (t);
                    } else if (t instanceof String) {
                        return t;
                    }
                }
                if (arr.size() <= (Integer) index)
                    return new WtfAreYouDoingException(
                            "Bro you're tryna access more data than there is in " + variable.name);
                return arr.get((Integer) index);
            } else if (index == null && (variable.variableType == VariableType.ARRAY
                    || variable.variableType == VariableType.A_FUCKING_AMALGAMATION)) {
                // return the arraylist of variables. and hjope something up the chain catches
                // the array list.
                // thats what we assume to happen since they just like passed a reference yknow.
                return variable.a_getAll();
            } else {
                // normal variable ref, return it
                return variable.s_get();
            }

        } else if (token instanceof Token<?> && ((Token<?>) token).getValue() instanceof TFuncCall) {
            TFuncCall tFuncCall = (TFuncCall) ((Token<?>) token).getValue();
            if (returnName) {
                Object t = toPrimitive(parseNonPrimitive(tFuncCall.functionName), vfs, returnName);
                if (t instanceof String) {
                    return t;
                }
            }
            Object funcName = toPrimitive(tFuncCall.functionName instanceof Token
                    ? toPrimitive(parseNonPrimitive(tFuncCall.functionName), vfs, true)
                    : tFuncCall.functionName, vfs, false);
            if (!(funcName instanceof String))
                throw new WeirdAhhFunctionException(tFuncCall);
            String name = (String) funcName;
            MapValue v = vfs.get(name);
            if (v == null)
                throw new UnknownVariableException(tFuncCall);
            if (!(v.getValue() instanceof BaseFunction))
                throw new WtfAreYouDoingException(v.getValue(), BaseFunction.class, tFuncCall.lineNumber);
            BaseFunction function = (BaseFunction) v.getValue();
            if (tFuncCall.functionName instanceof Token) {
                Object t = Primitives.toPrimitive(Primitives.parseNonPrimitive(tFuncCall.functionName), vfs,
                        returnName);
                if (t instanceof BaseFunction) {
                    function = (BaseFunction) (t);
                } else if (t instanceof String) {
                    return t;
                }
            }
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

    /**
     * Checks if the given object is a primitive type supported by the interpreter.
     * The supported types are:
     * <ul>
     * <li>Boolean</li>
     * <li>Integer</li>
     * <li>Double</li>
     * <li>String</li>
     * </ul>
     *
     * @param t the object to check
     * @return {@code true} if the object is an instance of one of the supported
     *         primitive types; {@code false} otherwise
     */
    public static boolean isPrimitive(Object t) {
        return t instanceof Boolean || t instanceof Integer
                || t instanceof Double || t instanceof String;
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

}
