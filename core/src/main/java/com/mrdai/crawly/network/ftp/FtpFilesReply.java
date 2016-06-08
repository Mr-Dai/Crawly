package com.mrdai.crawly.network.ftp;

import org.apache.commons.net.ftp.FTPFile;

public class FtpFilesReply extends FtpReply {
    private final FTPFile[] ftpFiles;

    public FtpFilesReply(FtpCommand command, int code, FTPFile ftpFile) {
        this(command, code, new FTPFile[] {ftpFile});
    }

    public FtpFilesReply(FtpCommand command, int code, FTPFile[] ftpFiles) {
        super(command, code);
        this.ftpFiles = ftpFiles;
    }

    public FTPFile[] getFtpFiles() {
        return ftpFiles;
    }
}
