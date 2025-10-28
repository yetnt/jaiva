package com.jaiva.tokenizer.tokens.specific;

import com.jaiva.errors.JaivaException;
import com.jaiva.tokenizer.tokens.TAtomicValue;
import com.jaiva.tokenizer.tokens.Token;
import com.jaiva.tokenizer.tokens.TokenDefault;

import java.util.ArrayList;

/**
 * Represents a function call such as {@code func1(10, 20)} or {@code func2()}
 * or {@code func3(10, 20) -> ... <~} or {@code func4(10, 20)!}. Any, if not ALL
 * function calls are possible.
 */
public class TFuncCall extends TokenDefault<TFuncCall> implements TAtomicValue {
    /**
     * The name of the function being called.
     * <p>
     * This is an object due to the fact that it might itself be a TVarRef which
     * retrns a function, or another function that returns a function or any other
     * case like that.
     */
    public Object functionName;
    /**
     * The arguments of the function call.
     * <p>
     * This is an arraylist of objects which can be a TStatement, TFuncCall,
     * TVarRef, or a primitive type.
     */
    public ArrayList<Object> args; // can be a TStatement, TFuncCall, TVarRef, or a primitive type
    /**
     * Indicates whether the function call is a length call.
     */
    public boolean getLength = false;

    /**
     * Constructor for TFuncCall
     *
     * @param name The name of the function being called.
     * @param args The arguments of the function call.
     * @param ln   The line number.
     */
    TFuncCall(Object name, ArrayList<Object> args, int ln) {
        super("TFuncCall", ln);
        this.functionName = name;
        this.args = args;
    }

    /**
     * Constructor for TFuncCall
     *
     * @param name The name of the function being called.
     * @param args The arguments of the function call.
     * @param ln   The line number.
     * @param getL Indicates whether the function call is a length call.
     */
    public TFuncCall(Object name, ArrayList<Object> args, int ln, boolean getL) {
        super("TFuncCall", ln);
        this.functionName = name;
        this.args = args;
        this.getLength = getL;
    }

    @Override
    public String toJson() throws JaivaException {
        json.append("functionName", functionName, false);
        json.append("getLength", getLength, false);
        json.append("args", args, true);
        return super.toJson();
    }

    /**
     * Converts this token to the default {@link Token}
     *
     * @return {@link Token} with a T value of {@link TFuncCall}
     */
    public Token<TFuncCall> toToken() {
        return new Token<>(this);
    }
}
