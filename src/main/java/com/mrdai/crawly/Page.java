package com.mrdai.crawly;

import com.mrdai.crawly.selector.Selector;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *     A {@code Page} represents a target web page fetched by {@code Downloader}.
 * </p>
 *
 * @author Mr-Dai
 * @since 0.1
 */
public class Page {
    private Request request;
    private int statusCode;
    private Map<String, String> responseHeaders;
    private String rawBody;
    private Map<Class<?>, Object> parsedResults = new HashMap<Class<?>, Object>();

    /**
     * Selects element from the body of the page using the given {@link Selector}
     *
     * @param selector the given selector
     * @return the element selected by the given selector
     */
    public <Parsed, Selected> Selected select(Selector<Parsed, Selected> selector) {
        Class<Parsed> parsedType = selector.getParsedType();
        Parsed parsedPage = parsedType.cast(parsedResults.get(parsedType));

        if (parsedPage == null) {
            parsedPage = selector.parse(rawBody);
            parsedResults.put(parsedType, parsedPage);
        }

        return selector.select(parsedPage);
    }

}
