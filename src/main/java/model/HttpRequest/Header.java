package model.HttpRequest;

import java.util.Map;

public class Header {
    private Map<String, String> headers;

    public Header(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
