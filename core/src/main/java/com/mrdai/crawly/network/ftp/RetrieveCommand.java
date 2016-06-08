package com.mrdai.crawly.network.ftp;

import java.io.OutputStream;

public class RetrieveCommand extends FtpCommand {
    private final String remotePathname;
    private final String localPathname;
    private final OutputStream localStream;

    public RetrieveCommand(String remotePathname, String localPathname) {
        this(remotePathname, localPathname, null);
    }

    public RetrieveCommand(String remotePathname, OutputStream localStream) {
        this(remotePathname, null, localStream);
    }

    protected RetrieveCommand(String remotePathname, String localPathname, OutputStream localStream) {
        this.remotePathname = remotePathname;
        this.localPathname = localPathname;
        this.localStream = localStream;
    }

    public String getRemotePathname() {
        return remotePathname;
    }

    public String getLocalPathname() {
        return localPathname;
    }

    public OutputStream getLocalStream() {
        return localStream;
    }
}
