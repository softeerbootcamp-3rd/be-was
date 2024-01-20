package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Request {

    private final String method;
    private final String url;
    private final String http;
    private final Map<String, String> headers = new HashMap<>();

    /**
     * 요청 메시지를 받아 시작 줄과 각 헤더를 저장합니다.
     *
     * @param in 요청 메시지
     * @throws IOException I/O 에러 발생
     */
    public Request(InputStream in) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(inputStreamReader);
        String header = br.readLine();

        // start line
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
}
