package com.jaiva.tokenizer.tokens.specific;

import com.jaiva.errors.JaivaException;
import com.jaiva.tokenizer.tokens.Token;
import com.jaiva.tokenizer.tokens.TokenDefault;

public class TVarReassign extends TokenDefault {
    /**
     * The new value of the variable.
     */
    public Object newValue;

    /**
     * Constructor for TVarReassign
     *
     * @param name  The name of the variable.
     * @param value The new value of the variable.
     * @param ln    The line number.
     */
    public TVarReassign(String name, Object value, int ln) {
        super(name, ln);
        this.newValue = value;
    }

    /**
     * Constructor for TVarReassign
     *
     * @param name  The name of the variable.
     * @param value The new value of the variable.
     * @param ln    The line number.
     */
    public TVarReassign(Object name, Object value, int ln) {
        super((String)name, ln);
        this.newValue = value;
    }

    @Override
    public String toJson() throws JaivaException {
        json.append("value", newValue, true);
        return super.toJson();
    }

    /**
     * Converts this token to the default {@link Token}
     *
     * @return {@link Token} with a T value of {@link TVarReassign}
     */
    public Token<TVarReassign> toToken() {
        return new Token<>(this);
    }
}
