package com.mrdai.crawly.response;

import com.mrdai.crawly.Request;
import com.mrdai.crawly.selector.Selector;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *     A {@code Response} represents a network response fetched by {@code Downloader}.
 * </p>
 *
 * @author Mr-Dai
 * @since 0.1
 */
public class Response {
    private final Request request;
    private final int statusCode;
    private final Map<String, String> responseHeaders;
    private final String rawBody;
    private final Map<Class<?>, Object> parsedResults = new HashMap<>();

    public Response(Request request, int statusCode,
                    Map<String, String> responseHeaders,
                    String body) {
        this.request = request;
        this.statusCode = statusCode;
        this.responseHeaders = Collections.unmodifiableMap(new HashMap<>(responseHeaders));
        this.rawBody = body;
    }

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
