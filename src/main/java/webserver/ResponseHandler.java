package webserver;

import constant.ErrorCode;
import constant.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

// 사용자에게 보낼 응답을 처리
public class ResponseHandler {
    private static final Logger logger = LoggerFactory.getLogger(ResponseHandler.class);

    public static void sendBody(DataOutputStream dos, HttpStatus httpStatus, byte[] body, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 " + httpStatus.getStatus() + " \r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
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
        sendBody(dos, errorCode.getHttpStatus(), errorMessage, "text/plain");
    }

    // 정적 html 파일로 리다이렉트
    public static void sendRedirect(DataOutputStream dos, String redirectUrl) {
        try {
            dos.writeBytes("HTTP/1.1 303 See Other \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Location: " + redirectUrl + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
