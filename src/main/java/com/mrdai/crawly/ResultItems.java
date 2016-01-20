package com.mrdai.crawly;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *     A {@code ResultItems} stores all useful information extracted from a {@link Page}.
 * </p>
 * <p>
 *     The {@code ResultItems}' behavior is similar to {@link java.util.Map Map},
 *     with a {@link #put(String, Object)} method for putting new key/value pair in the {@code ResultItems}
 *     and a {@link #get(String)} method for retrieving information.
 * </p>
 * <p>
 *     On default, {@code ResultItems} stores its content in a {@link java.util.HashMap HashMap},
 *     which should be good enough for most cases. For instantiating a {@code ResultItems} using
 *     customized {@code Map} class, see {@link #ResultItems(Class)}.
 * </p>
 *
 * @author Mr-Dai
 * @since 0.1
 */
public class ResultItems {
    private static final Logger LOG = LoggerFactory.getLogger(ResultItems.class);

    Map<String, Object> resultItems;

    public ResultItems() {
        resultItems = new HashMap<String, Object>();
    }

    public ResultItems(Class<? extends Map> mapClass) {
        try {
            resultItems = mapClass.newInstance();
        } catch (ReflectiveOperationException e) {
            LOG.error("Failed to instantiate the given map class `" + mapClass.getCanonicalName()
                      + "`, the underlying exception is:", e);
            throw new IllegalArgumentException("The given map class `" + mapClass.getCanonicalName()
                                               + "` cannot be instantiated.", e);
        }
    }

    void put(String key, Object value) {
        resultItems.put(key, value);
    }

    Object get(String key) {
        return resultItems.get(key);
    }

}
