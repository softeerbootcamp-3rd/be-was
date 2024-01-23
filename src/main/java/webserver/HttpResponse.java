package webserver;

import dto.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponse {

    // 로그 찍을 때 어떤 클래스인지 표시하는 용도
    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);

    public static void response(DataOutputStream dos, Response response) throws IOException {
        HttpStatus httpStatus = response.getHttpStatus();
        StringBuilder sb = new StringBuilder();

        String line = "HTTP/1.1 " + response.getHttpStatus().getStatusCode() + " " + response.getHttpStatus().getStatusMessage();
        dos.writeBytes(line + "\r\n");
        sb.append(line);

        if (httpStatus == HttpStatus.OK) {
            sb.append(response200Header(dos, response));
            dos.writeBytes("\r\n");
            responseBody(dos, response);
        } else if (httpStatus == HttpStatus.FOUND) {
            sb.append(redirect(dos, response));
        }

        dos.writeBytes("\r\n");
        dos.flush();
        dos.close();

        logger.debug("Response [" + sb + "]");
    }

    private static String response200Header(DataOutputStream dos, Response response) {
        try {
            String contentType = "Content-Type: " + response.getContentType();
            long bodyLength = response.getBody() != null ? response.getBody().length : 0;
            String contentLength = "Content-Length: " + bodyLength;

            dos.writeBytes(contentType + "\r\n");
            dos.writeBytes(contentLength + "\r\n");

            if (response.getCookie() != null) {
                String cookie = "Set-Cookie: sid=" + response.getCookie().getSid()
                        + "; Max-Age=" + response.getCookie().getMaxAge() + "; Path=/";
                dos.writeBytes(cookie + "\r\n");

                return ", " + contentType + ", " + contentLength + ", " + cookie;
            }

            return ", " + contentType + ", " + contentLength;
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    private static void responseBody(DataOutputStream dos, Response response) {
        try {
            dos.write(response.getBody(), 0, response.getBody().length);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

    }

    private static String redirect(DataOutputStream dos, Response response) {
        try {
            String redirectUrl = new String(response.getBody());
            String redirectResponse = "Location: " + redirectUrl;

            dos.writeBytes(redirectResponse + "\r\n");

            if (response.getCookie() != null) {
                String cookie = "Set-Cookie: sid=" + response.getCookie().getSid()
                        + "; Max-Age=" + response.getCookie().getMaxAge() + "; Path=/";
                dos.writeBytes(cookie + "\r\n");

                return ", " + redirectResponse + ", " + cookie;
            }

            return ", " + redirectResponse;
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return null;
    }


}
