package com.mrdai.crawly.pipeline;

import com.mrdai.crawly.ResultItems;

/**
 * The default implementation of {@link Pipeline}, which prints the information in {@link ResultItems}
 * on console.
 * <p>
 * It uses an underlying {@link OutputStreamPipeline} to write data to {@link System#out}.
 */
public class ConsolePipeline implements Pipeline {
    private final OutputStreamPipeline delegate = new OutputStreamPipeline(System.out);

    @Override
    public boolean process(ResultItems resultItems) {
        return delegate.process(resultItems);
    }
}
