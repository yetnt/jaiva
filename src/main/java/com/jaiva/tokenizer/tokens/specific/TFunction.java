package com.jaiva.tokenizer.tokens.specific;

import com.jaiva.errors.JaivaException;
import com.jaiva.lang.Chars;
import com.jaiva.tokenizer.jdoc.JDoc;
import com.jaiva.tokenizer.tokens.Token;
import com.jaiva.tokenizer.tokens.TokenDefault;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a function definition such as
 * {@code kwenza addition(param1, param2) -> khutla (param1 + param2)! <~}
 * This class is
 */
public class TFunction extends TokenDefault {
    /**
     * The arguments of the function.
     */
    public String[] args;
    /**
     * The body of the function.
     */
    public TCodeblock body;

    /**
     * An arraylist of booleans that specify which arguments are optional.
     */
    public ArrayList<Boolean> isArgOptional = new ArrayList<>();

    /**
     * Indicates whether the function accepts variable arguments (varargs).
     */
    public boolean varArgs = false;

    /**
     * Constructor for TFunction
     *
     * @param name The name of the function.
     * @param args The arguments of the function.
     * @param body The body of the function.
     * @param ln   The line number.
     */
    public TFunction(String name, String[] args, TCodeblock body, int ln) {
        super(name.startsWith(Character.toString(Chars.EXPORT_SYMBOL)),
                "F~" + (name.startsWith(Character.toString(Chars.EXPORT_SYMBOL)) ? name
                        .replaceFirst(Pattern.quote(Character.toString(Chars.EXPORT_SYMBOL)),
                                Matcher.quoteReplacement(""))
                        : name),
                ln);
        String[] newArgs = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (i == 0 && arg.startsWith(Chars.ASSIGNMENT)) {
                String newArg = arg.substring(2);
                if (!newArg.isBlank()) {
                    varArgs = true;
                    isArgOptional.add(true);
                    newArgs[i] = newArg;
                    break;
                }
            }
            isArgOptional.add(arg.endsWith("?"));
            if (arg.endsWith("?"))
                newArgs[i] = arg.substring(0, arg.length() - 1);
            else newArgs[i] = arg;
        }
        this.args = newArgs;
        this.body = body;
    }

    /**
     * Constructor for TFunction (for exporting globals.)
     *
     * @param name          The name of the function.
     * @param args          The arguments of the function.
     * @param body          The body of the function.
     * @param ln            The line number.
     * @param customToolTip The custom tooltip for the function.
     */
    public TFunction(String name, String[] args, TCodeblock body, int ln, JDoc customToolTip) {
        super("F~" + name, ln, customToolTip);

        String[] newArgs = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (i == 0 && arg.startsWith(Chars.ASSIGNMENT)) {
                String newArg = arg.substring(2);
                if (!newArg.isBlank()) {
                    varArgs = true;
                    isArgOptional.add(true);
                    newArgs[i] = newArg;
                    break;
                }
            }
            isArgOptional.add(arg.endsWith("?"));
            if (arg.endsWith("?"))
                newArgs[i] = arg.substring(0, arg.length() - 1);
        }
        this.args = newArgs;
        this.body = body;
    }

    @Override
    public String toJson() throws JaivaException {
        json.append("args", new ArrayList<>(Arrays.asList(args)), false);
        json.append("isArgOptional", isArgOptional, false);
        json.append("varArgs", varArgs, false);
        json.append("body", body != null ? body.toJsonInNest() : null, true);
        return super.toJson();
    }

    /**
     * Converts this token to the default {@link Token}
     *
     * @return {@link Token} with a T value of {@link TFunction}
     */
    public Token<TFunction> toToken() {
        return new Token<>(this);
    }
}
