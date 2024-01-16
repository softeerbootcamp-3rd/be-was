package model.http;

public class StartLine {
    private final String method;
    private final String pathUrl;
    private final String version;
    public StartLine(String method, String pathUrl, String version) {
        this.method = method;
        this.pathUrl = pathUrl;
        this.version = version;
    }
    public String getMethod() {
        return method;
    }

    public String getPathUrl() {
        return pathUrl;
    }

    public String getVersion() {
        return version;
    }
}
