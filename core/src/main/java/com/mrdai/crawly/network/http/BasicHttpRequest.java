package com.mrdai.crawly.network.http;

import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.methods.HttpUriRequest;

import java.net.URI;

/**
 * Basic implementation of {@link HttpRequest}.
 *
 * @since 0.1
 */
public class BasicHttpRequest extends AbstractHttpMessage implements HttpRequest {
    private final String method;
    private final URI uri;
    private final ProtocolVersion ver;

    /**
     * Creates an instance of this class using the given request method
     * and URI.
     * <p>
     * Convenient constants for request method can be found in {@link HttpMethods}.
     *
     * @param method request method.
     * @param uri request URI.
     *
     * @see HttpMethods
     */
    public BasicHttpRequest(final String method, final String uri) {
        this(method, uri, HttpVersion.HTTP_1_1);
    }

    /**
     * Creates an instance of this class using the given request method, URI
     * and the HTTP protocol version.
     * <p>
     * Convenient constants for request method and HTTP protocol version can be found
     * in {@link HttpMethods} and {@link HttpVersion}.
     *
     * @param method request method.
     * @param uri request URI.
     * @param ver HTTP protocol version.
     *
     * @see HttpMethods
     * @see HttpVersion
     */
    public BasicHttpRequest(final String method, final String uri, final ProtocolVersion ver) {
        this.method = method.trim().toUpperCase();
        this.uri = URI.create(uri);
        this.ver = ver;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public URI getRequestTarget() {
        return uri;
    }

    @Override
    public HttpUriRequest toApache() {
        return RequestConverter.toApache(this);
    }

    @Override
    public ProtocolVersion getProtocolVersion() {
        return ver;
    }

    @Override
    public String toString() {
        return getMethod().trim().toUpperCase() + ' ' + getRequestTarget().toASCIIString();
    }
}
