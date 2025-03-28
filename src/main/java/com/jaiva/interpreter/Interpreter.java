package com.jaiva.interpreter;

import java.util.ArrayList;
import java.util.HashMap;

import com.jaiva.tokenizer.Token;
import com.jaiva.tokenizer.Token.TArrayVar;
import com.jaiva.tokenizer.Token.TBooleanVar;
import com.jaiva.tokenizer.Token.TIntVar;
import com.jaiva.tokenizer.Token.TStatement;
import com.jaiva.tokenizer.Token.TStringVar;
import com.jaiva.tokenizer.Token.TUnknownVar;
import com.jaiva.tokenizer.Token.TVarReassign;
import com.jaiva.tokenizer.TokenDefault;

public class Interpreter {

    /**
     * The bane of my existance
     * The method that turns scrambled TStatement, TFuncCall, TVarRef and primitives
     * into... primitives.
     * 
     * @param t
     * @return
     */
    private static Object toPrimitive(Object t, HashMap<String, MapValue> vfs) {
        if (t instanceof TStatement) {
            t = (TStatement) t;
            Object lhs = ((TStatement) t).lHandSide;
            String op = ((TStatement) t).op;
            Object rhs = ((TStatement) t).rHandSide;

        } else {
            // its not a token so its def jus a primitive.
            try {
                // its an int
                return Integer.parseInt((String) t);
            } catch (NumberFormatException e) {
                // bool?
            }
        }
        return void.class;
    }

    private static boolean isVariableToken(TokenDefault t) {
        return t instanceof TIntVar || t instanceof TBooleanVar || t instanceof TArrayVar || t instanceof TStringVar
                || t instanceof TUnknownVar || t instanceof TVarReassign;
    }

    private static void handleVariables(TokenDefault t, HashMap<String, MapValue> vfs) {
        if (t instanceof TIntVar) {
            vfs.put(((TIntVar) t).name, new MapValue(((TIntVar) t).value));
        } else if (t instanceof TBooleanVar) {
            vfs.put(((TBooleanVar) t).name, new MapValue(((TBooleanVar) t).value));
        }
    }

    private static Object interpretWithoutContext(ArrayList<Token<?>> tokens, HashMap<String, MapValue> vfs) {
        return void.class;
    }

    public static void interpret(ArrayList<Token<?>> tokens, Object context, HashMap<String, MapValue> vfs) {
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
                handleVariables(token, vfs);
            }
        }
    }
}
