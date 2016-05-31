package com.mrdai.crawly.scheduler;

import com.mrdai.crawly.network.Request;

/**
 * This class is the super class of all {@code Scheduler}s that filter the requests
 * added to the {@code Scheduler}. These {@code Scheduler}s use another underlying
 * {@code Scheduler} to maintain all the registered requests.
 * <p>
 * Implementation classes must implement method {@link #shouldAdd(Request)} and {@link #record(Request)}.
 * The {@link #shouldAdd(Request)} method returns a {@code boolean}, designating if the given {@code Request}
 * should be added to the scheduler. The {@link #record(Request)} method records the added {@code Request}s.
 * <p>
 * The {@link #push(Request)} method of this class will first invoke {@link #shouldAdd(Request)} to determine
 * whether to add this {@code Request} to the scheduler. If it returns {@code true}, the method will invoke
 * {@link #record(Request)} and then add the request to the underlying scheduler.
 * <p>
 * Additionally, this class also provide a {@link #pushByForce(Request)} method, which can adds the given request
 * without considering the result of {@link #shouldAdd(Request)}. Note that this method will also invoke
 * {@link #record(Request)} before it adds the request to the underlying scheduler.
 */
public abstract class FilterScheduler implements Scheduler {
    protected final Scheduler scheduler;

    /**
     * Creates a {@code FilterScheduler} with the given underlying {@code Scheduler}.
     *
     * @param scheduler the given underlying {@code Scheduler}.
     */
    public FilterScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * Returns if the given {@code Request} should be added to this {@code Scheduler}.
     *
     * @param request the given {@code Request}.
     * @return {@code true} if the given {@code Request} should be added to this {@code Scheduler};
     *         {@code false} otherwise.
     */
    protected abstract boolean shouldAdd(Request request);

    /**
     * Records that the given {@code Request} has been added to the {@code Scheduler}.
     *
     * @param request the given {@code Request} which has been added to the {@code Scheduler}.
     */
    protected abstract void record(Request request);

    /**
     * Tries to the push the given {@code Request} to this {@code Scheduler} and returns {@code true}
     * upon success.
     * <p>
     * The method will first consider the result of {@link #shouldAdd(Request)}, and adds the
     * request to the scheduler if it returns {@code true}.
     *
     * @param request the {@code Request} to be pushed to the waiting list
     * @return {@code true} if the request is pushed to this {@code Scheduler} successfully; {@code false} otherwise.
     */
    @Override
    public boolean push(Request request) {
        if (shouldAdd(request)) {
            record(request);
            return scheduler.push(request);
        }
        return false;
    }

    /**
     * Pushes the given {@code Request} to this {@code Scheduler} without considering the result of
     * {@link #shouldAdd(Request)}, and returns {@code true} upon sucess.
     *
     * @param request the {@code Request} to be pushed to the waiting list
     * @return {@code true} if the request is pushed to this {@code Scheduler} successfully; {@code false} otherwise.
     */
    public boolean pushByForce(Request request) {
        record(request);
        return scheduler.push(request);
    }

    @Override
    public Request poll() {
        return scheduler.poll();
    }
}
