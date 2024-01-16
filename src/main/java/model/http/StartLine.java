package model.http;

public class StartLine {
    private final HttpMethod method;
    private final String pathUrl;
    private final String version;
    public StartLine(HttpMethod method, String pathUrl, String version) {
        this.method = method;
        this.pathUrl = pathUrl;
        this.version = version;
    }

    public String getPathUrl() {
        return pathUrl;
    }
    @Override
    public String toString() {
        return "{" +
                "method=" + method + "\n" +
                ", pathUrl='" + pathUrl + '\n' +
                ", version='" + version +
                '}' + "\n";
    }
}
