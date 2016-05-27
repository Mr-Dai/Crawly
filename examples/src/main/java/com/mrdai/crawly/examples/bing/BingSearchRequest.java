package com.mrdai.crawly.examples.bing;

import com.mrdai.crawly.Request;
import com.mrdai.crawly.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * {@link Request} for searching keyword in {@code http://cn.bing.com}.
 */
public class BingSearchRequest extends Request {
    private static final Logger LOG = LoggerFactory.getLogger(BingSearchRequest.class);
    private static final String urlPrefix = "http://cn.bing.com/search?q=";

    /**
     * Creates a Bing search request with the given keywords.
     *
     * @param keywords the given keywords.
     */
    public BingSearchRequest(String... keywords) {
        String realUrl = urlPrefix + StringUtils.concatStrings(keywords, "+");
        try {
            targetUrl = new URL(realUrl);
        } catch (MalformedURLException e) {
            LOG.error("Failed to instantiate Request with url " + realUrl);
        }
    }
}
