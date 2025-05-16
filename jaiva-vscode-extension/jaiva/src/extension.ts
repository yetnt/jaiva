import * as vscode from "vscode";
import { execSync, exec } from "child_process";
import { TokenDefault } from "./types";
import {
    HoverToken,
    mergeMaps,
    parseAndReturnHoverTokens,
    primitive,
} from "./utils";
import { MultiMap } from "./mmap";
import { createCompletionItemz } from "./autocomplete";
import * as globals from "./compileGlobals";
import path from "path";

export let hTokens: Map<string, MultiMap<string, HoverToken>> = new Map();
const completionItemsMap: Map<string, vscode.CompletionItem[]> = new Map();
let lastLineNumber: Map<string, number> = new Map();

export const jaiva_src_lib = path.join(
    execSync("jaiva-src", { encoding: "utf8" }).trim(),
    "lib"
);

function onSave(document: vscode.TextDocument) {
    const filePath = document.uri.fsPath;
    const command = `jaiva "${filePath}" -jg`;
    const output = execSync(command, { encoding: "utf8" }).trim().toString();
    if (!output.endsWith("]") && !output.startsWith("[")) {
        return null;
    }
    let tokens;
    try {
        tokens = JSON.parse(
            output.replace(/\\(?!\\|n|t|r)/g, "\\\\")
        ) as TokenDefault[];
    } catch (error) {
        throw error;
    }

    let hoverTokens = parseAndReturnHoverTokens(
        tokens,
        null,
        filePath,
        hTokens
    );
    completionItemsMap.set(
        filePath,
        createCompletionItemz(
            document,
            null,
            hoverTokens,
            lastLineNumber.get(filePath) || 0
        )
    );

    hTokens.set(filePath, hoverTokens);
}

function onActivate() {
    const pattern = "**/*.{jiv,jaiva,jva}";

    hTokens = mergeMaps(
        new Map<string, MultiMap<string, HoverToken>>(),
        globals.getOrCreateLibJsonMap(true)
    );

    // Using workspace.findFiles to search for matching files.
    vscode.workspace.findFiles(pattern).then(
        (uris) => {
            const filePaths = uris.map((uri) => uri.fsPath);
            filePaths.forEach((filePath) => {
                const command = `jaiva "${filePath}" -jg`;
                const output = execSync(command, {
                    encoding: "utf8",
                })
                    .trim()
                    .toString();
                if (!output.endsWith("]") && !output.startsWith("[")) {
                    return null;
                }
                let tokens;
                try {
                    tokens = JSON.parse(
                        output.replace(/\\(?!\\|n|t|r)/g, "\\\\")
                    ) as TokenDefault[];
                } catch (error) {
                    // console.log(output);
                    throw error;
                }

                let hoverTokens = parseAndReturnHoverTokens(
                    tokens,
                    null,
                    filePath,
                    hTokens
                );

                hTokens.set(filePath, hoverTokens);

                completionItemsMap.set(
                    filePath,
                    createCompletionItemz(
                        null,
                        null,
                        hoverTokens,
                        lastLineNumber.get(filePath) || 0
                    )
                );
            });
        },
        (error) => {
            console.error("Error during file search:", error);
        }
    );
}

/**
 * Finds a `HoverToken` within a specified range of line numbers.
 *
 * @param hoverTokens - An array of `HoverToken` objects to search through.
 * @param lineNumber - The line number to check against the ranges of the `HoverToken` objects.
 * @returns The first `HoverToken` that matches the criteria:
 *          - If the range of a `HoverToken` is `[-1, -1]`, it is considered globally defined and returned immediately.
 *          - If the `lineNumber` falls within the range `[range[0], range[1]]` of a `HoverToken`, that token is returned.
 *          - If no matching `HoverToken` is found, the function returns `undefined`.
 */
