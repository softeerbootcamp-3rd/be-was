package data;

import controller.RequestDataController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Response {
    private static final Logger logger = LoggerFactory.getLogger(Response.class);

    private final String httpVer = "HTTP/1.1";
    private final String status;
    private final String path;
    private final String cookie;
    public Response(String status, String path) {
        this.status = status;
        this.path = path;
        this.cookie = null;
    }
    public Response(String status, String path, String cookie) {
        this.status = status;
        this.path = path;
        this.cookie = cookie;
    }
    public String getStatus() { return status; }
    public String getPath() { return path; }
    public String getCookie() { return cookie; }
    @Override
    public String toString() {
        return "Response : " + status + "\n" +
                "Cookie : " + cookie + "\n";
    }
}
