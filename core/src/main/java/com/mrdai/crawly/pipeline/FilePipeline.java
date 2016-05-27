package com.mrdai.crawly.pipeline;

import com.mrdai.crawly.ResultItems;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * {@link Pipeline} which prints the information in {@link ResultItems} to the given file.
 * <p>
 * It uses an underlying {@link OutputStreamPipeline} to write data to the designated file.
 */
public class FilePipeline implements Pipeline {
    private final OutputStreamPipeline delegate;

    public FilePipeline(String fileName) throws FileNotFoundException {
        delegate = new OutputStreamPipeline(new FileOutputStream(fileName));
    }

    @Override
    public boolean process(ResultItems resultItems) {
        return delegate.process(resultItems);
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }
}
