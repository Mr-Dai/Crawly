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

    boolean push(Request request);

    Request poll();

}
