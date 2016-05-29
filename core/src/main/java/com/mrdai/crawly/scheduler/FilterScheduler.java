package com.mrdai.crawly.scheduler;

import com.mrdai.crawly.Request;

/**
 * This class is the super class of all {@code Scheduler}s that filter the requests
 * added to the {@code Scheduler}. These {@code Scheduler}s use another underlying
 * {@code Scheduler} to maintain all the registered requests. Their {@link #push(Request)}
 * method will invoke {@link #shouldAdd(Request)} to determine whether to add this request
 * to the underlying {@code Scheduler}. Also, {@code FilterScheduler} class provides
 * a {@link #pushByForce(Request)} method for client to add request without considering
 * the result of {@link #shouldAdd(Request)}.
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
        if (shouldAdd(request))
            return scheduler.push(request);
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
        return scheduler.push(request);
    }

    @Override
    public Request poll() {
        return scheduler.poll();
    }
}
