package utils;

import request.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestUtils {

    public static HttpRequest makeHttpRequest(InputStream in) throws IOException {


        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        String requestLine = br.readLine();
        String[] values = requestLine.split(" ");
        String method = values[0];
        String url = values[1];
        String version = values[2];
        HttpRequest request = new HttpRequest(method, url ,version);

        String headerLine;
        while ((headerLine = br.readLine()) != null && !headerLine.isEmpty()) {
            String[] pair = headerLine.split(":");
            if (pair.length == 2) {
                String fieldName = pair[0].trim();
                String value = pair[1].trim();
                request.getHeaders().put(fieldName, value);
            }
        }

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

        request.setBody(bodyBuilder.toString());
        return request;
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

}
