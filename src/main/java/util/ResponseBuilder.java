package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ResponseBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ResponseBuilder.class);

    public static void buildResponse(OutputStream out, String StatusCodeUrl) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        String[] tokens = StatusCodeUrl.split(" ");
        String statusCode = tokens[0];
        String targetPath = tokens[1];

        String extension = RequestParserUtil.getFileExtension(targetPath);
        String contentType;

        if(extension.equals("html")) {
            contentType = "text/html";
        } else if(extension.equals("css")) {
            contentType = "text/css";
        } else if(extension.equals("js")) {
            contentType = "application/javascript";
        } else if(extension.equals("woff")) {
            contentType = "font/" + extension;
        } else if(extension.equals("ttf")) {
            contentType = "font/" + extension;
        } else if(extension.equals("ico")) {
            contentType = "image/x-icon";
        } else if(extension.equals("png")) {
            contentType = "image/png";
        } else if(extension.equals("jpg")) {
            contentType = "image/jpg";
        } else {
            contentType = "text/html";
        }

        byte[] body;

        if(statusCode.equals("200")) {
            body = ResourceLoader.loadResource(targetPath);
            response200Header(dos, body.length, contentType);
            responseBody(dos, body);
        } else if(statusCode.equals("302")) {
            response302Header(dos, targetPath);
        } else if (statusCode.equals("400")) {
            body = ResourceLoader.loadResource(targetPath);
            response400Header(dos);
            responseBody(dos, body);
        } else if (statusCode.equals("404")) {
            body = ResourceLoader.loadResource(targetPath);
            response404Header(dos);
            responseBody(dos, body);
        } else {
            logger.error("지원하지 않는 상태 코드입니다.");
            return;
        }
    }
    private static void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static void response302Header(DataOutputStream dos, String redirectLocation) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found\r\n");
            dos.writeBytes("Location: " + redirectLocation + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static void response400Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 400 Bad Request\r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static void response404Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 404 Not Found\r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
