package webserver.response;

import constant.ErrorCode;
import constant.HttpStatus;
import constant.MimeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

// 사용자에게 보낼 응답을 처리
public class ResponseHandler {
    private static final Logger logger = LoggerFactory.getLogger(ResponseHandler.class);

    public static void send(DataOutputStream dos, HttpResponse httpResponse) {
        try {
            // 헤더 전송
            dos.writeBytes("HTTP/1.1 " + httpResponse.getStatus().toString() + " \r\n");
            for (String header : httpResponse.getHeaders()) {
                dos.writeBytes(header);
            }

            dos.writeBytes("\r\n");

            // 바디가 있는 경우 바디 전송
            if (httpResponse.getBody() != null) {
                dos.write(httpResponse.getBody(), 0, httpResponse.getBody().length);
            }

            dos.flush();
            dos.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void makeError(HttpResponse httpResponse, ErrorCode errorCode) {
        httpResponse.setStatus(errorCode.httpStatus);
        httpResponse.setBody(errorCode.errorMessage.getBytes());
        httpResponse.addHeader("Content-Type", (MimeType.TXT.contentType + ";charset=utf-8"));
    }

    public static void makeRedirect(HttpResponse httpResponse, String redirectUrl) {
        httpResponse.setStatus(HttpStatus.REDIRECT);
        httpResponse.addHeader("Location", redirectUrl);
        httpResponse.addHeader("Content-Type", (MimeType.HTML.contentType + ";charset=utf-8"));
    }

    public static void makeBody(HttpResponse httpResponse, byte[] body, String contentType) {
        httpResponse.setStatus(HttpStatus.OK);
        httpResponse.setBody(body);
        httpResponse.addHeader("Content-Length", String.valueOf(body.length));
        httpResponse.addHeader("Content-Type", (contentType + ";charset=utf-8"));
    }
}
