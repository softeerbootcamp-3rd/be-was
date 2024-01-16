package model.http;

public class RequestHeaders {
    private final String host;
    private final String userAgent;
    private final String accept;
    private final String connection;
    private final ContentType contentType;
    private final Integer contentLength;

    public RequestHeaders(String host, String userAgent, String accept, String connection, ContentType contentType, Integer contentLength) {
        this.host = host;
        this.userAgent = userAgent;
        this.accept = accept;
        this.connection = connection;
        this.contentType = contentType;
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

    public ContentType getContentType() {
        return contentType;
    }

    public Integer getContentLength() {
        return contentLength;
    }
}
