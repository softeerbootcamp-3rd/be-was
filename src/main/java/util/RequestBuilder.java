package util;

public class RequestBuilder {

    private String ip;
    private String port;
    private String httpMethod;
    private String uri;
    private String httpVersion;
    private String host;
    private String connection;
    private String userAgent;
    private String referer;
    private String accept;
    private String acceptLanguage;
    private String acceptEncoding;

    public RequestBuilder(String ip, String port, String httpMethod, String uri, String httpVersion, String host,
                          String connection, String userAgent, String referer, String accept,
                          String acceptLanguage, String acceptEncoding) {
        this.ip = ip;
        this.port = port;
        this.httpMethod = httpMethod;
        this.uri = uri;
        this.httpVersion = httpVersion;
        this.host = host;
        this.connection = connection;
        this.userAgent = userAgent;
        this.referer = referer;
        this.accept = accept;
        this.acceptLanguage = acceptLanguage;
        this.acceptEncoding = acceptEncoding;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return "Request [ip=" + ip + ", port=" + port + ", method=" + httpMethod + ", uri=" + uri +
                ", http_version=" + httpVersion + ", host=" + host + ", accept=" + accept + "]";
    }

}
