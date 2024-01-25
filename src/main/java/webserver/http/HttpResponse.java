package webserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.JsonConverter;

import java.io.*;

public class HttpResponse {

    // 로그 찍을 때 어떤 클래스인지 표시하는 용도
    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);

    public static void send(DataOutputStream dos, ResponseEntity responseEntity) throws IOException {
        HttpStatus httpStatus = responseEntity.getHttpStatus();
        StringBuilder sb = new StringBuilder();

        String line = "HTTP/1.1 " + responseEntity.getHttpStatus().getStatusCode() + " " + responseEntity.getHttpStatus().getStatusMessage();
        dos.writeBytes(line + "\r\n");
        sb.append(line);

        if (httpStatus == HttpStatus.OK) {
            sb.append(response200Header(dos, responseEntity));
            dos.writeBytes("\r\n");
            responseBody(dos, responseEntity);
        } else if (httpStatus == HttpStatus.FOUND) {
            redirect(dos, responseEntity);
        }

        dos.writeBytes("\r\n");
        dos.flush();
        dos.close();

        logger.debug("Response [" + sb + "]"); // TODO toString @override 해서 한 번에 처리하기
    }

    private static String response200Header(DataOutputStream dos, ResponseEntity response) {
        try {
            StringBuilder logBuilder = new StringBuilder(", ");
            String contentType = "Content-Type: " + response.getHeaders().getContentType();
            logBuilder.append(contentType);
            dos.writeBytes(contentType + "\r\n");

            byte[] body = null;
            if (response.getHeaders().getContentType().equals("application/json") && response.getBody() != null) {
                body = ((String) response.getBody()).getBytes();
            } else {
                body = (byte[]) response.getBody();
            }
            long bodyLength = body != null ? body.length : 0;
            String contentLength = "Content-Length: " + bodyLength;
            dos.writeBytes(contentLength + "\r\n");

            logBuilder.append(", " + contentLength);

            if (response.getHeaders().hasSetCookie()) {
                String cookie = "Set-Cookie: " + response.getHeaders().getSetCookie();
                dos.writeBytes(cookie + "\r\n");

                logBuilder.append(", " + cookie);
            }

            return logBuilder.toString();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    private static String redirect(DataOutputStream dos, ResponseEntity response) {
        try {
            StringBuilder logBuilder = new StringBuilder(", ");
            String redirectResponse = "Location: " + response.getHeaders().getLocation();
            logBuilder.append(redirectResponse);

            dos.writeBytes(redirectResponse + "\r\n");

            if (response.getHeaders().hasSetCookie()) {
                String cookie = "Set-Cookie: " + response.getHeaders().getSetCookie();
                dos.writeBytes(cookie + "\r\n");

                logBuilder.append(", " + cookie);
            }

            return logBuilder.toString();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    private static void responseBody(DataOutputStream dos, ResponseEntity response) {
        try {
            byte[] bytes = null;
            if (response.getHeaders().getContentType().equals("application/json")) {
                String body = (String) response.getBody();
                bytes = body.getBytes();
            } else {
                bytes = (byte[]) response.getBody();
            }

            dos.write(bytes, 0, bytes.length);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

    }

}
