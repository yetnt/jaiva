package com.jaiva.interpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.jaiva.errors.InterpreterException;
import com.jaiva.errors.JaivaException;
import com.jaiva.errors.InterpreterException.CatchAllException;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.BaseFunction;
import com.jaiva.interpreter.symbol.BaseVariable;
import com.jaiva.interpreter.symbol.BaseVariable.VariableType;
import com.jaiva.lang.EscapeSequence;
import com.jaiva.tokenizer.Token;
import com.jaiva.tokenizer.Token.TForLoop;
import com.jaiva.tokenizer.Token.TFuncCall;
import com.jaiva.tokenizer.Token.TIfStatement;
import com.jaiva.tokenizer.Token.TStatement;
import com.jaiva.tokenizer.Token.TVarRef;
import com.jaiva.tokenizer.Token.TVoidValue;
import com.jaiva.tokenizer.Token.TWhileLoop;

/**
 * The Primitives class is a utility class that provides methods for resolving
 * string operations, converting tokens to primitives, and evaluating conditions
 * for various statements in the Jaiva programming language.
 * <p>
 * It includes methods for handling arithmetic and boolean operations, as well
 * as
 * parsing and evaluating conditions in loops and if statements.
 * <p>
 */
public class Primitives {

    /**
     * Handles arithmetic operations between two numeric operands (Integer or
     * Double).
     * Supports the following operators: "+", "-", "*", "/", "%", and "^".
     * The method performs type checking and applies the operation according to the
     * types of the operands.
     * If both operands are integers, integer arithmetic is used except for "^"
     * (power), which returns a double.
     * If either operand is a double, the operation is performed in double
     * precision.
     * Throws a CatchAllException for invalid operators.
     * Returns a void value if operands are not numeric.
     *
     * @param op         The arithmetic operator as a string ("+", "-", "*", "/",
     *                   "%", "^").
     * @param lhs        The left-hand side operand (Integer or Double).
     * @param rhs        The right-hand side operand (Integer or Double).
     * @param lineNumber The line number for error reporting.
     * @return The result of the arithmetic operation, or a void value if operands
     *         are not numeric.
     * @throws InterpreterException If an invalid operator is provided or another
     *                              interpreter error occurs.
     */
    private static Object handleNumOperations(String op, Object lhs, Object rhs, int lineNumber)
            throws InterpreterException {
        Object result;
        if (lhs instanceof Integer iLhs && rhs instanceof Integer iRhs) {
            // Because the ^ returns a double, we use 2 variables, so that if yopu dont use
            // the ^ operator you dont receive a double output.
            switch (op) {
                case "+":
                    result = iLhs + iRhs;
                    break;
                case "-":
                    result = iLhs - iRhs;
                    break;
                case "*":
                    result = iLhs * iRhs;
                    break;
                case "/":
                    result = iLhs / iRhs;
                    break;
                case "%":
                    result = iLhs % iRhs;
                    break;
                case "^":
                    result = Math.pow(iLhs, iRhs);
                    break;
                default:
                    throw new CatchAllException("Invalid operator given", lineNumber);
            }
        } else if (lhs instanceof Double iLhs && rhs instanceof Double iRhs) {
            switch (op) {
                case "+":
                    result = iLhs + iRhs;
                    break;
                case "-":
                    result = iLhs - iRhs;
                    break;
                case "*":
                    result = iLhs * iRhs;
                    break;
                case "/":
                    result = iLhs / iRhs;
                    break;
                case "%":
                    result = iLhs % iRhs;
                    break;
                case "^":
                    result = Math.pow(iLhs, iRhs);
                    break;
                default:
                    throw new CatchAllException("Invalid operator given", lineNumber);

            }
        } else if (lhs instanceof Double iLhs && rhs instanceof Integer iRhs) {
            switch (op) {
                case "+":
                    result = iLhs + iRhs;
                    break;
                case "-":
                    result = iLhs - iRhs;
                    break;
                case "*":
                    result = iLhs * iRhs;
                    break;
                case "/":
                    result = iLhs / iRhs;
                    break;
                case "%":
                    result = iLhs % iRhs;
                    break;
                case "^":
                    result = Math.pow(iLhs, iRhs);
                    break;
                default:
                    throw new CatchAllException("Invalid operator given", lineNumber);

            }
        } else if (lhs instanceof Integer iLhs && rhs instanceof Double iRhs) {
            switch (op) {
                case "+":
                    result = iLhs + iRhs;
                    break;
                case "-":
                    result = iLhs - iRhs;
                    break;
                case "*":
                    result = iLhs * iRhs;
                    break;
                case "/":
                    result = iLhs / iRhs;
                    break;
                case "%":
                    result = iLhs % iRhs;
                    break;
                case "^":
                    result = Math.pow(iLhs, iRhs);
                    break;
                default:
                    throw new CatchAllException("Invalid operator given", lineNumber);

            }
        } else {
            result = Token.voidValue(lineNumber);
        }

        // Return int if the double is whole.
        if (result instanceof Double res) {
            double d = res;
            if (d == Math.rint(d)) {
                return (int) d;
            }
        }

        return result;
    }

