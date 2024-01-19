package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class HttpResponse {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private DataOutputStream dos;

    public HttpResponse(DataOutputStream dos) {
        this.dos = dos;
    }

    public void response200Header(int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n"); //이줄 필수
            dos.writeBytes("Content-Type: "+contentType+";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void response301RedirectHeader(String redirectUrl) {
        try {
            dos.writeBytes("HTTP/1.1 301 Moved Permanently\r\n"); //이줄 필수
            dos.writeBytes("Location: " + redirectUrl + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

//스레드 로컬
    public void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void respond404() {
        try {
            String response = "HTTP/1.1 404 Not Found\r\n\r\n";
            dos.writeBytes(response);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
