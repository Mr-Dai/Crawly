package com.mrdai.crawly.scheduler;

import com.mrdai.crawly.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * <p>
 *     Simple implementation of {@link Scheduler} interface, using
 *     a {@link java.util.concurrent.LinkedBlockingDeque LinkedBlockingDeque} as its underlying
 *     implementation, which guarantees to be thread-safe.
 * </p>
 */
public class BlockingQueueScheduler implements Scheduler {
    private static final Logger LOG = LoggerFactory.getLogger(BlockingQueueScheduler.class);

    private BlockingQueue<Request> queue = new LinkedBlockingDeque<>();

    @Override
    public boolean push(Request request) {
        return queue.add(request);
    }

    @Override
    public Request poll() {
        return queue.poll();
    }
}
