package request;

import java.io.BufferedReader;
import java.io.IOException;

public class HttpRequest {

    private String method;
    private String path;
    private String httpVersion;

    public HttpRequest(String requestLine) throws IOException {

        String[] tokens = requestLine.split(" ");

        this.method = tokens[0];
        this.path = tokens[1];
        this.httpVersion = tokens[2];
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public String getHttpRequst() {
        return method + " " + path + " " + httpVersion;
    }
}
