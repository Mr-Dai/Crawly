package com.mrdai.crawly.network.ftp;

import com.mrdai.crawly.network.Request;
import org.apache.commons.net.ftp.FTPCmd;

import java.net.InetSocketAddress;
import java.net.URI;

/**
 * An FTP command will be sent to the FTP server via the control connection, and
 * the FTP server should issue an FTP reply. Commonly, an FTP command is made up
 * of the {@code COMMAND} string and the {@code Parameters} string. The two getter
 * methods defined in this interface return the two fields respectively.
 * <p>
 * As the simplest FTP command implementation, the default FTP downloader will only
 * handle instances of this class via control connection, i.e. expecting server responses
 * only on control connection. If the FTP command you want to send involves data connection
 * or you want more detailed support for some specific FTP command, please refer to the
 * subclasses of this class.
 *
 * @see FtpReply
 */
public class FtpCommand implements Request {
    private final InetSocketAddress host;
    private final String command;
    private final String params;

    FtpCommand(InetSocketAddress host) {
        this(host, "", null);
    }

    /**
     * Creates an {@code FtpCommand} with the given command.
     *
     * @param command the given command.
     */
    public FtpCommand(InetSocketAddress host, FTPCmd command) {
        this(host, command.getCommand());
    }

    /**
     * Creates an {@code FtpCommand} with the given command string.
     * <p>
     * Convenient constants for standardized FTP commands can be found in
     * {@link org.apache.commons.net.ftp.FTPCmd FTPCmd}.
     *
     * @param host the target server to which this command should be sent to.
     * @param command the given command string.
     *
     * @see org.apache.commons.net.ftp.FTPCmd
     */
    public FtpCommand(InetSocketAddress host, String command) {
        this(host, command, null);
    }

    /**
     * Creates an {@code FtpCommand} with the given command and parameters string.
     *
     * @param host the target server to which this command should be sent to.
     * @param command the given command.
     * @param params the given parameters string.
     */
    public FtpCommand(InetSocketAddress host, FTPCmd command, String params) {
        this(host, command.getCommand(), params);
    }

    /**
     * Creates an {@code FtpCommand} with the given command string and parameters string.
     * <p>
     * Convenient constants for standardized FTP commands can be found in
     * {@link org.apache.commons.net.ftp.FTPCmd FTPCmd}.
     *
     * @param host the target server to which this command should be sent to.
     * @param command the given command string.
     * @param params the given parameters string.
     *
     * @see org.apache.commons.net.ftp.FTPCmd
     */
    public FtpCommand(InetSocketAddress host, String command, String params) {
        this.host = host;
        this.command = command;
        this.params = params;
    }

    /**
     * Returns the command field of the {@code FtpCommand} as {@code String}.
     * <p>
     * The returned value must not be {@code null} or empty.
     * @return the command field of the {@code FtpCommand}.
     */
    public String getCommand() {
        return command;
    }

    /**
     * Returns the parameters string of the {@code FtpCommand};
     * returns {@code null} if the {@code FtpCommand} has no parameter.
     *
     * @return the parameters string of the {@code FtpCommand};
     *         {@code null} if the {@code FtpCommand} has no parameter.
     */
    public String getParameters() {
        return params;
    }

    /**
     * Returns the target server of this {@code FtpCommand}.
     * @return the target server of this {@code FtpCommand}.
     */
    public InetSocketAddress getHost() {
        return host;
    }

    @Override
    public String toString() {
        return params == null || params.trim().isEmpty() ?
                   command : command + ' ' + params;
    }
}
