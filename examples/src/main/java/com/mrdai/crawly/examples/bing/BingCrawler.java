package com.mrdai.crawly.examples.bing;

import com.mrdai.crawly.ConcurrentCrawler;
import com.mrdai.crawly.Crawler;
import com.mrdai.crawly.downloader.HttpDownloader;
import com.mrdai.crawly.scheduler.QueueScheduler;

public class BingCrawler {

    public static void main(String[] args) throws Exception {
        Crawler crawler = new ConcurrentCrawler(new QueueScheduler(), new HttpDownloader());
        crawler.addProcessor(new BingSearchResultProcessor());
        crawler.addStartRequest(new BingSearchRequest("jquery"));
        crawler.addStartRequest(new BingSearchRequest("baidu"));
        crawler.addStartRequest(new BingSearchRequest("Meow", "Meow"));
        // Pipeline is not needed. Extracted result will be logged by `BingSearchResultProcessor`

        crawler.start();
    }

}