    /**
     * Resolves string operations between two operands (lhs and rhs) based on the
     * specified operator (op).
     * <p>
     * This method handles various string operations, including concatenation,
     * substring extraction, and comparison.
     *
     * @param lhs The left-hand side operand.
     * @param rhs The right-hand side operand.
     * @param op  The operator to be applied.
     * @param ts  The TStatement object associated with the operation.
     * @return The result of the string operation.
     * @throws JaivaException If an error occurs during
     *                        string calculation.
     */
    public static Object resolveStringOperations(Object lhs, Object rhs, String op, TStatement ts)
            throws JaivaException {
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
        if (rhs instanceof String) {
            rhs = EscapeSequence.fromEscape((String) rhs, ts.lineNumber);
        }
        if (lhs instanceof String) {
            lhs = EscapeSequence.fromEscape((String) lhs, ts.lineNumber);
        }
        String switchTing = leftHandSide + rightHandSide;

        ArrayList<String> IS = new ArrayList<>(Arrays.asList("+", "-", "*", "/", "=", "!="));
        ArrayList<String> SS = new ArrayList<>(Arrays.asList("+", "-", "=", "!=", "/"));

        try {
            switch (switchTing) {
                case "IS":
                    if (!IS.contains(op))
                        throw new InterpreterException.StringCalcException(ts);
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
                        throw new InterpreterException.StringCalcException(ts);
                    return op.equals("+") ? ((String) lhs) + ((Integer) rhs)
                            : op.equals("-") ? ((String) lhs).substring(0, ((String) lhs).length() - ((Integer) rhs))
                                    : op.equals("*") ? ((String) lhs).repeat((Integer) rhs)
                                            : op.equals("/")
                                                    ? ((String) lhs)
                                                            .substring(0, ((String) lhs).length() / ((Integer) rhs))
                                                    : op.equals("=") ? false : op.equals("!=") ? true : ((String) lhs);
                case "SS":
                    if (!SS.contains(op))
                        throw new InterpreterException.StringCalcException(ts);
                    return op.equals("+") ? (String) lhs + (String) rhs
                            : op.equals("-")
                                    ? ((String) lhs).replace(Pattern.quote((String) rhs), Matcher.quoteReplacement(""))
                                    : op.equals("/")
                                            ? ((String) lhs).replaceAll(Pattern.quote((String) rhs),
                                                    Matcher.quoteReplacement(""))
                                            : op.equals("=") ? ((String) lhs).equals((String) rhs)
                                                    : op.equals("!=") ? !((String) lhs).equals((String) rhs)
                                                            : ((String) lhs);
            }
        } catch (StringIndexOutOfBoundsException e) {
            // too big or too small of a number
            throw new InterpreterException.StringCalcException(ts, e);
        } catch (IllegalArgumentException e) {
            // something received a negative number, when it shouldn't have.
            throw new InterpreterException.StringCalcException(ts, e);
        }
        return void.class;
    }

