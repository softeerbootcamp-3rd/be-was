package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request {
    private static final String ROOT_PATH = "src/main/resources/";
    private static final String DEFAULT_PAGE = "/index.html";

    private String method;
    private String url;
    private String version;
    private Map<String, String> headers;

    public Request(String line) {
        List<String> startLine = parseStartLine(line);
        this.method = startLine.get(0);
        this.url = startLine.get(1);
        this.version = startLine.get(2);
        this.headers = new HashMap<>();
    }

    private List<String> parseStartLine(String startLine) {
        if (startLine == null || startLine.trim().isEmpty()) {
            throw new IllegalArgumentException("Request line is null or empty");
        }
        List<String> split = List.of(startLine.split(" "));
        if (split.size() != 3) {
            throw new IllegalArgumentException("Invalid Start line");
        }
        return split;
    }

    public void addHeader(String headerLine) {
        List<String> parts = List.of(headerLine.split(": "));
        if (parts.size() != 2) {
            throw new IllegalArgumentException("Invalid Header line");
        }
        headers.put(parts.get(0), parts.get(1));

    }

    public String getUrl() {
        return url;
    }

    public String getVersion() {
        return version;
    }

    public String getFilePath() {
        if (url.endsWith(".html")) {
            return ROOT_PATH + "templates" + url;
        }
        return ROOT_PATH + "static" + url;
    }

    @Override
    public String toString() {
        return "Request [method=" + method + ", url=" + url + "]";
    }
}