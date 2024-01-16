package model.http;

public class RequestHeaders {
    private final String host;
    private final String userAgent;
    private final String accept;
    private final String connection;
    private final ContentType contentType;
    private final String charSet;
    private final Integer contentLength;

    public RequestHeaders(String host, String userAgent, String accept, String connection, ContentType contentType, Integer contentLength) {
        this.host = host;
        this.userAgent = userAgent;
        this.accept = accept;
        this.connection = connection;
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.charSet="utf-8";
    }

    public RequestHeaders(String host, String userAgent, String accept, String connection, ContentType contentType, String charset, Integer contentLength) {
        this.host = host;
        this.userAgent = userAgent;
        this.accept = accept;
        this.connection = connection;
        this.contentType = contentType;
        this.charSet = charset;
        this.contentLength = contentLength;
    }

    public String getHost() {
        return host;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getAccept() {
        return accept;
    }

    public String getConnection() {
        return connection;
    }

    public String getContentType() {
        return contentType + ";" + charSet;
    }

    public Integer getContentLength() {
        return contentLength;
    }
}
