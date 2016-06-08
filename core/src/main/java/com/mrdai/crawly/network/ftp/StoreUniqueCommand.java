package com.mrdai.crawly.network.ftp;

import java.io.InputStream;
import java.net.InetSocketAddress;

public class StoreUniqueCommand extends StoreCommand {
    public StoreUniqueCommand(InetSocketAddress host, String remotePathname, String localPathname) {
        super(host, remotePathname, localPathname);
    }

    public StoreUniqueCommand(InetSocketAddress host, String remotePathname, InputStream localStream) {
        super(host, remotePathname, localStream);
    }
}
