package com.jaiva.interpreter.symbol;

import java.util.ArrayList;
import java.util.List;

import com.jaiva.errors.InterpreterException;
import com.jaiva.interpreter.ContextTrace;
import com.jaiva.tokenizer.TokenDefault;

/**
 * BaseVariable class is a base class for all variables in Jaiva.
 */
public class BaseVariable extends Symbol {
    /**
     * The scalar value of the variable.
     * <p>
     * This value is used when the variable is a scalar type.
     */
    private Object scalar;
    // we just give access to the array because why not.
    /**
     * The array value of the variable.
     * <p>
     * This value is used when the variable is an array type.
     */
    private ArrayList<Object> array = new ArrayList<>();
    /**
     * The type of the variable.
     * <p>
     * This value is used to determine if the variable is a scalar or an array type.
     */
    public VariableType variableType;

    /**
     * Update the variable type.
     * 
     * @param newType The new type of the variable.
     */
    public void updateVariableType(VariableType newType) {
        if (variableType == VariableType.UNKNOWN) {
            variableType = newType;
        } else if ((variableType == VariableType.SCALAR && newType == VariableType.ARRAY)
                || (variableType == VariableType.ARRAY && newType == VariableType.SCALAR)) {
            variableType = VariableType.A_FUCKING_AMALGAMATION;
        }
    }

    @Override
    public String toDebugString() {
        return scalar != null ? scalar.toString() : array.toString();
    }

    /**
     * Constructs a new BaseVariable instance with the specified name, token, and
     * scalar value.
     *
     * @param name   the name of the variable
     * @param t      the token associated with the variable
     * @param scalar the scalar value of the variable
     */
    public BaseVariable(String name, TokenDefault t, Object scalar) {
        super(name, SymbolType.VARIABLE, t);
        this.scalar = scalar;
        variableType = VariableType.SCALAR;
    }

    /**
     * Constructs a new BaseVariable instance with the specified name, token, and
     * array value.
     *
     * @param name the name of the variable
     * @param t    the token associated with the variable
     * @param arr  the array value of the variable
     */
    public BaseVariable(String name, TokenDefault t, ArrayList<Object> arr) {
        super(name, SymbolType.VARIABLE, t);
        this.array.addAll(arr);
        variableType = VariableType.ARRAY;
    }

    /**
     * Constructs a new BaseVariable instance with the specified name and token.
     *
     * @param name the name of the variable
     * @param t    the token associated with the variable
     */
    public BaseVariable(String name, TokenDefault t) {
        super(name, SymbolType.VARIABLE, t);
        variableType = VariableType.UNKNOWN;
    }

    /**
     * Constructs a new BaseVariable instance with the specified name.
     *
     * @param name the name of the variable
     */
    public BaseVariable(String name) {
        super(name, SymbolType.VARIABLE);
        variableType = VariableType.UNKNOWN;
    }

    /**
     * Sets the array value of the variable.
     * 
     * @param t
     * @throws InterpreterException.FrozenSymbolException
     */
    public void a_set(ArrayList<Object> t, ContextTrace cTrace) throws InterpreterException.FrozenSymbolException {
        if (isFrozen)
            throw new InterpreterException.FrozenSymbolException(cTrace, this);
        array = t;
    }

    /**
     * Adds an array to the array (addAll())
     * 
     * @param a
     * @throws InterpreterException.FrozenSymbolException
     */
    public void a_addAll(List<String> a, ContextTrace cTrace) throws InterpreterException.FrozenSymbolException {
        if (isFrozen)
            throw new InterpreterException.FrozenSymbolException(cTrace, this);
        array.addAll(a);
    }

    /**
     * Appends an element to the array.
     * 
     * @param a
     * @throws InterpreterException.FrozenSymbolException
     */
    public void a_add(Object a, ContextTrace cTrace) throws InterpreterException.FrozenSymbolException {
        if (isFrozen)
            throw new InterpreterException.FrozenSymbolException(cTrace, this);
        array.add(a);
    }

    /**
     * Gets the array value of the variable.
     * 
     * @return
     */
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
     * @throws InterpreterException.FrozenSymbolException
     */
    public void s_set(Object value, ContextTrace cTrace) throws InterpreterException.FrozenSymbolException {
        if (isFrozen)
            throw new InterpreterException.FrozenSymbolException(cTrace, this);
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
    public static DefinedVariable create(String name, TokenDefault t, ArrayList<Object> arr, boolean setArray) {
        if (arr.size() == 1 && !setArray)
            return new DefinedVariable(name, t, arr.get(0));
        return new DefinedVariable(name, t, arr);
    }

    /**
     * A class that represents a user-defined variable.
     */
    public static class DefinedVariable extends BaseVariable {
        /**
         * Constructs a new DefinedVariable instance with the specified name, token,
         * and scalar value.
         *
         * @param name   the name of the variable
         * @param t      the token associated with the variable
         * @param scalar the scalar value of the variable
         */
        DefinedVariable(String name, TokenDefault t, Object scalar) {
            super(name, t, scalar);
        }

        /**
         * Constructs a new DefinedVariable instance with the specified name, token,
         * and array value.
         *
         * @param name the name of the variable
         * @param t    the token associated with the variable
         * @param arr  the array value of the variable
         */
        DefinedVariable(String name, TokenDefault t, ArrayList<Object> arr) {
            super(name, t, arr);
        }
    }

    /**
     * Enum that represents the type of variable.
     */
    public enum VariableType {
        /**
         * Scalar type.
         */
        SCALAR,
        /**
         * Array type.
         */
        ARRAY,
        /**
         * Amalgamation of scalar and array types.
         */
        A_FUCKING_AMALGAMATION,
        /**
         * Unknown type.
         */
        UNKNOWN
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((scalar == null) ? 0 : scalar.hashCode());
        result = prime * result + ((array == null) ? 0 : array.hashCode());
        result = prime * result + ((variableType == null) ? 0 : variableType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        BaseVariable other = (BaseVariable) obj;
        if (scalar == null) {
            if (other.scalar != null)
                return false;
        } else if (!scalar.equals(other.scalar))
            return false;
        if (array == null) {
            if (other.array != null)
                return false;
        } else if (!array.equals(other.array))
            return false;
        if (variableType != other.variableType)
            return false;
        return true;
    }

}