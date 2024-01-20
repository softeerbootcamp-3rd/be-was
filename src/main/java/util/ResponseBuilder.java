package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

public class ResponseBuilder {
    private static final Logger logger = LoggerFactory.getLogger(ResponseBuilder.class);

    public static void sendResponse(DataOutputStream dos, byte[] body, HttpStatus httpStatus) throws IOException {
        try {
            sendHeader(dos, httpStatus, body.length);
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void sendHeader(DataOutputStream dos, HttpStatus httpStatus, int bodyLength) throws IOException {
        dos.writeBytes("HTTP/1.1  " + httpStatus.getCode() + " " + httpStatus.getMessage() + "\r\n");
        dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + bodyLength + "\r\n");
        dos.writeBytes("\r\n");
    }
}
