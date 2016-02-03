package com.mrdai.crawly.selector;

/**
 * Raw string selector, which selects element from the raw {@code String} of the page body.
 */
public abstract class RawSelector<Selected> implements Selector<String, Selected> {

    @Override
    public Class<String> getParsedType() {
        return String.class;
    }

    @Override
    public String parse(String rawBody) {
        // Do nothing
        return rawBody;
    }

}
