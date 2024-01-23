package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HttpResponseSender {
    private static final Map<String, String> MIME_TYPE = new HashMap<>();
    private static final String INDEX_HTML_PATH = "/index.html";
    private static final String LOGIN_FAILED_PATH = "/user/login_failed.html";
    private static final Logger logger = LoggerFactory.getLogger(HttpResponseSender.class);

    public HttpResponseSender() {
        this.setMimeType();
    }

    private void setMimeType() {
        MIME_TYPE.put("eot", "font/eot");
        MIME_TYPE.put("svg", "image/svg+xml");
        MIME_TYPE.put("ttf", "font/ttf");
        MIME_TYPE.put("woff", "font/woff");
        MIME_TYPE.put("woff2", "font/woff2");
        MIME_TYPE.put("css", "text/css");
        MIME_TYPE.put("png", "image/png");
        MIME_TYPE.put("js", "application/javascript");
        MIME_TYPE.put("ico", "image/avif");
        MIME_TYPE.put("html", "text/html");
    }

    public void sendHttpResponse(DataOutputStream dos, int lengthOfBodyContent, byte[] body, String mimeType) {
        create200OKResponseHeader(dos, lengthOfBodyContent, mimeType);
        createResponseBody(dos, body);
        flushResponse(dos);
    }

    public void redirectToHomePage(DataOutputStream dos) {
        create302FoundResponseHeader(dos, INDEX_HTML_PATH);
        flushResponse(dos);
    }

    public void redirectToHomePage(DataOutputStream dos, String sessionId, LocalDateTime expireDate) {
        create302FoundResponseHeader(dos, sessionId, expireDate);
        flushResponse(dos);
    }

    public void redirectToLoginFailedPage(DataOutputStream dos) {
        create302FoundResponseHeader(dos, LOGIN_FAILED_PATH);
        flushResponse(dos);
    }

    private void create200OKResponseHeader(DataOutputStream dos, int lengthOfBodyContent, String mimeType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + MIME_TYPE.get(mimeType) + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error("Error logging response making header: {}", e.getMessage());
        }
    }

    private void create302FoundResponseHeader(DataOutputStream dos, String sessionId, LocalDateTime expireDate) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + INDEX_HTML_PATH + "\r\n");
            dos.writeBytes("Set-Cookie: sid=" + sessionId + "; Expires=" + formattingDate(expireDate) + "; Path=/");
        } catch (IOException e) {
            logger.error("Error logging response: {}", e.getMessage());
        }
    }

    private void create302FoundResponseHeader(DataOutputStream dos, String redirectLocation) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + redirectLocation + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error("Error logging response: {}", e.getMessage());
        }
    }

    private void flushResponse(DataOutputStream dos) {
        try {
            dos.flush();
        } catch (IOException e) {
            logger.error("Error logging response flushing: {}", e.getMessage());
        }

    }

    private void createResponseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
        } catch (IOException e) {
            logger.error("Error logging response body: {}", e.getMessage());
        }
    }

    private String formattingDate(LocalDateTime date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zz", Locale.ENGLISH);
        return date.atZone(ZoneId.of("Asia/Seoul")).format(formatter);
    }

}
