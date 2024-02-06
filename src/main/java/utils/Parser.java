package utils;

import java.util.HashMap;
import java.util.Map;

public class Parser {

    public static Map<String, String> splitRequestList(String requestLine) {
        // " "로 request line 파싱
        String[] tokens = requestLine.split(" ", 3);

        // 맵에 method, uri, version 저장
        Map<String, String> result = new HashMap<>();
        result.put("method", tokens[0]);
        result.put("uri", tokens[1]);
        result.put("version", tokens[2]);

        return result;
    }

    public static Map<String, String> extractParams(String query) {
        // 요청에서 param 단위로 파싱
        String[] tokens = query.split("&");

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

    public static String extractCookieValue(String cookie, String key) {
        String[] tokens = cookie.split("; ");
        for (String token : tokens) {
            // "=" 구분자로 key, value 추출
            String[] t = token.split("=", 2);
            if (t[0].equals(key)) {
                return t[1];
            }
        }
        return "";
    }

    public static String extractBoundary(String contentType) {
        // 바운더리 추출 로직
        String[] parts = contentType.split(";");
        for (String part : parts) {
            part = part.trim();
            if (part.startsWith("boundary=")) {
                return part.substring("boundary=".length());
            }
        }
        return "";
    }

    public static String extractFieldName(String contentDisposition) {
        // 필드명 추출 로직
        String[] dispositionParts = contentDisposition.split("; ");
        for (String part : dispositionParts) {
            if (part.startsWith("name=")) {
                return part.substring("name=".length()).replace("\"", "");
            }
        }
        return "";
    }

    public static String extractFileName(String contentDisposition) {
        // 파일명 추출 로직
        String[] dispositionParts = contentDisposition.split("; ");
        for (String part : dispositionParts) {
            if (part.startsWith("filename=")) {
                return part.substring("filename=".length()).replace("\"", "");
            }
        }
        return "";
    }
}
