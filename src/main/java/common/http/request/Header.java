package common.http.request;

import java.util.Map;

public class Header {

    private Map<String, String> headers;

    public Header(Map<String, String> headers) {
        this.headers = headers;
    }


    public String getSpecificHeader(String key) {
        return headers.get(key);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        headers.forEach((key, value) -> sb.append(key).append(" : ").append(value).append("\n"));
        return sb.toString();
    }
}
