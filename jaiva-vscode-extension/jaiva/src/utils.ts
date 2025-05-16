import path from "path";
import { MultiMap } from "./mmap";
import {
    TokenDefault,
    TCodeblock,
    TBooleanVar,
    TArrayVar,
    TStatement,
    TokenType,
    TFunction,
    TWhileLoop,
    TForLoop,
    TVarRef,
    TNumberVar,
    TIfStatement,
    TTryCatchStatement,
    TImport,
} from "./types";

const MAX_STRING_LENGTH = 20;

export function mergeMaps<K, V>(
    target: Map<K, V>,
    source: Map<K, V>
): Map<K, V> {
    for (const [key, value] of source.entries()) {
        target.set(key, value);
    }

    return target;
}

export type HoverToken = {
    token: TokenDefault;
    range: [number, number];
    lineNumber: number;
    hoverMsg: string;
    name: string;
    isParam?: boolean;
    paramIsFuncRef?: boolean;
};
function hoverMessage(
    token: TokenDefault | null = null,
    t: number,
    name: string,
    value: any = null,
    type: TokenType | null = null,
    params: string[] = []
): string {
    let st: TStatement =
        value !== null &&
        typeof value === "object" &&
        value.hasOwnProperty("type") &&
        value.type === "TStatement"
            ? value
            : null;

    switch (t) {
        case 0:
        case 1: // variable
            return (
                typeInference(st) +
                name +
                (t == 0 ? " <- " : " <-| ") +
                simplify(value, type) +
                (token?.lineNumber == -1 ? " @ GLOBAL" : "")
            );
        case 2: // function
            return (
                name +
                "(" +
                params.join(", ") +
                ")" +
                (token?.lineNumber == -1 ? " @ GLOBAL" : "")
            );
        case 3: // function parameter
            return (
                "[parameter] " +
                name +
                (value === true ? "(???)" : "") /*+ " <- " + simplify(value)*/
            );
        case 4: // for loop : index variable
            return (
                "[index] " +
                typeInference(st) +
                name +
                " <- " +
                simplify(value, type)
            );
        case 5: // for loop : element variable
            return (
                "[element] " +
                typeInference(st) +
                name +
                " <- " +
                simplify(value, type)
            );
        case 5: // error in chaai block
            return (
                "[chaai error] " +
                typeInference(st) +
                name +
                " <- " +
                '("error message")'
            );

        default:
            return "";
    }
}
/**
 * Simplifies a given value into a string representation.
 *
 * - If the value is a number or a boolean, it converts it to a string.
 * - If the value is a string, it truncates it to a maximum length (defined by `MAX_STRING_LENGTH`)
 *   and wraps it in double quotes.
 * - If the value is an array, it recursively simplifies each element and joins them with commas,
 *   wrapping the result in square brackets.
 * - For all other types, it returns the placeholder string "_?_" to indicate an unsupported type.
 *
 * @param v - The value to simplify. Can be of any type.
 * @returns A string representation of the simplified value.
 */
function simplify(v: any, type: TokenType | null = null): string {
    const t = primitive(v);
    return type === "TStringVar"
        ? '"' +
              v.substring(0, Math.min(v.length, MAX_STRING_LENGTH / 2)) +
              (v.length > MAX_STRING_LENGTH / 2 ? "..." : "") +
              '"'
        : t != undefined
        ? t
        : Array.isArray(v)
        ? "[" + v.map((v) => simplify(v)).join(", ") + "]"
        : "???";
}

function typeInference(value: TStatement | null): string {
    return value !== null
        ? value.statementType == 0
            ? "(boolean?) "
            : "(number?, string?) "
        : "";
}

export function primitive(v: any) {
    let number = Number.parseInt(v);
    if (!isNaN(number)) return number.toString();

    if (v === true || v === "true" || v === "yebo") return "yebo";
    if (v === false || v === "false" || v === "aowa") return "aowa";
}

