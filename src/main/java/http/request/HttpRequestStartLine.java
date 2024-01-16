package http.request;

public class HttpRequestStartLine {
    private String httpMethod;
    private String requestTarget;
    private String httpVersion;

    public HttpRequestStartLine(String httpMethod, String requestTarget, String httpVersion) {
        this.httpMethod = httpMethod;
        this.requestTarget = requestTarget;
        this.httpVersion = httpVersion;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getRequestTarget() {
        return requestTarget;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        return sb
            .append("httpMethod : ").append(httpMethod).append("\n")
            .append("requestTarget : ").append(requestTarget).append("\n")
            .append("httpVersion : ").append(httpVersion).toString();
    }
}
