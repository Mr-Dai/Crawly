package com.mrdai.crawly.examples.ftp;

import com.mrdai.crawly.ResultItems;
import com.mrdai.crawly.network.Response;
import com.mrdai.crawly.network.ftp.FtpCommand;
import com.mrdai.crawly.network.ftp.FtpFilesReply;
import com.mrdai.crawly.processor.PageProcessor;
import org.apache.commons.net.ftp.FTPCmd;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FtpTraverseProcessor implements PageProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(FtpTraverseProcessor.class);

    @Override
    public boolean supports(Response response) {
        return response instanceof FtpFilesReply;
    }

    @Override
    public ResultItems process(Response response) {
        ResultItems result = new ResultItems(response.getRequest());
        FtpCommand fRequest = (FtpCommand) response.getRequest();
        FtpFilesReply fResponse = (FtpFilesReply) response;
        FTPFile[] fetchedNames = fResponse.getFtpFiles();

        for (FTPFile name : fetchedNames) {
            if (name.isDirectory()) {
                result.addRequest(new FtpCommand(fRequest.getHost(), FTPCmd.MLSD, name.getRawListing()));
                LOG.info("Found FTP directory {}.", name);
            } else if (name.isSymbolicLink()) {
                LOG.info("Found FTP symbolic link {} -> {}.", name, name.getLink());
            } else {
                LOG.info("Found FTP name {}.", name);
            }
        }

        return result;
    }
}
