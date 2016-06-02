package com.mrdai.crawly.pipeline;

import com.mrdai.crawly.ResultItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Map;

/**
 * {@link Pipeline} which writes the information in {@link ResultItems} to the given {@link OutputStream}.
 */
public class OutputStreamPipeline implements Pipeline {
    private static final Logger LOG = LoggerFactory.getLogger(OutputStreamPipeline.class);

    private final Writer out;

    public OutputStreamPipeline(OutputStream out) {
        this.out = new OutputStreamWriter(out);
    }

    @Override
    public boolean process(ResultItems resultItems) {
        try {
            out.write(resultItems.getRequest().toString());
            out.write(":\n-----------------------------------\n");
            for (Map.Entry<String, Object> entry : resultItems)
                out.write(entry.getKey() + ": " + entry.getValue() + "\n");
            out.write("===================================\n");
        } catch (IOException e) {
            LOG.error("Unexpected error occurred when writing data to the given stream: ", e);
        }

        return false;
    }

    @Override
    public void close() throws IOException {
        out.close();
    }
}
