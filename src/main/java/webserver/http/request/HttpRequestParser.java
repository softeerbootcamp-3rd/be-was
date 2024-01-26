package webserver.http.request;

import webserver.http.request.enums.HttpMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestParser {
    private static final HttpRequestParser instance = new HttpRequestParser();

    private HttpRequestParser(){}

    public static HttpRequestParser getInstance() {
        return instance;
    }

    public HttpRequest parse(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String requestLine = br.readLine();

        String[] requestParts = requestLine.split(" ");
        HttpMethod method = HttpMethod.valueOf(requestParts[0].toUpperCase());
        String[] pathParts = requestParts[1].split("\\?", 2);
        String protocolVersion = requestParts[2];

        String queryString = pathParts.length > 1 ? pathParts[1] : "";
        Map<String, String> queryParams = parseQueryParams(queryString);
        Map<String, String> headers = parseHeaders(br);
        Map<String, String> body = parseBody(br, headers);

        return new HttpRequest(method, pathParts[0], protocolVersion, queryParams, headers, body);
    }

    private Map<String, String> parseQueryParams(String queryString){
        Map<String, String> queryParams = new HashMap<>();
        if (!queryString.isEmpty()) {
            String[] pairs = queryString.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=", 2);
                String key = keyValue[0];
                String value = keyValue.length > 1 ? URLDecoder.decode(keyValue[1]) : "";
                queryParams.put(key, value);
            }
        }
        return queryParams;
    }

    private Map<String, String> parseHeaders(BufferedReader br) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String headerLine;
        while (!(headerLine = br.readLine()).isEmpty()) {
            String[] headerParts = headerLine.split(": ");
            headers.put(headerParts[0], headerParts[1]);
        }
        return headers;
    }

    private Map<String, String> parseBody(BufferedReader br, Map<String, String> headers) throws IOException {
        StringBuilder body = new StringBuilder();
        if (headers.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            for (int i = 0; i < contentLength; i++) {
                body.append((char) br.read());
            }
        }
        return parseQueryParams(body.toString());
    }
}
