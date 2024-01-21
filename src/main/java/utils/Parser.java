package utils;

import java.util.HashMap;
import java.util.Map;

public class Parser {

    public static String[] parsing(String request, String delimiter, int limit) {
        if (limit < 0) {
            return request.split(delimiter);
        }
        return request.split(delimiter, limit);
    }

    public static Map<String, String> splitRequestList(String request) {
        // " "로 request line 파싱
        String[] tokens = request.split(" ", 3);

        // 맵에 method, uri, version 저장
        Map<String, String> result = new HashMap<>();
        result.put("method", tokens[0]);
        result.put("uri", tokens[1]);
        result.put("version", tokens[2]);

        return result;
    }

    public static String extractQuery(String request) {
        String[] tokens = request.split("\\?", 2);
        return tokens[1];
    }
}
