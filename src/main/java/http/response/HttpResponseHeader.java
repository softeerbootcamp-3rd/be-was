package http.response;

import java.util.HashMap;
import java.util.Map;

public class HttpResponseHeader {

    private Map<String, String> headers;

    public HttpResponseHeader(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        headers.forEach((key, value) -> sb.append(key).append(": ").append(value).append("\r\n"));
        sb.append("\r\n");
        return sb.toString();
    }
}
