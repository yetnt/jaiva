package com.jaiva.tokenizer.jdoc;

import com.jaiva.errors.TokenizerException;
import com.jaiva.errors.TokenizerException.*;
import com.jaiva.lang.Chars;
import com.jaiva.tokenizer.jdoc.tags.Tag;
import com.jaiva.tokenizer.jdoc.tags.TagType;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class JDoc {

    public ArrayList<Tag> tags = new ArrayList<>();

    public JDoc(ArrayList<Tag> input) {
        tags = input;
    }

    public static JDocBuilder builder() {
        return new JDocBuilder();
    }

    public JDoc(int lineNumber, String input) throws TokenizerException {
        Scanner s = new Scanner(input);
        Tag.DGeneric generics = new Tag.DGeneric("");
        tags.add(generics);
//        String previousLine = "";
        while (s.hasNextLine()) {
            String current = s.nextLine();
            String[] split = current.split(Pattern.quote(Chars.DOC_TAG));
            if (split[0].equals(current)) {
                // No doc comment was given, so wrap as generic comment as its not a tag.
                generics.addToDescription(current);
                continue;
            }
            String tagName = split[0].trim();
            String args = split[1].trim();

            if (tagName.chars().anyMatch(Character::isUpperCase))
                throw new MalformedJDocException(lineNumber, "A tag cannot contain uppercase letters.");
            Tag tag = Tag.tagToClass(tagName, args);

            if (tag == null) throw new MalformedJDocException(lineNumber, "An invalid JDoc tag was used! Fix it wena");

            if (tag.tagType == TagType.EMPTY && tags.getLast().tagType == TagType.GENERIC) throw new MalformedJDocException(lineNumber, "You cannot have a random empty tag with a description.");

            if (tag.tagType == TagType.EMPTY) {
                tags.getLast().addToDescription((String) tag.attributes.get("description"));
                continue;
            }
            tags.add(tag);
        }

//        return this;
    }

    @Override
    public String toString() {
        return tags.size() == 1 && tags.getFirst() instanceof Tag.DGeneric ? ((String)tags.getFirst().attributes.get("description")) : tags.toString();
    }
}
