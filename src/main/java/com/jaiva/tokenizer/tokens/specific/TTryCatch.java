package com.jaiva.tokenizer.tokens.specific;

import com.jaiva.errors.JaivaException;
import com.jaiva.tokenizer.tokens.TConstruct;
import com.jaiva.tokenizer.tokens.Token;
import com.jaiva.tokenizer.tokens.TokenDefault;

/**
 * Represents a try-catch statement such as
 * {@code zama zama -> ... <~ chaai -> ... <~}
 */
public class TTryCatch extends TokenDefault<TTryCatch> implements TConstruct {
    /**
     * The try block of the try-catch statement.
     */
    public TCodeblock tryBlock;
    /**
     * The catch block of the try-catch statement.
     */
    public TCodeblock catchBlock;

    /**
     * Constructor for TTryCatchStatement
     *
     * @param tryBlock The try block of the try-catch statement.
     * @param ln       The line number.
     */
    public TTryCatch(TCodeblock tryBlock, int ln) {
        super("TTryCatchStatement", ln);
        this.tryBlock = tryBlock;
    }

    /**
     * Appends the provided catch block to the current token.
     *
     * @param catchBlock the TCodeblock representing the catch block to be appended
     */
    public void appendCatchBlock(TCodeblock catchBlock) {
        this.catchBlock = catchBlock;
    }

    @Override
    public String toJson() throws JaivaException {
        json.append("try", tryBlock.toJsonInNest(), false);
        json.append("catch", catchBlock.toJsonInNest(), true);
        return super.toJson();
    }

    /**
     * Converts this token to the default {@link Token}
     *
     * @return {@link Token} with a T value of {@link TTryCatch}
     */
    public Token<TTryCatch> toToken() {
        return new Token<>(this);
    }
}
