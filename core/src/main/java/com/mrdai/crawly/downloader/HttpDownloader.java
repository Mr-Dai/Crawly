package com.mrdai.crawly.downloader;

import com.mrdai.crawly.network.Request;
import com.mrdai.crawly.network.Response;
import com.mrdai.crawly.network.http.HttpRequest;
import com.mrdai.crawly.network.http.HttpResponse;
import com.mrdai.crawly.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * The default implementation of {@link Downloader}, which uses {@link URLConnection} to download
 * content with the given {@link Request}.
 *
 * @see Downloader
 */
public class HttpDownloader implements Downloader {
    private static final Logger LOG = LoggerFactory.getLogger(HttpDownloader.class);

    @Override
    public boolean supports(Request request) {
        return request instanceof HttpRequest;
    }

    @Override
    public Response download(Request request) throws IOException {
        HttpRequest hRequest = (HttpRequest) request;
        LOG.info("Handling request `{} {}`", hRequest.getHttpMethod(), hRequest.getRequestTarget().toString());

        URLConnection connection = hRequest.getRequestTarget().toURL().openConnection();

        if (!hRequest.getMessage().isEmpty())
            connection.setDoOutput(true);

        // Set request headers
        for (Map.Entry<String, String> header : hRequest.getHeaders().entrySet())
            connection.setRequestProperty(header.getKey(), header.getValue());

        // Send initial request
        connection.connect();

        Map<String, String> responseHeaders = new HashMap<>();

        // Get response headers
        for (Map.Entry<String, List<String>> header : connection.getHeaderFields().entrySet())
            responseHeaders.put(header.getKey(), StringUtils.concatStrings(header.getValue(), ","));

        // Post request message if exists
        if (!hRequest.getMessage().isEmpty()) {
            try (PrintWriter out = new PrintWriter(connection.getOutputStream())) {
                out.print(hRequest.getMessage());
            }
        }

        // Get response body
        StringBuilder responseBody = new StringBuilder();
        try (Scanner in = new Scanner(connection.getInputStream())) {
            while (in.hasNextLine()) {
                responseBody.append(in.nextLine());
                if (in.hasNextLine())
                    responseBody.append('\n');
            }
        } catch (IOException e) {
            // Try to handle the exception
            if (connection instanceof HttpURLConnection) {
                LOG.warn("Target HTTP server reports exception with code {}.",
                    ((HttpURLConnection) connection).getResponseCode());
                InputStream err = ((HttpURLConnection) connection).getErrorStream();
                if (err == null)
                    throw e;
                Scanner in = new Scanner(err);
                while (in.hasNextLine()) {
                    responseBody.append(in.nextLine());
                    if (in.hasNextLine())
                        responseBody.append('\n');
                }
            }
            throw e;
        }

        // Return the result
        if (connection instanceof HttpURLConnection)
            return new HttpResponse(hRequest, ((HttpURLConnection) connection).getResponseCode(),
                                   responseHeaders, responseBody.toString());

        return new HttpResponse(hRequest, 0, responseHeaders, responseBody.toString());
    }

}
