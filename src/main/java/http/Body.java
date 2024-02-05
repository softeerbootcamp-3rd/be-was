package http;

import utils.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static utils.StringUtils.appendNewLine;

public class Body {
    private final Map<String, String> body = new HashMap<>();

    private Body(BufferedReader br, int contentLength) throws IOException {
        char[] buffer = new char[contentLength];
        br.read(buffer);
        Uri.setQuery(body, StringUtils.decode(String.valueOf(buffer)));
    }

    public static Body of(BufferedReader br, int contentLength) throws IOException {
        return new Body(br, contentLength);
    }

    public Map<String, String> getBody() {
        return body;
    }

    @Override
    public String toString() {
        return body.keySet().stream()
                .map(key -> key + ": " + appendNewLine(body.get(key)))
                .collect(Collectors.joining());
    }
}
