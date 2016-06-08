package com.mrdai.crawly.network.ftp;

import java.io.InputStream;
import java.net.InetSocketAddress;

/**
 * An FTP command for storing files on the FTP server.
 */
public class StoreCommand extends FtpCommand {
    private final String remotePathname;
    private final String localPathname;
    private final InputStream localStream;

    public StoreCommand(InetSocketAddress host, String remotePathname, String localPathname) {
        this(host, remotePathname, localPathname, null);
    }

    public StoreCommand(InetSocketAddress host, String remotePathname, InputStream localStream) {
        this(host, remotePathname, null, localStream);
    }

    protected StoreCommand(InetSocketAddress host, String remotePathname,
                           String localPathname, InputStream localStream) {
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

    public InputStream getLocalStream() {
        return localStream;
    }
}
