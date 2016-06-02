package com.mrdai.crawly.network.http;

import com.mrdai.crawly.network.Request;
import com.mrdai.crawly.network.Response;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse implements Response {
    private final Request request;
    private final int statusCode;
    private final Map<String, String> responseHeaders;
    private final String message;

    public HttpResponse(Request request, Map<String, String> responseHeaders, String message) {
        this(request, 0, responseHeaders, message);
    }

    public HttpResponse(Request request, int statusCode,
                    Map<String, String> responseHeaders,
                    String message) {
        if (!(request instanceof HttpRequest))
            throw new IllegalArgumentException("The given request's type is not HttpRequest, which is invalid!");
        this.request = request;
        this.statusCode = statusCode;
        this.responseHeaders = Collections.unmodifiableMap(new HashMap<>(responseHeaders));
        this.message = message;
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
     * Returns the message body of this {@code Response}.
     * @return the message body of this {@code Response}.
     */
    public String getMessage() {
        return message;
    }
}
