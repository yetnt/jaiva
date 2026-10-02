package com.jaiva.tokenizer.tokens;

import com.jaiva.tokenizer.tokens.specific.TCodeblock;

import java.util.ArrayList;

/**
 * TConstruct is an empty interface which holds all the tokens
 * that have a TCodeblock with more tokens/executable code with in.
 */
public interface TConstruct {
    ArrayList<TCodeblock> getCodeBlocks();
}
