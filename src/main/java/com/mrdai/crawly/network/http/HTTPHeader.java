package com.mrdai.crawly.network.http;

/**
 * Constant header field names of HTTP request and response
 */
public abstract class HttpHeader {

    /* Request Headers */

    /** Character sets that are acceptable */
    public static final String ACCEPT_CHARSET = "Accept-Charset";
    /** List of acceptable encodings. See HTTP compression. */
    public static final String ACCEPT_ENCODING = "Accept-Encoding";
    /** Acceptable version in time */
    public static final String ACCEPT_DATETIME = "Accept-Datetime";
    /** Authentication credentials for HTTP authentication */
    public static final String AUTHORIZATION = "Authorization";
    /** Control options for the current connection and list of hop-by-hop request fields */
    public static final String CONNECTION = "Connection";
    /** Indicates that particular server behaviors are required by the client */
    public static final String EXPECT = "Expect";
    /** Disclose original information of a client connecting to a web server through an HTTP proxy */
    public static final String FORWARDED = "Forwarded";
    /** The email address of the user making the request */
    public static final String FROM = "From";
    /** Allows a 304 Not Modified to be returned if content is unchanged */
    public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
    /** If the entity is unchanged, send me the part(s) that I am missing; otherwise, send me the entire new entity */
    public static final String IF_RANGE = "If-Range";
    /** Only send the response if the entity has not been modified since a specific time. */
    public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
    /** Informs the server of proxies through which the request was sent. */
    public static final String VIA = "Via";
    /** A general warning about possible problems with the entity body. */
    public static final String WARNING = "Warning";

    /* Common non-standard request fields */

    /** X-Forwarded-For */
    public static final String X_FORWARDED_FOR = "X-Forwarded-For";
    /** X-Forwarded-Host */
    public static final String X_FORWARDED_HOST = "X-Forwarded-Host";
    /** Front-End-Https */
    public static final String FRONT_END_HTTPS = "Front-End-Https";
    /** X-Att-Deviceid */
    public static final String X_ATT_DEVICEID = "X-ATT-DeviceId";
    /** X-WAP-PROFILE */
    public static final String X_WAP_PROFILE = "X-Wap-Profile";

    /* Response fields */

    /** Specifying which web sites can participate in cross-origin resource sharing */
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    /** Specifies which patch document formats this server supports */
    public static final String ACCEPT_PATCH = "Accept-Patch";
    /** What partial content range types this server supports via byte serving */
    public static final String ACCEPT_RANGES = "Accept-Ranges";
    /** The age the object has been in a proxy cache in seconds */
    public static final String AGE = "Age";
    /** Valid actions for a specified resource. To be used for a 405 Method not allowed */
    public static final String ALLOW = "Allow";
    /** Tells all caching mechanisms from server to client whether they may cache this object. It is measured in seconds */
    public static final String CACHE_CONTROL = "Cache-Control";
    /** An opportunity to raise a "File Download" dialogue box for a known MIME type with binary format or suggest a filename for dynamic content. Quotes are necessary with special characters. */
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    /** The type of encoding used on the data. */
    public static final String CONTENT_ENCODING = "Content-Encoding";
    /** The length of the response body in octets (8-bit bytes) */
    public static final String CONTENT_LENGTH = "Content-Length";
    /** An alternate location for the returned data */
    public static final String CONTENT_LOCATION = "Content-Location";
    /** Where in a full body message this partial message belongs */
    public static final String CONTENT_RANGE = "Content-Range";
    /** The MIME type of this content */
    public static final String CONTENT_TYPE = "Content-Type";
    /** The date and time that the message was sent (in "HTTP-date" format as defined by RFC 7231) */
    public static final String DATE = "Date";
    /** An identifier for a specific version of a resource, often a message digest */
    public static final String ETAG = "ETag";
    /** Gives the date/time after which the response is considered stale (in "HTTP-date" format as defined by RFC 7231) */
    public static final String EXPIRES = "Expires";
    /** The last modified date for the requested object (in "HTTP-date" format as defined by RFC 7231) */
    public static final String LAST_MODIFIED = "Last-Modified";
    /** Used to express a typed relationship with another resource, where the relation type is defined by RFC 5988 */
    public static final String LINK = "Link";
    /** Used in redirection, or when a new resource has been created. */
    public static final String LOCATION = "Location";
    /** Implementation-specific fields that may have various effects anywhere along the request-response chain. */
    public static final String PRAGMA = "Pragma";
    /** Request authentication to access the proxy. */
    public static final String PROXY_AUTHENTICATE = "Proxy-Authenticate";
    /** If an entity is temporarily unavailable, this instructs the client to try again later. Value could be a specified period of time (in seconds) or a HTTP-date. */
    public static final String RETRY_AFTER = "Retry-After";
    /** A name for the server */
    public static final String SERVER = "Server";
    /** An HTTP cookie */
    public static final String SET_COOKIE = "Set-Cookie";
    /** CGI header field specifying the status of the HTTP response. Normal HTTP responses use a separate "Status-Line" instead, defined by RFC 7230.[6] */
    public static final String STATUS = "Status";
    /** A HSTS Policy informing the HTTP client how long to cache the HTTPS only policy and whether this applies to subdomains. */
    public static final String STRICT_TRANSPORT_SECURITY = "Strict-Transport-Security";
    /** The Trailer general field value indicates that the given set of header fields is present in the trailer of a message encoded with chunked transfer coding. */
    public static final String TRAILER = "Trailer";
    /** The form of encoding used to safely transfer the entity to the user. Currently defined methods are: chunked, compress, deflate, gzip, identity. */
    public static final String TRANSFER_ENCODING = "Transfer-Encoding";
    /** Tracking Status Value, value suggested to be sent in response to a DNT(do-not-track) */
    public static final String TSV = "TSV";
    /** Ask the client to upgrade to another protocol. */
    public static final String UPGRADE = "Upgrade";
    /** Tells downstream proxies how to match future request headers to decide whether the cached response can be used rather than requesting a fresh one from the origin server. */
    public static final String VARY = "Vary";
    /** Indicates the authentication scheme that should be used to access the requested entity. */
    public static final String WWW_AUTHENTICATE = "WWW-Authenticate";

    /* Common non-standard response fields */

    /** Content-Security-Policy */
    public static final String CONTENT_SECURITY_POLICY = "Content-Security-Policy";
    /** X-Content-Security-Policy */
    public static final String X_CONTENT_SECURITY_POLICY = "X-Content-Security-Policy";
    /** X-WebKit-Csp */
    public static final String X_WEBKIT_CSP = "X-WebKit-Csp";
}
