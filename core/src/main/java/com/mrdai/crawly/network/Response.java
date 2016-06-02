package com.mrdai.crawly.network;

/**
 * A {@code Response} represents a network response fetched by {@code Downloader}.
 *
 * @author Mr-Dai
 * @since 0.1
 */
public interface Response {
    Request getRequest();
}
