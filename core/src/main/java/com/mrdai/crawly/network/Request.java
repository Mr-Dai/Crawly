package com.mrdai.crawly.network;

/**
 * <p>
 *     A {@code Request} represents a target web site for crawler to fetch. Essentially, it stands for a
 *     well-formed HTTP request.
 * </p>
 * <p>
 *     {@code Request} is just a simple container, which should not be reused for multiple http requests,
 *     as it is not thread-safe and might cause many problems.
 * </p>
 *
 * @author Mr-Dai
 * @since 0.1
 */
public interface Request {
    String toString();
}
