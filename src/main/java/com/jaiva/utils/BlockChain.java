package com.jaiva.utils;

import com.jaiva.tokenizer.Token;

/**
 * This class represents a chain of code blocks, such as if-else chains or
 * try-catch blocks.
 * It is used to handle the parsing of these chains correctly by storing the
 * initial block
 * and the current line being processed.
 * <p>
 * The main idea is to parse the initial block and then, if a chain keyword
 * (like "mara" or "chaai")
 * is encountered, return a BlockChain instance instead of the final token. This
 * instance contains
 * the first block and the current line. The main class will then feed this line
 * back into the
 * readLine method, effectively inserting it before the next line to be read.
 * <p>
 * This process continues until the chain ends, at which point the original
 * block with all appended
 * else-if or catch blocks is returned.
 */
public class BlockChain {
    // either TIfStatement or TTryCatchStatement
    private final Token<?> initialIf;
    // the current line being processed. (With <~ removed)
    private final String currentLine;

    public BlockChain(Token<?> initialIf, String currentLine) {
        this.initialIf = initialIf;
        this.currentLine = currentLine;
    }

    public Token<?> getInitialIf() {
        return initialIf;
    }

    public String getCurrentLine() {
        return currentLine;
    }
}