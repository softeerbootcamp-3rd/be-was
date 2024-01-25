package http.request;

import java.util.Map;

public class Headers {
    private Map<String, String> headers;

    public Headers(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
