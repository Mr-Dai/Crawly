package com.mrdai.crawly.network.http;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.HeaderGroup;

/**
 * Basic implementation of {@link HttpMessage}.
 *
 * @since 0.1
 */
public abstract class AbstractHttpMessage implements HttpMessage {

    protected final HeaderGroup headers = new HeaderGroup();

    @Override
    public boolean containsHeader(final String name) {
        return headers.containsHeader(name);
    }

    @Override
    public Header[] getHeaders(final String name) {
        return headers.getHeaders(name);
    }

    @Override
    public Header getFirstHeader(final String name) {
        return headers.getFirstHeader(name);
    }

    @Override
    public Header getLastHeader(final String name) {
        return headers.getLastHeader(name);
    }

    @Override
    public Header[] getAllHeaders() {
        return headers.getAllHeaders();
    }

    @Override
    public void addHeader(final Header header) {
        headers.addHeader(header);
    }

    @Override
    public void addHeader(final String name, final String value) {
        headers.addHeader(new BasicHeader(name, value));
    }

    @Override
    public void setHeader(final Header header) {
        headers.updateHeader(header);
    }

    @Override
    public void setHeader(final String name, final String value) {
        headers.updateHeader(new BasicHeader(name, value));
    }

    @Override
    public void setHeaders(final Header[] newHeaders) {
        headers.setHeaders(newHeaders);
    }

    @Override
    public void removeHeader(final Header header) {
        headers.removeHeader(header);
    }

    @Override
    public void removeHeaders(final String name) {
        if (name == null) {
            return;
        }
        for (final HeaderIterator i = headers.iterator(); i.hasNext(); ) {
            final Header header = i.nextHeader();
            if (name.equalsIgnoreCase(header.getName())) {
                i.remove();
            }
        }
    }

    @Override
    public HeaderIterator headerIterator() {
        return headers.iterator();
    }

    @Override
    public HeaderIterator headerIterator(final String name) {
        return headers.iterator(name);
    }

}
