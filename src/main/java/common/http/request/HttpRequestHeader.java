package common.http.request;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestHeader {

    private Map<String, String> headers;

    public HttpRequestHeader(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getSpecificHeader(String key) {
        return headers.get(key);
    }

    public String getPath() {
        return headers.get("path");
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        headers.forEach((key, value) -> sb.append(key).append(" : ").append(value).append("\n"));
        return sb.toString();
    }
}
