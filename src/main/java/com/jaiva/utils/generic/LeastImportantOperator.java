package com.jaiva.utils.generic;

public class LeastImportantOperator {
    public String op;
    public int index;
    public int tStatementType;

    public LeastImportantOperator(String op, int index, int group) {
        this.op = op;
        this.index = index;
        switch (group) {
            case 0:// Exponentiation
            case 1:// DivMult
            case 2:// AddSub
            case 3:// Bitwise shifts, Also handled within number handling.
            case 4:// Bitwise operations. Normally this should be by itself, but since the
                // interprter knows how to handle bitwise stuff and its in the number handling
                // method, group it under numbers
                tStatementType = 1;
                break;
            case 5, 6: // Comparison and logical operators
                tStatementType = 0;
                break;
            default:
                throw new IllegalArgumentException("Invalid group: " + group);
        }
    }

    /**
     * Constructor for no return value.
     */
    public LeastImportantOperator() {
        this.op = null;
        this.index = -1;
        this.tStatementType = -1;
    }

    @Override
    public String toString() {
        return "LeastImportantOperator{" +
                "op='" + op + '\'' +
                ", index=" + index +
                ", tStatementType=" + tStatementType +
                '}';
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((op == null) ? 0 : op.hashCode());
        result = prime * result + index;
        result = prime * result + tStatementType;
        return result;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * <p>
     * The method checks for reference equality, nullity, class type, and then
     * compares
     * the fields {@code op}, {@code index}, and {@code tStatementType} for
     * equality.
     * </p>
     *
     * @param obj the reference object with which to compare
     * @return {@code true} if this object is the same as the obj argument;
     * {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LeastImportantOperator other = (LeastImportantOperator) obj;
        if (op == null) {
            if (other.op != null)
                return false;
        } else if (!op.equals(other.op))
            return false;
        if (index != other.index)
            return false;
        return tStatementType == other.tStatementType;
    }
}
