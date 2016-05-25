package com.mrdai.crawly.downloader;

import com.mrdai.crawly.Request;
import com.mrdai.crawly.response.Response;
import com.mrdai.crawly.util.CollectionUtils;
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
public class DefaultDownloader implements Downloader {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultDownloader.class);

    @Override
    public Response download(Request request) throws IOException {
        LOG.info("Requesting url `{}`", request.getTargetUrl().toString());
        URLConnection connection = request.getTargetUrl().openConnection();

        if (!request.getMessage().isEmpty())
            connection.setDoOutput(true);

        // Set request headers
        for (Map.Entry<String, String> header : request.getHeaders().entrySet())
            connection.setRequestProperty(header.getKey(), header.getValue());

        // Send initial request
        connection.connect();

        Map<String, String> responseHeaders = new HashMap<>();

        // Get response headers
        for (Map.Entry<String, List<String>> header : connection.getHeaderFields().entrySet())
            responseHeaders.put(header.getKey(), CollectionUtils.concatStrings(header.getValue(), ","));

        // Post request message if exists
        if (!request.getMessage().isEmpty()) {
            try (PrintWriter out = new PrintWriter(connection.getOutputStream())) {
                out.print(request.getMessage());
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
            return new Response(request, ((HttpURLConnection) connection).getResponseCode(),
                                   responseHeaders, responseBody.toString());

        return new Response(request, 0, responseHeaders, responseBody.toString());
    }

}
