package com.jaiva.tokenizer.jdoc.tags;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * The `Tag` class represents a documentation tag with a specific type and associated attributes.
 * It provides functionality to create tags, manipulate their attributes, and handle various tag types.
 */
public class Tag {
    /**
     * A map of attributes associated with the tag, such as description, type, or version.
     */
    public TagType tagType = TagType.UNKNOWN;

    /**
     * A map of attributes associated with the tag, such as description, type, or version.
     */
    public HashMap<String, Object> attributes = new HashMap<>();

    /**
     * Default constructor for Tag.
     */
    public Tag() {

    }

    /**
     * Maps a tag and its content to a specific subclass of `Tag` based on the tag type.
     *
     * @param tag The tag type as a string.
     * @param content The content associated with the tag.
     * @return A specific subclass of `Tag` corresponding to the tag type, or `null` if the tag type is unrecognized.
     */
    public static Tag tagToClass(String tag, String content) {
        for (TagType t : TagType.values()) {
            if (t.getTag().contains(tag)) {
                return switch (t) {
                    case PARAMETER -> new DParameter(content);
                    case RETURNS -> new DReturns(content);
                    case DEVNOTE -> new DDevNote(content);
                    case DEPRECATED -> new DDeprecated(content);
                    case FROM -> new DFrom(content);
                    case DEPENDS -> new DDepends(content);
                    case EMPTY -> new DEmpty(content);
                    case GENERIC -> new DGeneric(content);
                    default -> null;
                };
            }
        }

        return null;
    }

    /**
     * Constructor for Tag with a specific tag type.
     * If the tag type is DEPRECATED, RETURNS, or PARAMETER,
     * it initializes the description attribute with the first tag of the type.
     * @param tagType the type of the tag, which determines its behavior and attributes
     * @see TagType
     */
    public Tag(TagType tagType) {
        this.tagType = tagType;
        if (!Arrays.asList(TagType.DEPENDS, TagType.FROM).contains(tagType))
                attributes.put("description", null);
    }

    @Override
    public String toString() {
        return "{" +
                "\"tagType\":\"" + tagType.getTag().getFirst() + "\","
                + (attributes.containsKey("description") ? "\"description\":\"" + attributes.get("description") + "\"" : "");
    }

    /**
     * Adds a description to the tag if it exists.
     * This is used to add more information to the tag.
     * @param description the description to add
     * @return false if the description was added, true if the tag does not have a description attribute
     */
    public boolean addToDescription(String description) {
        if (attributes.containsKey("description")) {
            attributes.compute("description", (k, currentDescription) -> (currentDescription == null ? "" : currentDescription + " ") + description);
            return false;
        }
        // if it does not exist, it returns true.
        return true;
    }

    public static class DParameter extends Tag {
        public DParameter(String input) {
            super(TagType.PARAMETER);
            // given an input such as "par <- type docs"
            String[] parts = input.split("<-");
            String varName = parts[0].trim();
            boolean optional = varName.endsWith("?");
            if (optional) varName = varName.substring(0, varName.length() - 1);
            String trimmed = parts[1].trim();
            int firstSpace = trimmed.indexOf(' ');
            String type = firstSpace == -1 ? trimmed : trimmed.substring(0, firstSpace).trim();
            String docs = firstSpace == -1 ? "" : trimmed.substring(firstSpace + 1).trim();

            attributes.put("var", varName);
            attributes.put("type", type);
            attributes.put("optional", optional);
            if (addToDescription(docs)) attributes.put("description", docs);
        }

        @Override
        public String toString() {
            return super.toString() +
                    ",\"var\":\"" + attributes.get("var") + "\","
                    + "\"type\":\"" + attributes.get("type") + "\","
                    + "\"optional\":" + attributes.get("optional") + "}";
        }
    }

    public static class DReturns extends Tag {
        public DReturns(String input) {
            super(TagType.RETURNS);
            // given an input such as "docs"
            if (addToDescription(input)) attributes.put("description", input);
        }

        @Override
        public String toString() {
            return super.toString() + "}";
        }
    }

    public static class DDepends extends Tag {
        public DDepends(String input) {
            super(TagType.DEPENDS);
            // given an input such as "func1, func2, func3, func4"
            ArrayList<String> arr = new ArrayList<>();
            for (String symbol : input.split(",")) {
                arr.add(symbol.trim());
            }
            attributes.put("symbols", arr);
        }

        @Override
        public String toString() {
            StringBuilder st = new StringBuilder();
            ArrayList<String> symbols = (ArrayList<String>) attributes.get("symbols");
            st.append("[");
            for (String sym : symbols) {
                if (symbols.getFirst().equals(sym)) {
                    st.append("\"").append(sym).append("\"");
                } else {
                    st.append(",").append("\"").append(sym).append("\"");
                }
            }
            return super.toString()
                    + "\"symbols\":" + st.append("]") + "}";
        }
    }

    public static class DDevNote extends Tag {
        public DDevNote(String input) {
            // given an input such as "devstring"
            super(TagType.DEVNOTE);
            if (addToDescription(input)) attributes.put("description", input);
        }

        @Override
        public String toString() {
            return super.toString() + "}";
        }
    }

    public static class DDeprecated extends Tag {
        public DDeprecated(String input) {
            // given an input such as "deprString"
            super(TagType.DEPRECATED);
            if (addToDescription(input)) attributes.put("description", input);
        }

        @Override
        public String toString() {
            return super.toString() + "}";
        }
    }

    public static class DFrom extends Tag {
        public DFrom(String input) {
            // given an input such as "jaiva version"
            super(TagType.FROM);
            attributes.put("version", input.trim());
        }

        @Override
        public String toString() {
            return super.toString()
                    + "\"version\":\"" + attributes.get("version") + "\"}";
        }
    }

    /**
     * Empty is class is but a helper class to add on to the description of another tag.
     */
    public static class DEmpty extends Tag {
        public DEmpty(String input) {
            super(TagType.EMPTY);
            if (addToDescription(input)) attributes.put("description", input);
        }
    }

    /**
     * Generic is a class which only holds a description It isnt a tag but its the comment about a symbol
     * that precedes a tag.
     */
    public static class DGeneric extends Tag {
        public DGeneric(String input) {
            super(TagType.GENERIC);
            if (addToDescription(input)) attributes.put("description", input);
        }

        @Override
        public String toString() {
            return super.toString() + "}";
        }
    }
}
