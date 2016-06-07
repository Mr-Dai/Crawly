package com.mrdai.crawly.network.ftp;

import com.mrdai.crawly.network.Request;
import com.mrdai.crawly.network.Response;
import org.apache.commons.net.ftp.FTPReply;

/**
 * An FTP reply message sent by the FTP server in response to an FTP command
 * sent earlier. An FTP reply consists of a 3-digit FTP reply code followed
 * by some text.
 *
 * @see FtpCommand
 */
public class FtpReply implements Response {
    private final FtpCommand command;
    private final int code;
    private final String text;

    /**
     * Creates an {@code FtpReply} with the corresponding {@code FtpCommand}
     * and the given reply code.
     * <p>
     * Convenient constants for FTP reply code can be found in {@link FTPReply}.
     *
     * @param command the corresponding {@code FtpCommand}.
     * @param code the given reply code.
     *
     * @see FTPReply
     */
    public FtpReply(FtpCommand command, int code) {
        this(command, code, null);
    }

    /**
     * Creates an {@code FtpReply} with the corresponding {@code FtpCommand},
     * the given reply code and reply text.
     * <p>
     * Convenient constants for FTP reply code can be found in {@link FTPReply}.
     *
     * @param command the corresponding {@code FtpCommand}.
     * @param code the given reply code.
     * @param text the given reply text.
     *
     * @see FTPReply
     */
    public FtpReply(FtpCommand command, int code, String text) {
        this.command = command;
        this.code = code;
        this.text = text;
    }

    /**
     * Returns the 3-digit reply code of the {@code FtpReply}.
     * <p>
     * Convenient constants and util functions for FTP reply code can be found
     * in {@link FTPReply FTPReply}.
     *
     * @return the 3-digit reply code of the {@code FtpReply}.
     * @see FTPReply
     */
    int getReplyCode() {
        return code;
    }

    /**
     * Returns the text of the {@code FtpReply}. The reply text is mainly for human user,
     * and it may be server-dependent as different server may choose to return different text
     * for the same reply code.
     * <p>
     * The method returns {@code null} if the server did not send any reply string.
     *
     * @return the text of the {@code FtpReply}; {@code null} if the server did not send any reply string.
     */
    String getReplyText() {
        return text;
    }

    @Override
    public Request getRequest() {
        return command;
    }

    @Override
    public String toString() {
        return text == null || text.trim().isEmpty() ?
                   String.valueOf(code) : String.valueOf(code) + ' ' + text;
    }
}
