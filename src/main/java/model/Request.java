package model;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Request {

    private final String method;
    private final String url;
    private final String http;
    private final Map<String, String> headers = new HashMap<>();
    private final byte[] body;

    /**
     * 요청 메시지를 받아 시작 줄과 각 헤더를 저장합니다.
     *
     * <p> 만약 request 본문이 있을 경우 해당 내용을 저장합니다.
     *
     * @param in 요청 메시지
     * @throws IOException I/O 에러 발생
     */
    public Request(InputStream in) throws IOException {
        // start line
        String header = readLine(in);
        String[] generalHeader = header.split(" ");
        method = generalHeader[0];
        url = generalHeader[1];
        http = generalHeader[2];

        // request header
        String line = readLine(in);
        while (!line.isEmpty()) {
            String[] requestHeader = line.split(": ");
            headers.put(requestHeader[0], requestHeader[1]);
            line = readLine(in);
        }

        // request body
        if (headers.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            byte[] bodyData = new byte[contentLength];
            int bytesRead = 0;
            while (bytesRead < contentLength) {
                int count = in.read(bodyData, bytesRead, contentLength - bytesRead);
                if (count == -1) {
                    throw new IOException("Unexpected end of stream while reading request body.");
                }
                bytesRead += count;
            }
            body = bodyData;
        } else {
            body = new byte[0];
        }
    }

    /**
     * InputStream에서 1줄을 읽어 문자열로 반환합니다.
     *
     * <p> stream의 끝이나 개행문자를 만날때까지 문자를 읽어서 문자열로 반환합니다.
     *
     * @param in InputStream
     * @return 1줄 문자열
     * @throws IOException I/O 에러 발생
     */
    private String readLine(InputStream in) throws IOException {
        StringBuilder line = new StringBuilder();
        int c;
        while ((c = in.read()) != -1 && c != '\r' && c != '\n') {
            line.append((char) c);
        }
        if (c == '\r') {
            in.read();
        }
        return line.toString();
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getHttp() {
        return http;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public byte[] getBody() {
        return body;
    }
}
