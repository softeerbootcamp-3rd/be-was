package model.http.response;

import model.http.Status;

public class StatusLine {
    private final String version;
    private final Status status;

    public StatusLine(String version, Status status) {
        this.version = version;
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public String getStatusHeader() {
        return version + " " + status.getStatusCode() + " " + status.getStatusText() + " \r\n";
    }
}
