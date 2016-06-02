package com.mrdai.crawly.network.http;

import com.mrdai.crawly.network.Request;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@code HttpRequest} is simply a web request conformed to Http protocol.
 * <p>
 * {@code Request} is just a simple container, which should not be reused for multiple http requests,
 * as it is not thread-safe and might cause many problems.
 *
 * @author Mr-Dai
 * @since 0.1
 */
public class HttpRequest implements Request {
    private final String httpMethod;
    private final URI requestTarget;
    private final Map<String, String> headers = new HashMap<>();
    private String message = "";

    public HttpRequest(URI requestTarget) {
        this(HttpMethod.GET, requestTarget);
    }

    public HttpRequest(String httpMethod, URI requestTarget) {
        this.httpMethod = httpMethod;
        this.requestTarget = requestTarget;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String addHeader(String key, String value) {
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

    public URI getRequestTarget() {
        return requestTarget;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        // Request line
        builder.append(httpMethod).append(' ').append(requestTarget.toString()).append(" HTTP/1.1\r\n");
        // Headers
        for (Map.Entry<String, String> header : headers.entrySet())
            builder.append(header.getKey()).append(':').append(header.getValue()).append("\r\n");
        // Empty line
        builder.append("\r\n");
        // Message body
        builder.append(message);
        return builder.toString();
    }
}
