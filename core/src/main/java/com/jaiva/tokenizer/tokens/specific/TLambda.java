package com.jaiva.tokenizer.tokens.specific;

import com.jaiva.tokenizer.tokens.TAtomicValue;
import com.jaiva.tokenizer.tokens.TokenDefault;

import java.util.ArrayList;
import java.util.List;

public class TLambda extends TFunction implements TAtomicValue {
    public TLambda(String name, String[] args, Object input, int ln) {
        super(name, args, new TCodeblock(
                new ArrayList<>(List.of(
                        new TFuncReturn(input, ln).toToken()
                )), ln, ln
        ), ln);
    }
}
