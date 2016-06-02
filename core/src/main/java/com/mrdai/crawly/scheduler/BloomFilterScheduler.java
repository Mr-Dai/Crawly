package com.mrdai.crawly.scheduler;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.mrdai.crawly.network.Request;

import java.nio.charset.Charset;

/**
 * {@code FilterScheduler} which uses a bloom filter to filter all requests added before.
 * A Bloom filter is a space-efficient probabilistic data structure used to test whether an element is a member
 * of a set. False positive matches are possible, but false negatives are not.
 * The more elements that are added to the set, the larger the probability of false positives.
 * <p>
 * On default, the bloom filter used in this scheduler expects {@code 500} insertions of {@code Request}
 * and a false positives probability of {@code 0.01}. These variables can all be set by the scheduler's
 * constructor. Normally, higher number of expected insertions and false positives probability means
 * higher space cost and slightly higher time cost.
 */
public class BloomFilterScheduler extends FilterScheduler {
    private final BloomFilter<String> filter;

    /**
     * Creates a {@code BloomFilterScheduler} with the given underlying {@code Scheduler}.
     * The bloom filter expects {@code 500} insertions of {@code Request}
     * and a false positives probability of {@code 0.01}.
     *
     * @param scheduler the given underlying {@code Scheduler}.
     */
    public BloomFilterScheduler(Scheduler scheduler) {
        this(scheduler, 500, 0.01);
    }

    /**
     * Creates a {@code BloomFilterScheduler} with the given underlying {@code Scheduler}.
     * The bloom filter would expects given number of insertions and false positive probability.
     *
     * @param scheduler the given underlying {@code Scheduler}.
     * @param expectedInsertion the given number of expected insertions.
     * @param fpp the expecting value of false positive probability.
     */
    public BloomFilterScheduler(Scheduler scheduler, int expectedInsertion, double fpp) {
        super(scheduler);
        filter = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), expectedInsertion, fpp);
    }

    @Override
    protected boolean shouldAdd(Request request) {
        return !filter.mightContain(request.getRequestTarget().toString());
    }

    @Override
    protected void record(Request request) {
        filter.put(request.getRequestTarget().toString());
    }
}
