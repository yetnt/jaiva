package com.jaiva.tokenizer.tokens.specific;

import com.jaiva.errors.JaivaException;
import com.jaiva.errors.TokenizerException;
import com.jaiva.tokenizer.tokens.TAtomicValue;
import com.jaiva.tokenizer.tokens.Token;
import com.jaiva.tokenizer.tokens.TokenDefault;
import com.jaiva.utils.Find;
import com.jaiva.utils.cd.ContextDispatcher;

/**
 * Represents a statement such as {@code 10 + 1} or {@code true && false}
 * This class usually isn't used directly, but rather as a part of another
 * instance.
 */
public class TExpression extends TokenDefault<TExpression> implements TAtomicValue {
    /**
     * The left hand side of the statement.
     * <p>
     * This is an object due to the fact that it might itself be a TVarRef which
     * retrns a function, or another function that returns a function or any other
     * case like that.
     */
    public Object lHandSide = "null";
    /**
     * The operator of the statement.
     */
    public String op;
    /**
     * The right hand side of the statement.
     * <p>
     * This is an object due to the fact that it might itself be a TVarRef which
     * retrns a function, or another function that returns a function or any other
     * case like that.
     */
    public Object rHandSide = "null";
    /**
     * The statement as a string.
     */
    public String statement;
    /**
     * 0 = boolean logic |
     * 1 = int arithmetic
     */
    public int statementType;

    /**
     * Constructor for TStatement
     *
     * @param ln The line number.
     */
    public TExpression(int ln) {
        super("TStatement", ln);
    }

    /**
     * Helper function that handles negatives in a statement. This is used to handle
     * the case where a negative sign is used as a unary operator.
     *
     * @param s The statement to handle.
     * @return The handled statement.
     */
    public static Object handleNegatives(Object s) {
        if (s instanceof TExpression statement) {
            if (statement.lHandSide == null && statement.op.equals("-")) {
                statement.lHandSide = -1;
                statement.op = "*";
            }
            // handled by the interpreter

            return statement;
        }
        return s;
    }

    @Override
    public String toJson() throws JaivaException {
        json.append("lhs", lHandSide, false);
        json.append("op", op, false);
        json.append("rhs", rHandSide, false);
        json.append("statementType", statementType, false);
        json.append("statement", statement.replace("\"", "\\\""), true);

        return super.toJson();
    }

    /**
     * Parses a given string. This assumes you've already used braces to
     * indicate the correct order of operations.
     * <p>
     * NOTE : This method will call ContextDispatcher and if needed may switch to
     * using processContext if its instead function call or variable call.
     *
     * @param statement The statement to parse.
     */
    public Object parse(String statement) throws TokenizerException {
        statement = statement.trim();

        if (statement.isEmpty()) {
            return null;
        }
        ContextDispatcher d = new ContextDispatcher(statement);
        if (d.getDeligation() == ContextDispatcher.To.PROCESS_CONTENT) {
            return Token.processContext(statement, lineNumber);
        }
        this.statement = statement;

        int lastBraceIndex = Find.lastOutermostBracePair(statement);
        if ((statement.startsWith("(") && statement.endsWith(")")) && lastBraceIndex == 0) {
            return parse(statement.substring(1, statement.length() - 1).trim());
        }

        Find.LeastImportantOperator info = Find.leastImportantOperator(statement);
        if (info.index == -1) {
            // no operator found, so its a single value
            return Token.processContext(statement, lineNumber);
        }
        if (info.op.equals("=") && statement.charAt(info.index - 1) == '!') {
            info.index--;
            info.op = "!=";
        }
        if (info.op.equals("=") && statement.charAt(info.index - 1) == '<') {
            info.index--;
            info.op = "<=";
        }
        if (info.op.equals("=") && statement.charAt(info.index - 1) == '>') {
            info.index--;
            info.op = ">=";
        }

        lHandSide = handleNegatives(new TExpression(lineNumber).parse(statement.substring(0, info.index).trim()));
        this.op = info.op.trim();
        rHandSide = handleNegatives(
                new TExpression(lineNumber).parse(statement.substring(info.index + info.op.length()).trim()));
        statementType = info.tStatementType;
        return ((TExpression) handleNegatives(this)).toToken();
    }

    /**
     * Converts this token to the default {@link Token}
     *
     * @return {@link Token} with a T value of {@link TExpression}
     */
    public Token<TExpression> toToken() {
        return new Token<>(this);
    }
}
