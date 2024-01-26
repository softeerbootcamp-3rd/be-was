package util;

import model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

public class ResponseBuilder {
    private static final Logger logger = LoggerFactory.getLogger(ResponseBuilder.class);

    public static void sendResponse(DataOutputStream dos, byte[] body, HttpStatus httpStatus, String extension) throws IOException {
        try {
            sendHeader(dos, httpStatus, body.length, extension);
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void sendHeader(DataOutputStream dos, HttpStatus httpStatus, int bodyLength, String extension) throws IOException {
        dos.writeBytes("HTTP/1.1  " + httpStatus.getCode() + " " + httpStatus.getMessage() + "\r\n");
        if (httpStatus == HttpStatus.REDIRECT) {
            dos.writeBytes("Location: http://localhost:8080/index.html\r\n");
        }
        dos.writeBytes("Content-Type: text/" + extension + ";charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + bodyLength + "\r\n");
        Model.getAttribute("sessionId").ifPresent(
                value -> {
                    try {
                        dos.writeBytes("Set-Cookie: sid=" + value + "; Path=/\r\n");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        dos.writeBytes("\r\n");
    }
}
