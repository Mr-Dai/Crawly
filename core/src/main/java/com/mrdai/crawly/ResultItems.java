package com.mrdai.crawly;

import com.mrdai.crawly.network.Request;
import com.mrdai.crawly.network.Response;
import com.mrdai.crawly.processor.PageProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * <p>
 *     A {@code ResultItems} stores all useful information extracted from a {@link Response}.
 * </p>
 * <p>
 *     The {@code ResultItems}' behavior is similar to {@link java.util.Map Map},
 *     with a {@link #put(String, Object)} method for putting new key/value pair in the {@code ResultItems}
 *     and a {@link #get(String)} method for retrieving information.
 * </p>
 * <p>
 *     On default, {@code ResultItems} stores its content in a {@link java.util.HashMap HashMap},
 *     which should be good enough for most cases. For instantiating a {@code ResultItems} using
 *     customized {@code Map} class, see {@link #ResultItems(Request, Class)}.
 * </p>
 * <p>
 *     Additionally, {@link PageProcessor} can add {@link Request} to the scheduler by invoking {@link #addedRequests}.
 *     The added request will be stored in a {@link List} and pushed to the scheduler after the processor returns.
 * </p>
 *
 * @see PageProcessor
 *
 * @author Mr-Dai
 * @since 0.1
 */
public class ResultItems implements Iterable<Map.Entry<String, Object>> {
    private static final Logger LOG = LoggerFactory.getLogger(ResultItems.class);

    private final Request request;
    private final Map<String, Object> resultItems;
    private final List<Request> addedRequests = new LinkedList<>();

    public ResultItems(Request request) {
        this.request = request;
        resultItems = new HashMap<>();
    }

    public ResultItems(Request request, Class<? extends Map> mapClass) {
        this.request = request;
        try {
            resultItems = mapClass.newInstance();
        } catch (ReflectiveOperationException e) {
            LOG.error("Failed to instantiate the given map class `" + mapClass.getCanonicalName()
                      + "`, the underlying exception is:", e);
            throw new IllegalArgumentException("The given map class `" + mapClass.getCanonicalName()
                                               + "` cannot be instantiated.", e);
        }
    }

    public void addRequest(Request request) {
        addedRequests.add(request);
    }

    public List<Request> getAddedRequests() {
        return addedRequests;
    }

    public void put(String key, Object value) {
        resultItems.put(key, value);
    }

    public Object get(String key) {
        return resultItems.get(key);
    }

    public Iterator<Map.Entry<String, Object>> iterator() {
        return resultItems.entrySet().iterator();
    }

    public Request getRequest() {
        return request;
    }
}
