package webserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class RequestHeader {
    private Map<String, String> properties;

    private RequestHeader(Map<String, String> properties) {
        this.properties = properties;
    }

    public static RequestHeader createFromReader(BufferedReader reader) throws IOException {
        String requestHeaderLine = "";

        Map<String, String> properties = new HashMap<>();

        while ((requestHeaderLine = reader.readLine()) != null && !requestHeaderLine.isEmpty()) {
            requestHeaderLine = requestHeaderLine.trim();

            String[] line = requestHeaderLine.split(":");
            line[1] = line[1].trim();

            if (line.length >= 3) {
                for (int idx = 2; idx < line.length; idx++)
                    line[1] += ":" + line[idx];
            }

            properties.put(line[0], line[1]);
        }
        return new RequestHeader(properties);
    }

    public String getHeaderProperty(String key) {
        return properties.getOrDefault(key, "");
    }

    @Override
    public String toString() {
        Set<String> requiredHeaders = Set.of(
                "Host",
                "Connection",
                "Referer",
                "User-Agent",
                "Accept",
                "Accept-Language",
                "Accept-Encoding"
        );

        return properties.entrySet().stream()
                .filter(entry -> requiredHeaders.contains(entry.getKey()))
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining(" | "));
    }
}
