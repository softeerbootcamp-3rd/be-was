package model.http;

public class StatusLine {
    private final String version;
   private final Status status;

    public StatusLine(String version, Status status) {
        this.version = version;
        this.status = status;
    }

    @Override
    public String toString() {
        return version + " " + status.getStatusCode() + " " + status.getStatusText() + " \r\n";
    }
}
