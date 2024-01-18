package webserver;

import util.URIParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private final String method;
    private final String path;
    private final Map<String, String> paramMap;
    private final Map<String, String> header;

    public HttpRequest(String requestString) {
        String[] requestParts = requestString.split(" ");
        this.method = requestParts[0];
        this.path = URIParser.extractPath(requestParts[1]);
        this.paramMap = URIParser.parseQueryString(URIParser.extractQuery(requestParts[1]));
        this.header = new HashMap<>();
    }

    public HttpRequest(BufferedReader reader) throws IOException {
        String requestLine = reader.readLine();
        String[] requestParts = requestLine.split(" ");
        this.method = requestParts[0];
        this.path = URIParser.extractPath(requestParts[1]);
        this.paramMap = URIParser.parseQueryString(URIParser.extractQuery(requestParts[1]));
        this.header = new HashMap<>();

        String s;
        while (!(s = reader.readLine()).isEmpty()) {
            requestParts = s.split(":\\s*", 2);
            if (requestParts.length == 2) {
                this.header.put(requestParts[0], requestParts[1]);
            }
        }
    }

    public String getMethod() {
        return this.method;
    }

    public String getPath() {
        return this.path;
    }


    public Map<String, String> getParamMap() {
        return this.paramMap;
    }

    public Map<String, String> getHeader() {
        return this.header;
    }
}
