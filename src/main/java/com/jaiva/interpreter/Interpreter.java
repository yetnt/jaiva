package com.jaiva.interpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.jaiva.errors.IntErrs.*;
import com.jaiva.interpreter.symbol.BaseVariable;
import com.jaiva.tokenizer.Token;
import com.jaiva.tokenizer.Token.TArrayVar;
import com.jaiva.tokenizer.Token.TBooleanVar;
import com.jaiva.tokenizer.Token.TFuncCall;
import com.jaiva.tokenizer.Token.TNumberVar;
import com.jaiva.tokenizer.Token.TStatement;
import com.jaiva.tokenizer.Token.TStringVar;
import com.jaiva.tokenizer.Token.TUnknownVar;
import com.jaiva.tokenizer.Token.TVarReassign;
import com.jaiva.tokenizer.Token.TVarRef;
import com.jaiva.tokenizer.Token.TVoidValue;
import com.jaiva.utils.Validate;
import com.jaiva.tokenizer.TokenDefault;

public class Interpreter {

    private static boolean isVariableToken(Object t) {
        return t instanceof TNumberVar || t instanceof TBooleanVar || t instanceof TArrayVar || t instanceof TStringVar
                || t instanceof TUnknownVar || t instanceof TVarReassign || t instanceof TVarRef
                || t instanceof TFuncCall || t instanceof TStatement || t instanceof Boolean || t instanceof Integer
                || t instanceof Double || t instanceof String;
    }

    private static Object handleVariables(TokenDefault t, HashMap<String, MapValue> vfs)
            throws Exception {
        if (t instanceof TNumberVar) {
            Object number = Primitives.toPrimitive(((TNumberVar) t).value, vfs);
            BaseVariable var = BaseVariable.create(t.name, t, new ArrayList<>(Arrays.asList(number)));
            vfs.put(((TNumberVar) t).name, new MapValue(var));
        } else if (t instanceof TBooleanVar) {
            Object bool = Primitives.toPrimitive(((TBooleanVar) t).value, vfs);
            BaseVariable var = BaseVariable.create(t.name, t, new ArrayList<>(Arrays.asList(bool)));
            vfs.put(((TBooleanVar) t).name, new MapValue(var));
        } else if (t instanceof TStringVar) {
            BaseVariable var = BaseVariable.create(t.name, t, new ArrayList<>(Arrays.asList(((TStringVar) t).value)));
            vfs.put(((TStringVar) t).name, new MapValue(var));
        } else if (t instanceof TUnknownVar) {
            BaseVariable var = BaseVariable.create(t.name, t, new ArrayList<>(Arrays.asList(((TUnknownVar) t).value)));
            vfs.put(((TUnknownVar) t).name, new MapValue(var));
        } else if (t instanceof TArrayVar) {
            BaseVariable var = BaseVariable.create(t.name, t, new ArrayList<Object>(((TArrayVar) t).contents));
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

    private static Object interpretWithoutContext(ArrayList<Token<?>> tokens, HashMap<String, MapValue> vfs) {
        return void.class;
    }

    public static Object interpret(ArrayList<Token<?>> tokens, Object context, HashMap<String, MapValue> vfs)
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

        Object contextValue = null;

        // Step 2: go throguh eahc token
        for (Token<?> t : tokens) {
            TokenDefault token = t.getValue();
            if (isVariableToken(token)) {
                // handles the following cases:
                // TNumberVar, TBooleanVar, TStringVar, TUnknownVar, TVarReassign, TArrayVar
                // including TStatement, TFuncCall and TVarRef. This also includes primitives.
                contextValue = handleVariables(token, vfs);
            } else if (token instanceof TVoidValue) {
                // void
                System.out.println("dsfsdf");
                continue;
            }
            // return contextValue;
        }
        System.out.println("heyy");

        return void.class;
    }
}
