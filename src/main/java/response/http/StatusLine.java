package response.http;

import util.StatusCode;

public class StatusLine {
    private final String version;
    private final StatusCode statusCode;
    private final String reasonPhrase;

    public StatusLine(StatusCode statusCode) {
        this.version = "HTTP/1.1";
        this.statusCode = statusCode;
        this.reasonPhrase = statusCode.name();
    }

    public Integer getStatusCode() {
        return statusCode.getStatus();
    }
    public String getVersion() {
        return version;
    }
    public String getReasonPhrase() {
        return reasonPhrase;
    }
}
