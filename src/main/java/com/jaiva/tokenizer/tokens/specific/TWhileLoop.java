package com.jaiva.tokenizer.tokens.specific;

import com.jaiva.errors.JaivaException;
import com.jaiva.tokenizer.tokens.Token;
import com.jaiva.tokenizer.tokens.TokenDefault;

/**
 * Represents a while loop such as {@code nikhil (i > 10) -> ... <~}
 */
public class TWhileLoop extends TokenDefault {
    /**
     * The condition of the while loop.
     */
    public Object condition;
    /**
     * The body of the while loop.
     */
    public TCodeblock body;

    /**
     * Constructor for TWhileLoop
     *
     * @param condition The condition of the while loop.
     * @param body      The body of the while loop.
     * @param ln        The line number.
     */
    public TWhileLoop(Object condition, TCodeblock body, int ln) {
        super("TWhileLoop", ln);
        this.condition = condition;
        this.body = body;
    }

    @Override
    public String toJson() throws JaivaException {
        json.append("condition", condition, false);
        json.append("body", body.toJsonInNest(), true);
        return super.toJson();
    }

    /**
     * Converts this token to the default {@link Token}
     *
     * @return {@link Token} with a T value of {@link TWhileLoop}
     */
    public Token<TWhileLoop> toToken() {
        return new Token<>(this);
    }
}
