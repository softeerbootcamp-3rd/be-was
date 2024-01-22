package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.HashMap;
import java.util.Map;


public class HttpRequestParser {
    public HttpRequestParser() {
    }

    public Map<String, String> parseHttpRequest(InputStream in) throws IOException {
        Map<String, String> messageElement = new HashMap<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        //첫 번째 라인에서 메서드, 경로 추출
        String firstLine = reader.readLine();
        String[] firstLineParts = firstLine.split(" ");
        messageElement.put("Method", firstLineParts[0]);
        messageElement.put("Path", firstLineParts[1]);
        messageElement.put("HTTP version", firstLineParts[2]);
        //다른 헤더 추출
        String line;
        while (!(line = reader.readLine()).isEmpty()) {
            String[] headerParts = line.split(": ");
            if (headerParts.length == 2) {
                messageElement.put(headerParts[0], headerParts[1]);
            }
        }

        // 바디 추출
        StringBuilder bodyBuilder = new StringBuilder();
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            bodyBuilder.append(line).append("\n");
        }
        //마지막 개행 문자 제거
        if (bodyBuilder.length() > 0) {
            bodyBuilder.setLength(bodyBuilder.length() - 1);
        }
        messageElement.put("Body", bodyBuilder.toString());
        return messageElement;
    }

}
