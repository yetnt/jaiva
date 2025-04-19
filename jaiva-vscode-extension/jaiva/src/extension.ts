import * as vscode from "vscode";
import { execSync } from "child_process";
import { TBooleanVar, TCodeblock, Token, TokenDefault } from "./types";

function simplify(v: any): string {
    return v instanceof Number || v instanceof Boolean
        ? v.toString()
        : v instanceof String
        ? v.toString()
        : Array.isArray(v)
        ? "[" +
          v
              .map((v) => {
                  return simplify(v);
              })
              .join(", ") +
          "]"
        : "_?_";
}

function hoverMessage(
    t: number,
    name: string,
    value: any = null,
    params: string[] = []
): string {
    switch (t) {
        case 0: // variable
            return (
                "variable! **" +
                name +
                "** <- " +
                (value instanceof Number || value instanceof Boolean
                    ? "_" + value + "_"
                    : value instanceof String
                    ? "_" + value.substring(0, Math.min(value.length, 5)) + "_"
                    : simplify(value))
            );
        case 1: // function
            return "function! **" + name + "(" + params.join(", ") + ")**";
        case 2: // function parameter
            return (
                "parameter! **" +
                name +
                "** <- " +
                (value instanceof Number || value instanceof Boolean
                    ? "_" + value + "_"
                    : value instanceof String
                    ? "_" + value.substring(0, Math.min(value.length, 5)) + "_"
                    : simplify(value))
            );
        default:
            return "";
    }
}

type HoverToken = {
    token: TokenDefault;
    range: [number, number];
    lineNumber: number;
    hoverMsg: string;
};

function returnRanges(
    tokens: TokenDefault[],
    parentTCodeblock: TCodeblock | null = null
) {
    let hoverTokens: Map<String, HoverToken> = new Map();
    tokens.forEach((token) => {
        switch (token.type) {
            case "TStringVar":
            case "TBooleanVar":
            case "TNumberVar": {
                let t: TBooleanVar = token as TBooleanVar;
                hoverTokens.set(token.name, {
                    token: token,
                    range:
                        parentTCodeblock === null
                            ? [-1, -1]
                            : [
                                  parentTCodeblock.lineNumber,
                                  parentTCodeblock.lineEnd,
                              ],
                    lineNumber: token.lineNumber,
                    hoverMsg: hoverMessage(0, token.name, t.value),
                });
                break;
            }
        }
    });

    return hoverTokens;
}

export function activate(context: { subscriptions: vscode.Disposable[] }) {
    console.log("Jaiva extension activated.");

    context.subscriptions.push(
        vscode.languages.registerHoverProvider("jaiva", {
            provideHover(document, position) {
                try {
                    // Run the Jaiva parser to get JSON
                    const filePath = document.uri.fsPath;
                    const command = `jaiva "${filePath}" -j`;
                    const output = execSync(command).toString();
                    if (!output.endsWith("]") && !output.startsWith("[")) {
                        return null;
                    }
                    const tokens = JSON.parse(output) as TokenDefault[];

                    const wordRange = document.getWordRangeAtPosition(position);
                    const word = document.getText(wordRange);

                    const line = position.line + 1; // assuming 1-based line numbers
                    const char = position.character;

                    const vfs = returnRanges(tokens);

                    const markdown = new vscode.MarkdownString();
                    markdown.appendMarkdown(vfs.get(word)?.hoverMsg || "");
                    markdown.isTrusted = true;

                    return vfs.has(word)
                        ? new vscode.Hover(markdown)
                        : undefined;

                    // const token = tokens.find(
                    //     (t) =>
                    //         t.lineNumber === line &&
                    //         (!t.column || char >= t.column) // improve this as needed
                    // );

                    // if (!token) return;

                    // switch (token.type) {
                    //     case "TFunction":
                    //         return new vscode.Hover(
                    //             `Function: \`${token.name}\``
                    //         );
                    //     case "TFuncCall":
                    //         return new vscode.Hover(
                    //             `Call: \`${token.functionName}\``
                    //         );
                    //     case "TVarRef":
                    //         return new vscode.Hover(
                    //             `Variable: \`${token.varName}\``
                    //         );
                    //     default:
                    //         return new vscode.Hover(
                    //             `Token: \`${token.name ?? "?"}\``
                    //         );
                    // }
                } catch (err) {
                    console.error("Jaiva hover error:", err);
                    return;
                }
            },
        })
    );
}

export function deactivate() {}
