package com.mrdai.crawly.pipeline;

import com.mrdai.crawly.ResultItems;

import java.io.IOException;
import java.util.Map;

/**
 * {@link Pipeline} which prints the information in {@link ResultItems} to console.
 */
public class ConsolePipeline implements Pipeline {

    @Override
    public boolean process(ResultItems resultItems) {
        System.out.println(resultItems.getRequest().toString());
        System.out.println("-----------------------------------");
        for (Map.Entry<String, Object> entry : resultItems)
            System.out.println(entry.getKey() + ": " + entry.getValue());
        System.out.println("===================================");

        return false;
    }

    @Override
    public void close() throws IOException {}
}
