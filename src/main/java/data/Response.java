package data;

import controller.HttpStatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Response {
    private static final Logger logger = LoggerFactory.getLogger(Response.class);

    private final String httpVer = "HTTP/1.1";
    private final HttpStatusCode status;
    private final String path;
    private final CookieData cookie;

    public Response(HttpStatusCode status, String path) {
        this.status = status;
        this.path = path;
        this.cookie = null;
    }
    public Response(HttpStatusCode status, String path, CookieData cookie) {
        this.status = status;
        this.path = path;
        this.cookie = cookie;
    }
    public HttpStatusCode getStatus() { return status; }
    public String getPath() { return path; }
    public CookieData getCookie() { return cookie; }
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("=== Response Data ===\n");
        result.append("HTTP Version: ").append(httpVer).append("\n");
        result.append("Status: ").append(status).append("\n");
        result.append("Path: ").append(path).append("\n");

        if (cookie != null) {
            result.append("Cookie: ").append(cookie).append("\n");
        } else {
            result.append("Cookie: Empty\n");
        }

        result.append("=====================\n");
        return result.toString();
    }
}
