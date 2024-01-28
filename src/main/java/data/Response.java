package data;

import controller.HttpStatusCode;
import controller.RequestDataController;
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
        return "Response : " + status + "\n" +
                "Cookie : " + cookie + "\n";
    }
}