export function findTokenInRange(
    hoverTokens: HoverToken[],
    lineNumber: number
) {
    for (let hoverToken of hoverTokens) {
        if (hoverToken.range[0] == -1 && hoverToken.range[1] == -1)
            // define in global scope. return as is even if there's more
            return hoverToken;
        if (
            hoverToken.range[0] <= lineNumber &&
            hoverToken.range[1] >= lineNumber &&
            lineNumber >= hoverToken.lineNumber // Jaiva executes top to bottom, so this check makes sure you dont get tokens that were define later
        ) {
            // console.log(
            //     `HoverToken "${hoverToken.name}" is in the range. of ${hoverToken.range} in line ${lineNumber}`
            // );
            return hoverToken;
        }
    }
}

export function activate(context: { subscriptions: vscode.Disposable[] }) {
    console.log("Jaiva extension activated.");

    onActivate();

    const runCommand = vscode.commands.registerCommand("jaiva.run", () => {
        // Ensure there is an active editor
        const editor = vscode.window.activeTextEditor;
        if (!editor) {
            vscode.window.showInformationMessage("No active editor found.");
            return;
        }

        const filePath = editor.document.uri.fsPath;
        // Retrieve additional args from the configuration
        const config = vscode.workspace.getConfiguration("jaiva");
        const runArgs = config.get("runArgs", "");
        const command = `jaiva "${filePath}" ${runArgs}`;

        // Create or re-use an existing terminal
        let terminal =
            vscode.window.activeTerminal ||
            vscode.window.createTerminal("Jaiva!");
        terminal.show();
        terminal.sendText(command);
    });

    const provider = vscode.languages.registerCompletionItemProvider("jaiva", {
        provideCompletionItems(
            document: vscode.TextDocument,
            position: vscode.Position
        ) {
            const filePath = document.uri.fsPath;

            let completionItems = completionItemsMap.get(filePath);
            let hoverTokens = hTokens.get(filePath);
            if (hoverTokens === undefined) return;
            const line = position.line + 1;
            lastLineNumber.set(filePath, line);
            if (completionItems == undefined) {
                completionItemsMap.set(
                    filePath,
                    createCompletionItemz(document, position, hoverTokens, line)
                );
            }

            // Return the list of completion items.
            return completionItems;
        },
    });

    const runStatusBarItem = vscode.window.createStatusBarItem(
        vscode.StatusBarAlignment.Left,
        100
    );
    runStatusBarItem.text = "$(play) Run Jaiva";
    runStatusBarItem.command = "jaiva.run";
    runStatusBarItem.tooltip =
        "Run the current Jaiva file with additional arguments";
    runStatusBarItem.show();

    context.subscriptions.push(
        runStatusBarItem,
        runCommand,
        provider,
        vscode.workspace.onDidSaveTextDocument(onSave),
        vscode.workspace.onDidOpenTextDocument(onSave),
        vscode.languages.registerHoverProvider("jaiva", {
            provideHover(document, position) {
                try {
                    // Run the Jaiva parser to get JSON
                    const filePath = document.uri.fsPath;

                    const wordRange = document.getWordRangeAtPosition(position);
                    const word = document.getText(wordRange);

                    const line = position.line + 1;
                    lastLineNumber.set(filePath, line);

                    const p = primitive(word);
                    if (p !== undefined) {
                        const markdown = new vscode.MarkdownString();
                        markdown.appendCodeblock(p, "jaiva");
                        markdown.isTrusted = true;

                        return new vscode.Hover(markdown);
                    }

                    let tokens = hTokens.get(filePath);
                    if (!tokens) return undefined;

                    if (!tokens.has(word)) return undefined;
                    const hoverTokens = tokens.get(word);

                    let token = findTokenInRange(hoverTokens, line);
                    if (token === undefined) return undefined;

                    const markdown = new vscode.MarkdownString();
                    markdown.appendCodeblock(token.hoverMsg, "jaiva");
                    markdown.appendMarkdown(token.token.toolTip);
                    markdown.isTrusted = true;

                    return new vscode.Hover(markdown);

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

export function deactivate() {
    hTokens.clear();
}
