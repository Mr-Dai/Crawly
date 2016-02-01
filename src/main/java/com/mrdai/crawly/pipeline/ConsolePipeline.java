package com.mrdai.crawly.pipeline;

import com.mrdai.crawly.ResultItems;

import java.util.Map;

/**
 * The default implementation of {@link Pipeline}, which prints the information in {@link ResultItems} to console.
 */
public class ConsolePipeline implements Pipeline {

    public boolean process(ResultItems resultItems) {
        for (Map.Entry<String, Object> entry : resultItems.entrySet())
            System.out.println(entry.getKey() + ": " + entry.getValue());
        System.out.println("===================================");

        return false;
    }

}
