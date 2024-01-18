package webserver;

import constant.ErrorCode;
import constant.FilePath;
import constant.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

// 사용자에게 보낼 응답을 처리
public class ResponseHandler {
    private static final Logger logger = LoggerFactory.getLogger(ResponseHandler.class);

    public static void sendBody(DataOutputStream dos, HttpStatus httpStatus, byte[] body, String fileExtension) {
        try {
            dos.writeBytes("HTTP/1.1 " + httpStatus.getStatus() + " \r\n");
            dos.writeBytes("Content-Type: text/" + fileExtension + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + body.length + "\r\n");

            dos.writeBytes("\r\n");

            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void sendError(DataOutputStream dos, ErrorCode errorCode) {
        byte[] errorMessage = errorCode.getErrorMessage().getBytes();
        sendBody(dos, errorCode.getHttpStatus(), errorMessage, "plain");
    }

    public static void sendRedirect(DataOutputStream dos, String redirectUrl) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Location: " + redirectUrl + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
