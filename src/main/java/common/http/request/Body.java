package common.http.request;

import java.util.Map;

public class Body {

    private Map<String, String> body;

    public Body(Map<String, String> body) {
        this.body = body;
    }

    public Map<String, String> getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "HttpRequestBody{" +
            "body=" + body +
            '}';
    }

}
