package com.mrdai.crawly.network.http;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;
import org.apache.http.protocol.HTTP;

/**
 * Basic implementation of {@link HttpEntityEnclosingRequest}. It serves as an extension to {@link BasicHttpRequest}
 * by including a {@link HttpEntity} field.
 *
 * @since 0.1
 */
public class BasicHttpEntityEnclosingRequest extends BasicHttpRequest implements HttpEntityEnclosingRequest {
    private HttpEntity entity;

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
    public BasicHttpEntityEnclosingRequest(final String method, final String uri) {
        super(method, uri);
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
    public BasicHttpEntityEnclosingRequest(final String method, final String uri,
                                           final ProtocolVersion ver) {
        super(method, uri, ver);
    }

    @Override
    public boolean expectContinue() {
        final Header expect = getFirstHeader(HTTP.EXPECT_DIRECTIVE);
        return expect != null && HTTP.EXPECT_CONTINUE.equalsIgnoreCase(expect.getValue());
    }

    @Override
    public void setEntity(HttpEntity entity) {
        this.entity = entity;
    }

    @Override
    public HttpEntity getEntity() {
        return entity;
    }
}
