package com.jaiva;

import java.io.*;
import java.util.*;

import com.jaiva.errors.*;
import com.jaiva.errors.TokenizerException;
import com.jaiva.errors.JaivaException;
import com.jaiva.errors.JaivaException.*;
import com.jaiva.interpreter.*;
import com.jaiva.interpreter.globals.Types;
import com.jaiva.interpreter.globals.Globals;
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

/**
 * The Main class serves as the entry point for the Jaiva programming language
 * tokenizer and
 * interpreter. It handles command-line arguments, initializes the REPL
 * (Read-Eval-Print Loop),
 * and manages the parsing and interpretation of Jaiva source files.
 * 
 * The class contains methods for parsing tokens from source files, displaying
 * help information, and executing the interpreter.
 */
public class Main {
    /**
     * List of command-line arguments that are used to start the REPL mode.
     * These arguments include options for printing tokens, displaying help,
     * showing version information, running tests, and updating instructions.
     */
    public static ArrayList<String> replArgs = new ArrayList<>(
            Arrays.asList("--print-tokens", "-p", "--help", "-h", "--version", "-v", "--test", "-t", "--update",
                    "-u"));
    /**
     * List of command-line arguments that are used when running a Jaiva source
     * file.
     * These arguments include options for printing tokens in string format,
     * JSON format, JSON format with globals, and enabling debug mode.
     */
    public static ArrayList<String> tokenArgs = new ArrayList<>(
            Arrays.asList("-s", "-j", "--string", "-json", "-jg", "--json-with-globals", "-d", "--debug"));
    /**
     * Version of the Jaiva programming language interpreter. This is a string
     * variable that holds the version number in the format
     * "major.minor.patch-(beta|rc|alpha)
     * .<build number>"
     * (SemVar).
     */
    public static String version = "2.0.0-beta.4";
    /**
     * Author, it's just me.
     */
    public static String author = "@yetnt or @prod.yetnt on some socials";
    /**
     * ASCII art representation of the Jaiva logo. This is a multi-line string
     */
    public static String ASCII = """
                                       .+X$$&&&&&&&&$+.
                                       xX..         .;X$.
                                       xx              +&:
                                       xx              .&+
                                       xx               &+
                                       xx               &+
                                       xx               &+
                                       xx               &+
                                       xx               &+
                                       xx               &+
                                       xx               &+
                                       xx               &+
                                       xx               &+    .:;xX$$Xx;:.
                                       xx               &+ .:&&+.     ..x&$.
                                       xx               &+.$x.           .:$X.
                                       xx               &X&;               .Xx
                                       xx               &&X                 .&.
                                       xx               &&x                 .$:
                                       xx               &&&.                +$.
                                       xx               &+x$.             .+&:    .x&$$&x.
                                       xx               &+ :$X;.           :&;    :&:  :&:
                                       xx               &+   .+X&&$XX$&&&.  ;&.  .$x  .X$.
                                       xx               &+             .$x  .X$..+$.  ;&.
                                       xx               &+              .$+  .&+.&:  :&;
                                       xx               &+               ;&;  :&$+  .$x.
                                       xx               &+                ;$. .+$. .Xx
                                       xx               &+ .;xX$&&&&&&&X;..+&:    .X$.
                                       xx               &+ &.           .X&:+&:  .XX
                     +$&Xx+xX&$+.      xx               &+ &.             .$;.:++;:.
                    ;$..;++;. .x$.     xx               &+ &.             .x$
                     ;&&&$$&&; .&;     xx               &+ &.              x$.
                   .;&x. ..... .&;     xx               &+ &.              x$.
                   :$; .$&&$&+ .&;     Xx               &+ &.              x$.
                   .$+..;X$$X: .&;     $;              .&+ &.              x$.
                    .X$x;:::;+X&x.    +$.              :&; &.              x$.
                       .;+++;:.     .+&:               ;&: &.              x$.
              .;&x:...           ..+&X.                Xx  &.              x$.
             :$X..:+X&&$$$XXX$$$&&x:.                 +$.  &.              x$.
            .&;          .....                       +&:   &.              x$.  .+$$XxxxX&$;
            +$.                                    .x$.    &.              x$.  ;$..:;;;. .X$.
            x$.                                  .:$X.     &.              x$.  .+&&&$$&&: .&;
            .&+                                .;&X..      &.              x$.  ;&X......  .&;
             .X$;                           .+$$;.         &.              x$. :&; .$&$$&; .&;
               .+$$x+:.              ..;+X$$x;.            &.              x$. :&; .+$$$X: .&;
                   .:+xX$$$&&&&&&$$$XXx;.                  &.              x$.  :$$+:::::;x&x.
                                                           &.              x$.     .;+++;.
                        """;

