package dto;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import static common.Logger.printRequest;
import static webserver.RequestParser.*;

public class HttpRequest {

    private final String method;
    private final String path;
    private final String queryString;
    private final Map<String, String> headers;

    public HttpRequest(BufferedReader br) throws IllegalAccessException, IOException {
        String line = br.readLine();
        this.method = parseMethod(line);
        String[] tokens = parseUrl(line);
        this.path = tokens[0];
        this.queryString = tokens[1];
        this.headers = parseHeaders(br, line);
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
}
