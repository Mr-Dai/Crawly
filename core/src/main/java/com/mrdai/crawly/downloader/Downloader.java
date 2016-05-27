package com.mrdai.crawly.downloader;

import com.mrdai.crawly.response.Response;
import com.mrdai.crawly.Request;

import java.io.IOException;

/**
 * <p>
 *     A {@code Downloader} is used by {@link com.mrdai.crawly.Crawler Crawler} to download a web page
 *     from the given target site.
 * </p>
 * <p>
 *     The sole method for a {@code Downloader} is {@link #download(Request)}, which downloads a web page
 *     with the given {@link Request} and stores it in a {@link Response} object.
 * </p>
 *
 * @see Request
 * @see Response
 *
 * @author Mr-Dai
 * @since 0.1
 */
public interface Downloader {
    /**
     * Downloads web page designated by the given {@code Request} and stores it in a {@code Response} object.
     *
     * @param request the given {@code Request}, designating the web page to be downloaded
     * @return the downloaded web page
     *
     * @throws IOException if an I/O exception occurs.
     */
    Response download(Request request) throws IOException;
}
