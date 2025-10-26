package com.jaiva.tokenizer.tokens.specific;

import com.jaiva.errors.JaivaException;
import com.jaiva.tokenizer.tokens.Token;
import com.jaiva.tokenizer.tokens.TokenDefault;

/**
 * Represents a throw error statement such as {@code cima "Error message!"}
 */
public class TThrowError extends TokenDefault {
    /**
     * The error message to be thrown.
     */
    public Object errorMessage;

    /**
     * Constructor for TThrowError
     *
     * @param errorMessage The error message to be thrown.
     * @param ln           The line number.
     */
    public TThrowError(Object errorMessage, int ln) {
        super("TThrowError", ln);
        this.errorMessage = errorMessage;
    }

    @Override
    public String toJson() throws JaivaException {
        json.append("errorMessage", errorMessage, false);
        return super.toJson();
    }

    /**
     * Converts this token to the default {@link Token}
     *
     * @return {@link Token} with a T value of {@link TThrowError}
     */
    public Token<TThrowError> toToken() {
        return new Token<>(this);
    }
}
