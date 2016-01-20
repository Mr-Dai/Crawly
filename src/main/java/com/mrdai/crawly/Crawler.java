package com.mrdai.crawly;

import com.mrdai.crawly.downloader.Downloader;
import com.mrdai.crawly.pipeline.Pipeline;
import com.mrdai.crawly.processor.PageProcessor;
import com.mrdai.crawly.scheduler.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * and stores it in a {@link Page} object.
 * </p>
 * <p>
 * Afterwards, {@link PageProcessor} will be used to extract information from the {@code Page}
 * object. A crawler can have more than one page processors, as one processor can only be used to process
 * one kind of web page. {@link PageProcessor#supports(Page)} method will be called to find a appropriate
 * processor for each incoming {@code Page}. The information will be stored in a {@link ResultItems} object.
 * </p>
 * <p>
 * Finally, the {@code ResultItems} will be passed to every {@link Pipeline} sequentially, where
 * it can be further processed and persisted.
 * </p>
 * <p>
 *     {@code Crawler} has three possible running states: `initializing`, `running` and `ended`.
 *     Initially, the {@code Crawler} is `initializing`, in which you can configure the {@code Crawler} by setting
 *     or adding components. When you call {@link #run()}, the {@code Crawler} will step into state `running`,
 *     locks all components and starts to crawl. In this state, changing any components the {@code Crawler} are
 *     using will result in an exception. After the {@code Crawler} finished crawling every target page,
 *     the {@code Crawler} will step into state `ended` which stands for the end of its life cycle and
 *     is ready to be released.
 * </p>
 *
 * @see Scheduler
 * @see Downloader
 * @see PageProcessor
 * @see Pipeline
 */
public abstract class Crawler implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(Crawler.class);

    private static final int INITIALIZING = 0;
    private static final int RUNNING = 1;
    private static final int ENDED = 2;

    private final AtomicInteger state = new AtomicInteger();

    protected Scheduler scheduler;
    protected Downloader downloader;
    protected List<PageProcessor> processors;
    protected List<Pipeline> pipelines;

    /**
     * <p>
     * Constructs a {@code Crawler} without setting its {@code Scheduler} and {@code Downloader}.
     * </p>
     * <p>
     * Do not use this constructor if unnecessary, use {@link #Crawler(Scheduler, Downloader)} instead.
     * </p>
     */
    public Crawler() {
        this(null, null);
    }

    /**
     * Constructs a {@code Crawler} with given {@code Scheduler} and {@code Downloader}.
     *
     * @param scheduler  the {@code Scheduler} to be used by the new {@code Crawler}
     * @param downloader the {@code Downloader} to be used by the new {@code Crawler}
     */
    public Crawler(Scheduler scheduler, Downloader downloader) {
        this.scheduler = scheduler;
        this.downloader = downloader;
        processors = new ArrayList<PageProcessor>();
        pipelines = new ArrayList<Pipeline>();
    }

    /**
     * Asserts the {@code Crawler} is initializing, and throws an {@link IllegalStateException}
     * with the given message if the assertion failed.
     *
     * @param message the message to be wrapped in the {@code IllegealStateException}
     * @throws IllegalStateException if the {@code Crawler} has already started
     */
    protected void assertInitializing(String message) {
        if (!state.compareAndSet(INITIALIZING, INITIALIZING))
            throw new IllegalStateException(message);
    }

    /** Examines if the {@code Crawler} has been started */
    public boolean hasStarted() {
        return !state.compareAndSet(INITIALIZING, INITIALIZING);
    }

    /**
     * Starts the {@code Crawler}
     *
     * @throws IllegalStateException if the {@code Crawler} has already started
     */
    public void run() {
        // Lock `processors` and `pipelines`
        processors = Collections.unmodifiableList(processors);
        pipelines = Collections.unmodifiableList(pipelines);

        if (!state.compareAndSet(INITIALIZING, RUNNING))
            throw new IllegalStateException("Failed to start the crawler, as the crawler has already started.");

        // TODO Crawls
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
            processors = new ArrayList<PageProcessor>();
        processors.add(processor);
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
            pipelines = new ArrayList<Pipeline>();
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
    public Downloader getDownloader() {
        return downloader;
    }

    /**
     * Sets the {@code Downloader} of this {@code Crawler}
     *
     * @throws IllegalStateException if the {@code Crawler} has already started
     */
    public void setDownloader(Downloader downloader) {
        assertInitializing("Cannot set a new downloader as the crawler has already started.");
        this.downloader = downloader;
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
