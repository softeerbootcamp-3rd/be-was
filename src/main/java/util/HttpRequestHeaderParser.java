package util;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestHeaderParser {

    public static Map<String, String> parseRequestLine(String requestMessage){
        Map<String, String> requestLineMap = new HashMap<>();
        String[] splitRequestLine = splitRequestLine(requestMessage);
        putMethodField(splitRequestLine, requestLineMap);
        String uri = splitRequestLine[1];
        putUriField(uri, requestLineMap);
        return requestLineMap;
    }
    public static Map<String, String> parseHeaders(String requestMessage){
        Map<String, String> requestHeaderMap = new HashMap<>();
        String[] lines = requestMessage.split("\r\n");
        for (int i = 1; i < lines.length; i++) {
            String headerLine = lines[i];
            String[] header = headerLine.split(":", 2);
            if (header.length > 1) {
				String field = header[0].toLowerCase(); // 필드 이름은 case-insensitive 때문에 모두 소문자로 변경
				String value = header[1].strip(); // trailing whitespace 제거
                requestHeaderMap.put(field, value);
            }
        }
        return requestHeaderMap;
    }

	private static String[] splitRequestLine(String requestMessage) {
		String[] lines = requestMessage.split("\r\n");
		String requestLine = lines[0];
		return requestLine.split(" ");
	}
    private static void putMethodField(String[] splitRequestLine, Map<String, String> requestLineMap) {
        String method = splitRequestLine[0];
        requestLineMap.put("method", method);
    }
    private static void putUriField(String uri, Map<String, String> requestHeaderMap) {
        String requestUri;
        String queryString = "None";
        if (uri.contains("?")) { // 쿼리 스트링이 있는 경우
            int index = uri.indexOf("?"); // 쿼리 스트링에 사용자가 ?을 입력했을 수도 있으니까 처음 나오는 ?을 기준으로 파싱
            requestUri = uri.substring(0, index);
            queryString = uri.substring(index + 1);
        }else{
            requestUri = uri;
        }
        requestHeaderMap.put("requestUri", requestUri);
        requestHeaderMap.put("queryString", queryString);
    }
}
