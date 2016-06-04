package com.mrdai.crawly.network.http;

import org.apache.http.Header;
import org.apache.http.client.methods.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * Utility class for converting the request classes of this framework to those of
 * Apache HttpClient.
 */
public final class RequestConverter {
    private static final Logger LOG = LoggerFactory.getLogger(RequestConverter.class);

    /**
     * Converts the given {@link HttpRequest com.mrdai.crawly.network.http.HttpRequest}
     * to {@link HttpUriRequest org.apache.http.client.methods.HttpUriRequest}.
     *
     * @param request the given request.
     * @return the converted result.
     */
    public static HttpUriRequest toApache(HttpRequest request) {
        LOG.debug("Trying to convert " + request);
        HttpUriRequest result;

        String method = request.getMethod();
        String uriText = request.getRequestTarget().toASCIIString();
        if (method.trim().equalsIgnoreCase(HttpMethods.GET)) {
            result = new HttpGet(uriText);
        } else if (method.trim().equalsIgnoreCase(HttpMethods.POST)) {
            result = new HttpPost(uriText);
        } else if (method.trim().equalsIgnoreCase(HttpMethods.HEAD)) {
            result = new HttpHead(uriText);
        } else if (method.trim().equalsIgnoreCase(HttpMethods.PUT)) {
            result = new HttpPut(uriText);
        } else if (method.trim().equalsIgnoreCase(HttpMethods.DELETE)) {
            result = new HttpDelete(uriText);
        } else if (method.trim().equalsIgnoreCase(HttpMethods.OPTIONS)) {
            result = new HttpOptions(uriText);
        } else if (method.trim().equalsIgnoreCase(HttpMethods.TRACE)) {
            result = new HttpTrace(uriText);
        } else if (method.trim().equalsIgnoreCase(HttpMethods.PATCH)) {
            result = new HttpPatch(uriText);
        } else {
            LOG.debug("The given method `{}` is not supported. Converting it to generic request class.");
            if (request instanceof HttpEntityEnclosingRequest)
                result = new BasicEntityEnclosingRequestWrapper(method, uriText);
            else
                result = new BasicRequestWrapper(method, uriText);
        }

        // Copy headers
        for (Header header : request.getAllHeaders())
            result.setHeader(header);

        // Copy Entity
        if (result instanceof org.apache.http.HttpEntityEnclosingRequest
            && request instanceof HttpEntityEnclosingRequest) {
            ((org.apache.http.HttpEntityEnclosingRequest) result)
                .setEntity(((HttpEntityEnclosingRequest) request).getEntity());
        }

        LOG.debug("{} -> {}", request, result);
        return result;
    }

    private static class BasicRequestWrapper extends HttpRequestBase {
        private final String method;

        BasicRequestWrapper(String method, final String uri) {
            super();
            this.method = method;
            setURI(URI.create(uri));
        }

        @Override
        public String getMethod() {
            return method;
        }
    }

    private static class BasicEntityEnclosingRequestWrapper extends HttpEntityEnclosingRequestBase {
        private final String method;

        BasicEntityEnclosingRequestWrapper(String method, final String uri) {
            super();
            this.method = method;
            setURI(URI.create(uri));
        }

        @Override
        public String getMethod() {
            return method;
        }
    }

    private RequestConverter() {
        throw new AssertionError("RequestConverter should not be instantiated!");
    }
}
