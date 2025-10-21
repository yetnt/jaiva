package com.jaiva.tokenizer.jdoc;

import com.jaiva.tokenizer.jdoc.tags.Tag;

import java.util.ArrayList;

public class JDocBuilder {
    public ArrayList<Tag> tags = new ArrayList<>();

    JDocBuilder() {

    }

    public JDocBuilder addDesc(String str) {
        tags.add(new Tag.DGeneric(str));
        return this;
    }

    public JDocBuilder addParam(String name, String type, String description, boolean optional) {
        tags.add(new Tag.DParameter(name, type, description, optional));
        return this;
    }

    public JDocBuilder addReturns(String description) {
        tags.add(new Tag.DReturns(description));
        return this;
    }

    public JDocBuilder addNote(String note) {
        tags.add(new Tag.DDevNote(note));
        return this;
    }

    public JDocBuilder addDependencies(String... dependencies) {
        tags.add(new Tag.DDepends(dependencies));
        return this;
    }

    public JDocBuilder sinceVersion(String vers) {
        tags.add(new Tag.DFrom(vers));
        return this;
    }

    public JDocBuilder markDeprecated(String reason) {
        tags.add(new Tag.DDeprecated(reason));
        return this;
    }

    public JDoc build() {
        return new JDoc(tags);
    }
}
