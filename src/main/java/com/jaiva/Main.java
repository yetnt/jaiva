package com.jaiva;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import com.jaiva.interpreter.*;
import com.jaiva.interpreter.globals.Globals;
import com.jaiva.interpreter.runtime.GlobalResources;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.lang.EscapeSequence;
import com.jaiva.tokenizer.TConfig;
import com.jaiva.tokenizer.Token;
import com.jaiva.tokenizer.TokenDefault;
import com.jaiva.tokenizer.Tokenizer;
import com.jaiva.tokenizer.Token.TArrayVar;
import com.jaiva.tokenizer.Token.TBooleanVar;
import com.jaiva.tokenizer.Token.TDocsComment;
import com.jaiva.tokenizer.Token.TFunction;
import com.jaiva.tokenizer.Token.TNumberVar;
import com.jaiva.tokenizer.Token.TStringVar;
import com.jaiva.tokenizer.Token.TUnknownVar;
import com.jaiva.utils.*;

public class Main {
    public static String version = "1.0.0-beta.2";
    public static String author = "@yetnt or @prod.yetnt on some socials";
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
                Arrays.asList("--print-tokens", "-p", "--help", "-h", "--version", "-v", "--test", "-t", "--update",
                        "-u"));
        ArrayList<String> tokenArgs = new ArrayList<>(
                Arrays.asList("-s", "-j", "--string", "-json", "-jg", "--json-with-globals"));
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
                    System.out.println("Usage: jaiva-src");
                    System.out.println();
                    System.out.println("\tReturns the source folder of the jaiva source stuff");
                    System.out.println();
                    System.out.println("Usage: jaiva [options]");
                    System.out.println();
                    System.out.println("\tOptions:");
                    System.out.println();
                    System.out.println("\t(no flag): Starts a REPL.");
                    System.out.println("\t--print-tokens, -p: Print tokens REPL mode.");
                    System.out.println("\t--help, -h: Show this help message.");
                    System.out.println("\t--version, -v: Show the version of Jaiva.");
                    System.out.println("\t--test -t: Test command.");
                    System.out.println("\t--update, -u: Update Instructions.");
                    System.out.println();
                    System.out.println("Usage: jaiva <file.jiv> [options]");
                    System.out.println();
                    System.out.println("\tOptions:");
                    System.out.println();
                    System.out.println("\t(no flag): Run the file.");
                    System.out.println("\t--json, -j: Print tokens in JSON format.");
                    System.out.println("\t--json-with-globals, -jg: Print tokens including globals in JSON format.");
                    System.out.println("\t--string, -s: Print tokens in string format.");
                    System.out.println();
                    break;
                case "--version", "-v":
                    System.out.println(ASCII);
                    System.out.println("Jaiva! " + version);
                    System.out.println(
                            "Jaiva is a programming language that is designed to be easy to use and understand. (I'm speaking out my ass, I made this cuz i was bored on a random january)");
                    System.out.println("Made with love by: " + author);
                    break;
                case "--test", "-t":
                    String workingDir = System.getProperty("user.dir");
                    System.out.println(workingDir);
                    System.out.println("Test cmd");
                    break;
                case "--update", "-u":
                    System.out.println("");
                    System.out.println(
                            "Because i'm far too lazy to implement an auto upater, you'll have to just reinstall the jaiva folder into your existing one every time you want to update.");
                    System.out.println("I'm not making it easier, you can make a PR on the github though.");
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
        IConfig iconfig = new IConfig(args[0]);
        try {
            ArrayList<Token<?>> tokens = parseTokens(args[0], false);
            if (tokens.size() == 0)
                return;

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
                        System.out.println();
                        System.out.print("[");
                        for (int i = 0; i < tokens.size(); i++) {
                            Token<?> token = tokens.get(i);
                            System.out.print(token.getValue().toJson());
                            if (i != tokens.size() - 1) {
                                System.out.print(",");
                            }
                        }
                        System.out.print("]");
                        return;
                    case "-jg", "--json-with-globals":
                        System.out.println();
                        System.out.print("[");
                        System.out.print(new Globals().returnGlobalsJSON(false));
                        for (int i = 0; i < tokens.size(); i++) {
                            Token<?> token = tokens.get(i);
                            System.out.print(token.getValue().toJson());
                            if (i != tokens.size() - 1) {
                                System.out.print(",");
                            }
                        }
                        System.out.print("]");
                        return;
                    default:
                        System.out.println("Invalid token mode.");
                        return;
                }
            }

            Interpreter.interpret(tokens, Context.GLOBAL, new Globals().vfs, iconfig);

        } catch (Exception e) {
            iconfig.resources.release();
            System.err.println("Error.");
            e.printStackTrace();
        }
    }

    /**
     * Parses the tokens from a given file and returns them as an ArrayList of Token
     * objects.
     * 
     * @param filePath  The path to the file to be parsed. The file must have an
     *                  extension
     *                  of either `.jaiva`, `.jiv`, or `.jva`. If the file does not
     *                  have one
     *                  of these extensions, an empty list is returned.
     * @param returnVfs A boolean flag (currently unused in the method).
     * @return An ArrayList of Token objects parsed from the file. If the file is
     *         not found or an error occurs during parsing, an empty list is
     *         returned.
     * 
     *         The method processes the file line by line, identifying and handling
     *         various token types
     *         such as comments, variables, functions, and block chains. It also
     *         associates tooltips
     *         with certain token types when applicable. If the file cannot be
     *         found, an error message
     *         is printed to the console.
     * 
     *         Exceptions:
     *         - Prints an error message if the file is not found.
     *         - Prints the exception message if any other error occurs during
     *         parsing.
     */
    public static ArrayList<Token<?>> parseTokens(String filePath, boolean returnVfs) throws Exception {

        // here, args[0] is the file name that must end in either .jaiva, .jiv or .jva
        if (!filePath.endsWith(".jaiva") && !filePath.endsWith(".jiv") && !filePath.endsWith(".jva")) {
            System.out.println("I can't read whatever that lang is bro (i accept only .jaiva, .jiv or .jva)");
            return new ArrayList<>();
        }
        File myObj = new File(filePath);
        ArrayList<Token<?>> tokens = new ArrayList<>();
        Find.MultipleLinesOutput m = null;
        BlockChain b = null;
        Scanner scanner = new Scanner(myObj);
        String previousLine = "";
        String comment = null;
        TConfig config = new TConfig(callJaivaSrc());
        int lineNum = 1;
        while (scanner.hasNextLine()) {
            String line = (b != null ? b.getCurrentLine() : scanner.nextLine());
            // System.out.println(line);
            // System.out.println(line);
            Object something = Tokenizer.readLine(line, (b != null ? "" : previousLine), m, b, lineNum, config);
            if (something instanceof Find.MultipleLinesOutput) {
                m = (Find.MultipleLinesOutput) something;
                b = null;
            } else if (something instanceof ArrayList<?>) {
                m = null;
                b = null;
                ArrayList tks = (ArrayList<Token<?>>) ((ArrayList<Token<?>>) something).clone();
                if (((ArrayList<Token<?>>) tks).size() == 1) {
                    for (Token<?> t : ((ArrayList<Token<?>>) something)) {
                        TokenDefault l = t.getValue();
                        if (returnVfs && !l.exportSymbol) {
                            // clean.
                            ((ArrayList) tks).remove(t);
                            continue;
                        }
                        if (comment == null || (!(l instanceof TArrayVar) && !(l instanceof TNumberVar)
                                && !(l instanceof TStringVar)
                                && !(l instanceof TBooleanVar) && !(l instanceof TUnknownVar)
                                && !(l instanceof TFunction)))
                            continue;
                        l.tooltip = comment;
                        l.json.removeKey("toolTip");
                        l.json.append("toolTip", EscapeSequence.escapeJson(comment).trim(), true);
                    }
                }
                comment = null;
                tokens.addAll((ArrayList<Token<?>>) tks);
            } else if (something instanceof BlockChain) {
                m = null;
                comment = null;
                b = (BlockChain) something;
            } else if (something instanceof Token<?>
                    && ((Token<?>) something).getValue().name.equals("TDocsComment")) {
                b = null;
                m = null;
                comment = (comment == null ? "" : comment)
                        + ((TDocsComment) ((Token<?>) something).getValue()).comment;
            } else if (something instanceof Token<?>) {
                TokenDefault t = ((TokenDefault) ((Token<?>) something).getValue());
                if (returnVfs && !t.exportSymbol) {
                    // dont do anythin.
                } else if (comment != null
                        && ((t instanceof TArrayVar) || (t instanceof TNumberVar) || (t instanceof TStringVar)
                                || (t instanceof TBooleanVar) || (t instanceof TUnknownVar)
                                || (t instanceof TFunction))) {

                    t.tooltip = comment;
                    t.json.removeKey("toolTip");
                    t.json.append("toolTip", EscapeSequence.escapeJson(comment).trim(), true);
                    tokens.add((Token<?>) something);
                } else {
                    tokens.add((Token<?>) something);
                }
                b = null;
                m = null;
                comment = null;
            } else {
                b = null;
                m = null;
                comment = null;
            }
            previousLine = line;
            if (b == null)
                lineNum++;
            ;
        }

        // System.out.println(tokens.size());
        scanner.close();

        return tokens;
    }

    /**
     * Executes the "jaiva-src" command as an external process, captures its output,
     * and returns it as a string. The method combines the standard output and error
     * streams of the process and waits for the process to complete before returning
     * the result.
     *
     * @return A trimmed string containing the output of the "jaiva-src" process.
     *         If the process produces no output, an empty string is returned.
     * @throws IOException          If an I/O error occurs while starting or reading
     *                              from the process.
     * @throws InterruptedException If the current thread is interrupted while
     *                              waiting for the process to finish.
     */
    public static String callJaivaSrc() {
        StringBuilder output = new StringBuilder();
        try {
            ProcessBuilder builder = new ProcessBuilder("cmd", "/c", "jaiva-src");
            builder.redirectErrorStream(true);
            Process process = builder.start();

            // Read the output of the process
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append(System.lineSeparator());
                }
            }

            // Wait for the process to finish and check its exit code
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("jaiva-src exited with code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return output.toString().trim();
    }

}