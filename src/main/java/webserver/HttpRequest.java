package webserver;

import util.RequestParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private final String method;
    private final String path;
    private final Map<String, String> paramMap;
    private final Map<String, String> header;
    private final String body;

    public HttpRequest(BufferedReader reader) throws IOException {
        String requestLine = reader.readLine();
        String[] requestParts = requestLine.split(" ");
        this.method = requestParts[0];
        this.path = RequestParser.extractPath(requestParts[1]);
        this.paramMap = RequestParser.parseQueryString(RequestParser.extractQuery(requestParts[1]));
        this.header = new HashMap<>();

        String s;
        while ((s = reader.readLine()) != null && !s.isEmpty()) {
            requestParts = s.split(":\\s*", 2);
            if (requestParts.length == 2) {
                this.header.put(requestParts[0], requestParts[1]);
            }
        }
        if (header.get("Content-Length") != null) {
            char[] buf = new char[Integer.parseInt(header.get("Content-Length"))];
            reader.read();
            body = new String(buf);
        }else
            body = "";
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

    public String getBody() {
        return this.body;
    }
}