    @SuppressWarnings("unchecked")
    /**
     * The main method of the Jaiva programming language interpreter. It serves as
     * the entry point for the program.
     * 
     * @param args Command-line arguments passed to the program. These arguments
     *             can be used to specify options and configurations for the
     *             interpreter.
     */
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            new REPL(0);
            System.exit(0);
            return;
        } else if (replArgs.contains(args[0])) {
            switch (args[0]) {
                case "--print-tokens", "-p" -> {// System.out.println("Print token REPL mode.");
                    new REPL(1);
                    System.exit(0);
                }
                case "--help", "-h" -> {
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
                    System.out.println(
                            "\t--debug, -d: Enable debug mode. (All errors will print as an uncaught Java Exception.)");
                    System.out.println();
                    System.exit(0);
                }
                case "--version", "-v" -> {
                    System.out.println(ASCII);
                    System.out.println("Jaiva! " + version);
                    System.out.println(
                            "Jaiva is a programming language that is designed to be easy to use and understand. (I'm speaking out my ass, I made this cuz i was bored on a random january)");
                    System.out.println("Made with love by: " + author);
                    System.exit(0);
                }
                case "--test", "-t" -> {
                    System.out.println(new Types().toJson());
                    System.exit(0);
                }
                case "--update", "-u" -> {
                    System.out.println("");
                    System.out.println(
                            "Because i'm far too lazy to implement an auto upater, you'll have to just reinstall the jaiva folder into your existing one every time you want to update.");
                    System.out.println("I'm not making it easier, you can make a PR on the github though.");
                    System.exit(0);
                }
                default -> {
                    System.out.println("Invalid REPL mode.");
                    System.exit(-1);
                }
            }
            return;
        } else if (!args[0].contains(".")) {
            // this is to catch the case where the user does not provide a file name
            System.out.println("Provide a file name or a cmd flag kau.");
            System.exit(-1);
            return;
        }

        IConfig iconfig = new IConfig(args, args[0], callJaivaSrc());
        boolean debug = false;
        try {
            if (args.length > 1)
                if (args[1].equals("-d") || args[1].equals("--debug"))
                    debug = true;
            ArrayList<Token<?>> tokens = parseTokens(args[0], false);
            if (tokens.isEmpty()) {
                System.exit(0);
                return;
            }

            if ((args.length > 1) && tokenArgs.contains(args[1])) {
                // here, args[1] is the tokens mode
                // return tokens mode.
                switch (args[1]) {
                    case "-d", "--debug" -> {
                        break;
                    }
                    case "-s", "--string" -> {
                        for (Token<?> t : tokens) {
                            System.out.println(t.toString());
                        }
                        System.exit(0);
                        return;
                    }
                    case "-j", "--json" -> {
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
                        System.exit(0);
                        return;
                    }
                    case "-jg", "--json-with-globals" -> {
                        System.out.println();
                        System.out.print("[");
                        System.out.print(new Globals(iconfig).returnGlobalsJSON(false));
                        for (int i = 0; i < tokens.size(); i++) {
                            Token<?> token = tokens.get(i);
                            System.out.print(token.getValue().toJson());
                            if (i != tokens.size() - 1) {
                                System.out.print(",");
                            }
                        }
                        System.out.print("]");
                        System.exit(0);
                        return;
                    }
                    default -> {
                        System.out.println("Invalid token mode.");
                        System.exit(-1);
                        return;
                    }
                }
            }

            Interpreter.interpret(tokens, Context.GLOBAL, new Globals(iconfig).vfs, iconfig);

            // if we reached here, everythign went well!

            System.exit(0);

        } catch (Exception e) {
            iconfig.resources.release();
            if (debug)
                throw e; // throw the error as uncaught when debug mode is enabled so that we get the
                         // entire error along with the stack trace.
            if (e instanceof InterpreterException) {
                System.out.println("Error while interpreting code: ");
                System.out.println(e.getMessage());
                System.exit(1);
            } else if (e instanceof TokenizerException) {
                System.out.println("Error while parsing code: ");
                System.out.println(e.getMessage());
                System.exit(-1);
            } else if (e instanceof JaivaException) {
                // TokenizerException and InterpreterException extend off of JaivaException, so
                // if we dont catch those, cagtch this one with generic message.
                System.out.println("Error: ");
                System.out.println(e.getMessage());
                System.exit(-1);
            } else {
                // Some other thing, this genrally shouldnt happen as all eror cases we need to
                // account for where ever it is and not here in the Main class.
                System.out.println("\"Something\" went wrong, and it's proabaly not your fault.");
                System.out.println(e.getMessage());
                System.exit(-1);
            }
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
            throw new JaivaException("I can't read whatever that lang is bro (i accept only .jaiva, .jiv or .jva)");
        }
        File myObj = new File(filePath);
        ArrayList<Token<?>> tokens = new ArrayList<>();
        MultipleLinesOutput m = null;
        BlockChain b = null;
        Scanner scanner = null;
        try {
            scanner = new Scanner(myObj);
        } catch (FileNotFoundException e) {
            throw new UnknownFileException(filePath);
        }
        String previousLine = "";
        String comment = null;
        TConfig config = new TConfig(callJaivaSrc());
        int lineNum = 1;
        while (scanner.hasNextLine()) {
            String line = (b != null ? b.getCurrentLine() : scanner.nextLine());
            Object something = Tokenizer.readLine(line, (b != null ? "" : previousLine), m, b, lineNum, config);
            if (something instanceof MultipleLinesOutput multipleLinesOutput) {
                m = multipleLinesOutput;
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
            } else if (something instanceof BlockChain blockChain) {
                m = null;
                comment = null;
                b = blockChain;
            } else if (something instanceof Token<?>
                    && ((Token<?>) something).getValue().name.equals("TDocsComment")) {
                b = null;
                m = null;
                comment = (comment == null ? "" : comment)
                        + ((TDocsComment) ((Token<?>) something).getValue()).comment;
            } else if (something instanceof Token<?> token) {
                TokenDefault t = ((TokenDefault) token.getValue());
                if (returnVfs && !t.exportSymbol) {
                    // dont do anythin.
                } else if (comment != null
                        && ((t instanceof TArrayVar) || (t instanceof TNumberVar) || (t instanceof TStringVar)
                                || (t instanceof TBooleanVar) || (t instanceof TUnknownVar)
                                || (t instanceof TFunction))) {

                    t.tooltip = comment;
                    t.json.removeKey("toolTip");
                    t.json.append("toolTip", EscapeSequence.escapeJson(comment).trim(), true);
                    tokens.add(token);
                } else {
                    tokens.add(token);
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
        }

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
            if (exitCode != 0 && exitCode != 20) {
                // TODO: idk wtf is up with 20, but in linux it returns 20 and spams the console
                System.err.println("jaiva-src exited with code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return output.toString().trim();
    }

}