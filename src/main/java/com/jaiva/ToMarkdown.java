package com.jaiva;

import com.jaiva.errors.TokenizerException;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.tokenizer.jdoc.JDoc;
import com.jaiva.tokenizer.jdoc.tags.Tag;
import com.jaiva.tokenizer.tokens.TSymbol;
import com.jaiva.tokenizer.tokens.TVariable;
import com.jaiva.tokenizer.tokens.Token;
import com.jaiva.tokenizer.tokens.TokenDefault;
import com.jaiva.tokenizer.tokens.specific.*;
import com.jaiva.utils.CCol;
import com.jaiva.utils.bucket.Bucket;
import com.jaiva.utils.bucket.Buckets;
import com.jaiva.utils.MarkDownLiteral;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ToMarkdown {

    private ArrayList<String> fileContents = new ArrayList<>();
    private ArrayList<Token<?>> tokens;
    private Scanner reader = new Scanner(System.in);

    public ToMarkdown(ArrayList<Token<?>> ts, IConfig<?> i, Path outputDir, boolean globals) throws IOException, TokenizerException.CatchAllException {
        tokens = ts;
        run(i, outputDir, globals);
    }

    private void run(IConfig<?> i, Path outputDir, boolean globals) throws IOException, TokenizerException.CatchAllException {
        // For each token, we need to make a markdown file. Ohhh boy.

        // Pre-file: Filter the arraylist to only hold Exportable Tokens.
        List<TokenDefault> exportables = (List<TokenDefault>) tokens.stream()
                .map(Token::value)
                .filter(t -> t instanceof TSymbol)
                .filter(t -> (globals ? globals : t.exportSymbol))
                .toList();

        if (exportables.isEmpty()) {
            printToConsole(CCol.print("Found no exported symbols to document. Early exit.", CCol.TEXT.YELLOW));
            return;
        }

        // Step 1, Name the documentation
        printToFile(
                new MarkDownLiteral(i.filePath.getFileName().toString()).inlineCode().title(MarkDownLiteral.Title.TITLE).toString()
        );
        printToConsole("Documentation for:" + CCol.print(i.filePath.getFileName().toString(), CCol.FONT.UNDERLINE));

        printToFile();

        // Step 2, Ask for a small description.
        String desc = askForUserInput("Input a description describing this library:", "cool lib!!");
        printToFile(desc);

        printToFile();

        // Step 3, Ask if the user would like to split into a "Variables" and "Functions" categories, or a single "Symbols"
        char option = askForUserInput("Should symbols be split into a Variables and Functions subsections? [Y/N]:", 'n');

        // And Step 4, Print.
        if (option == 'n') {
            printToFile(
                    new MarkDownLiteral("Symbols").title(MarkDownLiteral.Title.SUBTITLE).toString()
            );
            for (TokenDefault t : exportables) {
                printToFile();
                printSymbolToFile(option, t);
            }
        } else if (option == 'y') {
            ArrayList<ArrayList<TokenDefault>> buckets = Buckets.of(
                    new ArrayList<>(exportables),
                    TFunction.class,
                    TVariable.class);
            printToFile();
            for (int j = 0; j < buckets.size(); j++) {
                ArrayList<TokenDefault> bucket = buckets.get(j);
                printToFile(
                        new MarkDownLiteral(
                                (j == 0 ? "Functions" : "Variables")
                        ).title(MarkDownLiteral.Title.SUBTITLE).toString()
                );
                for (TokenDefault t : bucket) {
                    printToFile();
                    printSymbolToFile(option, t);
                }
            }
        } else {
            printToConsole(CCol.print("Invalid input. Either Y or N", CCol.TEXT.RED));
            reader.close();
            return;
        }

        reader.close();

        // Create a new markdown file in outputDir
        File out = new File(
                outputDir.resolve(i.filePath.getFileName().toString() + ".md").toFile().toURI()
        );


        try {
            boolean created = out.createNewFile();
            if (created) {
                out.setWritable(true);
                printToConsole(CCol.print("File " + out.getAbsolutePath() + " has been created.", CCol.TEXT.GREEN));
            } else {
                printToConsole(CCol.print("File " + out.getAbsolutePath() + " and will be overwritten.", CCol.TEXT.YELLOW));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        PrintWriter fw = new PrintWriter(new FileWriter(out));
        fileContents.forEach(fw::println);
        fw.close();
    }

    /**
     * Prints the markdown representation of a given symbol (function or variable) to the file contents.
     *
     * @param option The user's choice for splitting symbols ('n' for no split, 'y' for split).
     * @param symbol The {@link TokenDefault} representing the symbol to be documented.
     * @throws TokenizerException.CatchAllException If an unexpected token type is encountered.
     */
    private void printSymbolToFile(char option, TokenDefault symbol) throws TokenizerException.CatchAllException {
        // .title(option == 'n' ? 3 : 4)
        if (symbol instanceof TFunction f) {
            String type = askForUserInput("What does " +
                    CCol.print(f.name.startsWith("F~") ? f.name.substring(2) : f.name, CCol.FONT.BOLD, CCol.FONT.ITALIC)
                    + " return? [leave blank if \"idk\"]:", "idk").toLowerCase();
            type = switch (type) {
                case "" -> "idk";
                case "bool" -> "boolean";
                case "array" -> "[]";
                case "int", "integer", "double" -> "number";
                default -> type; // For custom types. It's the users job to explain what they mean
                                // e.g. All functions within "jaiva/file" array
                                // are documented to return a "[array]". Which the markdown file explains
                                // that it's a structure of a normal array special to this library.
            };
            printToFile(
                    new MarkDownLiteral(new MarkDownLiteral(f.toDefinitionString() + " -> ").inlineCode().toString() +
                            new MarkDownLiteral(type).inlineCode().bold().italics().toString())
                            .title(MarkDownLiteral.Title.SECTION)
                            .toString()
            );
        } else  {
            // otherwise, it's just a variable.
            String type = switch (symbol) {
                case TNumberVar ignored -> "number";
                case TStringVar ignored -> "string";
                case TBooleanVar ignored -> "boolean";
                case TArrayVar ignored -> "[]";
                case TUnknownScalar<?, ?> ignored -> "idk";
                default -> throw new TokenizerException.CatchAllException("Unexpected Value: " + symbol, 0);
            };
            printToFile(
                    new MarkDownLiteral(new MarkDownLiteral(symbol.name + " <- ").inlineCode().toString() +
                            new MarkDownLiteral(type).inlineCode().bold().italics().toString())
                            .title(MarkDownLiteral.Title.SECTION)
                            .toString()
            );
        }

        JDoc jDoc = JDoc.from(symbol.tooltip);
        if (jDoc == null) {
            printToFile(new MarkDownLiteral("Symbol has no documentation...").bold().italics().toString());
            return;
        }

        if (!jDoc.getDependencies().isEmpty()) {
            printToFile();
            printToFile(
                    new MarkDownLiteral("Please properly link the dependencies. Thank you! :)").comment().toString()
            );
            printToFile();
            printToFile(
                    MarkDownLiteral.GithubBlockQuote(
                            MarkDownLiteral.GithubBlockQuote.WARNING,
                            new MarkDownLiteral(
                                    "This symbol depends on: " +
                                    new MarkDownLiteral(String.join(", ", jDoc.getDependencies())).italics().toString() +
                                            ". It may fail if all of these symbols aren't imported!"
                            ).bold().toString()
                    )
            );
        }

        if (!jDoc.getDeprecatedString().isEmpty()) {
            printToFile();
            printToFile(
                    MarkDownLiteral.GithubBlockQuote(
                            MarkDownLiteral.GithubBlockQuote.IMPORTANT,
                            new MarkDownLiteral("This symbol is deprecated!").bold().italics() +
                                    new MarkDownLiteral(jDoc.getDeprecatedString()).inlineCode().italics().toString()
                    )
            );
        }

        printToFile();
        printToFile(jDoc.getDescription());

        if (!jDoc.getParameters().isEmpty()) {
            printToFile();
            ArrayList<String> params = new ArrayList<>();
            for (Tag.DParameter p : jDoc.getParameters()) {
                MarkDownLiteral param = new MarkDownLiteral(p.varName + (p.optional ? "?" : "")).inlineCode();
                params.add(
                        (p.optional ? param.italics() : param.bold()).toString() +
                                " <- " +
                                new MarkDownLiteral("\""+p.type+"\"").inlineCode().toString() +
                                " : " +
                                new MarkDownLiteral(p.desc).italics().toString()
                );
            }
            printToFile(MarkDownLiteral.asList(params));
        }

        if (!jDoc.getReturns().isEmpty()) {
            printToFile();
            printToFile("Returns :");
            printToFile(
                    MarkDownLiteral.asBlockQuote(
                            new ArrayList<>(List.of(new MarkDownLiteral(jDoc.getReturns()).bold().italics().toString()))
                    )
            );
        }

        if (!jDoc.getExample().isEmpty()) {
            printToFile();
            printToFile(new MarkDownLiteral("Example:").bold().toString());
            printToFile(
                    MarkDownLiteral.asCodeBlock(
                            jDoc.getExample(),
                            "jaiva"
                    )
            );
        }

        if (!jDoc.getNote().isEmpty()) {
            printToFile();
            printToFile(
                    MarkDownLiteral.GithubBlockQuote(
                            MarkDownLiteral.GithubBlockQuote.NOTE,
                            new MarkDownLiteral(jDoc.getNote()).italics().toString()
                    )
            );
        }

    }

    private void printToFile() {
        fileContents.add(" ");
    }
    private void printToFile(String m) {
        fileContents.add(m);
    }
    private void printToFile(ArrayList<String> m) {
        fileContents.addAll(m);
    }

    private void printToConsole(String message) {
        System.out.println(message);
    }

    private <T> T askForUserInput(String message, T defaultInput) {
        System.out.print(message + " ");
        String line = reader.nextLine();

        if (line == null || line.isEmpty()) return defaultInput;

        Object result = switch (defaultInput) {
            case Integer ignored -> Integer.parseInt(line);
            case Double ignored -> Double.parseDouble(line);
            case Boolean ignored -> Boolean.parseBoolean(line);
            case Character ignored -> line.toLowerCase().charAt(0);
            default -> line;
        };

        return (T) result;
    }

}
