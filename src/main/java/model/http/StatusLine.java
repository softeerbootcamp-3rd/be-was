package model.http;

public class StatusLine {
    private final String version;
   private final Status status;

    public StatusLine(String version, Status status) {
        this.version = version;
        this.status = status;
    }

    public String getVersion() {
        return version;
    }

    public Status getStatus() {
        return status;
    }
}
