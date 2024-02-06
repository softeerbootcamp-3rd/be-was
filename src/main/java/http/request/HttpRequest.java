package http.request;

import http.enums.HttpMethod;
import http.request.parser.HttpRequestParser;
import http.request.parser.RequestCookieParser;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String method;
    private String path;
    private Map<String, String> headers;
    private Map<String, String> bodyParams;
    private Map<String, String> cookies;

    public HttpRequest(String requestText) {
        this.headers = new HashMap<>();
        this.bodyParams = new HashMap<>();
        HttpRequestParser.parse(this, requestText);
    }

    // Getter
    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getBodyParams() {
        return bodyParams;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public HttpMethod getHttpMethod() {
        return HttpMethod.valueOf(this.method.toUpperCase());
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public String getSessionId() {
        Map<String, String> cookies = RequestCookieParser.parseCookies(this.headers.get("Cookie"));
        return cookies.get("sid");
    }

    // Setter
    public void setMethod(String method) {
        this.method = method;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setBodyParams(Map<String, String> bodyParams) {
        this.bodyParams = bodyParams;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }
}
