package com.mrdai.crawly.scheduler;

import com.mrdai.crawly.Request;

import java.util.HashSet;
import java.util.Set;

/**
 * {@code FilterScheduler} which uses a {@link HashSet} to filter all requests added before.
 */
public class DistinctScheduler extends FilterScheduler {
    private final Set<String> requestSet = new HashSet<>();

    /**
     * Creates a {@code DistinctScheduler} with the given underlying {@code Scheduler}.
     *
     * @param scheduler the given underlying {@code Scheduler}.
     */
    public DistinctScheduler(Scheduler scheduler) {
        super(scheduler);
    }

    @Override
    protected boolean shouldAdd(Request request) {
        return !requestSet.contains(request.getTargetUrl().toString());
    }

    @Override
    protected void record(Request request) {
        requestSet.add(request.getTargetUrl().toString());
    }
}
