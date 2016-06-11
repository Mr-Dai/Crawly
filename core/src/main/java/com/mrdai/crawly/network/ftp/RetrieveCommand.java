package com.mrdai.crawly.network.ftp;

import java.io.OutputStream;
import java.net.InetSocketAddress;

public class RetrieveCommand extends FtpCommand {
    private final String remotePathname;
    private final String localPathname;
    private final OutputStream localStream;

    public RetrieveCommand(InetSocketAddress host, String remotePathname, String localPathname) {
        this(host, remotePathname, localPathname, null);
    }

    public RetrieveCommand(InetSocketAddress host, String remotePathname, OutputStream localStream) {
        this(host, remotePathname, null, localStream);
    }

    protected RetrieveCommand(InetSocketAddress host, String remotePathname,
                              String localPathname, OutputStream localStream) {
        super(host);
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
