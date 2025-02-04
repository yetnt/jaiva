let variable = 10;
let arrayVar = [10, 23, 234, 23];
console.log("Hello World!");
console.log(variable);

if (variable != 100) {
    console.log("Variable is not 100");
} else {
    console.log("It's 100");
}

// comment

/*
multi line comment
*/

function addition(a, b) {
    return a + b;
}

for (i = 0; i <= 10; i++) {
    console.log(addition(arrayVar[i], 10));
}

arrayVar.forEach((element) => {
    console.log(element);
});

try {
    throw new Error("This is an error");
} catch (e) {
    console.log("error msg" + chaai);
}
