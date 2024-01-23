package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Request {

    private final String method;
    private final String url;
    private final String http;
    private final Map<String, String> headers = new HashMap<>();
    private String body;

    /**
     * 요청 메시지를 받아 시작 줄과 각 헤더를 저장합니다.
     *
     * <p> 만약 request body가 있을 경우 해당 내용을 문자열로 저장합니다.
     *
     * @param in 요청 메시지
     * @throws IOException I/O 에러 발생
     */
    public Request(InputStream in) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(in, "UTF-8");
        BufferedReader br = new BufferedReader(inputStreamReader);

        // start line
        String header = br.readLine();
        String[] generalHeader = header.split(" ");
        method = generalHeader[0];
        url = generalHeader[1];
        http = generalHeader[2];

        // request header
        String line = br.readLine();
        while (!line.isEmpty()) {
            String[] requestHeader = line.split(": ");
            headers.put(requestHeader[0], requestHeader[1]);
            line = br.readLine();
        }

        if (headers.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            char[] bodyData = new char[contentLength];
            br.read(bodyData);
            body = new String(bodyData);
        }
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

    public String getBody() {
        return body;
    }
}
