package com.mrdai.crawly.util;

import java.util.Iterator;

public final class StringUtils {

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

    /**
     * Concatenates the given array of {@code String}s with the given delimiter.
     *
     * @param strs the given array of {@code String}s.
     * @param delimiter the given delimiter.
     * @return the concatenation.
     */
    public static String concatStrings(String[] strs, String delimiter) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < strs.length; i++) {
            builder.append(strs[i]);
            if (i < strs.length - 1)
                builder.append(delimiter);
        }

        return builder.toString();
    }

    private StringUtils() {
        throw new AssertionError("StringUtils should not be instantiated!");
    }
}
