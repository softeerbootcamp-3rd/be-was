package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class RequestHeader {
    private String method;
    private String path;
    private String protocol;
    private String host;
    private String connection;
    private String accept;
    private String referer;
    private String contentLength;

    private static final Logger logger = LoggerFactory.getLogger(RequestHeader.class);

    public RequestHeader(String method, String path, String protocol) {
        this.method = method;
        this.path = path;
        this.protocol = protocol;
        this.contentLength = "0";
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }

    public String getContentLength() {
        return contentLength;
    }

    public static void setHeader(RequestHeader requestHeader, String key, String value) {
        Class<RequestHeader> requestHeaderClazz = RequestHeader.class;
        try {
            Field declaredField = requestHeaderClazz.getDeclaredField(key);
            declaredField.setAccessible(true);
            declaredField.set(requestHeader, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.error("error = {}", e.toString());
        }
    }

    public void printHeader() {
        logger.debug("Header = {}", "\n===" +
                "\n method = " + method +
                "\n path = " + path +
                "\n protocol = " + protocol +
                "\n host = " + host +
                "\n connection = " +  connection +
                "\n accpet = " + accept +
                "\n referer = " + referer + "\n===");
    }
}
