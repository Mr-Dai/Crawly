package com.mrdai.crawly.network.http;

import com.mrdai.crawly.network.Response;
import org.apache.http.HttpEntity;
import org.apache.http.ProtocolVersion;

/**
 * After receiving and interpreting a request message, a server responds
 * with an HTTP response message.
 * <pre>
 *     Response      = Status-Line
 *                     *(( general-header
 *                      | response-header
 *                      | entity-header ) CRLF)
 *                     CRLF
 *                     [ message-body ]
 * </pre>
 * Crawly uses the {@link #toString()} method of this interface to log its information,
 * implementation classes of this interface should provide a human-readable string representation
 * by overriding {@link #toString()}. The basic implementation of this interface, being
 * {@link BasicHttpResponse}, has already provided a simple implementation of {@link #toString()}.
 *
 * @see BasicHttpResponse
 *
 * @since 0.1
 */
public interface HttpResponse extends HttpMessage, Response {
    ProtocolVersion getProtocolVersion();

    int getStatusCode();

    String getReason();

    /**
     * Obtains the message entity of this response, if any.
     * The entity is provided by calling {@link #setEntity setEntity}.
     *
     * @return  the response entity, or
     *          {@code null} if there is none
     */
    HttpEntity getEntity();

    /**
     * Associates a response entity with this response.
     * <p>
     * Please note that if an entity has already been set for this response and it depends on
     * an input stream ({@link HttpEntity#isStreaming()} returns {@code true}),
     * it must be fully consumed in order to ensure release of resources.
     *
     * @param entity    the entity to associate with this response, or
     *                  {@code null} to unset
     *
     * @see HttpEntity#isStreaming()
     * @see org.apache.http.util.EntityUtils#updateEntity(org.apache.http.HttpResponse, HttpEntity)
     */
    void setEntity(HttpEntity entity);
}
