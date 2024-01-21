package http.header;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class RequestHeader extends Header {
    private Map<String, String> properties;

    private RequestHeader(Map<String, String> properties) {
        this.properties = properties;
    }

    public Map<String, String> getProperties() {
        return properties;
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

    @Override
    public String toString() {
        Set<String> requiredHeaders = new HashSet<>();

        requiredHeaders.add("Host");
        requiredHeaders.add("Connection");
        requiredHeaders.add("Referer");
        requiredHeaders.add("User-Agent");
        requiredHeaders.add("Accept");
        requiredHeaders.add("Accept-Language");
        requiredHeaders.add("Accept-Encoding");

        return properties.entrySet().stream().map(e -> {
                    if (requiredHeaders.contains(e.getKey()))
                        return e.getKey() + ": " + e.getValue();
                    else return "";
                })
                .filter(e -> !e.isEmpty())
                .collect(Collectors.joining(" | "));
    }
}
