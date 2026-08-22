package com.jaiva.interpreter.libBuilders.func.arg;

import com.jaiva.interpreter.libBuilders.func.Argument;

public class AFuncArgument extends Argument implements NeighbouringArg{
    public AFuncArgument(String name, String description, boolean optional) {
        super(name,description, optional, Argument.Prefix.FUNCTIONAL, Type.FUNCTION);
    }
}
