package com.jaiva;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import com.jaiva.interpreter.Context;
import com.jaiva.interpreter.Interpreter;
import com.jaiva.interpreter.MapValue;
import com.jaiva.interpreter.globals.Globals;
import com.jaiva.tokenizer.Token;
import com.jaiva.tokenizer.Token.TFuncCall;
import com.jaiva.tokenizer.Token.TStatement;
import com.jaiva.tokenizer.Token.TVarReassign;
import com.jaiva.tokenizer.Token.TVarRef;
import com.jaiva.tokenizer.TokenDefault;
import com.jaiva.tokenizer.Tokenizer;
import com.jaiva.utils.BlockChain;
import com.jaiva.utils.Find.MultipleLinesOutput;

enum REPLMode {
    STANDARD(0),
    PRINT_TOKEN(1);

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

        System.out.println("type 'exit' to exit.");
        System.out.println();

        while (state == State.ACTIVE || state == State.ERROR) {
            try {
                state = State.ACTIVE;
                System.out.print(m != null || b != null ? "Jaiva! >... " : "Jaiva! > ");
                String line = b != null ? b.getCurrentLine() : reader.readLine().trim();
                if (line == null || line.equals("exit")) {
                    close();
                    break;
                }
                Object something = read(line, (b != null ? "" : previousLine), m, b);
                if (something instanceof ReadOuput) {
                    m = null;
                    b = null;
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

    /**
     * Closes the reader resource associated with this instance.
     * This method ensures that the reader is properly closed to release
     * any system resources associated with it.
     * 
     * If an IOException occurs during the closing process, the exception
     * is caught and its stack trace is printed.
     */
    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
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
            something = Tokenizer.readLine(line, previousLine, m, b, 0);
            if (something instanceof MultipleLinesOutput) {
                return something;
            } else if (something instanceof BlockChain) {
                return something;
            } else if (something instanceof Token<?>) {
                if (mode == REPLMode.STANDARD) {
                    TokenDefault token = ((Token<?>) something).getValue();
                    if (Interpreter.isVariableToken(token)) {
                        boolean isReturnTokenClass = token instanceof TStatement || token instanceof TVarRef
                                || token instanceof TFuncCall;
                        Object input = isReturnTokenClass
                                ? ((Token<?>) something)
                                : token;
                        Object value = Interpreter.handleVariables(input, this.vfs);
                        return new ReadOuput(value.toString());
                    } else {
                        Interpreter.interpret(new ArrayList<>(Arrays.asList((Token<?>) something)), Context.GLOBAL,
                                this.vfs);

                        return new ReadOuput();
                    }
                } else if (mode == REPLMode.PRINT_TOKEN) {
                    return new ReadOuput(((Token<?>) something).toString());
                }
            } else if (something instanceof ArrayList<?>) {
                ArrayList<Token<?>> tokens = (ArrayList<Token<?>>) something;
                if (mode == REPLMode.STANDARD) {
                    if (tokens.size() == 1) {
                        TokenDefault token = ((Token<?>) tokens.get(0)).getValue();
                        if (Interpreter.isVariableToken(token)) {
                            boolean isReturnTokenClass = token instanceof TStatement || token instanceof TVarRef
                                    || token instanceof TFuncCall;
                            Object input = isReturnTokenClass
                                    ? ((Token<?>) tokens.get(0))
                                    : token;
                            Object value = Interpreter.handleVariables(input, this.vfs);
                            return isReturnTokenClass ? new ReadOuput(value.toString())
                                    : new ReadOuput();
                        } else {
                            Interpreter.interpret(new ArrayList<>(Arrays.asList((Token<?>) tokens.get(
                                    0))),
                                    Context.GLOBAL,
                                    this.vfs);

                            return new ReadOuput();
                        }
                    } else {
                        Interpreter.interpret(tokens, Context.GLOBAL, this.vfs);
                        return new ReadOuput();
                    }
                } else if (mode == REPLMode.PRINT_TOKEN) {
                    StringBuilder sb = new StringBuilder();
                    for (Token<?> t : tokens) {
                        sb.append(t.toString()).append(tokens.size() != 1 ? "\n" : "");
                    }
                    return new ReadOuput(sb.toString());
                }
            }
        } catch (Exception e) {
            this.state = State.ERROR;
            // add stack trace as a string separated by new lines.

            StringBuilder stackTrace = new StringBuilder();
            stackTrace.append(e.getMessage()).append("\n");
            for (StackTraceElement element : e.getStackTrace()) {
                stackTrace.append("\t").append(element.toString()).append("\n");
            }
            return new ReadOuput(stackTrace.toString());
        }
        return null;
    }
}
