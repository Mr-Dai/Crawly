package com.mrdai.crawly.network.ftp;

import com.mrdai.crawly.network.Request;
import org.apache.commons.net.ftp.FTPCmd;

/**
 * An FTP command will be sent to the FTP server via the control connection, and
 * the FTP server should issue an FTP reply. Commonly, an FTP command is made up
 * of the {@code COMMAND} string and the {@code Parameters} string. The two getter
 * methods defined in this interface return the two fields respectively.
 *
 * @see FtpReply
 */
public class FtpCommand implements Request {
    private final String command;
    private final String params;

    /**
     * Creates an {@code FtpCommand} with the given command.
     *
     * @param command the given command.
     */
    public FtpCommand(FTPCmd command) {
        this(command.getCommand());
    }

    /**
     * Creates an {@code FtpCommand} with the given command string.
     * <p>
     * Convenient constants for standardized FTP commands can be found in
     * {@link org.apache.commons.net.ftp.FTPCmd FTPCmd}.
     *
     * @param command the given command string.
     *
     * @see org.apache.commons.net.ftp.FTPCmd
     */
    public FtpCommand(String command) {
        this(command, null);
    }

    /**
     * Creates an {@code FtpCommand} with the given command and parameters string.
     *
     * @param command the given command.
     * @param params the given parameters string.
     */
    public FtpCommand(FTPCmd command, String params) {
        this(command.getCommand(), params);
    }

    /**
     * Creates an {@code FtpCommand} with the given command string and parameters string.
     * <p>
     * Convenient constants for standardized FTP commands can be found in
     * {@link org.apache.commons.net.ftp.FTPCmd FTPCmd}.
     *
     * @param command the given command string.
     * @param params the given parameters string.
     *
     * @see org.apache.commons.net.ftp.FTPCmd
     */
    public FtpCommand(String command, String params) {
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

    @Override
    public String toString() {
        return params == null || params.trim().isEmpty() ?
                   command : command + ' ' + params;
    }
}
