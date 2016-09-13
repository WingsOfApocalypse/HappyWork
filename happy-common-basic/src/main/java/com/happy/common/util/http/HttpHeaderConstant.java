package com.happy.common.util.http;

/**
 * @author Derek.Wu
 * @Date 2016年09月13日
 */
public class HttpHeaderConstant {

    public static final String RESPONSE_CODE             = "response-code";
    public static final String CACHE_CONTROL             = "cache-control";
    public static final String MAX_AGE                   = "max-age";
    public static final String NO_CACHE                  = "no-cache";
    public static final String LAST_MODIFIED             = "last-modified";
    public static final String IF_MODIFIED_SINCE         = "if-modified-since";
    public static final String IF_NONE_MATCH             = "if-none-match";
    public static final String REDIRECT_LOCATION         = "location";
    public static final String ETAG                      = "etag";
    public static final String EXPIRES_TIME              = "expires";
    public static final String GZIP                      = "gzip";
    public static final String ACCEPT                    = "accept";
    public static final String ACCEPT_ENCODING           = "accept-encoding";
    public static final String SET_COOKIE                = "Set-Cookie";

    public static final String CHARSET                   = "charset";
    public static final String CHARSET_DEFAULT           = "ISO-8859-1";
    public static final String CHARSET_UTF8              = "UTF-8";

    public static final String CONTENT_LENGTH            = "Content-Length";
    public static final String CONTENT_ENCODING          = "Content-Encoding";
    public static final String CONTENT_RANGE             = "Content-Range";

    public static final String CONTENT_TYPE              = "Content-Type";
    public static final String CONTENT_TYPE_HTML         = "text/html";
    public static final String CONTENT_TYPE_FORM         = "application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE_FORM_DATA    = "multipart/form-data";
    public static final String CONTENT_TYPE_FILE         = "application/octet-stream";
    public static final String CONTENT_TYPE_RESUME_FILE  = "application/offset+octet-stream";
    public static final String CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";
    public static final String CONTENT_DISPOSITION       = "Content-Disposition";

    public static final String ENTITY_LENGTH             = "Entity-Length";

    /**
     * HTTP Header key: User-Agent
     */
    public static final String USER_AGENT                = "User-Agent";

}
