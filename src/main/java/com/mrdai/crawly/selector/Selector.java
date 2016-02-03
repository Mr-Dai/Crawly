package com.mrdai.crawly.selector;

import com.mrdai.crawly.Page;

/**
 * <p>
 *     A {@code Selector} is used to select designated element from a given page.
 * </p>
 * <p>
 *     Three methods need to be implemented for this interface, including {@link #getParsedType()}, {@link #parse(String)}
 *     and {@link #select(Object)}.
 * </p>
 * <p>
 *     For most existed HTML page selector like JSoup, the raw page needs to be parsed into a given type before the
 *     selection. For example, for JSoup selector, the raw page needs to be parsed into a {@code Document} by calling:
 * </p>
 * <pre>
 * Document doc = Jsoup.parse(html);
 * </pre>
 * <p>
 *     Of course, it would be unnecessary to parse the page repeatedly for every selection.
 * </p>
 * <p>
 *     In this case, every {@code Page} object holds the parsed result for every type designated by
 *     {@link #getParsedType()} of every different {@code Selector}. Before the selection happens, the {@code Page} object
 *     will invoke {@link #getParsedType()} and see if the page has already been parsed for this type before. If it does,
 *     the parsed result will be passed to {@link #select(Object)}; otherwise, the {@link #parse(String)} method will be
 *     invoked to parse the whole page, and the result will be cached and passed to {@link #select(Object)}.
 * </p>
 * <p>
 *     For more information of this procedure, see the source code of {@link Page#select(Selector)}.
 * </p>
 *
 * @see Page#select(Selector)
 *
 * @author Mr-Dai
 */
public interface Selector<Parsed, Selected> {

    /**
     * Returns the type of parsed result for this selector.
     */
    Class<Parsed> getParsedType();

    /**
     * Parses the raw content of the given page. The result will be used to select the designated element
     * in {@link #select(Object)} of this {@code Selector}.
     *
     * @param rawBody the raw body of the page to be selected
     * @return the parsed result of the given page body
     */
    Parsed parse(String rawBody);

    /**
     * Selects the designated element from the parsed page.
     *
     * @param parsedBody the parsed body of the page
     * @return the designated element of the given page
     */
    Selected select(Parsed parsedBody);

}
