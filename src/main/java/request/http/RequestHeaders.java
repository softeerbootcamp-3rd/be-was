package request.http;

import java.util.HashMap;
import java.util.Map;

public class RequestHeaders {
    private final Map<String, String> requestHeaders = new HashMap<>();

    public void addHeader(String headerLine) {
        String[] tokens = headerLine.split(": ");
        requestHeaders.put(tokens[0], tokens[1]);
    }

    public String getValue(String key) {
        return requestHeaders.get(key);
    }

    public Map<String, String> getRequestHeaders() {
        return requestHeaders;
    }
}
