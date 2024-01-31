package webserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.JsonConverter;
import util.ResourceManager;

import java.io.*;

public class HttpResponse {

    // 로그 찍을 때 어떤 클래스인지 표시하는 용도
    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);

    public static void send(DataOutputStream dos, ResponseEntity responseEntity) throws IOException {
        HttpStatus httpStatus = responseEntity.getHttpStatus();
        StringBuilder logBuilder = new StringBuilder();

        String statusLine = "HTTP/1.1 " + httpStatus.getStatusCode() + " " + httpStatus.getStatusMessage();
        dos.writeBytes(statusLine + System.lineSeparator());
        logBuilder.append(statusLine);

        if (httpStatus == HttpStatus.OK) {
            logBuilder.append(response200Header(dos, responseEntity));
            dos.writeBytes(System.lineSeparator());
            responseBody(dos, responseEntity);
        } else if (httpStatus == HttpStatus.FOUND) {
            logBuilder.append(redirect(dos, responseEntity));
        }

        dos.writeBytes(System.lineSeparator());
        dos.flush();
        dos.close();

        logger.debug("Response [{}]", logBuilder); // TODO toString @override 해서 한 번에 처리하기
    }

    private static String response200Header(DataOutputStream dos, ResponseEntity response) throws IOException {
        String contentType = "Content-Type: " + response.getHeaders().getContentType();
        dos.writeBytes(contentType + System.lineSeparator());

        StringBuilder logBuilder = new StringBuilder(", ").append(contentType);

        String contentLength = "Content-Length: " + response.getHeaders().getContentLength();
        dos.writeBytes(contentLength + System.lineSeparator());
        logBuilder.append(", ").append(contentLength);

        if (response.getHeaders().hasSetCookie()) {
            String cookie = "Set-Cookie: " + response.getHeaders().getSetCookie();
            dos.writeBytes(cookie + System.lineSeparator());
            logBuilder.append(", ").append(cookie);
        }

        return logBuilder.toString();
    }

    private static String redirect(DataOutputStream dos, ResponseEntity response) throws IOException {
        String redirectHeader = "Location: " + response.getHeaders().getLocation();
        dos.writeBytes(redirectHeader + System.lineSeparator());

        StringBuilder logBuilder = new StringBuilder(", ").append(redirectHeader);

        if (response.getHeaders().hasSetCookie()) {
            String cookie = "Set-Cookie: " + response.getHeaders().getSetCookie();
            dos.writeBytes(cookie + System.lineSeparator());

            logBuilder.append(", ").append(cookie);
        }

        return logBuilder.toString();
    }

    private static void responseBody(DataOutputStream dos, ResponseEntity response) throws IOException {
        byte[] bytes = getResponseBodyBytes(response);
        dos.write(bytes, 0, bytes.length);
    }

    private static byte[] getResponseBodyBytes(ResponseEntity response) throws IOException {
        if (response.getBody().getClass().getName().equals("java.lang.String")) {
            String body = (String) response.getBody();
            return body.getBytes();
        } else if (response.getBody().getClass().getName().equals("java.io.File")){
            return ResourceManager.readAllBytes((File)response.getBody());
        }
        return null;
    }

}
