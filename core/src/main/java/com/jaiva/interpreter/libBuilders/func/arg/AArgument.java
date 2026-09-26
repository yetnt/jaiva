package com.jaiva.interpreter.libBuilders.func.arg;

import com.jaiva.interpreter.libBuilders.func.Argument;

public class AArgument extends Argument implements NeighbouringArg {
    public AArgument(String name, String description, boolean optional, Type type) {
        super(name, description, optional, Argument.Prefix.NONE, type);
    }
}
