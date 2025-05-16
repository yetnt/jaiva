import * as vscode from "vscode";
import { findTokenInRange, hTokens } from "./extension";
import { MultiMap } from "./mmap";
import { HoverToken } from "./utils";
import { TArrayVar, TFunction } from "./types";
import { libFileNames } from "./compileGlobals";

function keywordsCompletion(map: MultiMap<string, HoverToken>) {
    let t: TArrayVar = map.get("reservedKeywords")[0].token as TArrayVar;
    return t.value.map((keyword) => {
        const completion = new vscode.CompletionItem(
            keyword as string,
            vscode.CompletionItemKind.Keyword
        );

        completion.detail = "Inserts keyword";
        completion.insertText = keyword as string;
        return completion;
    });
}

function snippetCompletion() {
    // variable declaration.
    let arr: vscode.CompletionItem[] = [];

    const funcS = new vscode.CompletionItem(
        "kwenza",
        vscode.CompletionItemKind.Snippet
    );
    funcS.detail = "Function Declaration";
    funcS.insertText = new vscode.SnippetString(
        "kwenza ${1:functionName}(${2:paramName}) ->" +
            "\n\t@ Functions." +
            "\n\tkhutla ${3:returnValue}" +
            "\n<~"
    );

    arr.push(funcS);

    const vdS = new vscode.CompletionItem(
        "maak",
        vscode.CompletionItemKind.Snippet
    );
    vdS.detail = "Variable Declaration.";
    vdS.insertText = new vscode.SnippetString(
        "maak ${1:variableName} <- ${2:value}!"
    );

    arr.push(vdS);

    const vdarrS = new vscode.CompletionItem(
        "maak array",
        vscode.CompletionItemKind.Snippet
    );
    vdarrS.detail = "Variable Array Declaration.";
    vdarrS.insertText = new vscode.SnippetString(
        "maak ${1:variableName} <-| ${2:value1}, ${3:value2}! @ More , separated values."
    );

    arr.push(vdarrS);

    const ifS = new vscode.CompletionItem(
        "if",
        vscode.CompletionItemKind.Snippet
    );
    ifS.detail = "If Statement";
    ifS.insertText = new vscode.SnippetString(
        "if (${1:condition}) ->" +
            "\n\t@TODO: Write horrible code." +
            '\n\tkhuluma("wus good world")' +
            "\n<~"
    );

    arr.push(ifS);

    const ifMS = new vscode.CompletionItem(
        "mara if",
        vscode.CompletionItemKind.Snippet
    );
    ifMS.detail = "Mara If Statement";
    ifMS.insertText = new vscode.SnippetString(
        "mara if (${1:condition}) ->" +
            "\n\t@TODO: Write horrible code." +
            '\n\tkhuluma("wus good world")' +
            "\n<~"
    );

    arr.push(ifMS);

    const mS = new vscode.CompletionItem(
        "mara",
        vscode.CompletionItemKind.Snippet
    );
    mS.detail = "Mara Statement";
    mS.insertText = new vscode.SnippetString(
        "mara ->" + "\n\t@TODO: Write horrible code." + "\n<~"
    );

    arr.push(mS);

    const nikhilS = new vscode.CompletionItem(
        "nikhil",
        vscode.CompletionItemKind.Snippet
    );
    nikhilS.detail = "Nikhil Statement";
    nikhilS.insertText = new vscode.SnippetString(
        "nikhil (${1:condition}) ->" +
            "\n\t@ shout out to all da nikhils" +
            "\n<~"
    );

    arr.push(nikhilS);

    const colonizeS = new vscode.CompletionItem(
        "colonize",
        vscode.CompletionItemKind.Snippet
    );
    colonizeS.detail = "Colonize Statement";
    colonizeS.insertText = new vscode.SnippetString(
        "colonize i <- ${1:value} | ${2:condition} | ${3:increment} ->" +
            "\n\tkhuluma(i)!" +
            "\n\t@ my favourite loop" +
            "\n<~"
    );

    arr.push(colonizeS);

    const colonizeWithS = new vscode.CompletionItem(
        "colonize with",
        vscode.CompletionItemKind.Snippet
    );
    colonizeWithS.detail = "Colonize With Statement";
    colonizeWithS.insertText = new vscode.SnippetString(
        "colonize ${1:variable} with ${2:arrayVariable} ->" +
            "\n\t@ my favourite loop" +
            "\n<~"
    );

    arr.push(colonizeWithS);

    const tryS = new vscode.CompletionItem(
        "zama zama",
        vscode.CompletionItemKind.Snippet
    );
    tryS.detail = "Zama zama Statement";
    tryS.insertText = new vscode.SnippetString(
        "zama zama ->" +
            "\n\tcima <== ${1:Message}! @ This line will trigger the error handler." +
            "\n<~ chaai ->" +
            "\n\t@ if an error is thrown it's chaai." +
            "\n\tkhuluma(error)!" +
            "\n<~"
    );

    arr.push(tryS);

    return arr;
}

function createParamterJumps(token: TFunction) {
    let arr: string[] = [];
    for (let argIndex = 0; argIndex < token.args.length; argIndex++) {
        const argName = token.args[argIndex];
        arr.push("${" + (argIndex + 1) + `:${argName}}`);
    }
    return arr.join(", ");
}

/**
 * Creates an array of VS Code completion items based on the provided map of hover tokens
 * and the specified line number. The function filters, sorts, and processes hover tokens
 * to generate meaningful completion suggestions.
 *
 * @param map - A `MultiMap` containing hover tokens, where the key is a string and the value
 *              is an array of `HoverToken` objects.
 * @param lineNumber - The current line number used to filter hover tokens within a valid range.
 * @returns An array of `vscode.CompletionItem` objects representing the completion suggestions.
 */
export function createCompletionItemz(
    document: vscode.TextDocument | null,
    position: vscode.Position | null,
    map: MultiMap<string, HoverToken>,
    lineNumber: number
) {
    let arr: vscode.CompletionItem[] = [
        ...keywordsCompletion(map),
        ...snippetCompletion(),
    ];
    let validHovers: HoverToken[] = [];
    // get all valid hover tokens
    for (let entry of map.entries()) {
        let name = entry[0];
        let value = entry[1];
        let hToken = findTokenInRange(value, lineNumber);
        if (hToken !== undefined) validHovers.push(hToken);
    }
    // sort by ranges. and create compoletion items
    validHovers = validHovers.sort(
        (a, b) => a.range[1] - a.range[0] - (b.range[1] - b.range[0])
    );

    validHovers.forEach((h) => {
        // Create a simple completion item with its display text, detail and insertion value.
        let isFunc =
            h.token.type === "TFunction" && !h.hoverMsg.includes("parameter");
        const completion = new vscode.CompletionItem(
            h.name,
            isFunc
                ? vscode.CompletionItemKind.Function
                : vscode.CompletionItemKind.Variable
        );

        completion.detail =
            "Inserts " +
            h.name +
            (isFunc ? " function" : " variable/parameter");
        completion.insertText = new vscode.SnippetString(
            isFunc
                ? h.name + `(${createParamterJumps(h.token as TFunction)})`
                : h.name
        );

        arr.push(completion);
    });

    return arr;
}
