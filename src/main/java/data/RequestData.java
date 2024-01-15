package data;

public class RequestData {
    private final String method;
    private final String requestContent;
    private final String httpVersion;

    public RequestData(String method, String requestContent, String httpVersion) {
        this.method = method;
        this.requestContent = requestContent;
        this.httpVersion = httpVersion;
    }

    public String getMethod() {
        return method;
    }

    public String getRequestContent() {
        return requestContent;
    }

    public String getHttpVersion() {
        return httpVersion;
    }
}
