package response;

import utils.HttpResponseUtils;

import java.util.Map;

public class HttpResponseBuilder {
    private HttpResponse httpResponse;

    public HttpResponseBuilder() {
        this.httpResponse = new HttpResponse();
    }

    public HttpResponseBuilder status(HttpResponseStatus status) {
        this.httpResponse.setStatusLine(HttpResponseUtils.getResponseStatusLine(status));
        return this;
    }

    public HttpResponseBuilder body(byte[] body) {
        this.httpResponse.setBody(body);
        return this;
    }

    public HttpResponseBuilder headers(Map<String, String> headers) {
        for (String key : headers.keySet()) {
            this.httpResponse.getHeaders().put(key, headers.get(key));
        }
        return this;
    }

    public HttpResponse build() {
        return this.httpResponse;
    }
}
