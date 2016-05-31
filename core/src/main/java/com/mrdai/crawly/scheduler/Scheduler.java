package com.mrdai.crawly.scheduler;

import com.mrdai.crawly.network.Request;

/**
 * <p>
 *     A {@code Scheduler} manages when and which URLs for a {@code Crawler} to crawler.
 * </p>
 * <p>
 *     Typically, a {@code Scheduler} acts like a {@link java.util.Queue Queue},
 *     with a {@link #push(Request)} method for adding more target URLs to the crawler
 *     and a {@link #poll()} method for retrieving next URL to crawl.
 * </p>
 * <p>
 *     For each call to {@link #poll()}, the {@code Scheduler} must return a brand-new {@code Request}.
 *     Reusing {@code Request} instance(s) might cause many problems, as {@code Request} is
 *     not thread-safe.
 * </p>
 *
 * @author Mr-Dai
 * @since 0.1
 */
public interface Scheduler {

    /**
     * Pushes the given {@code Request} to the waiting list and returns {@code true} upon success.
     * <p>
     * Many reason can result in a failed push, including insufficient memory or duplicate entry,
     * which should be determined by concrete implementations.
     *
     * @param request the {@code Request} to be pushed to the waiting list
     * @return {@code true} if the {@code Request} was added successfully, {@code false} otherwise
     */
    boolean push(Request request);

    /**
     * Retrieves and removes a {@code Request} from the waiting list. Returns {@code null}
     * if no more entry can be found.
     * <p>
     *     For each call to this method, it must return a brand-new {@code Request}.
     *     Reusing {@code Request} instance might cause many problems, as {@code Request} is
     *     not thread-safe.
     * </p>
     *
     * @return a {@code Request} from the waiting list; {@code null} if there is no more waiting entry.
     */
    Request poll();

}
