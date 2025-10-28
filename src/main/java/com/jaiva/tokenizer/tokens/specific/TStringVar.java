package com.jaiva.tokenizer.tokens.specific;

import com.jaiva.errors.JaivaException;
import com.jaiva.lang.EscapeSequence;
import com.jaiva.tokenizer.jdoc.JDoc;
import com.jaiva.tokenizer.tokens.Token;

/**
 * Represents a string variable such as {@code maak name <- "John"};
 */
public class TStringVar extends TUnknownScalar<String, TStringVar> {

    /**
     * Constructor for TStringVar
     *
     * @param name  The name of the variable.
     * @param value The value of the variable.
     * @param ln    The line number.
     */
    public TStringVar(String name, String value, int ln) {
        super(name, value, ln);
        this.value = value;
    }

    /**
     * Constructor for TStringVar (for exporting globals.)
     *
     * @param name  The name of the variable.
     * @param value The value of the variable.
     * @param ln    The line number.
     */
    public TStringVar(String name, String value, int ln, JDoc customToolTip) {
        super(name, value, ln, customToolTip);
        this.value = value;
    }

    @Override
    public String toJson() throws JaivaException {
        json.append("value",
                value != null ? EscapeSequence.fromEscape((String) value, lineNumber) : value,
                true);
        return super.toJson();
    }

    /**
     * Converts this token to the default {@link Token}
     *
     * @return {@link Token} with a T value of {@link TStringVar}
     */
    @Override
    public Token<TStringVar> toToken() {
        return new Token<>(this);
    }
}
