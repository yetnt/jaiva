// ALl globally improted hovertokens, when the extension starts are taking from a .../jaiva/lib.json which
// contais a JSON structure with all global defintions.

import { exec, execSync } from "child_process";
import path from "path";
import { MultiMap } from "./mmap";
import { TCodeblock, TokenDefault } from "./types";
import { HoverToken, parseAndReturnHoverTokens } from "./utils";
import { jaiva_src_lib } from "./extension";
import fs from "fs";

// This file serves to recompile any of those defintions when we need to.

const conv = [
    {
        type: "TFunction",
        name: "F~stringToNum",
        exportSymbol: "false",
        toolTip: "converts a string to a number",
        lineNumber: -1,
        args: ["number"],
        isArgOptional: [false],
        body: null,
    },
    {
        type: "TFunction",
        name: "F~numToString",
        exportSymbol: "false",
        toolTip: "converts a number to a string",
        lineNumber: -1,
        args: ["string"],
        isArgOptional: [false],
        body: null,
    },
];

export const libFileNames = ["arrays.jiv", "convert.jiv"]; // the files in .../jaiva/lib/

/**
 * Make sure to only call this function when you really need it. As it relies on compiling every lib using the
 * CLI slowing down exection time of the extension.
 * @returns A Map who's keys are the global file paths, and values are the corresponding MultiMaps for each global defintion.
 */
function getlibHTokens() {
    let empty: Map<string, MultiMap<string, HoverToken>> = new Map();
    console.log(jaiva_src_lib);
    libFileNames.forEach((fileName) => {
        let tokens;
        const filePath = path.join(jaiva_src_lib, fileName);
        if (fileName == "convert.jiv") {
            tokens = conv as TokenDefault[];
        } else {
            let output = execSync(`jaiva "${filePath}" -j`).toString();
            if (!output.endsWith("]") && !output.startsWith("[")) {
                return null;
            }
            tokens = JSON.parse(output) as TokenDefault[];
        }

        let hoverTokens = parseAndReturnHoverTokens(tokens, null);

        empty.set(filePath, hoverTokens);
    });
    return empty;
}

function mapToJson(map: Map<string, MultiMap<string, HoverToken>>): string {
    let string = [];
    for (const [name, data] of map.entries()) {
        const dataArr = data.toJson((htokens) => {
            const arr = [];
            for (let ht of htokens) {
                ht.token.lineNumber = -1; // yes so it sets to global.
                ht.lineNumber = -1; // so it gets set to global.
                ht.range = [-1, -1];
                if (ht.isParam || ht.token.exportSymbol != "true") continue;
                // if its a parameter, we dont care about it
                if ("body" in ht.token) {
                    (
                        ht.token as TokenDefault & { body: TCodeblock | null }
                    ).body = null;
                }
                arr.push(JSON.stringify(ht).replace("\\", "\\\\"));
            }
            return arr;
        });
        string.push(`"${name.replace(/\\/g, "\\\\")}":${dataArr}`);
    }

    return "{" + string.join(",") + "}";
}

function jsonToMap(json: string): Map<string, MultiMap<string, HoverToken>> {
    const map = new Map<string, MultiMap<string, HoverToken>>();
    const parsed = JSON.parse(json);

    for (const [name, data] of Object.entries(parsed)) {
        const multiMap = MultiMap.fromJson<string, HoverToken>(
            data as { [key: string]: string[] },
            (htokens: string[]) => {
                const arr: HoverToken[] = [];
                for (const ht of htokens) {
                    arr.push(ht as never as HoverToken);
                }
                return arr;
            }
        );
        map.set(name, MultiMap.clearEmptyKeys(multiMap));
    }

    return map;
}

export function updateOrCreateLibJson(
    override: boolean = false
): Map<string, MultiMap<string, HoverToken>> {
    const libJsonPath = path.join(jaiva_src_lib, "lib.json");

    if (!override && fs.existsSync(libJsonPath)) {
        const contents = fs.readFileSync(libJsonPath, "utf-8");
        return jsonToMap(contents);
    }

    const libTokensMap = getlibHTokens();
    const libJson = mapToJson(libTokensMap);

    fs.writeFileSync(libJsonPath, libJson, "utf-8");
    console.log("lib.json has been updated.");

    return libTokensMap;
}

export function getOrCreateLibJsonMap(
    force: boolean = false
): Map<string, MultiMap<string, HoverToken>> {
    const libJsonPath = path.join(jaiva_src_lib, "lib.json");

    if (fs.existsSync(libJsonPath) && !force) {
        const contents = fs.readFileSync(libJsonPath, "utf-8");
        if (contents.trim()) {
            return jsonToMap(contents);
        }
    }

    return updateOrCreateLibJson();
}
