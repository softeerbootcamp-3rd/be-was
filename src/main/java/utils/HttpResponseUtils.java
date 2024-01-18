package utils;

import response.HttpResponseStatus;

import java.util.HashMap;
import java.util.Map;

public class HttpResponseUtils {
    private static Map<HttpResponseStatus, String> responseStatusLineMap = new HashMap<>();

    static {
        responseStatusLineMap.put(HttpResponseStatus.OK, "HTTP/1.1 200 OK");
        responseStatusLineMap.put(HttpResponseStatus.FOUND, "HTTP/1.1 302 FOUND");
        responseStatusLineMap.put(HttpResponseStatus.NOT_FOUND, "HTTP/1.1 404 NOT FOUND");
    }

    public static String getResponseStatusLine(HttpResponseStatus status) {
        return responseStatusLineMap.get(status);
    }
}
