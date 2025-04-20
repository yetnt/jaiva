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

export type Generic = number | string | GenericBool;
export type GenericStatements = TStatement | TVarRef | TFuncCall;
export type GenericBool = boolean | GenericStatements;

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
    value: GenericStatements | string;
};
export type TNumberVar = TokenDefault & {
    value: GenericStatements | number;
};
export type TBooleanVar = TokenDefault & {
    value: GenericBool;
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
    condition: GenericBool;
    increment: "+" | "-" | null;
    body: TCodeblock;
};

export type TWhileLoop = TokenDefault & {
    condition: GenericBool;
    body: TCodeblock;
};

export type TIfStatement = TokenDefault & {
    condition: GenericBool;
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
    functionName: GenericStatements | string;
    getLenght: boolean;
    args: Generic[];
};

export type TVarRef = TokenDefault & {
    varName: GenericStatements | string;
    getLength: boolean;
    index: GenericStatements | number;
    // type: null;
};

export type TLoopControl = TokenDefault & {
    loopType: "CONTINUE" | "BREAK";
};

export type TStatement = TokenDefault & {
    lhs: GenericBool;
    op: string;
    rhs: GenericBool;
    statementType: 1 | 0;
    statement: string;
};
