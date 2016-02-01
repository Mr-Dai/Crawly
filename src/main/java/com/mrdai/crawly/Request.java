package com.mrdai.crawly;

import com.mrdai.crawly.network.HTTPMethod;

import java.util.Map;

/**
 * <p>
 *     A {@code Request} represents a target web site for crawler to fetch. Essentially, it stands for a
 *     well-formed HTTP request.
 * </p>
 *
 * @author Mr-Dai
 * @since 0.1
 */
public class Request {
    private HTTPMethod method;
    private String targetUrl;
    private Map<String, String> headers;
    private String message;

    /**
     * <p>
     *     Creates an empty {@code Request}.
     * </p>
     * <p>
     *     This constructor exists for Java Reflection, it returns an empty {@code Request} which cannot be used
     *     without further configuration. To creates an usable {@code Request}, use {@link #Request(HTTPMethod, String)}
     *     instead.
     * </p>
     *
     * @see #Request(HTTPMethod, String)
     */
    public Request() {
        this(null, null);
    }

    /**
     * Creates a {@code Request} with the given {@link HTTPMethod} and {@code targetUrl}
     * @param method the given {@code HTTPMethod}
     * @param targetUrl the given {@code targetUrl}
     */
    public Request(HTTPMethod method, String targetUrl) {
        this.method = method;
        this.targetUrl = targetUrl;
    }
}
