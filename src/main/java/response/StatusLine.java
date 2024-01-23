package response;

import util.StatusCode;

public class StatusLine {
    private final String version;
    private final StatusCode statusCode;

    public StatusLine(StatusCode statusCode) {
        this.version = "HTTP/1.1";
        this.statusCode = statusCode;
    }

    public Integer getStatusCode() {
        return statusCode.getStatus();
    }
}
