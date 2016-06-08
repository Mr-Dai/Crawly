package com.mrdai.crawly.network.ftp;

import java.io.InputStream;
import java.net.InetSocketAddress;

public class AppendCommand extends StoreCommand {
    public AppendCommand(InetSocketAddress host, String remotePathname, InputStream localStream) {
        super(host, remotePathname, localStream);
    }

    public AppendCommand(InetSocketAddress host, String remotePathname, String localPathname) {
        super(host, remotePathname, localPathname);
    }
}
