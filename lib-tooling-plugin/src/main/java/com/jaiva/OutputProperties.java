package com.jaiva;

import org.apache.maven.plugins.annotations.Parameter;

import java.util.ArrayList;

public class OutputProperties {

    /**
     * Whether to include a table of contents heading with a table.
     */
    @Parameter(defaultValue = "false")
    private boolean includeTableOfContents;

    /**
     * Whether to split symbols into functions and variables
     */
    @Parameter(defaultValue = "false")
    private boolean splitSymbols;

    /**
     * If split symbols is true, then prefer functions over variables
     */
    @Parameter(defaultValue = "false")
    private boolean preferFunctions;

    /**
     * Whether the library should declare that it exports other libraries
     */
    @Parameter(defaultValue = "false")
    private boolean declareExportedLibraries;

    /**
     * If declare exported libraries is true, then this will further
     */
    @Parameter(defaultValue = "false")
    private boolean listExports;

    // getters — Maven needs these for some injection paths depending on version,
    // and you'll want them anyway to build your MDOutputProps record from this
    public boolean isIncludeTableOfContents() { return includeTableOfContents; }
    public boolean isSplitSymbols() { return splitSymbols; }
    public boolean isPreferFunctions() { return preferFunctions; }
    public boolean isDeclareExportedLibraries() { return declareExportedLibraries; }
    public boolean isListExports() { return listExports; }

    public ArrayList<Boolean> asList() {
        ArrayList<Boolean> result = new ArrayList<>();
        result.add(includeTableOfContents);
        result.add(splitSymbols);
        result.add(preferFunctions);
        result.add(declareExportedLibraries);
        result.add(listExports);
        return result;
    }

    public static ArrayList<Boolean> allTrue() {
        return new  ArrayList<Boolean>() {{
            add(true); add(true); add(true); add(true); add(true);
        }};
    }
}
