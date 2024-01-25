package util;

import http.request.Body;
import http.request.Headers;
import http.request.Request;
import http.request.StartLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestGenerator {

    public Request createHttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        StartLine startLine = createStartLine(br);
        Headers header = createHeader(br);
        Body body = createBody(br, header.getHeaders());

        return new Request(startLine, header, body);
    }

    private StartLine createStartLine(BufferedReader br) throws IOException {
        String startLineStr = br.readLine();
        List<String> startLine = validateStartLine(startLineStr);
        return new StartLine(startLine.get(0), startLine.get(1), startLine.get(2));
    }

    private List<String> validateStartLine(String startLine) {
        if (startLine == null || startLine.trim().isEmpty()) {
            throw new IllegalArgumentException("Request line is null or empty");
        }
        List<String> split = List.of(startLine.split(" "));
        if (split.size() != 3) {
            throw new IllegalArgumentException("Invalid Start line");
        }
        return split;
    }

    private Headers createHeader(BufferedReader br) throws IOException {
        String headerLine;
        Map<String, String> headers = new HashMap<>();
        while ((headerLine = br.readLine()) != null && !headerLine.isEmpty()) {
            List<String> parts = List.of(headerLine.split(": "));
            if (parts.size() != 2) {
                throw new IllegalArgumentException("Invalid Header line");
            }
            headers.put(parts.get(0), parts.get(1));
        }
        return new Headers(headers);
    }

    private Body createBody(BufferedReader br, Map<String, String> headers) throws IOException {
        if (!headers.containsKey("Content-Length")) {
            return new Body("");
        }

        int contentLength = Integer.parseInt(headers.get("Content-Length"));
        StringBuilder bodyBuilder = new StringBuilder(contentLength);
        int charsToRead = contentLength;
        char[] buffer = new char[4098];
        while (charsToRead > 0) {
            int read = br.read(buffer, 0, Math.min(buffer.length, charsToRead));
            if (read == -1) {
                throw new IOException("Unexpected end of stream");
            }
            bodyBuilder.append(buffer, 0, read);
            charsToRead -= read;
        }

        return new Body(bodyBuilder.toString());
    }


}
