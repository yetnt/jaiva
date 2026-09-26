package com.jaiva.interpreter.libBuilders.func.arg;

import com.jaiva.interpreter.libBuilders.func.Argument;

public class AVarArgument extends Argument {


    public AVarArgument(String name, String description) {
        super(name, description, true, Argument.Prefix.VAR_ARGS, Type.ARRAY);
    }
}
