package com.mrdai.crawly.processor;

import com.mrdai.crawly.Page;
import com.mrdai.crawly.ResultItems;

/**
 * <p>
 *     A {@code PageProcessor} is responsible of extracting useful information from a
 *     given {@link Page}.
 * </p>
 *
 * @author Mr-Dai
 * @since 0.1
 */
public interface PageProcessor {

    /**
     * <p>
     *     Returns if the {@code PageProcessor} is able to process the given {@code Page}.
     * </p>
     * <p>
     *     Implementation class should not perform time-consuming operations in this method, as this method
     *     will be invoked frequently.
     * </p>
     *
     * @param page the {@code Page} submitted for the process attempt
     * @return {@code true} if this {@code PageProcess} can/will process the given {@code Page},
     *         {@code false} otherwise.
     */
    boolean supports(Page page);

    /**
     * Extracts useful information from the given {@code Page} and stores it in a {@code ResultItems}
     *
     * @param page the {@code Page} to be processed
     * @return {@code ResultItems} extracted from the given page
     */
    ResultItems process(Page page);

}
