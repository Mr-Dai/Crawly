package com.mrdai.crawly.examples.bing;

import com.mrdai.crawly.ResultItems;
import com.mrdai.crawly.network.Response;
import com.mrdai.crawly.network.http.HttpRequest;
import com.mrdai.crawly.network.http.HttpResponse;
import com.mrdai.crawly.processor.PageProcessor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BingSearchResultProcessor implements PageProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(BingSearchResultProcessor.class);
    private static final String supportPrefix = "http://cn.bing.com/search?q=";

    @Override
    public boolean supports(Response response) {
        return (response instanceof HttpResponse) &&
                   (response.getRequest() instanceof HttpRequest) &&
                   ((HttpRequest) response.getRequest()).getRequestTarget().toString().startsWith(supportPrefix);
    }

    @Override
    public ResultItems process(Response response) {
        LOG.info("Processing response of requets {} " + response.getRequest());
        HttpResponse hResponse = (HttpResponse) response;

        StringBuilder contentBuilder = new StringBuilder();
        try (Scanner scan = new Scanner(hResponse.getEntity().getContent())) {
            while (scan.hasNextLine()) {
                contentBuilder.append(scan.nextLine());
                contentBuilder.append("\r\n");
            }
        } catch (IOException e) {}

        Document document = Jsoup.parse(contentBuilder.toString());
        Elements resultEntries = document.select("#b_results .b_algo");
        LOG.debug("Fetched {} list items.", resultEntries.size());

        List<BingSearchResult> results = new ArrayList<>(10);
        for (Element result : resultEntries) {
            try {
                Element title = result.select("h2 a").first();
                String titleText = title.text();
                String caption = result.select(".b_caption p").first().text();
                String url = title.attr("href");
                LOG.info("Extracted result item:\nTitle: {}\nURL: {}\nCaption: {}\n", titleText, url, caption);
                results.add(new BingSearchResult(titleText, caption, url));
            } catch (Throwable e) {
                LOG.error("Failed to parse html: \n" + result.html(), e);
            }
        }

        ResultItems items = new ResultItems(hResponse.getRequest());
        items.put("results", results);
        return items;
    }
}
