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
    private final URL targetUrl;
    private final Map<String, String> headers = new HashMap<>();
    private String message = "";

    /**
     * Creates a {@code Request} with the given {@link HttpMethod} and {@code targetUrl}.
     *
     * @param targetUrl the given {@code targetUrl}.
     *
     * @throws MalformedURLException if the given url string is invalid.
     */
    public Request(String targetUrl) throws MalformedURLException {
        this(new URL(targetUrl));
    }

    /**
     * Creates a {@code Request} with the given {@code targetUrl}.
     *
     * @param targetUrl the given {@code targetUrl}.
     */
    public Request(URL targetUrl) {
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

    public URL getTargetUrl() {
        return targetUrl;
    }
}
