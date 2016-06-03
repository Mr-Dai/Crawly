package com.mrdai.crawly.downloader;

import com.mrdai.crawly.network.Request;
import com.mrdai.crawly.network.Response;
import com.mrdai.crawly.network.http.BasicHttpResponse;
import com.mrdai.crawly.network.http.HttpRequest;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLConnection;

/**
 * The default implementation of {@link Downloader}, which uses {@link URLConnection} to download
 * content with the given {@link Request}.
 *
 * @see Downloader
 */
public class HttpDownloader implements Downloader {
    private static final Logger LOG = LoggerFactory.getLogger(HttpDownloader.class);

    private final HttpClient client;

    public HttpDownloader() {
        this(HttpClients.createDefault());
    }

    public HttpDownloader(HttpClient client) {
        this.client = client;
    }

    @Override
    public boolean supports(Request request) {
        return request instanceof HttpRequest;
    }

    @Override
    public Response download(Request request) throws IOException {
        HttpRequest hRequest = (HttpRequest) request;
        LOG.info("Handling request `{}`", hRequest.toString());

        HttpResponse response = client.execute(hRequest.toApache());

        BasicHttpResponse result = new BasicHttpResponse(request, response.getProtocolVersion(),
                                                            response.getStatusLine().getStatusCode(),
                                                            response.getStatusLine().getReasonPhrase());
        for (Header header : response.getAllHeaders())
            result.addHeader(header);
        result.setEntity(response.getEntity());

        return result;
    }

}
