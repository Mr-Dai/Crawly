package com.mrdai.crawly.examples.ftp;

import com.mrdai.crawly.Crawler;
import com.mrdai.crawly.downloader.FtpDownloader;
import com.mrdai.crawly.network.ftp.FtpCommand;
import com.mrdai.crawly.scheduler.QueueScheduler;
import org.apache.commons.net.ftp.FTPCmd;

import java.net.InetSocketAddress;

public class FtpCrawler {

    public static void main(String[] args) {
        Crawler crawler = new Crawler(new QueueScheduler(), new FtpDownloader());
        crawler.addProcessor(new FtpTraverseProcessor());
        crawler.addStartRequest(new FtpCommand(new InetSocketAddress("my.ss.sysu.edu.cn/~wh", 21), FTPCmd.MLSD));
        // Pipeline is not needed. Listing results will be logged by `FtpTraverseProcessor`

        crawler.start();
    }

}
