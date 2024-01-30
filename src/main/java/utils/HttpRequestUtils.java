package utils;

import request.HttpRequest;
import session.SessionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestUtils {
    private static final SessionManager sessionManager = new SessionManager();

    public static HttpRequest makeHttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        HttpRequest request = new HttpRequest();
        parseRequestLine(br, request);
        parseHeader(br, request);
        parseBody(request, br);
        if(isLogin(request)) {
            request.setAuth(true);
        } else {
            request.setAuth(false);
        }

        // POST Form 방식 처리 구현
        if (request.getMethod().equals("POST") && request.getHeaders().get("Content-Type").equals("application/x-www-form-urlencoded") && !request.getUrl().equals("/user/logout")) {
            Map<String, String> params = parseQueryString(request.getBody());
            request.setParams(params);
        }

        return request;
    }

    private static void parseRequestLine(BufferedReader br, HttpRequest request) throws IOException {
        String requestLine = br.readLine();
        String[] values = requestLine.split(" ");
        String method = values[0];
        String url = values[1];
        String version = values[2];
        request.setMethod(method);
        request.setUrl(url);
        request.setVersion(version);

        // GET 방식 query string 처리 관련
        if (url.contains("?")) {
            String queryString = url.substring(url.indexOf("?") + 1);
            Map<String, String> params = parseQueryString(queryString);
            request.setParams(params);
            request.setUrl(url.substring(0, url.indexOf("?")));
        }
    }

    private static void parseHeader(BufferedReader br, HttpRequest request) throws IOException {
        String headerLine;
        while ((headerLine = br.readLine()) != null && !headerLine.isEmpty()) {
            String[] pair = headerLine.split(":");
            if (pair.length == 2) {
                String fieldName = pair[0].trim();
                String value = pair[1].trim();
                request.getHeaders().put(fieldName, value);
            }
        }
    }

    private static void parseBody(HttpRequest request, BufferedReader br) throws IOException {
        StringBuilder bodyBuilder = new StringBuilder();
        int contentLength = getContentLength(request);
        if (contentLength > 0) {
            char[] buffer = new char[1024];
            int bytesRead;
            int totalBytesRead = 0;
            while (totalBytesRead < contentLength && (bytesRead = br.read(buffer, 0, Math.min(buffer.length, contentLength - totalBytesRead))) != -1) {
                bodyBuilder.append(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
            }
        }
        String result = URLDecoder.decode(bodyBuilder.toString());
        request.setBody(result);
    }



    private static int getContentLength(HttpRequest request) {
        String contentLengthHeader = request.getHeaders().get("Content-Length");
        if (contentLengthHeader != null) {
            try {
                return Integer.parseInt(contentLengthHeader);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    public static Map<String, String> parseQueryString(String query) {
        Map<String, String> params = new HashMap<>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 1) {
                params.put(keyValue[0], "");
            } else {
                params.put(keyValue[0], keyValue[1]);
            }
        }
        return params;
    }

    private static boolean isLogin(HttpRequest request) {
        return sessionManager.getUserBySessionId(request) != null;
    }
}
