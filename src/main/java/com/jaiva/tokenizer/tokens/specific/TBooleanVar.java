package com.jaiva.tokenizer.tokens.specific;

import com.jaiva.errors.JaivaException;
import com.jaiva.tokenizer.tokens.Token;

/**
 * Represents a boolean variable such as {@code maak isTrue <- true};
 */
public class TBooleanVar extends TUnknownScalar<Object, TBooleanVar> {

    /**
     * Constructor for TBooleanVar
     *
     * @param name  The name of the variable.
     * @param value The value of the variable.
     * @param ln    The line number.
     */
    public TBooleanVar(String name, boolean value, int ln) {
        super(name, value, ln);
        this.value = value;
    }

    /**
     * Constructor for TBooleanVar
     *
     * @param name  The name of the variable.
     * @param value The value of the variable.
     * @param ln    The line number.
     */
    public TBooleanVar(String name, Object value, int ln) {
        super(name, value, ln);
        this.value = value;
    }

//    @Override
    public String toJson() throws JaivaException {
        json.append("value", value, true);
        return super.toJson();
    }

    /**
     * Converts this token to the default {@link Token}
     *
     * @return {@link Token} with a T value of {@link TBooleanVar}
     */
    @Override
    public Token<TBooleanVar> toToken() {
        return new Token<>(this);
    }
}
