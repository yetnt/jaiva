package com.jaiva.interpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.jaiva.errors.*;
import com.jaiva.errors.InterpreterException.*;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.*;
import com.jaiva.interpreter.symbol.BaseVariable.VariableType;
import com.jaiva.lang.EscapeSequence;
import com.jaiva.tokenizer.jdoc.JDoc;
import com.jaiva.tokenizer.tokens.Token;
import com.jaiva.tokenizer.tokens.TokenDefault;
import com.jaiva.tokenizer.tokens.specific.*;
import com.jaiva.utils.CCol;

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
     * Checks if a given {@link Symbol} has a {@link SymbolConfig} annotation and prints
     * deprecation or experimental warnings if applicable.
     *
     * @param s The {@link Symbol} to check for annotations.
     */
    private static void checkSymbolAnnotation(Symbol s, int lineNumber, Scope scope) throws NoWarningsException {
        if (s == null) return;
        Class<?> actual = s.getClass();
        SymbolConfig c = actual.getAnnotation(SymbolConfig.class);
        TokenDefault token = s.token;
        JDoc doc = JDoc.from(token.tooltip);
        if (!Objects.isNull(c) || !Objects.isNull(doc)) {
            String depStr = Objects.isNull(doc) ? "" : doc.getDeprecatedString();
            if (!depStr.isEmpty() || (!Objects.isNull(c) && c.deprecated())) {
                Warnings.println(
                        lineNumber,
                        s.name + " is deprecated. " + CCol.printInline(depStr, CCol.FONT.BOLD, CCol.FONT.UNDERLINE, CCol.FONT.ITALIC),
                        scope
                );
            }
            if (!Objects.isNull(c) && c.experimental()) {
                Warnings.println(
                        lineNumber,
                        s.name + " is marked as experimental. Be careful!!",
                        scope
                );
            }
        }
    }

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
     * @param scope Context Trace
     * @return The result of the arithmetic operation, or a void value if operands
     *         are not numeric.
     * @throws InterpreterException If an invalid operator is provided or another
     *            interpreter error occurs.
     */
    private static Object handleNumOperations(String op, Object lhs, Object rhs, int lineNumber, Scope scope)
            throws InterpreterException {
        Object result = switch (lhs) {
            case Integer iLhs when rhs instanceof Integer iRhs ->
                // Because the ^ returns a double, we use 2 variables, so that if yopu dont use
                // the ^ operator you dont receive a double output.
                    switch (op) {
                        case "+" -> iLhs + iRhs;
                        case "-" -> iLhs - iRhs;
                        case "*" -> iLhs * iRhs;
                        case "/" -> iLhs / iRhs;
                        case "%" -> iLhs % iRhs;
                        case "^" -> Math.pow(iLhs, iRhs);
                        case "&" -> iLhs & iRhs;
                        case "|" -> iLhs | iRhs;
                        case "<<" -> // bitshift left
                                iLhs << iRhs;
                        case ">>" -> // bitshift right
                                iLhs >> iRhs;
                        case "<x" -> // hexshift left
                                iLhs << (iRhs * 4);
                        case ">x" -> // hexshift right
                                iLhs >> (iRhs * 4);
                        default -> throw new CatchAllException(scope, "Invalid operator given", lineNumber);
                    };
            case Double iLhs when rhs instanceof Double iRhs -> switch (op) {
                case "+" -> iLhs + iRhs;
                case "-" -> iLhs - iRhs;
                case "*" -> iLhs * iRhs;
                case "/" -> iLhs / iRhs;
                case "%" -> iLhs % iRhs;
                case "^" -> Math.pow(iLhs, iRhs);
                default -> throw new CatchAllException(scope, "Invalid operator given", lineNumber);
            };
            case Double iLhs when rhs instanceof Integer iRhs -> switch (op) {
                case "+" -> iLhs + iRhs;
                case "-" -> iLhs - iRhs;
                case "*" -> iLhs * iRhs;
                case "/" -> iLhs / iRhs;
                case "%" -> iLhs % iRhs;
                case "^" -> Math.pow(iLhs, iRhs);
                default -> throw new CatchAllException(scope, "Invalid operator given", lineNumber);
            };
            case Integer iLhs when rhs instanceof Double iRhs -> switch (op) {
                case "+" -> iLhs + iRhs;
                case "-" -> iLhs - iRhs;
                case "*" -> iLhs * iRhs;
                case "/" -> iLhs / iRhs;
                case "%" -> iLhs % iRhs;
                case "^" -> Math.pow(iLhs, iRhs);
                default -> throw new CatchAllException(scope, "Invalid operator given", lineNumber);
            };
            case null, default -> Token.voidValue(lineNumber);
        };

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
     * @param scope Context Trace.
     * @return The result of the string operation.
     * @throws JaivaException If an error occurs during
     *                        string calculation.
     */
    public static Object resolveStringOperations(Object lhs, Object rhs, String op, TStatement ts, Scope scope)
            throws JaivaException {
        String I = Integer.class.getSimpleName().charAt(0) + "";
        String S = String.class.getSimpleName().charAt(0) + "";
        String D = Double.class.getSimpleName().charAt(0) + "";
        String B = Boolean.class.getSimpleName().charAt(0) + "";

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
        ArrayList<String> SS = new ArrayList<>(Arrays.asList("+", "-", "=", "!=", "/", "?"));
        ArrayList<String> idk = new ArrayList<>(Arrays.asList("+", "=", "!="));

        try {
            switch (switchTing) {
                case "idkS":
                    // case "idkI":
                    // case "idkD":
                    // case "idkB":
                    if (!idk.contains(op))
                        throw new StringCalcException(scope, ts);
                    return op.equals("=") ? false : op.equals("!=") ? true : "idk" + rhs;
                case "Sidk":
                    // case "Iidk":
                    // case "Didk":
                    // case "Bidk":
                    if (!idk.contains(op))
                        throw new StringCalcException(scope, ts);
                    return op.equals("=") ? false : op.equals("!=") ? true : lhs + "idk";
                case "IS":
                    if (!IS.contains(op))
                        throw new StringCalcException(scope, ts);
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
                        throw new StringCalcException(scope, ts);
                    return op.equals("+") ? ((String) lhs) + ((Integer) rhs)
                            : op.equals("-") ? ((String) lhs).substring(0, ((String) lhs).length() - ((Integer) rhs))
                                    : op.equals("*") ? ((String) lhs).repeat((Integer) rhs)
                                            : op.equals("/")
                                                    ? ((String) lhs)
                                                            .substring(0, ((String) lhs).length() / ((Integer) rhs))
                                                    : op.equals("=") ? false : op.equals("!=") ? true : ((String) lhs);
                case "SS":
                    if (!SS.contains(op))
                        throw new StringCalcException(scope, ts);
                    return op.equals("+") ? (String) lhs + (String) rhs
                            : op.equals("-")
                                    ? ((String) lhs).replaceFirst(Pattern.quote((String) rhs),
                                            Matcher.quoteReplacement(""))
                                    : op.equals("/")
                                            ? ((String) lhs).replaceAll(Pattern.quote((String) rhs),
                                                    Matcher.quoteReplacement(""))
                                            : op.equals("=") ? ((String) lhs).equals((String) rhs)
                                                    : op.equals("!=") ? !((String) lhs).equals((String) rhs)
                                                            : op.equals("?") ? ((String) lhs).contains((String) rhs)
                                                                    : ((String) lhs);
            }
        } catch (StringIndexOutOfBoundsException e) {
            // too big or too small of a number
            throw new StringCalcException(scope, ts, e);
        } catch (IllegalArgumentException e) {
            // something received a negative number, when it shouldn't have.
            throw new StringCalcException(scope, ts, e);
        }
        return void.class;
    }

    /**
     * The bane of my existance
     * The method that turns scrambled TStatement, TFuncCall, TVarRef and primitives
     * into... primitives.
     * <p>
     * It essentially un
     * 
     * @param token the token or primitive in question
     * @param returnName Boolean flag which tells this method to only return the name of whatever you're trying to parse.
     * @param config Interpreter config
     * @param scope Context Trace
     * @return A primitive, which can be either a number, string, boolean, array, function reference or idk.
     * @throws UnknownVariableException      when the
     *                                       TVarRef cannot be
     *                                       found.
     * @throws TStatementResolutionException
     *                                       when
     *                                       one of the sides
     *                                       of a
     *                                       {@link TStatement}
     *                                       cannot be resolved
     *                                       to a
     *                                       primitive.
     * @throws WtfAreYouDoingException
     *                                       when you try
     *                                       use a function as
     *                                       a
     *                                       variable or a
     *                                       variable as a
     *                                       function.
     * @throws WeirdAhhFunctionException
     *                                       when the
     *                                       function name
     *                                       cannot be turned
     *                                       into a proper ting
     *                                       yknow
     */
    public static Object toPrimitive(Object token, boolean returnName,
                                     IConfig<Object> config, Scope scope)
            throws Exception {

        if (token instanceof Token<?> && ((Token<?>) token).value() instanceof TStatement tStatement) {
            // If the input is a TStatement, resolve the lhs and rhs.
            Object lhs = toPrimitive(tStatement.lHandSide, false, config, scope);
            String op = tStatement.op;
            Object rhs = toPrimitive(tStatement.rHandSide, false, config, scope);

            Object sTuff = resolveStringOperations(lhs, rhs, op, tStatement, scope);
            if (sTuff != void.class)
                return sTuff;

            // Check the input type, where input 1 is arithmatic, and 0 is boolean.
            if (tStatement.statementType == 1 || (op.equals("|") || op.equals("&"))) {
                // check input first of all
                if (!(lhs instanceof Integer) && !(lhs instanceof Double))
                    throw new TStatementResolutionException(scope,
                            tStatement, "left hand side",
                            lhs.toString());
                if (!(rhs instanceof Integer) && !(rhs instanceof Double))
                    throw new TStatementResolutionException(scope,
                            tStatement, "right hand side",
                            rhs.toString());

                // For the following if, thanks to the above condition
                // if one is an integer then the other is an integer too
                Object v = handleNumOperations(op, lhs, rhs, tStatement.lineNumber, scope);
                if (!(v instanceof TVoidValue)) {
                    return v;
                }
            } else {
                // In this else branch, the type is boolean logic
                switch (op) {
                    case "&&", "||", "'" -> {
                        // if the logic operator is one of these naturally, the input has to be boolean
                        // check input first of all
                        if (!(lhs instanceof Boolean))
                            throw new TStatementResolutionException(scope,
                                    tStatement, "left hand side",
                                    lhs.toString());
                        if (!op.equals("'") && !(rhs instanceof Boolean))
                            throw new TStatementResolutionException(scope,
                                    tStatement, "right hand side",
                                    rhs.toString());

                        switch (op) {
                            case "&&":
                                return ((Boolean) lhs) && ((Boolean) rhs);
                            case "||":
                                return ((Boolean) lhs) || ((Boolean) rhs);
                            case "'":
                                return !((Boolean) lhs);
                        }
                    }
                    case ">=", "<=", "<", ">" -> {
                        // however if its these the input is a number or a double
                        // check input first of all
                        if (!(lhs instanceof Integer) && !(lhs instanceof Double))
                            throw new TStatementResolutionException(scope,
                                    tStatement, "left hand side",
                                    lhs.toString());
                        if (!(rhs instanceof Integer) && !(rhs instanceof Double))
                            throw new TStatementResolutionException(scope,
                                    tStatement, "right hand side",
                                    rhs.toString());

                        if (lhs instanceof Integer) {
                            // handle ints
                            assert rhs instanceof Integer;
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
                            assert rhs instanceof Double;
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
                    }
                    case "=", "!=" -> {
                        // here, the inputs can be either a
                        // int, double, boolean or string.
                        // check input first of all
                        // TODO: This is literally just anything that can be input. Fix
                        if (!(lhs instanceof Integer) && !(lhs instanceof Double) && !(lhs instanceof Boolean)
                                && !(lhs instanceof String) && !(lhs instanceof TVoidValue) && !(lhs instanceof ArrayList)
                                && !(lhs instanceof BaseFunction))
                            throw new TStatementResolutionException(scope,
                                    tStatement, "left hand side",
                                    lhs.toString());
                        if (!(rhs instanceof Integer) && !(rhs instanceof Double) && !(rhs instanceof Boolean)
                                && !(rhs instanceof String) && !(rhs instanceof TVoidValue) && !(lhs instanceof ArrayList)
                                && !(lhs instanceof BaseFunction))
                            throw new TStatementResolutionException(scope,
                                    tStatement, "right hand side",
                                    rhs.toString());

                        // for TVoidValue, set to void.class
                        if (rhs instanceof TVoidValue)
                            rhs = void.class;
                        if (lhs instanceof TVoidValue)
                            lhs = void.class;

                        // handle ALL types.
                        switch (op) {
                            case "=":
                                return lhs.equals(rhs);
                            case "!=":
                                return !lhs.equals(rhs);
                        }
                    }
                }

            }

        } else if (token instanceof Token<?> && ((Token<?>) token).value() instanceof TVarRef tVarRef) {
            // just find the reference in the table and return whatever it is
            if (returnName) {
                Object t = toPrimitive(tVarRef.varName, returnName, config, scope);
                if (t instanceof String) {
                    return t;
                }
            }
            MapValue v = scope.vfs.get(tVarRef.varName instanceof Token<?>
                    ? toPrimitive(tVarRef.varName, true, config,
                            scope)
                    : (tVarRef).varName);
            Object index = (tVarRef).index == null ? null : toPrimitive(tVarRef.index, false, config, scope);
            if (index != null && (Integer) index <= -1)
                return new WtfAreYouDoingException(scope,
                        "Now tell me, how do you access negative data in ana array?",
                        tVarRef.lineNumber);
            if (v == null)
                throw new UnknownVariableException(scope, tVarRef);
            if (!(v.getValue() instanceof BaseVariable variable)) {
                if (v.getValue() instanceof BaseFunction) {
                    if (index == null)
                        return v.getValue();
                    // in this case, index is something so we need to call the function and it
                    // SHOULD return an array.
                    // therefore, we want to call toPrimitive on it again
                    // If we got BaseFunction, that means tVarRef.varName is a TFuncCall.
                    Object ret = toPrimitive(parseNonPrimitive(tVarRef.varName), false, config, scope);
                    if (!(ret instanceof ArrayList))
                        throw new WtfAreYouDoingException(scope,
                                "The function you used there did not return an array, and you expect to be able to index into that?",
                                tVarRef.lineNumber);
                    return ((ArrayList<?>) ret).get((Integer) index) instanceof ArrayList && tVarRef.getLength
                            ? ((ArrayList<?>) ((ArrayList<?>) ret).get((Integer) index)).size()
                            : ((ArrayList<?>) ret)
                                    .get((Integer) index);
                } else {
                    throw new WtfAreYouDoingException(scope, v.getValue(), BaseVariable.class,
                            tVarRef.lineNumber);
                }
            }
            checkSymbolAnnotation(variable, tVarRef.lineNumber, scope);
            if (index != null && (variable.variableType == VariableType.ARRAY
                    || variable.variableType == VariableType.A_FUCKING_AMALGAMATION
                    || tVarRef.varName instanceof TVarRef)) {
                // it's an array ref, where we have an index
                ArrayList<Object> arr = variable.a_getAll();
//                if (!(index instanceof Integer) && index != null)
//                    throw new WtfAreYouDoingException(cTrace,
//                            tVarRef, Integer.valueOf(0).getClass(),
//                            tVarRef.lineNumber);
                if (tVarRef.varName instanceof Token) {
                    Object t = Primitives.toPrimitive(Primitives.parseNonPrimitive(tVarRef.varName), returnName,
                            config, scope);
                    if (t instanceof ArrayList) {
                        arr = (ArrayList) (t);
                    } else if (t instanceof String) {
                        return t;
                    }
                }
                if (arr.size() <= (Integer) index)
                    return new WtfAreYouDoingException(scope,
                            "Bro you're tryna access more data than there is in " + variable.name, tVarRef.lineNumber);
                return arr.get((Integer) index) instanceof ArrayList && tVarRef.getLength
                        ? ((ArrayList<?>) arr.get((Integer) index)).size()
                        : arr
                                .get((Integer) index);
            } else if (index == null && (variable.variableType == VariableType.ARRAY
                    || variable.variableType == VariableType.A_FUCKING_AMALGAMATION)) {
                // return the arraylist of variables. and hope something up the chain catches
                // the array list.
                // thats what we assume to happen since they just like passed a reference yknow.
                return tVarRef.getLength ? variable.a_size() : variable.a_getAll();
            } else {
                // normal variable ref, return it
                try {
                    if (variable.s_get() instanceof String vs) {
                        return tVarRef.getLength ? vs.length()
                                : (index instanceof Integer newI) ? vs.charAt(newI) : variable.s_get();
                    } else {
                        return variable.s_get();
                    }
                } catch (IndexOutOfBoundsException e) {
                    // user gave an invalid index.
                    throw new WtfAreYouDoingException(scope,
                            "I don't think " + variable.name + " has a position " + index,
                            tVarRef.lineNumber);
                }
            }

        } else if (token instanceof Token<?> && ((Token<?>) token).value() instanceof TFuncCall tFuncCall) {
            if (returnName) {
                Object t = toPrimitive(parseNonPrimitive(tFuncCall.functionName), returnName, config, scope);
                if (t instanceof String) {
                    return t;
                }
            }
            Object funcName = toPrimitive(tFuncCall.functionName instanceof Token
                    ? toPrimitive(parseNonPrimitive(tFuncCall.functionName), true, config,
                            scope)
                    : tFuncCall.functionName, false, config, scope);

            if (!(funcName instanceof String name))
                throw new WeirdAhhFunctionException(scope, tFuncCall);
            BaseFunction f = null;
            if (!(tFuncCall.functionName instanceof String)) {
                Object j = toPrimitive(tFuncCall.functionName, false, config, scope);
                if (j instanceof BaseFunction) {
                    f = (BaseFunction) j;
                } else {
                    return j;
                }
            }
            MapValue v = scope.vfs.get(name);
            if (v == null)
                throw new UnknownVariableException(scope, tFuncCall);

            BaseFunction function = null;

            if (tFuncCall.functionName instanceof Token) {
                Object t = toPrimitive(parseNonPrimitive(tFuncCall.functionName),
                        returnName, config, scope);
                if (t instanceof BaseFunction) {
                    function = (BaseFunction) (t);
                } else if (t instanceof String) {
                    return t;
                }
            }
// This was above the above if, along with the bottom function reassignment
// commenting out this code fixed having arr[10](). Where since arr is an array, even if index 10 had a function the bottom if gets called.
//            if (!(v.getValue() instanceof BaseFunction)) {
//                throw new WtfAreYouDoingException(scope, v.getValue(), BaseFunction.class, tFuncCall.lineNumber);
//            }

            function = function != null ? function : f == null ? (BaseFunction) v.getValue() : f;

            checkSymbolAnnotation(function, tFuncCall.lineNumber, scope);

            Object returnValue = function.call(tFuncCall, tFuncCall.args, config, scope);
            return returnValue instanceof String && tFuncCall.getLength
                    ? EscapeSequence.fromEscape((String) returnValue, tFuncCall.lineNumber).length()
                    : returnValue instanceof ArrayList && tFuncCall.getLength ? ((ArrayList<?>) returnValue).size()
                            : returnValue;
        } else if (token instanceof Token<?> && ((Token<?>) token).value() instanceof TTernary ternary) {
            // parse the condition.
            Object condition = setCondition(ternary, config, scope);

            if ((Boolean) condition)
                return toPrimitive(ternary.trueExpr, false, config, scope);
            else
                return toPrimitive(ternary.falseExpr, false, config, scope);
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
                        : t instanceof TFuncCall ? ((TFuncCall) t).toToken()
                                : t instanceof TTernary ? ((TTernary) t).toToken() : t;
    }

    /**
     * Evaluates and sets the condition for a `TForLoop`, `TWhileLoop`,
     * `TIfStatement` and `TTernary` statement.
     *
     * @param t   The Object containing the condition to be evaluated.
     * @param config THe interpreter configuration instance
     * @param scope Scope
     * @return The evaluated condition as an `Object`. The returned value is
     *         expected
     *         to be a `Boolean`.
     * @throws Exception If the condition cannot be resolved to a `Boolean` or if
     *                   there is an error during variable handling or parsing.
     */
    public static Object setCondition(TokenDefault t, IConfig<Object> config,
            Scope scope)
            throws Exception {
        Object c = t instanceof TForLoop ? ((TForLoop) t).condition
                : t instanceof TWhileLoop ? ((TWhileLoop) t).condition
                        : t instanceof TIfStatement ? ((TIfStatement) t).condition
                                : t instanceof TTernary ? ((TTernary) t).condition : null;
        Object condition = Interpreter.handleVariables(
                parseNonPrimitive(c), config, scope);
        if (!(condition instanceof Boolean)) {
            assert condition != null;
            assert c != null;
            throw new TStatementResolutionException(scope,
                    t, ((TStatement) c),
                    "boolean", condition.getClass().getName());
        }

        return condition;
    }

}
