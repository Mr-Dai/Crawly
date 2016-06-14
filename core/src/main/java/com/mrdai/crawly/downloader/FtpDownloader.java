package com.mrdai.crawly.downloader;

import com.mrdai.crawly.network.Request;
import com.mrdai.crawly.network.Response;
import com.mrdai.crawly.network.ftp.*;
import org.apache.commons.net.ftp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FtpDownloader implements Downloader {
    private static final Logger LOG = LoggerFactory.getLogger(FtpDownloader.class);

    private final Map<InetSocketAddress, FTPClientWrapper> wrappers = new HashMap<>();
    private final Thread keepAliveThread;

    public FtpDownloader() {
        this(0);
    }

    public FtpDownloader(int keepAliveInterval) {
        if (keepAliveInterval <= 0)
            keepAliveThread = null;
        else {
            LOG.info("Starting keep-alive worker thread. Sending keep-alive signal every {} seconds.", keepAliveInterval);
            keepAliveThread = new Thread(new KeepAliveWorker(keepAliveInterval), "FtpDownloader.KeepAliveThread");
            keepAliveThread.setDaemon(true);
            keepAliveThread.start();
        }
    }

    @Override
    public Response download(Request request) throws IOException {
        FtpCommand command = (FtpCommand) request;
        InetSocketAddress host = command.getHost();
        FTPClient client;
        FTPClientWrapper wrapper;

        // Synchronize on wrappers map to avoid duplicate constructions
        synchronized (wrappers) {
            if (!wrappers.containsKey(host)) {
                LOG.debug("Predefined constructor for host {} not found, using default FTPClient.", host);
                client = new FTPClient();
                client.enterLocalPassiveMode();
                wrapper = new FTPClientWrapper(client, null);
                wrappers.put(host, wrapper);
            } else {
                wrapper = wrappers.get(host);
            }
        }

        // Synchronize on single wrapper to avoid concurrent usage of single FTPClient,
        // which is not thread safe.
        synchronized (wrapper) {
            if (wrapper.client == null) {
                LOG.debug("Constructing FTPClient for host {} with predefined constructor.", host);
                client = wrapper.configurer.construct();
                wrapper.client = client;
            } else {
                client = wrapper.client;
            }

            if (!client.isConnected()) {
                LOG.info("The FTPClient is not connected. Connecting to {}.", host);
                client.connect(host.getAddress(), host.getPort());
                LOG.debug("Log in anonymously.");
                client.login("anonymous", "https://github.com/Mr-Dai/Crawly");
                LOG.debug("Server returned \"{}\".",  client.getReplyString().trim());
                if (!FTPReply.isPositiveCompletion(client.getReplyCode())) {
                    LOG.warn("Failed to connect to target host {}. The server reply is: {} {}",
                        host, client.getReplyCode(), client.getReplyString());
                } else
                    LOG.info("Connected to {}:{}.", client.getRemoteAddress(), client.getRemotePort());
            }

            try {
                // Commands whose replies can be parsed into array of `FTPFile`s
                if (command.getCommand().equalsIgnoreCase(FTPCmd.MLST.getCommand())) {
                    LOG.debug("Issue MLST command: {}", command);
                    FTPFile file = client.mlistFile(command.getParameters());
                    LOG.debug("Server returned {}.", file);
                    return new FtpFilesReply(command, client.getReplyCode(), file);
                }
                if (command.getCommand().equalsIgnoreCase(FTPCmd.MLSD.getCommand())) {
                    LOG.debug("Issue MLSD command: {}", command);
                    FTPFile[] files = client.mlistDir(command.getParameters());
                    LOG.debug("Server returned {}.", Arrays.toString(files));
                    return new FtpFilesReply(command, client.getReplyCode(), files);
                }
                if (command.getCommand().equalsIgnoreCase(FTPCmd.LIST.getCommand())) {
                    LOG.debug("Issue LIST command: {}", command);
                    FTPFile[] files = client.listFiles(command.getParameters());
                    LOG.debug("Server returned {}.", Arrays.toString(files));
                    return new FtpFilesReply(command, client.getReplyCode(), files);
                }

                // Subclasses of `FtpCommand`
                if (command instanceof RetrieveCommand) {
                    LOG.debug("Issue file retrieve command: {}", command);
                    RetrieveCommand rCommand = (RetrieveCommand) command;
                    OutputStream localStream =
                        rCommand.getLocalStream() == null ?
                            rCommand.getLocalStream() : new FileOutputStream(rCommand.getLocalPathname());
                    client.retrieveFile(rCommand.getRemotePathname(), localStream);
                    localStream.close();
                } else if (command instanceof AppendCommand) {
                    LOG.debug("Issue file append command: {}", command);
                    AppendCommand aCommand = (AppendCommand) command;
                    InputStream localStream =
                        aCommand.getLocalStream() == null ?
                            aCommand.getLocalStream() : new FileInputStream(aCommand.getLocalPathname());
                    client.appendFile(aCommand.getRemotePathname(), localStream);
                    localStream.close();
                } else if (command instanceof StoreUniqueCommand) {
                    LOG.debug("Issue file store unique command: {}", command);
                    StoreUniqueCommand suCommand = (StoreUniqueCommand) command;
                    InputStream localStream =
                        suCommand.getLocalStream() == null ?
                            suCommand.getLocalStream() : new FileInputStream(suCommand.getLocalPathname());
                    client.storeUniqueFile(suCommand.getRemotePathname(), localStream);
                    localStream.close();
                } else if (command instanceof StoreCommand) {
                    LOG.debug("Issue file store command: {}", command);
                    StoreCommand sCommand = (StoreCommand) command;
                    InputStream localStream =
                        sCommand.getLocalStream() == null ?
                            sCommand.getLocalStream() : new FileInputStream(sCommand.getLocalPathname());
                    client.storeFile(sCommand.getRemotePathname(), localStream);
                    localStream.close();
                } else if (command instanceof LoginRequest) {
                    LOG.debug("Issue login request: {}", command);
                    LoginRequest lRe = (LoginRequest) command;
                    client.login(lRe.getUsername(), lRe.getPassword());
                    LOG.debug("Server returned: {}.", client.getReplyString());
                } else {
                    LOG.debug("Issue command FTP command: {}", command);
                    client.doCommand(command.getCommand(), command.getParameters());
                    LOG.debug("Server returned: {}.", client.getReplyString());
                }
            } catch (FTPConnectionClosedException e) {
                LOG.warn("Remote server {}:{} closed the connection prematurely. Reconnect on next request.",
                    client.getRemoteAddress(), client.getRemotePort());
                client.disconnect();
            }

            return new FtpReply(command, client.getReplyCode(), client.getReplyString());
        }
    }

    @Override
    public boolean supports(Request request) {
        return request instanceof FtpCommand;
    }

    @Override
    public void close() throws IOException {
        if (keepAliveThread != null)
            keepAliveThread.interrupt();

        for (FTPClientWrapper wrapper : wrappers.values()) {
            FTPClient client = wrapper.client;
            if (client == null)
                continue;

            if (client.isConnected()) {
                client.abort();
                client.logout();
                client.disconnect();
            }
        }

        wrappers.clear();
    }

    public void addConfigureers(InetSocketAddress host, FTPClientConstructor configurer) {
        if (wrappers.containsKey(host))
            wrappers.get(host).configurer = configurer;
        else
            wrappers.put(host, new FTPClientWrapper(null, configurer));
    }

    private static class FTPClientWrapper {
        FTPClient client;
        FTPClientConstructor configurer;

        FTPClientWrapper(FTPClient client, FTPClientConstructor configurer) {
            this.client = client;
            this.configurer = configurer;
        }
    }

    private class KeepAliveWorker implements Runnable {
        private final int sleepTime;

        KeepAliveWorker(int sleepTime) {
            this.sleepTime = sleepTime * 1000;
        }

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                try {
                    wait(sleepTime);
                } catch (InterruptedException e) {
                    LOG.debug("Interrupted.");
                }

                LOG.debug("Sending NOOP command to keep alive.");

                for (FTPClientWrapper wrapper : wrappers.values()) {
                    if (wrapper.client != null && wrapper.client.isConnected()) {
                        try {
                            wrapper.client.sendNoOp();
                        } catch (IOException e) {
                            LOG.warn("Exception occurred when sending NOOP command to server "
                                         + wrapper.client.getRemoteAddress() + ':' + wrapper.client.getRemotePort(),
                                e);
                        }
                    }
                }
            }
        }
    }
}
