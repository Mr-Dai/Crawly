package com.mrdai.crawly.examples.bing;

import com.mrdai.crawly.Crawler;
import com.mrdai.crawly.downloader.DefaultDownloader;
import com.mrdai.crawly.pipeline.ConsolePipeline;
import com.mrdai.crawly.scheduler.QueueScheduler;

public class BingCrawler {

    public static void main(String[] args) {
        Crawler crawler = new Crawler(new QueueScheduler(), new DefaultDownloader());
        crawler.addProcessor(new BingSearchResultProcessor());
        crawler.addStartRequest(new BingSearchRequest("jquery"));
        crawler.addStartRequest(new BingSearchRequest("baidu"));
        crawler.addStartRequest(new BingSearchRequest("Meow", "Meow"));
        crawler.addPipeline(new ConsolePipeline());

        crawler.run();
    }

}
