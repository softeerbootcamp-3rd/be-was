package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.Arrays;
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
        while((line=reader.readLine()) != null && !line.isEmpty()) {
            String[] headerParts = line.split(": ");
            if (headerParts.length == 2) {
                messageElement.put(headerParts[0], headerParts[1]);
            }
        }

        // 바디 추출
        if(messageElement.get("Method").equals("POST")){
            char[] body = new char[Integer.parseInt(messageElement.get("Content-Length"))];
            reader.read(body);
            messageElement.put("Body", new String(body));
        }

        return messageElement;
    }

}