/**
 * Parses a list of tokens and returns a map of hover tokens with associated metadata.
 *
 * @param tokens - An array of tokens of type `TokenDefault[]` to be processed.
 * @param parentTCodeblock - An optional parent code block of type `TCodeblock`
 *                           that provides additional context for the tokens. Defaults to `null`.
 * @returns A `Map` where the keys are token names (as `String`) and the values are `HoverToken` objects
 *          containing metadata such as the token, range, line number, hover message, and name.
 *
 * @remarks
 * This function processes tokens of specific types (`TStringVar`, `TBooleanVar`, `TNumberVar`)
 * and generates hover information for them. If a parent code block is provided, its line range
 * is included in the hover token metadata; otherwise, a default range of `[-1, -1]` is used.
 *
 * @example
 * ```typescript
 * const tokens: TokenDefault[] = [
 *     { type: "TBooleanVar", name: "isActive", lineNumber: 5, value: true },
 *     { type: "TStringVar", name: "username", lineNumber: 10, value: "JohnDoe" }
 * ];
 * const hoverTokens = parseAndReturnHoverTokens(tokens);
 * console.log(hoverTokens);
 * ```
 */
export function parseAndReturnHoverTokens(
    tokens: TokenDefault[],
    parentTCodeblock: TCodeblock | null = null,
    currentFilePath?: string,
    hTokens?: Map<string, MultiMap<string, HoverToken>>
) {
    let hoverTokens: MultiMap<string, HoverToken> = new MultiMap();
    tokens.forEach((token) => {
        switch (token.type) {
            case "TUnknownVar":
            case "TStringVar":
            case "TBooleanVar":
            case "TArrayVar":
            case "TNumberVar": {
                let t: TBooleanVar = token as TBooleanVar;
                hoverTokens.add(token.name, {
                    token: token,
                    range:
                        parentTCodeblock === null
                            ? [-1, -1]
                            : [
                                  parentTCodeblock.lineNumber,
                                  parentTCodeblock.lineEnd,
                              ],
                    lineNumber: token.lineNumber,
                    hoverMsg: hoverMessage(
                        token,
                        token.type === "TArrayVar" ? 1 : 0,
                        token.name,
                        t.value,
                        token.type
                    ),
                    name: token.name,
                });
                break;
            }
            case "TFunction": {
                let t: TFunction = token as TFunction;
                let Fname = t.name.replace("F~", "");
                hoverTokens.add(Fname, {
                    token: token,
                    range:
                        parentTCodeblock === null
                            ? [-1, -1]
                            : [
                                  parentTCodeblock.lineNumber,
                                  parentTCodeblock.lineEnd,
                              ],
                    lineNumber: token.lineNumber,
                    hoverMsg: hoverMessage(
                        token,
                        2,
                        Fname,
                        null,
                        token.type,
                        t.args
                    ),
                    name: Fname,
                });
                if (token.lineNumber !== -1) {
                    // if lineNumber is == -1 then this is a global function's paramters which is not user defined meaning they dont need hovers for this.
                    t.args.forEach((argName) => {
                        let isFuncReference =
                            argName.charAt(0) === "F" &&
                            argName.charAt(1) === "~";
                        let name = argName.substring(argName.indexOf("~") + 1);
                        hoverTokens.add(name, {
                            token: token,
                            range: [t.lineNumber, t.body.lineEnd],
                            lineNumber: token.lineNumber,
                            hoverMsg: hoverMessage(
                                token,
                                3,
                                name,
                                isFuncReference
                            ),
                            name,
                            isParam: true,
                            paramIsFuncRef: isFuncReference,
                        });
                    });
                }
                // handle TCodeblock
                if (t.body !== null) {
                    let block = parseAndReturnHoverTokens(t.body.lines, t.body);
                    hoverTokens.addAll(block);
                }
                break;
            }
            case "TWhileLoop": {
                let t: TWhileLoop = token as TWhileLoop;

                if (t.body !== null) {
                    let block = parseAndReturnHoverTokens(t.body.lines, t.body);
                    hoverTokens.addAll(block);
                }
                break;
            }
            case "TForLoop": {
                let t: TForLoop = token as TForLoop;

                let variable: TNumberVar = t.variable as TNumberVar;

                if (t.body !== null) {
                    let block = parseAndReturnHoverTokens(t.body.lines, t.body);
                    hoverTokens.addAll(block);
                    hoverTokens.add(variable.name, {
                        token: t as TokenDefault,
                        range: [t.lineNumber, t.body.lineEnd],
                        lineNumber: variable.lineNumber,
                        hoverMsg: hoverMessage(
                            t as TokenDefault,
                            t.arrayVariable == null ? 4 : 5,
                            variable.name,
                            variable.value,
                            "TNumberVar"
                        ),
                        name: variable.name,
                    });
                }
                break;
            }
            case "TIfStatement": {
                let t: TIfStatement = token as TIfStatement;

                if (t.body !== null) {
                    let block = parseAndReturnHoverTokens(t.body.lines, t.body);
                    hoverTokens.addAll(block);
                }
                if (t.elseBody !== null) {
                    let block = parseAndReturnHoverTokens(
                        t.elseBody.lines,
                        t.elseBody
                    );
                    hoverTokens.addAll(block);
                }
                if (t.elseIfs !== null && t.elseIfs.length > 0)
                    t.elseIfs.forEach((ifS) => {
                        let block = parseAndReturnHoverTokens(
                            ifS.body.lines,
                            ifS.body
                        );
                        hoverTokens.addAll(block);
                    });
                break;
            }
            case "TImport": {
                if (currentFilePath && hTokens) {
                    let t: TImport = token as TImport;
                    let filePath = t.filePath.replace("\t", "\\t").trim();
                    if (!path.isAbsolute(t.filePath)) {
                        filePath = path.resolve(
                            currentFilePath || "",
                            "..",
                            t.filePath
                        );
                    }

                    let hToken: MultiMap<string, HoverToken> | null =
                        hTokens?.get(filePath) ?? null;

                    if (hToken) {
                        let filtered = hToken.filter((key, value) => {
                            return (
                                !("isParam" in value[0]) &&
                                (t.symbols.length > 0
                                    ? t.symbols.includes(key) &&
                                      (value[0].token.exportSymbol == true ||
                                          value[0].token.exportSymbol == "true")
                                    : value[0].token.exportSymbol == true ||
                                      value[0].token.exportSymbol == "true")
                            );
                        });
                        // make entries global
                        let singleValues: MultiMap<string, HoverToken> =
                            new MultiMap();
                        for (let [name, value] of filtered.entries()) {
                            const shiftedValue = value.shift();
                            if (shiftedValue) {
                                shiftedValue.range = [-1, -1];
                                value = [shiftedValue];
                            }
                            singleValues.set(name, ...value);
                        }

                        hoverTokens.addAll(singleValues);
                    }
                }
                break;
            }
            case "TTryCatchStatement": {
                let t: TTryCatchStatement = token as TTryCatchStatement;

                if (t.try !== null) {
                    let block = parseAndReturnHoverTokens(t.try.lines, t.try);
                    hoverTokens.addAll(block);
                }
                if (t.catch !== null) {
                    let block = parseAndReturnHoverTokens(
                        t.catch.lines,
                        t.catch
                    );
                    hoverTokens.addAll(block);
                    hoverTokens.add("error", {
                        token: {
                            type: "TStringVar",
                            name: "error",
                            lineNumber: t.lineNumber,
                            exportSymbol: false,
                            toolTip:
                                "The error message that was caught (hopefully a string)",
                        },
                        range: [t.lineNumber, t.catch.lineEnd],
                        lineNumber: t.catch.lineNumber,
                        hoverMsg: hoverMessage(t as TokenDefault, 5, "error"),
                        name: "error",
                    });
                }
                break;
            }
        }
    });

    return hoverTokens;
}
