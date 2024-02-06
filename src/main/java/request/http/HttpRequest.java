package request.http;

import java.io.IOException;
import java.io.InputStream;

public class HttpRequest {
    private String method;
    private String uri;
    private String httpVersion;
    private RequestHeaders requestHeaders; // 일급 컬렉션을 사용하여 헤더 정보 저장
    private byte[] requestBodyB; // 바이트 배열로 처리해야 하는 경우 (multipart/form-data)
    private String requestBodyS; // 문자열로 처리해야 하는 경우 (그 외)

    //헤더 정보 저장 & 테스트
    public HttpRequest(InputStream in) throws IOException {
        // request line 읽기
        String requestLine = readLine(in);
        String[] tokens = requestLine.split(" ");

        this.method = tokens[0];
        this.uri = tokens[1];
        this.httpVersion = tokens[2];
        this.requestHeaders = new RequestHeaders();

        // header 정보 읽기
        String headerLine;
        while (!(headerLine = readLine(in)).isEmpty()) {
            requestHeaders.addHeader(headerLine);
        }

        // Body 처리
        processRequestBody(in);
    }

    private String readLine(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        int ch;
        while ((ch = in.read()) != -1) {
            if (ch == '\n') {
                break;
            } else if (ch != '\r') {
                sb.append((char) ch);
            }
        }
        return sb.toString();
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

    public String getRequestBodyS() {
        return requestBodyS;
    }

    public byte[] getRequestBodyB() {
        return requestBodyB;
    }

    private void processRequestBody(InputStream in) throws IOException {
        String contentType = getHeaderValueIgnoreCase(requestHeaders, "Content-Type");
        String contentLength = getHeaderValueIgnoreCase(requestHeaders, "Content-Length");

        if (contentLength != null && !contentLength.equals("0")) { // Body가 존재하는 경우 (POST 요청)
            int length = Integer.parseInt(contentLength);
            if (contentType != null && contentType.contains("multipart/form-data")) { // Multipart 형식일 경우, 바이트 배열로 처리
                byte[] body = new byte[length];
                in.read(body, 0, length);
                requestBodyB = body;
            } else {                                                                  // 그 외의 경우, 문자열로 처리
                char[] body = new char[length];
                for (int i = 0; i < length; i++) {
                    body[i] = (char) in.read();
                }
                requestBodyS = new String(body);
            }
        }
    }

    private String getHeaderValueIgnoreCase(RequestHeaders headers, String headerName) {
        for (String key : headers.getRequestHeaders().keySet()) {
            if (key.equalsIgnoreCase(headerName)) {
                return headers.getValue(key);
            }
        }
        return null;
    }


    public String getFilePath(String path) {
        if (path.endsWith(".html")) {
            return "src/main/resources/templates" + path;
        }
        return "src/main/resources/static" + path;
    }
}
