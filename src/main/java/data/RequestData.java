package data;

import controller.HttpMethod;

import java.util.Map;

public class RequestData {
    private final HttpMethod method;
    private final String requestContent;
    private final String httpVersion;
    private final Map<String, String> headers;
    private final String body;

    private boolean loggedIn;

    public RequestData(HttpMethod method, String requestContent, String httpVersion, Map<String, String> headers, boolean loggedIn) {
        this.method = method;
        this.requestContent = requestContent;
        this.httpVersion = httpVersion;
        this.headers = headers;
        this.body = null;
        this.loggedIn = loggedIn;
    }
    public RequestData(HttpMethod method, String requestContent, String httpVersion, Map<String, String> headers, String body, boolean loggedIn) {
        this.method = method;
        this.requestContent = requestContent;
        this.httpVersion = httpVersion;
        this.headers = headers;
        this.body = body;
        this.loggedIn = loggedIn;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getRequestContent() {
        return requestContent;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getHeaderValue(String headerName) {
        return headers.get(headerName);
    }

    public String getBody() { return body; }

    public boolean isLoggedIn() { return loggedIn; }
    public void setLoggedOut() { loggedIn = false; }

    @Override
    public String toString() {
        return "\n===\n" +
                "Method: \t" + method + "\n" +
                "Request Content: \t" + requestContent + "\n" +
                "HTTP Version: \t" + httpVersion + "\n===\n" +
                "Cookie: \t" + headers.getOrDefault("Cookie", "Empty") + "\n" +
                "Accept: \t" + headers.getOrDefault("Accept", "Empty") + "\n";
    }
}
