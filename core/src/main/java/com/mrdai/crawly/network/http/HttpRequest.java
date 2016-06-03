package com.mrdai.crawly.network.http;

import com.mrdai.crawly.network.Request;
import org.apache.http.client.methods.HttpUriRequest;

import java.net.URI;

/**
 * A request message from a client to a server includes, within the
 * first line of that message, the method to be applied to the resource,
 * the identifier of the resource, and the protocol version in use.
 * <pre>
 *      Request       = Request-Line
 *                      *(( general-header
 *                       | request-header
 *                       | entity-header ) CRLF)
 *                      CRLF
 *                      [ message-body ]
 * </pre>
 *
 * @since 0.1
 */
public interface HttpRequest extends HttpMessage, Request {
    /**
     * Returns the HTTP method this request uses, such as {@code GET},
     * {@code PUT}, {@code POST}, or other.
     */
    String getMethod();

    /**
     * Returns the URI this request uses, such as
     * {@code http://example.org/path/to/file}.
     * <p>
     * Note that the URI may be absolute URI (as above) or may be a relative URI.
     * </p>
     * <p>
     * Implementations are encouraged to return
     * the URI that was initially requested.
     * </p>
     * <p>
     * To find the final URI after any redirects have been processed,
     * please see the section entitled
     * <a href="http://hc.apache.org/httpcomponents-client-ga/tutorial/html/fundamentals.html#d4e205">HTTP execution context</a>
     * in the
     * <a href="http://hc.apache.org/httpcomponents-client-ga/tutorial/html">HttpClient Tutorial</a>
     * </p>
     */
    URI getRequestTarget();

    HttpUriRequest toApache();
}
