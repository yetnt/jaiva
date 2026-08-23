package com.jaiva.interpreter.libBuilders.func;

import com.jaiva.tokenizer.jdoc.JDoc;
import com.jaiva.tokenizer.jdoc.JDocBuilder;

public class Argument {

    private final String name;
    private final boolean optional;
    private final Prefix prefix;
    private final Type type;
    private final String description;

    protected Argument(String name, String description, boolean optional, Prefix prefix, Type type) {
        this.name = name;
        this.description = description;
        this.optional = optional;
        this.prefix = prefix;
        this.type = type;
    }

    public String name() {
        return name;
    }
    
    public boolean isOptional() {
        return optional;
    }
    
    public Prefix argumentPrefix() {
        return prefix;
    }

    public Type argumentType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String toTokenString() {
        return argumentPrefix().getPrefix() + name() + (isOptional() ? "?" : "");
    }

    public enum Prefix {
        VAR_ARGS("<-"),
        FUNCTIONAL("F~"),
        NONE("");

        private final String prefix;
        Prefix(String prefix) {
            this.prefix = prefix;
        }
        public String getPrefix() {
            return prefix;
        }
    }

    public enum Type {
        ANY("idk"),
        FUNCTION("F~any"),
        STRING("string"),
        BOOLEAN("boolean"),
        NUMBER("number"),
        ARRAY("[]");

        private final String type;
        Type(String type) {
            this.type = type;
        }
        public String getType() {
            return type;
        }
    }
}
