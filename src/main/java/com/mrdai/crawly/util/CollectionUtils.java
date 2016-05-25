package com.mrdai.crawly.util;

import java.util.Iterator;

public final class CollectionUtils {

    /**
     * Concatenates the given collection of {@code String}s with the given delimiter.
     *
     * @param strs the given collection of {@code String}s.
     * @param delimiter the given delimiter.
     * @return the concatenation.
     */
    public static String concatStrings(Iterable<String> strs, String delimiter) {
        StringBuilder builder = new StringBuilder();
        Iterator<String> iter = strs.iterator();

        while (iter.hasNext()) {
            builder.append(iter.next());
            if (iter.hasNext())
                builder.append(delimiter);
        }

        return builder.toString();
    }

    private CollectionUtils() {
        throw new AssertionError("CollectionUtils should not be instantiated!");
    }
}
