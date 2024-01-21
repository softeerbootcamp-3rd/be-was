package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.HashMap;
import java.util.Map;


public class HttpRequestParser {
    public HttpRequestParser(){
    }

    public Map<String, String> parseHttpRequest(InputStream in) throws IOException {
        Map<String, String> header = new HashMap<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        //첫 번째 라인에서 메서드, 경로 추출
        String firstLine = reader.readLine();
        String[] firstLineParts = firstLine.split(" ");
        header.put("Method", firstLineParts[0]);
        header.put("Path", firstLineParts[1]);
        header.put("HTTP version", firstLineParts[2]);
        //다른 헤더 추출
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            String[] headerParts = line.split(": ");
            if (headerParts.length == 2) {
                header.put(headerParts[0], headerParts[1]);
            }
        }
        //TODO: 닫으면 왜 소켓연결이 끊기는지 공부하기
        //reader.close();
        return header;
    }

}
