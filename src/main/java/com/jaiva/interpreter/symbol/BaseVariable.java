package com.jaiva.interpreter.symbol;

import java.util.ArrayList;
import java.util.List;

import com.jaiva.errors.IntErrs.FrozenSymbolException;
import com.jaiva.tokenizer.Token.TVarRef;
import com.jaiva.tokenizer.Token;
import com.jaiva.tokenizer.TokenDefault;

public class BaseVariable extends Symbol {
    private Object scalar;
    // we just give access to the array because why not.
    private ArrayList<Object> array = new ArrayList<>();
    public VariableType variableType;

    public void updateVariableType(VariableType newType) {
        if (variableType == VariableType.UNKNOWN) {
            variableType = newType;
        } else if ((variableType == VariableType.SCALAR && newType == VariableType.ARRAY)
                || (variableType == VariableType.ARRAY && newType == VariableType.SCALAR)) {
            variableType = VariableType.A_FUCKING_AMALGAMATION;
        }
    }

    public BaseVariable(String name, TokenDefault t, Object scalar) {
        super(SymbolType.VARIABLE, t);
        this.scalar = scalar;
        variableType = VariableType.SCALAR;
    }

    public BaseVariable(String name, TokenDefault t, ArrayList<Object> arr) {
        super(SymbolType.VARIABLE, t);
        this.array.addAll(arr);
        variableType = VariableType.ARRAY;
    }

    public BaseVariable(String name, TokenDefault t) {
        super(SymbolType.VARIABLE, t);
        variableType = VariableType.UNKNOWN;
    }

    public BaseVariable(String name) {
        super(SymbolType.VARIABLE);
        variableType = VariableType.UNKNOWN;
    }

    /**
     * Adds an array to the array (addAll())
     * 
     * @param a
     * @throws FrozenSymbolException
     */
    public void a_addAll(List<String> a) throws FrozenSymbolException {
        if (isFrozen)
            throw new FrozenSymbolException(this);
        array.addAll(a);
    }

    /**
     * Appends an element to the array.
     * 
     * @param a
     * @throws FrozenSymbolException
     */
    public void a_add(Object a) throws FrozenSymbolException {
        if (isFrozen)
            throw new FrozenSymbolException(this);
        array.add(a);
    }

    public ArrayList<Object> a_getAll() {
        return array;
    }

    /**
     * Gets an element from the array.
     * 
     * @param i
     * @return
     */
    public Object a_get(int i) {
        return array.get(i);
    }

    /**
     * Get array size
     * 
     * @return
     */
    public int a_size() {
        return array.size();
    }

    /**
     * Set scalar value
     * 
     * @param value
     * @throws FrozenSymbolException
     */
    public void s_set(Object value) throws FrozenSymbolException {
        if (isFrozen)
            throw new FrozenSymbolException(this);
        this.scalar = value;
    }

    /**
     * get scalar value
     * 
     * @return
     */
    public Object s_get() {
        return scalar;
    }

    /**
     * This creates a user-defined variable.
     * <p>
     * If this variable only has a scalar value, create an arraylist with only that
     * 1 value.
     * 
     * @param name Name of the variable.
     * @param t    The TVarRef token.
     * @param arr  The arraylist.
     * @return
     */
    public static DefinedVariable create(String name, TokenDefault t, ArrayList<Object> arr) {
        if (arr.size() == 1)
            return new DefinedVariable(name, t, arr.get(0));
        return new DefinedVariable(name, t, arr);
    }

    public static class DefinedVariable extends BaseVariable {

        DefinedVariable(String name, TokenDefault t, Object scalar) {
            super(name, t, scalar);
        }

        DefinedVariable(String name, TokenDefault t, ArrayList<Object> arr) {
            super(name, t, arr);
        }
    }

    public enum VariableType {
        SCALAR, ARRAY, A_FUCKING_AMALGAMATION, UNKNOWN
    }
}