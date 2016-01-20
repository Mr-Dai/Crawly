package com.mrdai.crawly.processor;

import com.mrdai.crawly.Page;
import com.mrdai.crawly.ResultItems;

/**
 * <p>
 *     A {@code PageProcessor} is responsible of extracting useful information from a
 *     given {@link Page}.
 * </p>
 */
public interface PageProcessor {

    ResultItems process(Page page);

}
