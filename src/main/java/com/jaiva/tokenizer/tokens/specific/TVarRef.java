package com.jaiva.tokenizer.tokens.specific;

import com.jaiva.errors.JaivaException;
import com.jaiva.lang.Chars;
import com.jaiva.tokenizer.tokens.TAtomicValue;
import com.jaiva.tokenizer.tokens.Token;
import com.jaiva.tokenizer.tokens.TokenDefault;

/**
 * Represents a variable reference such as {@code name} or {@code age} or
 * {@code array[index]}.
 */
public class TVarRef extends TokenDefault<TVarRef> implements TAtomicValue {
    /**
     * The type of the variable reference.
     */
    public int type;
    /**
     * The name of the variable being referenced.
     * <p>
     * This is an object due to the fact that it might itself be a TVarRef which
     * retrns a function, or another function that returns a function or any other
     * case like that.
     */
    public Object varName;
    /**
     * The index of the variable reference.
     * <p>
     * This is an object due to the fact that it might itself be a TVarRef which
     * retrns a function, or another function that returns a function or any other
     * case like that.
     */
    public Object index = null;
    /**
     * Indicates whether the variable reference is a length call.
     */
    public boolean getLength = false;

    /**
     * Constructor for TVarRef
     *
     * @param name The name of the variable being referenced.
     * @param ln   The line number.
     */
    public TVarRef(Object name, int ln, boolean getLength) {
        super("TVarRef", ln);
        if (name instanceof String n)
            this.varName = n.endsWith(Chars.LENGTH_CHAR + "") ? n.substring(0, n.length() - 1) : n;
        else
            this.varName = name;
        this.getLength = getLength;
    }

    /**
     * Constructor for TVarRef
     *
     * @param name      The name of the variable being referenced.
     * @param index     The index of the variable reference.
     * @param ln        The line number.
     * @param getLength Indicates whether the variable reference is a length call.
     */
    public TVarRef(Object name, Object index, int ln, boolean getLength) {
        super("TVarRef", ln);
        this.index = index;
        if (name instanceof String n)
            this.varName = n.endsWith(Chars.LENGTH_CHAR + "") ? n.substring(0, n.length() - 1) : n;
        else
            this.varName = name;
        this.getLength = getLength;
    }

    @Override
    public String toJson() throws JaivaException {
        json.append("varName", varName, false);
        json.append("getLength", getLength, false);
        json.append("index", index != null ? index : null, true);
        return super.toJson();
    }

    /**
     * Converts this token to the default {@link Token}
     *
     * @return {@link Token} with a T value of {@link TVarRef}
     */
    public Token<TVarRef> toToken() {
        return new Token<>(this);
    }
}
