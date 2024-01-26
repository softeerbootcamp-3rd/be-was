package dto;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import static common.Logger.printRequest;
import static http.RequestParser.*;

public class HttpRequest {

    private final String method;
    private final String path;
    private final String queryString;
    private final Map<String, String> headers;
    private final String body;

    public HttpRequest(BufferedReader br) throws IllegalAccessException, IOException {
        String line = br.readLine();
        this.method = parseMethod(line);
        String[] tokens = parseUrl(line);
        this.path = tokens[0];
        this.queryString = tokens[1];
        this.headers = parseHeaders(br, line);
        if (headers.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            this.body = getRequestBody(br, contentLength);
        } else {
            this.body = null;
        }
        printRequest(this);
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getQueryString() {
        return queryString;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    private String getRequestBody(BufferedReader br, int contentLength) throws IOException {
        char[] buffer = new char[contentLength];
        br.read(buffer, 0, contentLength);
        return new String(buffer);
    }
}