    /**
     * The bane of my existance
     * The method that turns scrambled TStatement, TFuncCall, TVarRef and primitives
     * into... primitives.
     * <p>
     * This throws {@link InterpreterException.UnknownVariableException} when the
     * TVarRef cannot be
     * found.
     * <p>
     * This throws {@link InterpreterException.TStatementResolutionException} when
     * one of the sides of a
     * {@link TStatement} cannot be resolved to a primitive.
     * <p>
     * This throws {@link InterpreterException.WtfAreYouDoingException} when you try
     * use a function as a
     * variable or a variable as a function.
     * <p>
     * This throws {@link InterpreterException.WeirdAhhFunctionException} when the
     * function name cannot
     * be a string.
     * 
     * @param token the token or primitive in question
     * @param vfs   The variable functions store
     * @return
     * @throws Exception
     */
    public static Object toPrimitive(Object token, HashMap<String, MapValue> vfs, boolean returnName,
            IConfig config)
            throws Exception {
        if (token instanceof Token<?> && ((Token<?>) token).getValue() instanceof TStatement) {
            // If the input is a TStatement, resolve the lhs and rhs.
            TStatement tStatement = (TStatement) ((Token<?>) token).getValue();
            Object lhs = toPrimitive(tStatement.lHandSide, vfs, false, config);
            String op = tStatement.op;
            Object rhs = toPrimitive(tStatement.rHandSide, vfs, false, config);

            Object sTuff = resolveStringOperations(lhs, rhs, op, tStatement);
            if (sTuff != void.class)
                return sTuff;

            // Check the input type, where input 1 is arithmatic, and 0 is boolean.
            if (tStatement.statementType == 1) {
                // check input first of all
                if (!(lhs instanceof Integer) && !(lhs instanceof Double))
                    throw new InterpreterException.TStatementResolutionException(tStatement, "left hand side",
                            lhs.toString());
                if (!(rhs instanceof Integer) && !(rhs instanceof Double))
                    throw new InterpreterException.TStatementResolutionException(tStatement, "right hand side",
                            rhs.toString());

                // For the following if, thanks to the above condition
                // if one is an integer then the other is an integer too
                Object v = handleNumOperations(op, lhs, rhs, tStatement.lineNumber);
                if (!(v instanceof TVoidValue)) {
                    return v;
                }
            } else {
                // In this else branch, the type is boolean logic
                if (op.equals("&&") || op.equals("&") || op.equals("||") || op.equals("|")) {
                    // if the logic operator is one of these naturally, the input has to be boolean
                    // check input first of all
                    if (!(lhs instanceof Boolean))
                        throw new InterpreterException.TStatementResolutionException(tStatement, "left hand side",
                                lhs.toString());
                    if (!(rhs instanceof Boolean))
                        throw new InterpreterException.TStatementResolutionException(tStatement, "right hand side",
                                rhs.toString());

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
                        throw new InterpreterException.TStatementResolutionException(tStatement, "left hand side",
                                lhs.toString());
                    if (!(rhs instanceof Integer) && !(rhs instanceof Double))
                        throw new InterpreterException.TStatementResolutionException(tStatement, "right hand side",
                                rhs.toString());

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
                            && !(lhs instanceof String) && !(lhs instanceof TVoidValue))
                        throw new InterpreterException.TStatementResolutionException(tStatement, "left hand side",
                                lhs.toString());
                    if (!(rhs instanceof Integer) && !(rhs instanceof Double) && !(rhs instanceof Boolean)
                            && !(rhs instanceof String) && !(rhs instanceof TVoidValue))
                        throw new InterpreterException.TStatementResolutionException(tStatement, "right hand side",
                                rhs.toString());

                    // for TVoidValue, set to void.class
                    if (rhs instanceof TVoidValue)
                        rhs = void.class;
                    if (lhs instanceof TVoidValue)
                        lhs = void.class;

                    // handle ints
                    switch (op) {
                        case "=":
                            return lhs.equals(rhs);
                        case "!=":
                            return !lhs.equals(rhs);
                    }
                }

            }

        } else if (token instanceof Token<?> && ((Token<?>) token).getValue() instanceof TVarRef) {
            // just find the reference in the table and return whatever it is
            TVarRef tVarRef = (TVarRef) ((Token<?>) token).getValue();
            if (returnName) {
                Object t = Primitives.toPrimitive(tVarRef.varName, vfs, returnName, config);
                if (t instanceof String) {
                    return t;
                }
            }
            if (tVarRef.varName instanceof String)
                tVarRef.varName = ((String) tVarRef.varName).replace("~", "");
            MapValue v = vfs.get(tVarRef.varName instanceof Token<?>
                    ? Primitives.toPrimitive(tVarRef.varName, vfs, true, config)
                    : (tVarRef).varName);
            Object index = (tVarRef).index == null ? null : toPrimitive(tVarRef.index, vfs, false, config);
            if (index != null && (Integer) index <= -1)
                return new InterpreterException.WtfAreYouDoingException(
                        "Now tell me, how do you access negative data in ana array?",
                        tVarRef.lineNumber);
            if (v == null)
                throw new InterpreterException.UnknownVariableException(tVarRef);
            if (!(v.getValue() instanceof BaseVariable)) {
                if (v.getValue() instanceof BaseFunction) {
                    if (index == null || !(index instanceof Integer))
                        return v.getValue();
                    // in this case, index is something so we need to call the function and it
                    // SHOULD return an array.
                    // therefore, we want to call toPrimitive on it again
                    // If we got BaseFunction, that means tVarRef.varName is a TFuncCall.
                    Object ret = toPrimitive(parseNonPrimitive(tVarRef.varName), vfs, false, config);
                    if (!(ret instanceof ArrayList))
                        throw new InterpreterException.WtfAreYouDoingException(
                                "The function you used there did not return an array, and you expect to be able to index into that?",
                                tVarRef.lineNumber);
                    return ((ArrayList) ret).get((Integer) index) instanceof ArrayList && tVarRef.getLength
                            ? ((ArrayList) ((ArrayList) ret).get((Integer) index)).size()
                            : ((ArrayList) ret)
                                    .get((Integer) index);
                } else {
                    throw new InterpreterException.WtfAreYouDoingException(v.getValue(), BaseVariable.class,
                            tVarRef.lineNumber);
                }
            }
            BaseVariable variable = (BaseVariable) v.getValue();
            if (index != null && (variable.variableType == VariableType.ARRAY
                    || variable.variableType == VariableType.A_FUCKING_AMALGAMATION
                    || tVarRef.varName instanceof TVarRef)) {
                // it's an array ref, where we have an index
                ArrayList<Object> arr = variable.a_getAll();
                if (!(index instanceof Integer) && index != null)
                    throw new InterpreterException.WtfAreYouDoingException(tVarRef, Integer.valueOf(0).getClass(),
                            tVarRef.lineNumber);
                if (tVarRef.varName instanceof Token) {
                    Object t = Primitives.toPrimitive(Primitives.parseNonPrimitive(tVarRef.varName), vfs, returnName,
                            config);
                    if (t instanceof ArrayList) {
                        arr = (ArrayList) (t);
                    } else if (t instanceof String) {
                        return t;
                    }
                }
                if (arr.size() <= (Integer) index)
                    return new InterpreterException.WtfAreYouDoingException(
                            "Bro you're tryna access more data than there is in " + variable.name, tVarRef.lineNumber);
                return arr.get((Integer) index) instanceof ArrayList && tVarRef.getLength
                        ? ((ArrayList) arr.get((Integer) index)).size()
                        : arr
                                .get((Integer) index);
            } else if (index == null && (variable.variableType == VariableType.ARRAY
                    || variable.variableType == VariableType.A_FUCKING_AMALGAMATION)) {
                // return the arraylist of variables. and hjope something up the chain catches
                // the array list.
                // thats what we assume to happen since they just like passed a reference yknow.
                return tVarRef.getLength ? variable.a_size() : variable.a_getAll();
            } else {
                // normal variable ref, return it
                return variable.s_get() instanceof String && tVarRef.getLength ? ((String) variable
                        .s_get()).length() : variable.s_get();
            }

        } else if (token instanceof Token<?> && ((Token<?>) token).getValue() instanceof TFuncCall) {
            TFuncCall tFuncCall = (TFuncCall) ((Token<?>) token).getValue();
            if (returnName) {
                Object t = toPrimitive(parseNonPrimitive(tFuncCall.functionName), vfs, returnName, config);
                if (t instanceof String) {
                    return t;
                }
            }
            Object funcName = toPrimitive(tFuncCall.functionName instanceof Token
                    ? toPrimitive(parseNonPrimitive(tFuncCall.functionName), vfs, true, config)
                    : tFuncCall.functionName, vfs, false, config);

            if (!(funcName instanceof String))
                throw new InterpreterException.WeirdAhhFunctionException(tFuncCall);
            String name = (String) funcName;
            BaseFunction f = null;
            if (!(tFuncCall.functionName instanceof String)) {
                Object j = toPrimitive(tFuncCall.functionName, vfs, false, config);
                if (j instanceof BaseFunction) {
                    f = (BaseFunction) j;
                } else {
                    return j;
                }
            }
            MapValue v = vfs.get(name);
            if (v == null)
                throw new InterpreterException.UnknownVariableException(tFuncCall);
            // if (!(v.getValue() instanceof BaseFunction)) {
            // if (v.getValue() instanceof BaseVariable) {
            // ret = toPrimitive(parseNonPrimitive(name), vfs, false);
            // if (!(ret instanceof BaseFunction) && !(name instanceof String)) {
            // return ret;
            // }
            // } else {
            // throw new WtfAreYouDoingException(v.getValue(), BaseFunction.class,
            // tFuncCall.lineNumber);
            // }
            // }
            BaseFunction function = f == null ? (BaseFunction) v.getValue() : f; /*
                                                                                  * ret != null && ret instanceof
                                                                                  * BaseFunction ?
                                                                                  * (BaseFunction) ret
                                                                                  * :
                                                                                  */

            if (tFuncCall.functionName instanceof Token) {
                Object t = Primitives.toPrimitive(Primitives.parseNonPrimitive(tFuncCall.functionName), vfs,
                        returnName, config);
                if (t instanceof BaseFunction) {
                    function = (BaseFunction) (t);
                } else if (t instanceof String) {
                    return t;
                }
            }
            Object returnValue = function.call(tFuncCall, tFuncCall.args, vfs, config);
            return returnValue instanceof String && tFuncCall.getLength
                    ? EscapeSequence.fromEscape((String) returnValue, tFuncCall.lineNumber).length()
                    : returnValue instanceof ArrayList && tFuncCall.getLength ? ((ArrayList) returnValue).size()
                            : returnValue;
            // MapValue mapValue = vfs.get();
        } else if (token instanceof Integer || token instanceof Double || token instanceof Boolean
                || token instanceof String) {
            // its not a token so its def jus a primitive, so we wanna parse it as a
            // primitive.
            // also for the above recursive call where it may already be a primitive.
            if (token instanceof String) {
                return EscapeSequence.fromEscape((String) token, -1);
            }
            // Return int if the double is whole.
            if (token instanceof Double) {
                double d = (Double) token;
                if (d == Math.rint(d)) {
                    return (int) d;
                }
            }
            return token;
        } else if (token instanceof TVoidValue) {
            return (TVoidValue) token; // just return as is.
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
                        : t instanceof TFuncCall ? ((TFuncCall) t).toToken() : t;
    }

