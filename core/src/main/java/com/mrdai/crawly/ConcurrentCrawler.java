package com.mrdai.crawly;

import com.mrdai.crawly.downloader.Downloader;
import com.mrdai.crawly.network.Request;
import com.mrdai.crawly.network.Response;
import com.mrdai.crawly.pipeline.Pipeline;
import com.mrdai.crawly.processor.PageProcessor;
import com.mrdai.crawly.scheduler.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Subclass of {@link Crawler}, which provides crawler-level concurrency support. It will create given number of
 * download and process threads to handle requests and process responses.
 * <p>
 * To handle new requests added by {@code PageProcessor}, the crawler will explicitly request for the intrinsic
 * lock of the {@code Scheduler}, hence using a thread-safe {@code Scheduler} in this crawler is <b>unnecessary</b>.
 * <p>
 * However, the process threads will concurrently use {@code PageProcessor}s and {@code Pipeline}s to process
 * every incoming response, it would be important to make sure they are thread-safe when you are using more
 * than {@code 1} process threads.
 * Also, the download threads will concurrently use {@code Downloader} to handle requests.
 * <p>
 * Despite of download threads and process threads, the main thread will be used to check if all these worker
 * threads are done, and ends the crawling if so. The main thread will continuously check on all worker threads
 * with a given time interval.
 * <p>
 * On default, the number of process threads is {@code 1}, the number of download threads is {@code 5},
 * and the main thread will wait for {@code 500}ms each time it checks on the worker threads.
 * All of these variables can be set by the crawler's constructors.
 */
public class ConcurrentCrawler extends Crawler {
    private static final Logger LOG = LoggerFactory.getLogger(ConcurrentCrawler.class);
    private static final int DEFAULT_PROCESS_THREAD = 1;
    private static final int DEFAULT_DOWNLOAD_THREAD = 5;
    private static final long DEFAULT_WAIT_TIME = 500;

    private final Thread[] threads;
    private final int downloadThreadNum;
    private final long waitTime;
    private final Queue<Response> queue = new LinkedList<>();
    private final AtomicInteger waitingThreads = new AtomicInteger(0);

    /**
     * Constructs a {@code ConcurrentCrawler} with given {@code Scheduler} and {@code Downloader}.
     * The crawler will used {@code 5} threads to handle requests and {@code 1} thread to process responses.
     * The crawler's main thread will wait for {@code 500}ms each time it checks on the worker threads.
     *
     * @param scheduler  the {@code Scheduler} to be used by the new {@code Crawler}.
     * @param downloader the {@code Downloader} to be used by the new {@code Crawler}.
     */
    public ConcurrentCrawler(Scheduler scheduler, Downloader downloader) {
        this(scheduler, downloader, DEFAULT_DOWNLOAD_THREAD, DEFAULT_PROCESS_THREAD);
    }

    /**
     * Constructs a {@code ConcurrentCrawler} with given {@code Scheduler}, {@code Downloader}.
     * Additionally, users can use parameters {@code downloadThread} and {@code workerThread}
     * to determine how many threads the crawler uses to handle requests and process responses.
     * The crawler's main thread will wait for {@code 500}ms each time it checks on the worker threads.
     *
     * @param scheduler  the {@code Scheduler} to be used by the new {@code Crawler}.
     * @param downloader the {@code Downloader} to be used by the new {@code Crawler}.
     * @param downloadThread the given number of threads for the crawler to handle requests.
     * @param workerThread the given number of threads for the crawler to process responses.
     */
    public ConcurrentCrawler(Scheduler scheduler, Downloader downloader,
                             int downloadThread, int workerThread) {
        this(scheduler, downloader, downloadThread, workerThread, DEFAULT_WAIT_TIME);
    }

    /**
     * Constructs a {@code ConcurrentCrawler} with given {@code Scheduler}, {@code Downloader}.
     * Additionally, users can use parameters {@code downloadThread}, {@code workerThread} and {@code waitTime}
     * to determine how many threads the crawler uses to handle requests and process responses,
     * and how long the main thread waits each time it checks on the worker threads.
     *
     * @param scheduler  the {@code Scheduler} to be used by the new {@code Crawler}.
     * @param downloader the {@code Downloader} to be used by the new {@code Crawler}.
     * @param downloadThread the given number of threads for the crawler to handle requests.
     * @param workerThread the given number of threads for the crawler to process responses.
     * @param waitTime the given time interval (in milliseconds) for the crawler's main thread
     *                 to check on worker threads.
     */
    public ConcurrentCrawler(Scheduler scheduler, Downloader downloader,
                             int downloadThread, int workerThread, long waitTime) {
        super(scheduler, downloader);
        this.downloadThreadNum = downloadThread;
        this.waitTime = waitTime;
        threads = new Thread[downloadThread + workerThread];
    }

