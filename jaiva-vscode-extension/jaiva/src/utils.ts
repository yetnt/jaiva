import { MultiMap } from "./mmap";
import {
    TokenDefault,
    TCodeblock,
    TBooleanVar,
    TArrayVar,
    TStatement,
    TokenType,
    TFunction,
} from "./types";

const MAX_STRING_LENGTH = 20;

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
    token: TokenDefault,
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
                (token.lineNumber == -1 ? " @ GLOBAL" : "")
            );
        case 2: // function
            return (
                name +
                "(" +
                params.join(", ") +
                ")" +
                (token.lineNumber == -1 ? " @ GLOBAL" : "")
            );
        case 3: // function parameter
            return (
                "[parameter] " +
                name +
                (value === true ? "(???)" : "") /*+ " <- " + simplify(value)*/
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
    parentTCodeblock: TCodeblock | null = null
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
            }
        }
    });

    return hoverTokens;
}
