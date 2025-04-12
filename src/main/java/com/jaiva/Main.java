package com.jaiva;

import java.io.File; // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner; // Import the Scanner class to read text files

import com.jaiva.interpreter.Context;
import com.jaiva.interpreter.Globals;
import com.jaiva.interpreter.Interpreter;
import com.jaiva.repl.REPL;
import com.jaiva.tokenizer.Token;
import com.jaiva.tokenizer.Tokenizer;
import com.jaiva.utils.BlockChain;
import com.jaiva.utils.Find;

public class Main {
    public static String ASCII = """
                                                                        ,---,
                     ,---._                                           ,`--.' |
                   .-- -.' \\                                          |   :  :
                   |    |   :              ,--,                       '   '  ;
                   :    ;   |            ,--.'|                       |   |  |
                   :        |            |  |,      .---.             '   :  ;
                   |    :   :  ,--.--.   `--'_    /.  ./|   ,--.--.   |   |  '
                   :          /       \\  ,' ,'| .-' . ' |  /       \\  '   :  |
                   |    ;   |.--.  .-. | '  | |/___/ \\: | .--.  .-. | ;   |  ;
               ___ l          \\__\\/ : . . |  | :.   \\  ' .  \\__\\/ : . . `---'. |
             /    /\\    J   : ," .--.; | '  : |_\\   \\   '  ," .--.; |  `--..`;
            /  ../  `..-    ,/  /  ,.  | |  | '.'\\   \\    /  /  ,.  | .--,_
            \\    \\         ;;  :   .'   \\;  :    ;\\   \\ |;  :   .'   \\|    |`.
             \\    \\      ,' |  ,     .-./|  ,   /  '---" |  ,     .-./`-- -`, ;
              "---....--'    `--`---'     ---`-'          `--`---'      '---`"

                        """;

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        ArrayList<String> replArgs = new ArrayList<>(
                Arrays.asList("--print-tokens", "-p", "--help", "-h", "--version", "-v"));
        ArrayList<String> tokenArgs = new ArrayList<>(Arrays.asList("-s", "-j", "--string", "-json"));
        if (args.length == 0) {
            new REPL(0);
            return;
        } else if (replArgs.contains(args[0])) {
            switch (args[0]) {
                case "--print-tokens", "-p":
                    // System.out.println("Print token REPL mode.");
                    new REPL(1);
                    break;
                case "--help", "-h":
                    System.out.println();
                    System.out.println("Usage: jaiva [options]");
                    System.out.println("Options:");
                    System.out.println("(no flag): Starts a REPL.");
                    System.out.println("--print-tokens, -p: Print tokens REPL mode.");
                    System.out.println("--help, -h: Show this help message.");
                    System.out.println("--version, -v: Show the version of Jaiva.");
                    System.out.println();
                    System.out.println("Usage: jaiva <file.jiv> [options]");
                    System.out.println("Options:");
                    System.out.println("(no flag): Run the file.");
                    System.out.println("--json, -j: Print tokens in JSON format.");
                    System.out.println("--string, -s: Print tokens in string format.");
                    System.out.println();
                    break;
                case "--version", "-v":
                    System.out.println(ASCII);
                    System.out.println("Jaiva! 1.0.0");
                    System.out.println(
                            "Jaiva is a programming language that is designed to be easy to use and understand. (I'm speaking out my ass)");
                    break;
                default:
                    System.out.println("Invalid REPL mode.");
                    break;
            }
            return;
        } else if (!args[0].contains(".")) {
            // this is to catch the case where the user does not provide a file name
            System.out.println("Provide a file name or a cmd flag kau.");
            return;
        }
        // here, args[0] is the file name that must end in either .jaiva, .jiv or .jva
        if (!args[0].endsWith(".jaiva") && !args[0].endsWith(".jiv") && !args[0].endsWith(".jva")) {
            System.out.println("I can't read whatever that lang is bro (i accept only .jaiva, .jiv or .jva)");
            return;
        }
        File myObj = new File(args[0]);
        try {
            ArrayList<Token<?>> tokens = new ArrayList<>();
            Find.MultipleLinesOutput m = null;
            BlockChain b = null;
            Scanner myReader = new Scanner(myObj);
            String previousLine = "";
            while (myReader.hasNextLine()) {
                String line = (b != null ? b.getCurrentLine() : myReader.nextLine());
                // System.out.println(line);
                // System.out.println(line);
                Object something = Tokenizer.readLine(line, (b != null ? "" : previousLine), m, b);
                if (something instanceof Find.MultipleLinesOutput) {
                    m = (Find.MultipleLinesOutput) something;
                    b = null;
                } else if (something instanceof ArrayList<?>) {
                    m = null;
                    b = null;
                    tokens.addAll((ArrayList<Token<?>>) something);
                } else if (something instanceof BlockChain) {
                    m = null;
                    b = (BlockChain) something;
                } else if (something instanceof Token<?>) {
                    b = null;
                    m = null;
                    tokens.add((Token<?>) something);
                } else {
                    b = null;
                    m = null;
                }
                previousLine = line;
            }

            // System.out.println(tokens.size());
            myReader.close();

            if ((args.length > 1) && tokenArgs.contains(args[1])) {
                // here, args[1] is the tokens mode
                // return tokens mode.
                switch (args[1]) {
                    case "-s", "--string":
                        for (Token<?> t : tokens) {
                            System.out.println(t.toString());
                            // System.out.println(t.getValue().getContents(0));
                        }
                        return;
                    case "-j", "--json":
                        System.out.println("Json mode.");
                        System.out.println("[");
                        for (int i = 0; i < tokens.size(); i++) {
                            Token<?> token = tokens.get(i);
                            System.out.print(token.getValue().toJson());
                            if (i != tokens.size() - 1) {
                                System.out.print(",\n");
                            }
                        }
                        System.out.println("]");
                        return;
                    default:
                        System.out.println("Invalid token mode.");
                        return;
                }
            }
            Interpreter.interpret(tokens, Context.GLOBAL, new Globals().vfs);

        } catch (FileNotFoundException e) {
            System.out.println("Ayo i cannot find the file " + args[0] + ". Make sure it exists homie.");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error.");
            e.printStackTrace();
        }
    }
}