    /**
     * {@inheritDoc}
     */
    protected void initialize() {
        super.initialize();

        int i;
        // Initialize download threads
        for (i = 0; i < downloadThreadNum; i++) {
            threads[i] = new Thread(new Download(), String.valueOf(i + 1));
            threads[i].setDaemon(true);
        }

        // Initialize download threads
        for (; i < threads.length; i++) {
            threads[i] = new Thread(new Process(), String.valueOf(i - downloadThreadNum + 1));
            threads[i].setDaemon(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    protected synchronized void run() {
        for (Thread thread : threads)
            thread.start();
        LOG.info("Main thread waiting for worker threads to complete...");
        while (true) {
            // Wait for designated time period
            try {
                wait(waitTime);
            } catch (InterruptedException e) {
                LOG.warn("Main thread is interrupted. Interrupting all worker threads...", e);
                interruptAll();
                break;
            }
            // Check if all worker threads are waiting
            if (waitingThreads.compareAndSet(threads.length, threads.length)) {
                LOG.info("All worker threads are completed.");
                interruptAll();
                break;
            }
        }
    }

    /** Interrupts all worker threads. */
    private void interruptAll() {
        for (Thread thread : threads)
            thread.interrupt();
    }

    /**
     * {@inheritDoc}
     */
    protected void shutdown() {
        super.shutdown();
        for (int i = 0; i < threads.length; i++)
            threads[i] = null;
    }

    /**
     * Concrete implementation of download threads.
     */
    private class Download implements Runnable {
        @Override
        public void run() {
            outer:
            for (Request request;;) {
                synchronized (scheduler) {
                    request = scheduler.poll();
                    while (request == null) {
                        LOG.debug("No more request for download thread #{}. Waiting for new request...",
                            Thread.currentThread().getName());
                        try {
                            waitingThreads.getAndIncrement();
                            scheduler.wait();
                        } catch (InterruptedException e) {
                            LOG.debug("Received interrupt signal. Download thread #{} returns.",
                                Thread.currentThread().getName());
                            break outer;
                        }
                        // Notified by process thread
                        waitingThreads.getAndDecrement();
                        request = scheduler.poll();
                    }
                }
                LOG.debug("Download thread #{} received request url: {}",
                    Thread.currentThread().getName(), request.getRequestTarget().toString());

                Response response;
                try {
                    response = downloader.download(request);
                    // Add the response to the queue and notify all waiting process threads.
                    synchronized (queue) {
                        queue.add(response);
                        queue.notifyAll();
                    }
                } catch (IOException e) {
                    LOG.error("Unexpected exception occurred when requesting url: " + request.getRequestTarget(), e);
                    // Push back the request to retry later
                    synchronized (scheduler) {
                        scheduler.push(request);
                    }
                }
            }
        }
    }

    /**
     * Concrete implementation of process threads.
     */
    private class Process implements Runnable {
        @Override
        public void run() {
            outer:
            for (Response response;;) {
                synchronized (queue) {
                    response = queue.poll();
                    while (response == null) {
                        LOG.debug("No more response for process thread #{}. Waiting for new response...",
                            Thread.currentThread().getName());
                        try {
                            waitingThreads.getAndIncrement();
                            queue.wait();
                        } catch (InterruptedException e) {
                            LOG.debug("Received interrupt signal. Process thread #{} returns.",
                                Thread.currentThread().getName());
                            break outer;
                        }
                        // Notified by download thread
                        waitingThreads.getAndDecrement();
                        response = queue.poll();
                    }
                }
                LOG.debug("Process thread #{} received response from url: {}",
                    Thread.currentThread().getName(), response.getRequest().getRequestTarget().toString());

                ResultItems resultItems = null;
                for (PageProcessor processor : processors) {
                    if (processor.supports(response)) {
                        resultItems = processor.process(response);
                        break;
                    }
                }
                if (resultItems == null) {
                    LOG.error("Cannot find suitable processor for response {}", response);
                    continue;
                }

                // Adds new requests to the scheduler and notify all waiting download threads.
                synchronized (scheduler) {
                    for (Request addedRequest : resultItems.getAddedRequests())
                        scheduler.push(addedRequest);
                    if (!resultItems.getAddedRequests().isEmpty())
                        scheduler.notifyAll();
                }

                // Go through pipelines
                for (Pipeline pipeline : pipelines) {
                    if (!pipeline.process(resultItems)) {
                        break;
                    }
                }
            }
        }
    }

}
