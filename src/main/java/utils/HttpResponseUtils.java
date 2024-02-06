package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import response.HttpResponse;
import response.HttpResponseStatus;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpResponseUtils {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static Map<HttpResponseStatus, String> responseStatusLineMap = new HashMap<>();

    static {
        responseStatusLineMap.put(HttpResponseStatus.OK, "HTTP/1.1 200 OK");
        responseStatusLineMap.put(HttpResponseStatus.FOUND, "HTTP/1.1 302 FOUND");
        responseStatusLineMap.put(HttpResponseStatus.NOT_FOUND, "HTTP/1.1 404 NOT FOUND");
    }

    public static String getResponseStatusLine(HttpResponseStatus status) {
        return responseStatusLineMap.get(status);
    }

    public static HttpResponse makeResponse(Object result) {
        HttpResponse response = new HttpResponse();
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("location", result.toString());
        response.setResponse(HttpResponseStatus.FOUND, null, responseHeaders);
        return response;
    }

    public static void renderResponse(DataOutputStream dos, HttpResponse response) {
        try {
            dos.writeBytes(response.getStatusLine() + " \r\n");
            Map<String, String> headers = response.getHeaders();
            for (String key : headers.keySet()) {
                dos.writeBytes(key+": ");
                dos.writeBytes(headers.get(key)+"\r\n");
            }
            dos.writeBytes("\r\n");

            dos.write(response.getBody(), 0, response.getBody().length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
