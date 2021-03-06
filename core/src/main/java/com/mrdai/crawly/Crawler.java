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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * Main entrance class of `Crawly` framework. A {@code Crawler} represents a specific crawling task,
 * designated by the components it consists.
 * </p>
 * <p>
 * A {@code Crawler} is made up of four basic components: {@link Scheduler}, {@link Downloader},
 * {@link PageProcessor}, and {@link Pipeline}.
 * </p>
 * <p>
 * A {@link Scheduler} is responsible of managing all target URLs to be crawled.
 * Each time, crawler retrieves a new target URL via {@link Scheduler#poll()} and starts crawling,
 * and stores every new target URL found during the procedure via {@link Scheduler#push(Request)}.
 * </p>
 * <p>
 * {@link Downloader} is where the crawler access the outside world. For a given {@link Request}
 * retrieved from {@code Scheduler}, crawler will use {@link Downloader} to download the web page
 * and stores it in a {@link Response} object.
 * </p>
 * <p>
 * Afterwards, {@link PageProcessor} will be used to extract information from the {@code Response}
 * object. A crawler can have more than one page processors, as one processor can only be used to process
 * one kind of web page. {@link PageProcessor#supports(Response)} method will be called to find a appropriate
 * processor for each incoming {@code Response}. The information will be stored in a {@link ResultItems} object.
 * </p>
 * <p>
 * Finally, the {@code ResultItems} will be passed to every {@link Pipeline} sequentially, where
 * it can be further processed and persisted.
 * </p>
 * <p>
 *     {@code Crawler} has three possible running states: `initializing`, `running` and `ended`.
 *     Initially, the {@code Crawler} is `initializing`, in which you can configure the {@code Crawler} by setting
 *     or adding components. When you call {@link #start()}, the {@code Crawler} will step into state `running`,
 *     locks all components and starts to crawl. In this state, changing any components the {@code Crawler} are
 *     using will result in an exception. After the {@code Crawler} finished crawling every target page,
 *     the {@code Crawler} will step into state `ended` which stands for the end of its life cycle and
 *     is ready to be released.
 * </p>
 * As a default implementation, {@code Crawler} only supports crawling in single thread in a pretty naive way.
 * Subclasses can choose to override {@link #start()} and {@link #run()} methods to add additional support.
 * <p>
 * For crawler-level concurrency support, see {@link ConcurrentCrawler}.
 *
 * @see Scheduler
 * @see Downloader
 * @see PageProcessor
 * @see Pipeline
 *
 * @author Mr-Dai
 * @since 0.1
 */
public class Crawler {
    private static final Logger LOG = LoggerFactory.getLogger(Crawler.class);

    private static final int INITIALIZING = 0;
    private static final int RUNNING = 1;
    private static final int ENDED = 2;

    private final AtomicInteger state = new AtomicInteger();

    protected Scheduler scheduler;
    protected List<Downloader> downloaders;
    protected List<PageProcessor> processors;
    protected List<Pipeline> pipelines;



    Crawler() {
        this(null);
    }

    /**
     * Constructs a {@code Crawler} with given {@code Scheduler}.
     *
     * @param scheduler the given {@code Scheduler}.
     */
    public Crawler(Scheduler scheduler) {
        this.scheduler = scheduler;
        downloaders = new ArrayList<>();
        processors = new ArrayList<>();
        pipelines = new ArrayList<>();
    }

    /**
     * Constructs a {@code Crawler} with given {@code Scheduler} and {@code Downloader}.
     *
     * @param scheduler  the given {@code Scheduler}.
     * @param downloader the given {@code Downloader}.
     */
    public Crawler(Scheduler scheduler, Downloader downloader) {
        this(scheduler);
        addDownloader(downloader);
    }

    /**
     * Asserts the {@code Crawler} is initializing, and throws an {@link IllegalStateException}
     * with the given message if the assertion failed.
     *
     * @param message the message to be wrapped in the {@code IllegalStateException}
     * @throws IllegalStateException if the {@code Crawler} has already started
     */
    protected void assertInitializing(String message) {
        if (!state.compareAndSet(INITIALIZING, INITIALIZING))
            throw new IllegalStateException(message);
    }

    /**
     * Asserts the {@code Crawler} is running, and throws an {@link IllegalStateException}
     * with the given message if the assertion failed.
     *
     * @param message the message to be wrapped in the {@code IllegalStateException}
     * @throws IllegalStateException if the {@code Crawler} is not running
     */
    protected void assertRunning(String message) {
        if (!state.compareAndSet(RUNNING, RUNNING))
            throw new IllegalStateException(message);
    }

    /** Examines if the {@code Crawler} has been started */
    public boolean hasStarted() {
        return !state.compareAndSet(INITIALIZING, INITIALIZING);
    }

    /**
     * Starts the {@code Crawler}.
     *
     * @throws IllegalStateException if the {@code Crawler} has already started
     */
    public void start() {
        initialize();
        if (!state.compareAndSet(INITIALIZING, RUNNING))
            throw new IllegalStateException("Failed to start the crawler, as the crawler has already started.");
        run();
        state.compareAndSet(RUNNING, ENDED);
        shutdown();
    }

    /**
     * Initializes the crawler. The crawler begins to crawl right after this method is returned.
     * This method will be invoked at the start of the {@link #start()} method.
     */
    protected void initialize() {
        // Lock `downloaders`, `processors` and `pipelines`
        downloaders = Collections.unmodifiableList(downloaders);
        processors = Collections.unmodifiableList(processors);
        pipelines = Collections.unmodifiableList(pipelines);
    }

    /**
     * The concrete crawling logic of a crawler which is invoked within {@link #start()} method after setting
     * the state of the crawler.
     *
     * @implSpec
     * The default implementation only supports crawling in single thread. Subclasses can choose to override
     * this method to provide additional support.
     */
    protected void run() {
        Request request = scheduler.poll();
        while (request != null) {
            Response response = null;
            try {
                LOG.info("Executing request: {}", request.toString());
                for (Downloader downloader : downloaders) {
                    if (downloader.supports(request)) {
                        response = downloader.download(request);
                        break;
                    }
                }
            } catch (IOException e) {
                LOG.error("Unexpected exception occurred when executing request: " + request, e);
                // Push back the request to retry later
                scheduler.push(request);
                continue;
            }
            if (response == null) {
                LOG.error("Supported downloader not found for request: {}.", request);
                continue;
            }

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

            // Go through pipelines
            for (Pipeline pipeline : pipelines) {
                if (!pipeline.process(resultItems)) {
                    break;
                }
            }

            for (Request addedRequest : resultItems.getAddedRequests())
                scheduler.push(addedRequest);

            request = scheduler.poll();
        }
    }

    /**
     * Shutdowns the crawler by releasing all its related resources.
     * This method will be invoked at the end of {@link #start()}.
     */
    protected void shutdown() {
        for (Downloader downloader : downloaders) {
            try {
                downloader.close();
            } catch (IOException e) {
                LOG.error("Failed to close downloader " + downloader, e);
            }
        }

        for (Pipeline pipeline : pipelines) {
            try {
                pipeline.close();
            } catch (IOException e) {
                LOG.error("Failed to close pipeline " + pipeline, e);
            }
        }
    }

    /**
     * Adds a new {@code PageProcessor} to the {@code Crawler}
     *
     * @param processor the new {@code PageProcess} to be added
     * @throws IllegalStateException if the {@code Crawler} has already started
     */
    public void addProcessor(PageProcessor processor) {
        assertInitializing("Cannot add new processor as the crawler has already started.");
        if (processors == null)
            processors = new ArrayList<>();
        processors.add(processor);
    }

    /**
     * Adds a new starting {@code Request} to the {@code Scheduler} of this {@code Crawler}.
     *
     * @param request the new starting {@code Request}.
     * @throws IllegalStateException if the {@code Crawler} has already started.
     */
    public void addStartRequest(Request request) {
        assertInitializing("Cannot add new starting request as the crawler has already started.");
        scheduler.push(request);
    }

    /**
     * Adds a new {@code Pipeline} to the {@code Crawler}
     *
     * @param pipeline the new {@code Pipeline} to be added
     * @throws IllegalStateException if the {@code Crawler} has already started
     */
    public void addPipeline(Pipeline pipeline) {
        assertInitializing("Cannot add new pipeline as the crawler has already started.");
        if (pipelines == null)
            pipelines = new ArrayList<>();
        pipelines.add(pipeline);
    }

    /**
     * Returns the {@code Scheduler} used by this {@code Crawler}
     */
    public Scheduler getScheduler() {
        return scheduler;
    }

    /**
     * Sets the {@code Scheduler} of this {@code Crawler}
     *
     * @throws IllegalStateException if the {@code Crawler} has already started
     */
    public void setScheduler(Scheduler scheduler) {
        assertInitializing("Cannot set a new scheduler as the crawler has already started.");
        this.scheduler = scheduler;
    }

    /**
     * Returns the {@code Downloader} used by this {@code Crawler}
     */
    public List<Downloader> getDownloaders() {
        return downloaders;
    }

    /**
     * Adds the given {@code Downloader} to this {@code Crawler}
     *
     * @throws IllegalStateException if the {@code Crawler} has already started
     */
    public void addDownloader(Downloader downloader) {
        assertInitializing("Cannot set a new downloader as the crawler has already started.");
        this.downloaders.add(downloader);
    }

    /**
     * Returns the {@code PageProcessor}s used by this {@code Crawler}
     */
    public List<PageProcessor> getProcessors() {
        return processors;
    }

    /**
     * Sets the {@code PageProcess}s used by this {@code Crawler}
     *
     * @throws IllegalStateException if the {@code Crawler} has already started
     * @throws IllegalArgumentException if the given processors are null
     */
    public void setProcessors(List<PageProcessor> processors) {
        if (processors == null)
            throw new IllegalArgumentException("The given processors cannot be null");
        assertInitializing("Cannot change processors as the crawler has already started.");
        this.processors = processors;
    }

    /**
     * Returns the {@code Pipeline}s registered in this {@code Crawler}
     */
    public List<Pipeline> getPipelines() {
        return pipelines;
    }

    /**
     * Sets the {@code Pipeline}s used by this {@code Crawler}
     *
     * @throws IllegalStateException if the {@code Crawler} has already started
     * @throws IllegalArgumentException if the given pipelines are null
     */
    public void setPipelines(List<Pipeline> pipelines) {
        if (pipelines == null)
            throw new IllegalArgumentException("The given processors cannot be null");
        assertInitializing("Cannot change pipelines as the crawler has already started.");
        this.pipelines = pipelines;
    }
}
