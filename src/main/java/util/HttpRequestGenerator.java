package util;

import model.HttpRequest.Body;
import model.HttpRequest.Header;
import model.HttpRequest.Request;
import model.HttpRequest.StartLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequestGenerator {

    public Request createHttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        StartLine startLine = createStartLine(br);
        Header header = createHeader(br);
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

    private Header createHeader(BufferedReader br) throws IOException {
        String headerLine;
        Map<String, String> headers = new HashMap<>();
        while ((headerLine = br.readLine()) != null && !headerLine.isEmpty()) {
            List<String> parts = List.of(headerLine.split(": "));
            if (parts.size() != 2) {
                throw new IllegalArgumentException("Invalid Header line");
            }
            headers.put(parts.get(0), parts.get(1));
        }
        return new Header(headers);
    }

    private Body createBody(BufferedReader br, Map<String, String> headers) throws IOException {
        if (headers.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            char[] bodyChars = new char[contentLength];
            int totalRead = 0;
            int read;

            while (totalRead < contentLength) {
                read = br.read(bodyChars, totalRead, contentLength - totalRead);
                if (read == -1) {
                    throw new IOException("Unexpected end of stream");
                }
                totalRead += read;
            }

            return new Body(new String(bodyChars));
        } else {
            return new Body("");
        }
    }


}
