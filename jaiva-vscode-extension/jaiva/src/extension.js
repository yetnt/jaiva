const vscode = require("vscode");

function activate(context) {
    console.log("My language extension is now active!");
}

function deactivate() {}

module.exports = {
    activate,
    deactivate,
};
