package com.jaiva.md;

/**
 * The format of the output MD file
 * @param includeTableOfContents   Whether to include a table of contents. in this case the entire structure is
 * @param splitSymbols             Whether to split the file into a dedicated variables and functions headings.
 *                                 Otherwise its headed symbols
 * @param preferFunctions          If {@link #splitSymbols} is true, then prefer the Functions heading before the variables.
 * @param declareExportedLibraries If this library is a class and exports other symbols, for it to be declared at the top level.
 *                                 otherwise skipped.
 * @param listExports              If {@link #declareExportedLibraries} is true, whether this should then further walk the graph and list said libraries
 */
public record MDOutputProps(boolean includeTableOfContents, boolean splitSymbols, boolean preferFunctions,
                            boolean declareExportedLibraries, boolean listExports) {

}
