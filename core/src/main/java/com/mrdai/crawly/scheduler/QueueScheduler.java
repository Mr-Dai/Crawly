package com.mrdai.crawly.scheduler;

import com.mrdai.crawly.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * The default implementation of {@link Scheduler}, which uses a provided {@link Queue}
 * to manage the registered {@link Request}s. Using a {@link BlockingQueue} can make this class thread-safe.
 */
public class QueueScheduler implements Scheduler {
    private static final Logger LOG = LoggerFactory.getLogger(QueueScheduler.class);

    private final Queue<Request> queue;

    /**
     * Constructs a {@code QueueScheduler} using {@link LinkedList} to manage the registered {@code Request}s.
     */
    public QueueScheduler() {
        this(new LinkedList<>());
    }

    /**
     * Constructs a {@code QueueScheduler} using the given {@code Queue} to manage the registered {@code Request}s.
     */
    public QueueScheduler(Queue<Request> queue) {
        this.queue = queue;
    }

    @Override
    public boolean push(Request request) {
        return queue.add(request);
    }

    @Override
    public Request poll() {
        return queue.poll();
    }
}
