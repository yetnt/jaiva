package com.jaiva.interpreter.libs.debug;

import java.util.ArrayList;
import java.util.List;

import com.jaiva.errors.InterpreterException;
import com.jaiva.errors.JaivaException.DebugException;
import com.jaiva.interpreter.MapValue;
import com.jaiva.interpreter.Scope;
import com.jaiva.interpreter.Primitives;
import com.jaiva.interpreter.libBuilders.func.Argument;
import com.jaiva.interpreter.libBuilders.func.Arguments;
import com.jaiva.interpreter.libBuilders.func.FunctionBuilder;
import com.jaiva.interpreter.libBuilders.func.arg.AArgument;
import com.jaiva.interpreter.libBuilders.func.arg.AVarArgument;
import com.jaiva.interpreter.libs.BaseLibrary;
import com.jaiva.interpreter.libs.annotation.JaivaLibrary;
import com.jaiva.interpreter.runtime.IConfig;
import com.jaiva.interpreter.symbol.BaseFunction;
import com.jaiva.interpreter.symbol.BaseVariable;
import com.jaiva.interpreter.symbol.Symbol;
import com.jaiva.interpreter.symbol.SymbolConfig;
import com.jaiva.tokenizer.jdoc.JDoc;
import com.jaiva.tokenizer.tokens.Token;
import com.jaiva.tokenizer.tokens.TokenDefault;
import com.jaiva.tokenizer.tokens.specific.TFuncCall;
import com.jaiva.tokenizer.tokens.specific.TVarRef;

@JaivaLibrary(path = "debug")
public class Debug extends BaseLibrary {

    public Debug() {
        vfs.put("d_emit", new FEmit());
        vfs.put("d_vfs", new FVfs());
        vfs.put("d_link", new FLink());
        vfs.put("d_getScope", new FGetScope());
    }

    public class FLink extends BaseFunction {
        FLink() {
            super(
                    FunctionBuilder.start()
                            .name("d_link")
                            .arguments(
                                    Arguments.getInstance()
                                            .add(new AArgument("a", "The symbol which holds the MapValue to be linked.", false, Argument.Type.ANY))
                                            .add(new AArgument("b", "The symbol who's MapValue will either be created or overwritten", false, Argument.Type.ANY))
                            )
                            .docs(
                                    JDoc.builder()
                                            .addDesc("Links the MapValue instance of 'a' into 'b' such that they hold the same value and if one is edited the other will also have that edit.")
                                            .sinceVersion("5.0.4")
                                            .addReturns("idk")
                                            .addNote("""
                                    A usual (b <- a) syntax would suffice if you'd like to copy the value of a into b.
                                    However when a is changed, b will stay the value you set earlier. This function fixes that where it will
                                    link the exact MapValue from a into b, discarding b's old MapValue. such that editing any one of the symbols
                                    via the reassignment syntax will update the linked variable.
                                    
                                    In the case that the "b" parameter does not actually exist in the symbol table, d_link will try to make it, itself.
                                    """)
                                            .addExample("""
                                    maak a <- f~() : 10! @ Lambda that returns 10
                                    maak b <- true! @ boolean value true
                                    
                                    @ With normal reassignment syntax, setting b to a then changing b does not update a
                                    b <- a!
                                    b <- 10!
                                    khuluma(a)! @ prints the lambda signature and not 10.
                                    
                                    @ With d_link, the exact MapValue held by that alias is copied.
                                    d_link(a, b)!
                                    b <- 10!
                                    khuluma(a)! @ prints 10
                                    a <- 100!
                                    khuluma(b)! @ prints 100
                                    """)
                            )
            );
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config, Scope scope) throws Exception {
            checkParams(tFuncCall, scope);
            TokenDefault refA = null;
            TokenDefault refB = null;

            if (tFuncCall.args.getFirst() instanceof Token<?>(Object value))
                refA = (TokenDefault) value;
            else if (tFuncCall.args.getFirst() instanceof TokenDefault t)
                refA = t;
            if (tFuncCall.args.get(1) instanceof Token<?>(Object value))
                refB = (TokenDefault) value;
            else  if (tFuncCall.args.get(1) instanceof TokenDefault t)
                refB = t;

            assert refA != null;
            assert refB != null;
            if (!(refA instanceof TVarRef))
                throw new InterpreterException.WtfAreYouDoingException(scope, "The first parameter has to be a reference or variable.", tFuncCall.lineNumber);
            if  (!(refB instanceof TVarRef))
                throw new InterpreterException.WtfAreYouDoingException(scope, "The second parameter has to be a reference or variable.", tFuncCall.lineNumber);

            MapValue mv = scope.vfs.get(((TVarRef) refA).varName.toString());
            if (mv == null || MapValue.isEmpty(mv))
                throw new InterpreterException.UnknownVariableException(scope, refA.name, tFuncCall.lineNumber);

            scope.vfs.put(((TVarRef) refB).varName.toString(), mv); // This does mean, if the second reference doesnt exist this function woll create it.
            return Token.voidValue(tFuncCall.lineNumber);
        }
    }