    /**
     * Evaluates and sets the condition for a `TForLoop` statement.
     *
     * @param t   The `TForLoop` object containing the condition to be evaluated.
     * @param vfs A `HashMap` representing the variable frame stack, where variable
     *            names are mapped to their corresponding `MapValue` objects.
     * @return The evaluated condition as an `Object`. The returned value is
     *         expected
     *         to be a `Boolean`.
     * @throws Exception If the condition cannot be resolved to a `Boolean` or if
     *                   there is an error during variable handling or parsing.
     */
    public static Object setCondition(TForLoop t, HashMap<String, MapValue> vfs, IConfig config)
            throws Exception {
        Object condition = Interpreter.handleVariables(
                parseNonPrimitive(t.condition), vfs, config);
        if (!(condition instanceof Boolean))
            throw new InterpreterException.TStatementResolutionException(
                    t, ((TStatement) t.condition),
                    "boolean", condition.getClass().getName());

        return condition;
    }

    /**
     * Evaluates and sets the condition for a TWhileLoop statement.
     *
     * @param t   The TWhileLoop object containing the condition to be evaluated.
     * @param vfs A HashMap representing the variable function scope, where variable
     *            names are mapped to their corresponding MapValue objects.
     * @return The evaluated condition as an Object, which must be a Boolean.
     * @throws Exception If the condition cannot be resolved to a Boolean or if
     *                   there is an error during variable handling or parsing.
     */
    public static Object setCondition(TWhileLoop t, HashMap<String, MapValue> vfs, IConfig config)
            throws Exception {
        Object condition = Interpreter.handleVariables(
                parseNonPrimitive(t.condition), vfs, config);
        if (!(condition instanceof Boolean))
            throw new InterpreterException.TStatementResolutionException(
                    t, ((TStatement) t.condition),
                    "boolean", condition.getClass().getName());

        return condition;
    }

    /**
     * Evaluates and sets the condition for an `if` statement by parsing and
     * resolving
     * the provided condition expression. Ensures that the condition evaluates to a
     * boolean value.
     *
     * @param t   The `TIfStatement` object containing the condition to be
     *            evaluated.
     * @param vfs A map of variable names to their corresponding `MapValue` objects,
     *            used for resolving variables in the condition.
     * @return The evaluated condition as a boolean object.
     * @throws Exception If the condition cannot be resolved to a boolean or if any
     *                   error occurs during variable handling or parsing.
     */
    public static Object setCondition(TIfStatement t, HashMap<String, MapValue> vfs,
            IConfig config)
            throws Exception {
        Object condition = Interpreter.handleVariables(
                parseNonPrimitive(t.condition), vfs, config);
        if (!(condition instanceof Boolean))
            throw new InterpreterException.TStatementResolutionException(
                    t, ((TStatement) t.condition),
                    "boolean", condition.getClass().getName());

        return condition;
    }

}
