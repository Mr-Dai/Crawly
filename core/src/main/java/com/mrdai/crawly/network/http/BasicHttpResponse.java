package com.mrdai.crawly.network.http;

import com.mrdai.crawly.network.Request;
import org.apache.http.HttpEntity;
import org.apache.http.ProtocolVersion;

/**
 * Basic implementation of {@link HttpResponse}.
 *
 * @since 0.1
 */
public class BasicHttpResponse extends AbstractHttpMessage implements HttpResponse {
    private final Request request;
    private ProtocolVersion ver;
    private int statusCode;
    private String reason;
    private HttpEntity entity;

    /**
     * Creates a response from elements of a status line.
     * The response will not have a reason phrase catalog and
     * use the system default locale.
     *
     * @param ver       the protocol version of the response
     * @param code      the status code of the response
     * @param reason    the reason phrase to the status code, or
     *                  {@code null}
     */
    public BasicHttpResponse(Request request, final ProtocolVersion ver, final int code, final String reason) {
        this.request = request;
        this.ver = ver;
        this.statusCode = code;
        this.reason = reason;
    }

    @Override
    public ProtocolVersion getProtocolVersion() {
        return ver;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getReason() {
        return reason;
    }

    @Override
    public HttpEntity getEntity() {
        return entity;
    }

    @Override
    public void setEntity(final HttpEntity entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return ver.toString() + ' ' + statusCode + ' ' + reason;
    }

    @Override
    public Request getRequest() {
        return request;
    }
}
