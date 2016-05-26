package com.mrdai.crawly.response;

import com.mrdai.crawly.Request;
import com.mrdai.crawly.selector.Selector;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@code Response} represents a network response fetched by {@code Downloader}.
 *
 * @author Mr-Dai
 * @since 0.1
 */
public class Response {
    private final Request request;
    private final int statusCode;
    private final Map<String, String> responseHeaders;
    private final String rawBody;
    private final Map<Class, Object> parsedResults = new HashMap<>();

    public Response(Request request, Map<String, String> responseHeaders, String body) {
        this(request, 0, responseHeaders, body);
    }

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

    /**
     * Returns the original {@code Request} corresponding to this {@code Response}.
     * @return the original {@code Request} corresponding to this {@code Response}.
     */
    public Request getRequest() {
        return request;
    }

    /**
     * Returns the status code of this {@code Response}; returns {@code 0} if it is not set.
     * @return the status code of this {@code Response}; {@code 0} if it is not set.
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Returns a {@code Map} representing the headers of this {@code Response}.
     * @return a {@code Map} representing the headers of this {@code Response}.
     */
    public Map<String, String> getResponseHeaders() {
        return responseHeaders;
    }

    /**
     * Returns the body of this {@code Response}.
     * @return the body of this {@code Response}.
     */
    public String getRawBody() {
        return rawBody;
    }

}
