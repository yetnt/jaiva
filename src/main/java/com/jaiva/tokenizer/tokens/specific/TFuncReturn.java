package com.jaiva.tokenizer.tokens.specific;

import com.jaiva.errors.JaivaException;
import com.jaiva.tokenizer.tokens.Token;
import com.jaiva.tokenizer.tokens.TokenDefault;

/**
 * Represents a function return which can only be defined in a function (Duh)
 * such as {@code khulta 10!}
 */
public class TFuncReturn extends TokenDefault {
    /**
     * The value which the function should return.
     */
    public Object value;

    /**
     * Constructor for TFuncReturn
     *
     * @param value The value which the function should return.
     * @param ln    The line number.
     */
    public TFuncReturn(Object value, int ln) {
        super("TFuncReturn", ln);
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
     * @return {@link Token} with a T value of {@link TFuncReturn}
     */
    public Token<TFuncReturn> toToken() {
        return new Token<>(this);
    }
}
