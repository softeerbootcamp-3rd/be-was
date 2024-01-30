package data;

import controller.HttpMethod;
import model.Post;

import java.util.Map;

public class RequestData {
    private final HttpMethod method;
    private final String requestContent;
    private final String httpVersion;
    private final Map<String, String> headers;
    private final String body;

    private final Post postData;

    private boolean loggedIn;

    public RequestData(HttpMethod method, String requestContent, String httpVersion, Map<String, String> headers, boolean loggedIn) {
        this.method = method;
        this.requestContent = requestContent;
        this.httpVersion = httpVersion;
        this.headers = headers;
        this.body = null;
        this.postData = null;
        this.loggedIn = loggedIn;
    }
    public RequestData(HttpMethod method, String requestContent, String httpVersion, Map<String, String> headers, String body, boolean loggedIn) {
        this.method = method;
        this.requestContent = requestContent;
        this.httpVersion = httpVersion;
        this.headers = headers;
        this.body = body;
        this.postData = null;
        this.loggedIn = loggedIn;
    }

    public RequestData(HttpMethod method, String requestContent, String httpVersion, Map<String, String> headers, Post postData, boolean loggedIn) {
        this.method = method;
        this.requestContent = requestContent;
        this.httpVersion = httpVersion;
        this.headers = headers;
        this.body = null;
        this.postData = postData;
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

    public Post getPostData() {
        return postData;
    }

    public boolean isLoggedIn() { return loggedIn; }
    public void setLoggedOut() { loggedIn = false; }

    @Override
    public String toString() {
        return "=== Request Data ===\n" +
                "Method: " + method + "\n" +
                "Request Content: " + requestContent + "\n" +
                "HTTP Version: " + httpVersion + "\n===\n" +
                "Cookie: " + headers.getOrDefault("Cookie", "Empty") + "\n" +
                "Accept: " + headers.getOrDefault("Accept", "Empty") + "\n" +
                "Body: " + (body != null ? body : "Empty") + "\n" +
                "Logged In: " + loggedIn + "\n" +
                "=====================\n";
    }
}
