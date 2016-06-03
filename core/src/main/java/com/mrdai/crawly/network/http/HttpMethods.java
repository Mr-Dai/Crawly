package com.mrdai.crawly.network.http;

/**
 * Methods of HTTP request
 */
public final class HttpMethods {
    public static String GET = "GET";
    public static String HEAD = "HEAD";
    public static String POST = "POST";
    public static String PUT = "PUT";
    public static String DELETE = "DELETE";
    public static String CONNECT = "CONNECT";
    public static String OPTIONS = "OPTIONS";
    public static String TRACE = "TRACE";
    public static String PATCH = "PATCH";

    private HttpMethods() {
        throw new AssertionError("HttpMethods should not be instantiated!");
    }
}
