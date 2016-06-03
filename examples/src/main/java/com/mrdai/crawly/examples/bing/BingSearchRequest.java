package com.mrdai.crawly.examples.bing;

import com.mrdai.crawly.network.Request;
import com.mrdai.crawly.network.http.AbstractHttpMessage;
import com.mrdai.crawly.network.http.HttpMethods;
import com.mrdai.crawly.network.http.HttpRequest;
import com.mrdai.crawly.network.http.RequestConverter;
import com.mrdai.crawly.util.StringUtils;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.methods.HttpUriRequest;

import java.net.URI;

/**
 * {@link Request} for searching keyword in {@code http://cn.bing.com}.
 */
public class BingSearchRequest extends AbstractHttpMessage implements HttpRequest {
    private static final String urlPrefix = "http://cn.bing.com/search?q=";

    private final URI uri;
    private final ProtocolVersion ver;

    /**
     * Creates a Bing search request with the given keywords.
     *
     * @param keywords the given keywords.
     */
    public BingSearchRequest(String... keywords) {
        this(new ProtocolVersion("Http", 1, 1), keywords);
    }

    /**
     * Creates a Bing search request with the given keywords.
     *
     * @param keywords the given keywords.
     */
    public BingSearchRequest(ProtocolVersion ver, String... keywords) {
        this.ver = ver;
        String realUrl = urlPrefix + StringUtils.concatStrings(keywords, "+");
        uri = URI.create(realUrl);
    }

    @Override
    public String getMethod() {
        return HttpMethods.GET;
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
}
