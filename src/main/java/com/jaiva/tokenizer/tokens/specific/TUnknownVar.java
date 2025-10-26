package com.jaiva.tokenizer.tokens.specific;

import com.jaiva.errors.JaivaException;
import com.jaiva.lang.Chars;
import com.jaiva.tokenizer.jdoc.JDoc;
import com.jaiva.tokenizer.tokens.Token;
import com.jaiva.tokenizer.tokens.TokenDefault;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a variable where it's type can only be resolved by the interpeter
 * such as {@code maak name <- functionCall()!}; or if they made a variable but
 * didnt declare the value.
 *
 * @param <Type> The type of the variable.
 */
public class TUnknownVar<Type> extends TokenDefault {
    /**
     * The value of the variable.
     */
    public Type value;

    /**
     * Constructor for basic.
     *
     * @param name  The name of the variable.
     * @param value The value of the variable.
     * @param ln    The line number.
     */
    public TUnknownVar(String name, Type value, int ln) {
        super(name.startsWith(Character.toString(Chars.EXPORT_SYMBOL)),
                (name.startsWith(Character.toString(Chars.EXPORT_SYMBOL))
                        ? name.replaceFirst(Pattern.quote(Character.toString(Chars.EXPORT_SYMBOL)),
                        Matcher.quoteReplacement(""))
                        : name),
                ln);
        this.value = value;
    }

    /**
     * Constructor for exporting globals.
     *
     * @param name  The name of the variable.
     * @param value The value of the variable.
     * @param ln    The line number.
     */
    public TUnknownVar(String name, Type value, int ln, JDoc customToolTip) {
        super(name, ln, customToolTip);
        this.value = value;
    }

    @Override
    public String toJson() throws JaivaException {
        if (!json.keyExists("value"))
            json.append("value", value, true);
        return super.toJson();
    }

    /**
     * Converts this token to the default {@link Token}
     *
     * @return {@link Token} with a T value of {@link TUnknownVar}
     */
    public Token<TUnknownVar<?>> toToken() {
        return new Token<>(this);
    }
}
