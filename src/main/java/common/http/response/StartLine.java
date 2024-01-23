package common.http.response;

public class StartLine {

    private String httpVersion;
    private HttpStatusCode statusCode;
    private String statusText;

    public StartLine(HttpStatusCode statusCode) {
        this.httpVersion = "HTTP/1.1";
        this.statusCode = statusCode;
        this.statusText = statusCode.getReasonPhrase();
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusCode(HttpStatusCode statusCode) {
        this.statusCode = statusCode;
        this.statusText = statusCode.getReasonPhrase();
    }

    @Override
    public String toString() {
        return httpVersion + " " + statusCode + " " + statusText + "\r\n";
    }
}
