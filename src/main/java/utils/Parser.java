package utils;

import java.util.HashMap;
import java.util.Map;

public class Parser {

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

    public static Map<String, String> extractParams(String request) {
        // 요청에서 param 단위로 파싱
        String[] tokens = request.split("&");

        // 맵에 param값 저장
        Map<String, String> result = new HashMap<>();
        for (String param : tokens) {
            // "=" 구분자로 key, value 추출
            String[] p = param.split("=", 2);
            // 맵에 저장
            result.put(p[0], p[1]);
        }

        return result;
    }

    public static String extractQuery(String uri) {
        // uri에서 쿼리 부분 파싱
        return uri.split("\\?", 2)[1];
    }

    public static String extractPath(String uri) {
        if (uri.contains("?")) {
            return uri.split("\\?", 2)[0];
        }
        return uri;
    }

    public static String extractSid(String cookie) {
        if (!cookie.contains("sid")) {
            return null;
        }

        String[] tokens = cookie.split("; ");
        String sid = "";
        for (String token : tokens) {
            // "=" 구분자로 key, value 추출
            String[] t = token.split("=", 2);
            if (t[0].equals("sid")) {
                sid = t[1];
                break;
            }
        }

        return sid;
    }
}
