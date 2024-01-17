package model;

import java.io.BufferedReader;
import java.io.IOException;

public class RequestHeader {

    private Integer port;
    private String method;
    private String path;
    private String httpVersion;

    public RequestHeader(BufferedReader br, Integer port) throws IOException {
        String line = br.readLine(); // 예시: GET /index.html HTTP/1.1 중 /index.html 추출
        String[] tokens = line.split(" ");

        this.port = port;
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

    public Integer getPort() {
        return port;
    }
}
