package com.mrdai.crawly;

import com.mrdai.crawly.network.http.HttpMethod;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *     A {@code Request} represents a target web site for crawler to fetch. Essentially, it stands for a
 *     well-formed HTTP request.
 * </p>
 * <p>
 *     {@code Request} is just a simple container, which should not be reused for multiple http requests,
 *     as it is not thread-safe and might cause many problems.
 * </p>
 *
 * @author Mr-Dai
 * @since 0.1
 */
public class Request {
    private final HttpMethod method;
    private final URL targetUrl;
    private final Map<String, String> headers = new HashMap<>();
    private String message = "";

    /**
     * Creates a {@code Request} with the given {@link HttpMethod} and {@code targetUrl}.
     *
     * @param method the given {@code HttpMethod}.
     * @param targetUrl the given {@code targetUrl}.
     *
     * @throws MalformedURLException if the given url string is invalid.
     */
    public Request(HttpMethod method, String targetUrl) throws MalformedURLException {
        this(method, new URL(targetUrl));
    }

    /**
     * Creates a {@code Request} with the given {@link HttpMethod} and {@code targetUrl}.
     *
     * @param method the given {@code HttpMethod}.
     * @param targetUrl the given {@code targetUrl}.
     */
    public Request(HttpMethod method, URL targetUrl) {
        this.method = method;
        this.targetUrl = targetUrl;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String putHeader(String key, String value) {
        return headers.put(key, value);
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public URL getTargetUrl() {
        return targetUrl;
    }
}
