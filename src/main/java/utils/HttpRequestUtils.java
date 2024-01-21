package utils;

import request.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HttpRequestUtils {

    public static HttpRequest makeHttpRequest(InputStream in) throws IOException {
        HttpRequest request = new HttpRequest();

        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        String requestLine = br.readLine();
        String[] values = requestLine.split(" ");
        request.setMethod(values[0]);
        request.setUrl(values[1]);
        request.setVersion(values[2]);

        String headerLine;
        while ((headerLine = br.readLine()) != null && !headerLine.isEmpty()) {
            String[] pair = headerLine.split(":");
            if (pair.length == 2) {
                String fieldName = pair[0].trim();
                String value = pair[1].trim();
                request.getHeaders().put(fieldName, value);
            }
        }
        return request;
    }
}
