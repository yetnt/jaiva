package com.jaiva.full;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Objects;

public class Files {
    static final Path FILE_JIV;
    static final Path FILE2_JIV;
    static final Path IMPORT_JIV;
    static final Path COMMENTS_JIV;
    static final Path BUNDLER_JIV;
    static final Path LAMBDA_JVA;


    static {
        try {

            FILE_JIV = Path.of(
                    Objects.requireNonNull(
                                    Files.class.getClassLoader()
                                            .getResource("file.jiv"))
                            .toURI());

            FILE2_JIV = Path.of(
                    Objects.requireNonNull(
                                    Files.class.getClassLoader()
                                            .getResource("file2.jiv"))
                            .toURI());

            IMPORT_JIV = Path.of(
                    Objects.requireNonNull(
                                    Files.class.getClassLoader()
                                            .getResource("import.jiv"))
                            .toURI());

            COMMENTS_JIV = Path.of(
                    Objects.requireNonNull(
                                    Files.class.getClassLoader()
                                            .getResource("comments.jiv"))
                            .toURI());

            BUNDLER_JIV = Path.of(
                    Objects.requireNonNull(
                                    Files.class.getClassLoader()
                                            .getResource("bundler.jiv"))
                            .toURI());

            LAMBDA_JVA = Path.of(
                    Objects.requireNonNull(
                                    Files.class.getClassLoader()
                                            .getResource("lambda.jva"))
                            .toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
