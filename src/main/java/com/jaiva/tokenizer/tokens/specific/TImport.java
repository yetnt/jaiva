package com.jaiva.tokenizer.tokens.specific;

import com.jaiva.errors.JaivaException;
import com.jaiva.tokenizer.tokens.TStatement;
import com.jaiva.tokenizer.tokens.Token;
import com.jaiva.tokenizer.tokens.TokenDefault;

import java.util.ArrayList;

/**
 * Represents an important statemnent such as:
 * tsea "path"!
 * or
 * {@code tsea "path" <- func1, func2, func3!}
 */
public class TImport extends TokenDefault<TImport> implements TStatement {
    /**
     * Indicates whether this import is a library import.
     * <p>
     * If true, the import is considered a library import.
     */
    public boolean isLib = false;
    /**
     * The import file path.
     */
    public String filePath;
    /**
     * Specified files name without any folders
     */
    public String fileName;
    /**
     * The imported symbols. This arraylist may be empty if no symbols are imported
     * specifically.
     */
    public ArrayList<String> symbols = new ArrayList<>();

    /**
     * Constructor for TImport
     *
     * @param file     The file path.
     * @param fileName the file name
     * @param isLib    Whther this is part of a library or not
     * @param ln       The line number.
     */
    public TImport(String file, String fileName, boolean isLib, int ln) {
        super("TImport", ln);
        filePath = file;
        this.isLib = isLib;
        this.fileName = fileName;
    }

    /**
     * Constructor for TImport
     *
     * @param file     The file path.
     * @param fileName File name
     * @param isLib    Whther its a libaray import
     * @param names    The imported symbols.
     * @param ln       The line number.
     */
    public TImport(String file, String fileName, boolean isLib, ArrayList<String> names, int ln) {
        super("TImport", ln);
        filePath = file;
        this.isLib = isLib;
        this.fileName = fileName;
        symbols.addAll(names);
    }

    @Override
    public String toJson() throws JaivaException {
        json.append("filePath", filePath, false);
        json.append("fileName", fileName, false);
        json.append("isLib", isLib, false);
        json.append("symbols", symbols, true);
        return super.toJson();
    }

    /**
     * Converts this token to the default {@link Token}
     *
     * @return {@link Token} with a T value of {@link TImport}
     */
    public Token<TImport> toToken() {
        return new Token<>(this);
    }
}
