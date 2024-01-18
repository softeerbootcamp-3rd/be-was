package webserver.http.request;

import webserver.http.request.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestParser {

    public HttpRequest parse(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String requestLine = br.readLine();

        String[] requestParts = requestLine.split(" ");
        String method = requestParts[0];
        String[] pathParts = requestParts[1].split("\\?", 2);
        String protocolVersion = requestParts[2];

        String queryString = pathParts.length > 1 ? pathParts[1] : "";
        Map<String, String> queryParams = parseQueryParams(queryString);
        Map<String, String> headers = parseHeaders(br);
        String body = parseBody(br, headers);

        return new HttpRequest(method, pathParts[0], protocolVersion, queryParams, headers, body);
    }

    private Map<String, String> parseQueryParams(String queryString){
        Map<String, String> queryParams = new HashMap<>();
        if (!queryString.isEmpty()) {
            String[] pairs = queryString.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=", 2);
                String key = keyValue[0];
                String value = keyValue.length > 1 ? keyValue[1] : "";
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

    private String parseBody(BufferedReader br, Map<String, String> headers) throws IOException {
        StringBuilder body = new StringBuilder();
        if (headers.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            for (int i = 0; i < contentLength; i++) {
                body.append((char) br.read());
            }
        }
        return body.toString();
    }
}
