package com.mrdai.crawly.selector;

/**
 * Created by Robert on 2016/2/3.
 */
public abstract class RawSelector<Selected> implements Selector<String, Selected> {

    public Class<String> getParsedType() {
        return String.class;
    }

    public String parse(String rawBody) {
        // Do nothing
        return rawBody;
    }

}
