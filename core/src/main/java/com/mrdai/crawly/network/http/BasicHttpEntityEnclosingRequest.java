package com.mrdai.crawly.network.http;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.ProtocolVersion;
import org.apache.http.protocol.HTTP;

/**
 * Basic implementation of {@link HttpEntityEnclosingRequest}.
 *
 * @since 0.1
 */
public class BasicHttpEntityEnclosingRequest extends BasicHttpRequest implements HttpEntityEnclosingRequest {
    private HttpEntity entity;

    public BasicHttpEntityEnclosingRequest(final String method, final String uri) {
        super(method, uri);
    }

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
