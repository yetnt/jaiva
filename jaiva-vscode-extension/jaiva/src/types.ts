export type TokenType =
    | "TVoidValue"
    | "TCodeblock"
    | "TUnknownVar"
    | "TVarReassign"
    | "TStringVar"
    | "TNumberVar"
    | "TBooleanVar"
    | "TArrayVar"
    | "TFuncReturn"
    | "TFunction"
    | "TForLoop"
    | "TWhileLoop"
    | "TIfStatement"
    | "TTryCatchStatement"
    | "TThrowError"
    | "TFuncCall"
    | "TVarRef"
    | "TLoopControl"
    | "TStatement";

export type Generic = number | string | boolean | Token<null>;

export type TokenDefault = {
    type: TokenType;
    name: string;
    lineNumber: number;
};

export type Token<T extends TokenDefault | null> = {
    value: T;
};

export type TVoidValue = TokenDefault & {};
export type TCodeblock = TokenDefault & {
    lines: TokenDefault[];
    lineEnd: number;
};
export type TUnknownVar = TokenDefault & {
    value: Generic;
};
export type TVarReassign = TokenDefault & {
    value: Generic;
};
export type TStringVar = TokenDefault & {
    value: Generic;
};
export type TNumberVar = TokenDefault & {
    value: Generic;
};
export type TBooleanVar = TokenDefault & {
    value: Generic;
};
export type TArrayVar = TokenDefault & {
    value: Generic[];
};
export type TFuncReturn = TokenDefault & {
    value: Generic;
};

export type TFunction = TokenDefault & {
    args: string[];
    body: TCodeblock;
};

export type TForLoop = TokenDefault & {
    variable: TokenDefault;
    arrayVariable: TVarRef;
    condition: Generic;
    increment: "+" | "-" | null;
    body: TCodeblock;
};

export type TWhileLoop = TokenDefault & {
    condition: Generic;
    body: TCodeblock;
};

export type TIfStatement = TokenDefault & {
    condition: Generic;
    body: TCodeblock;
    elseIfs: TIfStatement[];
    elseBody: TCodeblock;
};

export type TTryCatchStatement = TokenDefault & {
    try: TCodeblock;
    catch: TCodeblock;
};

export type TThrowError = TokenDefault & {
    errorMessage: string;
};

export type TFuncCall = TokenDefault & {
    functionName: Generic;
    getLenght: boolean;
    args: Generic[];
};

export type TVarRef = TokenDefault & {
    varName: Generic;
    getLength: boolean;
    index: Generic;
    // type: null;
};

export type TLoopControl = TokenDefault & {
    loopType: "CONTINUE" | "BREAK";
};

export type TStatement = TokenDefault & {
    lhs: Generic;
    op: string;
    rhs: Generic;
    statementType: 1 | 0;
    statement: string;
};
