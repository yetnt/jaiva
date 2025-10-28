package com.jaiva.tokenizer.jdoc;

import com.jaiva.errors.TokenizerException;
import com.jaiva.errors.TokenizerException.*;
import com.jaiva.lang.Chars;
import com.jaiva.tokenizer.jdoc.tags.Tag;
import com.jaiva.tokenizer.jdoc.tags.TagType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    /**
     * Creates a JDoc object from various input types.
     * @param obj The input object, which can be a JDoc itself, a String, or other types.
     * @return A new JDoc instance based on the input, or null if the input type is not supported.
     */
    public static JDoc from(Object obj) {
        return switch (obj) {
            case JDoc j -> j;
            case String s -> from(s);
            default -> null;
        };
    }

    /**
     * Creates a JDoc object from a single string, treating the entire string as a generic description.
     * @param str The input string to be wrapped as a generic JDoc.
     * @return A new JDoc instance containing a single generic tag with the provided string.
     */
    public static JDoc from(String str) {
        return JDoc.builder().addDesc(str).build();
    }

    /**
     * Retrieves the description associated with a {@code @deprecated} tag, if present.
     * @return The description string of the deprecated tag, or an empty string if no such tag is found.
     */
    public String getDeprecatedString() {
        for (Tag tag : tags) {
            if (tag.tagType == TagType.DEPRECATED) {
                return (String) tag.attributes.get("description");
            }
        }
        return "";
    }

    public String getDescription() {
        for (Tag tag : tags) {
            if (tag.tagType == TagType.GENERIC) {
                return (String) tag.attributes.get("description");
            }
        }
        return "";
    }

    public String getReturns() {
        for (Tag tag : tags) {
            if (tag.tagType == TagType.RETURNS) {
                return (String) tag.attributes.get("description");
            }
        }
        return "";
    }

    public String getNote() {
        for (Tag tag : tags) {
            if (tag.tagType == TagType.DEVNOTE) {
                return (String) tag.attributes.get("description");
            }
        }
        return "";
    }

    public ArrayList<String> getDependencies() {
        for (Tag tag : tags) {
            if (tag.tagType == TagType.DEPENDS) {
                return (ArrayList<String>) tag.attributes.get("symbols");
            }
        }
        return new ArrayList<>();
    }

    public ArrayList<Tag.DParameter> getParameters() {
        ArrayList<Tag.DParameter> params = new ArrayList<>();
        for (Tag tag : tags) {
            if (tag.tagType == TagType.PARAMETER) {
                params.add((Tag.DParameter) tag);
            }
        }
        return params;
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
