package com.mrdai.crawly.examples.bing;

import com.mrdai.crawly.network.Request;
import com.mrdai.crawly.network.http.HttpRequest;
import com.mrdai.crawly.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * {@link Request} for searching keyword in {@code http://cn.bing.com}.
 */
public class BingSearchRequest extends HttpRequest {
    private static final Logger LOG = LoggerFactory.getLogger(BingSearchRequest.class);
    private static final String urlPrefix = "http://cn.bing.com/search?q=";

    private final URI requestTarget;

    /**
     * Creates a Bing search request with the given keywords.
     *
     * @param keywords the given keywords.
     */
    public BingSearchRequest(String... keywords) {
        super(null);
        String realUrl = urlPrefix + StringUtils.concatStrings(keywords, "+");
        requestTarget = URI.create(realUrl);
    }

    @Override
    public URI getRequestTarget() {
        return requestTarget;
    }
}
