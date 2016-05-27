package com.mrdai.crawly.processor;

import com.mrdai.crawly.response.Response;
import com.mrdai.crawly.ResultItems;

/**
 * <p>
 *     A {@code PageProcessor} is responsible of extracting useful information from a
 *     given {@link Response}.
 * </p>
 *
 * @author Mr-Dai
 * @since 0.1
 */
public interface PageProcessor {

    /**
     * <p>
     *     Returns if the {@code PageProcessor} is able to process the given {@code Response}.
     * </p>
     * <p>
     *     Implementation class should not perform time-consuming operations in this method, as this method
     *     will be invoked frequently.
     * </p>
     *
     * @param response the {@code Response} submitted for the process attempt
     * @return {@code true} if this {@code PageProcess} can/will process the given {@code Response},
     *         {@code false} otherwise.
     */
    boolean supports(Response response);

    /**
     * Extracts useful information from the given {@code Response} and stores it in a {@code ResultItems}
     *
     * @param response the {@code Response} to be processed
     * @return {@code ResultItems} extracted from the given response
     */
    ResultItems process(Response response);

}
