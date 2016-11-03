package com.mynation.famapricewatch.data.parser;

import org.jsoup.nodes.Element;

public class JsoupParserUtil {
    /**
     * Recursively find first parent with certain HTML tag.
     *
     * @param current Current element
     * @param tag     HTML tag to find
     * @return First parent of the given tag or null.
     */
    public static Element findFirstParentWithTag(final Element current, final String tag) {

        final Element parent = current.parent();
        if (parent != null) {
            final String tagName = parent.tagName();
            if (tagName.equalsIgnoreCase(tag)) {
                return parent;
            } else return findFirstParentWithTag(parent, tag);
        } else return null;
    }

    /**
     * Recursively find first sibling with certain HTML tag.
     *
     * @param current Current element
     * @param tag     HTML tag to find
     * @return First sibling of the given tag or null.
     */
    public static Element findFirstSiblingWithTag(final Element current, final String tag) {

        final Element sibling = current.nextElementSibling();
        if (sibling != null) {
            final String tagName = sibling.tagName();
            if (tagName.equalsIgnoreCase(tag)) {
                return sibling;
            } else return findFirstSiblingWithTag(sibling, tag);
        } else return null;
    }
}