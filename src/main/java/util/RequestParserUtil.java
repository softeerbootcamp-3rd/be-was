package util;

import controller.HttpMethod;
import controller.HttpStatusCode;
import data.RequestData;
import data.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class RequestParserUtil {

    private static final Logger logger = LoggerFactory.getLogger(RequestParserUtil.class);

    private RequestParserUtil() {}

    public static RequestData parseRequest(BufferedReader br) throws IOException {
        String line = br.readLine();

        if (line == null) {
            throw new IOException("Invalid HTTP request: Request line is null");
        }

        String[] tokens = line.split(" ");

        Map<String, String> headers = parseHeaders(br);

        // 요청의 로그인 여부 판단
        boolean isLoggedIn = false;
        if (headers.get("Cookie") != null) {
            isLoggedIn = UserService.isLoggedIn(headers.get("Cookie"));
        }

        String requestBody = "";

        if(tokens[0].equals("POST")) {
            int contentLength = Integer.parseInt(headers.getOrDefault("Content-Length", "0"));
            if (contentLength > 0) {
                char[] buffer = new char[contentLength];
                br.read(buffer, 0, contentLength);
                requestBody = new String(buffer);

                logger.debug("requestBody: {}", requestBody);

                return new RequestData(HttpMethod.valueOf(tokens[0]), tokens[1], tokens[2], headers, requestBody, isLoggedIn);
            }
        }

        return new RequestData(HttpMethod.valueOf(tokens[0]), tokens[1], tokens[2], headers, isLoggedIn);
    }

    private static Map<String, String> parseHeaders(BufferedReader br) throws IOException {
        Map<String, String> headers = new HashMap<>();

        String line = br.readLine();

        while (!line.equals("")) {
            // Trailing white space 고려하여 ":"으로 split() 후 trim() 호출
            int colonIndex = line.indexOf(':');
            if (colonIndex != -1) {
                String headerName = line.substring(0, colonIndex).trim();
                String headerValue = line.substring(colonIndex + 1).trim();
                headers.put(headerName, headerValue);
            }
            line = br.readLine();
        }

        return headers;
    }

    public static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1);
        } else {
            return ""; // 확장자가 없는 경우
        }
    }

    public static Map<String, String> parseUserRegisterQuery(String url) {
        // HTTP 요청으로부터 사용자 데이터 추출
        String[] pairs = url.split("&");

        Map<String, String> userProps = new HashMap<>();
        for (String pair : pairs) {
            String[] splitPair = pair.split("=");
            if (splitPair.length == 2) {
                String key = splitPair[0];
                String val;
                val = URLDecoder.decode(splitPair[1]);
                userProps.put(key, val);
            }
        }

        return userProps;
    }
}
