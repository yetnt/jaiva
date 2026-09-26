package com.jaiva.tokenizer.tokens.specific;

import com.jaiva.errors.JaivaException;
import com.jaiva.tokenizer.jdoc.JDoc;
import com.jaiva.tokenizer.tokens.Token;

/**
 * Represents a number variable such as {@code maak age <- 20};
 */
public class TNumberVar extends TUnknownScalar<Object, TNumberVar> {
    /**
     * Constructor for TNumberVar
     *
     * @param name  The name of the variable.
     * @param value The value of the variable.
     * @param ln    The line number.
     */
    public TNumberVar(String name, Object value, int ln) {
        super(name, value, ln);
        this.value = value;
    }

    /**
     * Constructor for TNumberVar (used for exporting globals.)
     *
     * @param name  The name of the variable.
     * @param value The value of the variable.
     * @param ln    The line number.
     */
    public TNumberVar(String name, Object value, int ln, JDoc customTooltip) {
        super(name, value, ln, customTooltip);
        this.value = value;
    }

    @Override
    public String toJson() throws JaivaException {
        json.append("value", value, true);
        return super.toJson();
    }

    /**
     * Converts this token to the default {@link Token}
     *
     * @return {@link Token} with a T value of {@link TNumberVar}
     */
    @Override
    public Token<TNumberVar> toToken() {
        return new Token<>(this);
    }
}
