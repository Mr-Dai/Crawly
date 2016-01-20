package com.mrdai.crawly.scheduler;

import com.mrdai.crawly.Request;

/**
 * <p>
 *     A {@code Scheduler} manages when and which URLs for a {@code Crawler} to crawler.
 * </p>
 * <p>
 *     Typically, a {@code Scheduler} acts like a {@link java.util.Queue Queue},
 *     with a {@link #push(Request)} method for adding more target URLs to the crawler
 *     and a {@link #poll()} method for retrieving next URL to crawl.
 * </p>
 */
public interface Scheduler {

    /**
     * Pushes the given {@code Request} to the waiting list and returns {@code true} upon success.
     * <p>
     * Many reason can result in a failed push, including insufficient memory or duplicate entry,
     * which should be determined by concrete implementations.
     *
     * @param request the {@code Request} to be pushed to the waiting list
     * @return {@code true} if the {@code Request} was added successfully; otherwise {@code false}
     */
    boolean push(Request request);

    /**
     * Retrieves and removes a {@code Request} from the waiting list. Returns {@code null}
     * if no more entry can be found.
     */
    Request poll();

}
