package model;

import webserver.HttpStatus;
import webserver.ResponseEnum;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import static constant.HttpRequestConstant.*;

public class HttpResponse {
    private static final String VERSION = "HTTP/1.1 ";
    private final HttpRequest httpRequest;
    private final HttpStatus httpStatus;
    private final DataOutputStream dos;

    public HttpResponse(HttpRequest httpRequest, HttpStatus httpStatus, OutputStream out) {
        this.httpRequest = httpRequest;
        this.httpStatus = httpStatus;
        this.dos = new DataOutputStream(out);
    }

    public void send() {
        String path = httpRequest.getUri().getPath();
        String extension = path.split(EXTENSION_DELIMITER)[EXTENSION_POS];

        try {
            byte[] body = Files.readAllBytes(new File(ResponseEnum.getPathName(extension) + path).toPath());
            response200Header(body.length, ResponseEnum.getContentType(extension));
            responseBody(body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void redirect(String location) {
        String startLine =  VERSION + httpStatus.getCode() + " " + httpStatus.getStatus() + " \r\n";

        try {
            dos.writeBytes(startLine);
            dos.writeBytes("Location: " + location + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void response400Header(String errorMessage) {
        String startLine = VERSION + httpStatus.getCode() + " " + httpStatus.getStatus() + " \r\n";

        try {
            dos.writeBytes(startLine);
            dos.writeBytes("Content-Type: text/plain;charset=utf-8\r\n");
            dos.writeBytes("\r\n");
            dos.writeBytes(new String(errorMessage.getBytes("UTF-8"), "ISO-8859-1"));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(int lengthOfBodyContent, String contentType) {
        String startLine =  VERSION + httpStatus.getCode() + " " + httpStatus.getStatus() + " \r\n";

        try {
            dos.writeBytes(startLine);
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
