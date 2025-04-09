package com.jaiva.repl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import com.jaiva.interpreter.Globals;
import com.jaiva.interpreter.Interpreter;
import com.jaiva.interpreter.MapValue;
import com.jaiva.tokenizer.Token;
import com.jaiva.tokenizer.Tokenizer;
import com.jaiva.utils.BlockChain;
import com.jaiva.utils.Find.MultipleLinesOutput;

enum REPLMode {
    STANDARD(0),
    PRINT_TOKEN(1),
    ARITHMETIC_LOGIC(2);

    private int mode;

    REPLMode(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }

    public REPLMode toEnum(int mode) {
        for (REPLMode m : REPLMode.values()) {
            if (m.getMode() == mode) {
                return m;
            }
        }
        return STANDARD;
    }
}

enum State {
    ACTIVE,
    INACTIVE,
    ERROR,
    EXIT,
}

class ReadOuput {
    private String output = null;

    ReadOuput(String output) {
        this.output = output;
    }

    ReadOuput() {
    }

    public boolean isCleanExit() {
        return output == null;
    }

    public String getOutput() {
        return output;
    }
}

public class REPL {
    private REPLMode mode = REPLMode.STANDARD;
    public State state = State.INACTIVE;
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private HashMap<String, MapValue> vfs = new Globals().vfs;
    // private Token<?> tokenContainer = new Token<>(null);

    public REPL(int mode) {
        this.state = State.ACTIVE;
        this.mode = REPLMode.STANDARD.toEnum(mode);

        MultipleLinesOutput m = null;
        BlockChain b = null;
        String previousLine = "";

        while (state == State.ACTIVE || state == State.ERROR) {
            try {
                state = State.ACTIVE;
                System.out.print(m != null ? "Jaiva! ~>" : "Jaiva! > ");
                String line = b != null ? b.getCurrentLine() : reader.readLine().trim();
                if (line == null || line.equals("exit")) {
                    close();
                    break;
                }
                Object something = read(line, (b != null ? "" : previousLine), m, b);
                if (something instanceof ReadOuput) {
                    ReadOuput ro = (ReadOuput) something;
                    if (this.state == State.ERROR) {
                        System.out.println("Chaai an error: " + ro.getOutput());
                    } else if (!ro.isCleanExit()) {
                        System.out.println(ro.getOutput());
                    }
                } else if (something instanceof MultipleLinesOutput) {
                    m = (MultipleLinesOutput) something;
                    b = null;
                } else if (something instanceof BlockChain) {
                    m = null;
                    b = (BlockChain) something;
                } else if (something instanceof Token<?>) {
                    b = null;
                    m = null;
                } else {
                    b = null;
                    m = null;
                }
                previousLine = line;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        close();
        System.out.println("hai bye mfana.");
    }

    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Reads, parses, and executes a line of code.
     * 
     * @param line
     */
    public Object read(String line, String previousLine, MultipleLinesOutput m, BlockChain b) {
        Object something;
        try {
            something = Tokenizer.readLine(line, previousLine, m, b);
            if (something instanceof MultipleLinesOutput) {
                return something;
            } else if (something instanceof BlockChain) {
                return something;
            } else if (something instanceof Token<?>) {
                if (mode == REPLMode.STANDARD || mode == REPLMode.ARITHMETIC_LOGIC) {
                    Interpreter.interpret(new ArrayList<>(Arrays.asList((Token<?>) something)), "global",
                            this.vfs);
                    return new ReadOuput();
                } else if (mode == REPLMode.PRINT_TOKEN) {
                    return new ReadOuput(((Token<?>) something).toString());
                }
            } else if (something instanceof ArrayList<?>) {
                ArrayList<Token<?>> tokens = (ArrayList<Token<?>>) something;
                if (mode == REPLMode.STANDARD || mode == REPLMode.ARITHMETIC_LOGIC) {
                    Interpreter.interpret(tokens, "global", this.vfs);
                    return new ReadOuput();
                } else if (mode == REPLMode.PRINT_TOKEN) {
                    StringBuilder sb = new StringBuilder();
                    for (Token<?> t : tokens) {
                        sb.append(t.toString()).append(tokens.size() != 1 ? "\n" : "");
                    }
                    return new ReadOuput(sb.toString());
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            this.state = State.ERROR;
            return new ReadOuput(e.getMessage());
        }
        return null;
    }
}
