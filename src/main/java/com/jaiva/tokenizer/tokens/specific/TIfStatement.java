package com.jaiva.tokenizer.tokens.specific;

import com.jaiva.errors.JaivaException;
import com.jaiva.tokenizer.tokens.TConditional;
import com.jaiva.tokenizer.tokens.TConstruct;
import com.jaiva.tokenizer.tokens.Token;
import com.jaiva.tokenizer.tokens.TokenDefault;

import java.util.ArrayList;

/**
 * Represents an if statement such as {@code if (i > 10) -> ... <~}, also can
 * have chains of else ifs and a last else if.
 * {@code if (i > 10) -> ... <~ mara if (i > 4) -> ... <~ mara -> ... <~}
 * <p>
 * If this is in an else if of a base if statement, it will not contain an else
 * body nor its own chain of elseIfs. only the original if contaisn all the else
 * ifs and the last else block.
 */
public class TIfStatement extends TokenDefault<TIfStatement> implements TConstruct, TConditional {
    /**
     * The condition of the if statement.
     */
    public Object condition;
    /**
     * The body of the if statement.
     */
    public TCodeblock body;
    /**
     * The else body of the if statement.
     */
    public TCodeblock elseBody = null;
    /**
     * The chain of else if statements.
     */
    public ArrayList<TIfStatement> elseIfs = new ArrayList<>();

    /**
     * Constructor for TIfStatement
     *
     * @param condition The condition of the if statement.
     * @param body      The body of the if statement.
     * @param ln        The line number.
     */
    public TIfStatement(Object condition, TCodeblock body, int ln) {
        super("TIfStatement", ln);
        this.condition = condition;
        this.body = body;
    }

    /**
     * Appends the provided else if body to the current token.
     *
     * @param body the TIfStatement representing the else if body to be appended
     */
    public void appendElseIf(TIfStatement body) {
        if (elseIfs == null) {
            elseIfs = new ArrayList<>();
        }
        elseIfs.add(body);
    }

    /**
     * Appends the provided else body to the current token.
     *
     * @param elseBody the TCodeblock representing the else body to be appended
     */
    public void appendElse(TCodeblock elseBody) {
        this.elseBody = elseBody;
    }

    @Override
    public String toJson() throws JaivaException {
        json.append("condition", condition, false);
        json.append("body", body.toJsonInNest(), false);
        json.append("elseIfs", elseIfs != null && !elseIfs.isEmpty() ? elseIfs : null, false);
        json.append("elseBody", elseBody != null ? elseBody.toJsonInNest() : null, false);
        return super.toJson();
    }

    /**
     * Converts this token to the default {@link Token}
     *
     * @return {@link Token} with a T value of {@link TIfStatement}
     */
    public Token<TIfStatement> toToken() {
        return new Token<>(this);
    }

    @Override
    public Object getConditionToken() {
        return condition;
    }
}
