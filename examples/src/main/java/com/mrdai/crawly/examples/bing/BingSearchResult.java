package com.mrdai.crawly.examples.bing;

/**
 * Container class for Bing search results. A {@code BingSearchResult} consists of three fields:
 *
 * <ol>
 *     <li>{@code title}: the title of the result, displayed on the top of a result entry;</li>
 *     <li>{@code caption}: the brief caption of the result, displayed right below the result title;</li>
 *     <li>{@code url}: the url of the result.</li>
 * </ol>
 *
 * A {@code BingSearchResult} is immutable once it is created.
 */
public class BingSearchResult {
    private final String title;
    private final String caption;
    private final String url;

    public BingSearchResult(String title, String caption, String url) {
        this.title = title;
        this.caption = caption;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getCaption() {
        return caption;
    }

    public String getUrl() {
        return url;
    }
}