    @SymbolConfig(experimental = true)
    public class FVfs extends BaseFunction {
        FVfs() {
            super(FunctionBuilder.start()
                    .name("d_vfs")
                    .docs(JDoc.builder()
                            .addDesc("Returns the current context's vfs.")
                            .sinceVersion("4.1.0")
                            .addReturns("A 2d array, first array containing the keys, second array the values.")
                            .addNote("This function does not allow you to edit the current vfs, only to get" +
                                    " everything that is currently within the vfs as an array." +
                                    " Everytime this function is called a new array containing all the stuff is made.")
                    )
            );
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config, Scope scope) throws Exception {
            ArrayList<String> names = new ArrayList<>();
            ArrayList<Object> symbols = new ArrayList<>();
            scope.vfs.forEach((v, f)-> {
                names.add(v);
                Symbol sym = f.getValue();
                if (sym instanceof BaseVariable var) {
                    if (var.variableType == BaseVariable.VariableType.ARRAY)
                        symbols.add(var.a_getAll());
                    else
                        symbols.add(var.s_get());
                } else if (sym instanceof BaseFunction function) {
                    symbols.add(function);
                }

            });
            return new ArrayList<>(List.of(names, symbols));
        }
    }

    public class FEmit extends BaseFunction {
        FEmit() {
            super(FunctionBuilder.start()
                    .name("d_emit")
                    .arguments(
                            Arguments.getInstance()
                                    .addVarArg(new AVarArgument("arr", "The array of values to pass to the exception"))
                    )
                    .docs(
                           JDoc.builder()
                                   .addDesc("Throws a DebugException to be caught by a Java test class and emits the given variables")
                                   .addReturns("Physically can't return. As it always throws an error")
                                   .addNote(
                                           "If you aren't familiar with Java, the language Jaiva is developed in. " +
                                                   "This will essentially forcefully stop the execution of the interpreter, with the intent" +
                                                   " for said error to be caught and dealt with by another Java class. This serves 0 purpose if " +
                                                   " you're just running a Jaiva file."
                                   )
                                   .sinceVersion("1.0.2")
                    )
            );
            this.freeze();
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config,
                           Scope scope)
                throws DebugException {
            // Any implementation of normal interpreter functions which could throw such an
            // exception should be in the catch.
            ArrayList<Object> components = new ArrayList<>();
            try {
                checkParams(tFuncCall, scope); // throws if params aren't correct. So naturally we loose having
                                                // varargs, so its
                // fine.
                if (!params.isEmpty()) {
                    for (Object param : params) {
                        components.add(Primitives.toPrimitive(
                                param,
                                false,
                                config, scope
                        ));
                    }
                }
            } catch (Exception e) {
                throw new DebugException(e);
            }
            throw new DebugException(components, scope, config, tFuncCall.lineNumber);
        }
    }

    public class FGetScope extends BaseFunction {
        public FGetScope() {
            super(FunctionBuilder.start()
                    .name("d_getScope")
                    .docs(JDoc.builder()
                            .sinceVersion("5.0.4")
                            .addDesc("gets the scope. wahtd you expect")
                            .addReturns("the scope string")
                            .addNote("This is the exact same scope string that an error outputs")
                    )
            );
        }

        @Override
        public Object call(TFuncCall tFuncCall, ArrayList<Object> params, IConfig<Object> config, Scope scope) throws Exception {
            return scope.toString();
        }
    }
}
