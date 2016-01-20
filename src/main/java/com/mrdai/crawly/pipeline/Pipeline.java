package com.mrdai.crawly.pipeline;

import com.mrdai.crawly.ResultItems;

/**
 * <p>
 *     Pipeline is the last component to process a crawling task, which is generally used to
 *     process or persist a given {@link ResultItems}.
 * </p>
 * <p>
 *     As the name indicates, {@code Pipeline}s can be concatenated to process {@code ResultItems}
 *     step by step. A {@link com.mrdai.crawly.Crawler Crawler} will maintain a list of {@code Pipeline}s
 *     and used them to process each {@code ResultItem} sequentially, until the end of the list is met or
 *     one of the {@code Pipeline}s' {@link #process(ResultItems)} methods returns {@code false}.
 * </p>
 * <p>
 *     For more information, see {@link com.mrdai.crawly.Crawler Crawler} and {@link #process(ResultItems)};
 * </p>
 *
 * @see ResultItems
 */
public interface Pipeline {

    /**
     * <p>
     *     Processes the given {@code ResultItems}, and returns a boolean flag indicating whether the
     *     {@code ResultItems} should be processed any more.
     * </p>
     * <p>
     *     <b>Note</b>: the returned result is just a hint to the {@code Crawler}, whether the {@code ResultItems}
     *     will be further processed is determined by {@code Crawler} itself.
     * </p>
     *
     * @param resultItems the {@code ResultItems} to be processed
     * @return whether the {@code ResultItems} should be processed any more.
     */
    boolean process(ResultItems resultItems);

}
