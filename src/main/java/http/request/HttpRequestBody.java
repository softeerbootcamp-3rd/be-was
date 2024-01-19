package http.request;

import java.util.Map;

public class HttpRequestBody {

    private Map<String, String> body;

    public HttpRequestBody(Map<String, String> body) {
        this.body = body;
    }

    public Map<String, String> getBody() {
        return body;
    }

}
