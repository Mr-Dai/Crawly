package com.mrdai.crawly.pipeline;

import com.mrdai.crawly.ResultItems;

/**
 * <p>
 *     Pipeline is the last component to process a crawling task, which is generally used to
 *     persist the information stored in a {@link ResultItems}
 * </p>
 *
 * @see ResultItems
 */
public interface Pipeline {

    void process(ResultItems resultItems);

}
