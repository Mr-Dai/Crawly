package com.mrdai.crawly.network.ftp;

import com.mrdai.crawly.network.Response;

import java.net.InetSocketAddress;

/**
 * A login request to the specific FTP server. A complete login consists of
 * a {@code USER} command and a following {@code PASS} command. The username
 * and password specified in this class will be sent in two respective commands,
 * and the replies will be merged into one {@link Response}.
 */
public class LoginRequest extends FtpCommand {
    private final String username;
    private final String password;

    /**
     * Creates an FTP login request with the given username and password.
     *
     * @param username the given username.
     * @param password the given password.
     */
    public LoginRequest(InetSocketAddress host, String username, String password) {
        super(host);
        this.username = username;
        this.password = password;
    }

    /**
     * Returns the username of this {@code LoginRequest}.
     * @return the username of this {@code LoginRequest}.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the password of this {@code LoginRequest}.
     * @return the password of this {@code LoginRequest}.
     */
    public String getPassword() {
        return password;
    }
}
