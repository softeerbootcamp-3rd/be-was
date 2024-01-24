package common.http.request;

public class StartLine {

    private HttpMethod httpMethod;
    private String requestTarget;
    private String httpVersion;

    public StartLine(String httpMethod, String requestTarget, String httpVersion) {
        this.httpMethod = HttpMethod.of(httpMethod);
        this.requestTarget = requestTarget;
        this.httpVersion = httpVersion;
    }

    public HttpMethod getHttpMethod() {
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
