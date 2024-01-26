package http.response;

import http.HttpStatus;
import http.request.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);
    private final HttpStatus status;
    private Map<String, String> responseHeader = new HashMap<>();
    private byte[] body;

    public HttpResponse(HttpStatus status, Map<String, String> additionalHeader) {
        this.status = status;
        responseHeader.putAll(additionalHeader);
    }

    public HttpResponse(HttpStatus status, String contentType, byte[] body) {
        this.status = status;
        responseHeader.put("Content-Type", contentType + ";charset=utf-8");
        responseHeader.put("Content-Length", String.valueOf(body.length));
        this.body = body;
    }

    public HttpResponse(HttpStatus status) {
        this.status = status;
    }

    public static HttpResponse of(HttpStatus status) {
        return new HttpResponse(status);
    }

    public static HttpResponse of(HttpStatus status, Map<String, String> additionalHeader) {
        return new HttpResponse(status, additionalHeader);
    }

    public static HttpResponse of(HttpStatus status, String contentType, byte[] body) {
        return new HttpResponse(status, contentType, body);
    }

    public void send(DataOutputStream dos, HttpRequest request) {
        Map<String, String> generalHeader = request.getGeneralHeader().getGeneralHeaders();
        try {
            dos.writeBytes("HTTP/1.1 " + status + "\r\n");
            generalHeader.forEach((key, value) -> {
                try {
                    dos.writeBytes(key + ": " + value + "\r\n");
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            });
            responseHeader.forEach((key, value) -> {
                try {
                    dos.writeBytes(key + ": " + value + "\r\n");
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            });
            dos.writeBytes("\r\n");
            if (body != null) {
                dos.write(body, 0, body.length);
            }
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public HttpStatus getStatus() {
        return status;
    }

    public byte[] getBody() {
        return body;
    }
}
