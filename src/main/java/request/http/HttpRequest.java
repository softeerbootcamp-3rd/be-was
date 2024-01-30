package request.http;

import model.User;

import java.io.BufferedReader;
import java.io.IOException;

import static db.Session.*;

public class HttpRequest {
    private String method;
    private String uri;
    private String httpVersion;
    private RequestHeaders requestHeaders;
    private String requestBody;

    //헤더 정보 저장 & 테스트
    public HttpRequest(BufferedReader br) throws IOException {
        String readLine = br.readLine();
        String[] tokens = readLine.split(" ");

        //request line 저장
        this.method = tokens[0];
        this.uri = tokens[1];
        this.httpVersion = tokens[2];
        this.requestHeaders = new RequestHeaders();

        //header 정보 저장
        requestHeaders.setRequestHeaders(readLine, br);

        String contentLength = requestHeaders.getValue("Content-Length");
        //body 정보 저장
        if (contentLength != null && !contentLength.equals("0")) {
            setRequestBody(br, contentLength);
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

    public RequestHeaders getRequestHeaders() {
        return requestHeaders;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(BufferedReader br, String contentLength) throws IOException {;
        char[] body = new char[Integer.parseInt(contentLength)];
        br.read(body, 0, Integer.parseInt(contentLength));

        this.requestBody = String.valueOf(body);
    }

    public String getFilePath(String path) {
        if (path.endsWith(".html")) {
            return "src/main/resources/templates" + path;
        }
        return "src/main/resources/static" + path;
    }
}
