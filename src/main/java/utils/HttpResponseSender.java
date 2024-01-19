package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponseSender {
    private static final String INDEX_HTML_PATH = "/index.html";
    private static final Logger logger = LoggerFactory.getLogger(HttpResponseSender.class);

    public void sendHttpResponse(DataOutputStream dos, int lengthOfBodyContent, byte[] body) {
        create200OKResponseHeader(dos, lengthOfBodyContent);
        createResponseBody(dos, body);
        flushResponse(dos);
    }

    public void redirectToHomePage(DataOutputStream dos) {
        create302FoundResponseHeader(dos, INDEX_HTML_PATH);
        flushResponse(dos);
    }

    private void create200OKResponseHeader(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error("Error logging response making header: {}", e.getMessage());
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
            logger.error("Error logging response: {}", e.getMessage());
        }
    }

}
