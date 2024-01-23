package request;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private String method;
    private String uri;
    private String httpVersion;
    private final Map<String, String> requestHeaders = new HashMap<>();
    private String requestBody;

    //헤더 정보 저장 & 테스트
    public HttpRequest(BufferedReader br) throws IOException {
        String readLine = br.readLine();
        String[] tokens = readLine.split(" ");

        //request line 저장
        this.method = tokens[0];
        this.uri = tokens[1];
        this.httpVersion = tokens[2];

        //header 정보 저장
        setRequestHeaders(readLine, br);

        //body 정보 저장
        if (requestHeaders.containsKey("Content-Length") && !requestHeaders.get("Content-Length").equals("0")) {
            setRequestBody(br);
        }
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestHeaders(String readLine, BufferedReader br) throws IOException {
        for (readLine = br.readLine(); !readLine.isEmpty(); readLine = br.readLine()) {
            String[] headerTokens = readLine.split(": ");
            requestHeaders.put(headerTokens[0], headerTokens[1]);
        }
    }

    public void setRequestBody(BufferedReader br) throws IOException {
        int contentLength = Integer.parseInt(requestHeaders.get("Content-Length"));
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);

        this.requestBody = String.valueOf(body);
    }

    public Path getFilePath(String path) {
        if (path.endsWith(".html")) {
            return Paths.get("src/main/resources/templates", path);
        }
        return Paths.get("src/main/resources/static", path);
    }
}
