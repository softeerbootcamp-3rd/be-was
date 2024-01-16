package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResponseBuilder {
    private static final Logger logger = LoggerFactory.getLogger(ResponseBuilder.class);

    public static void responseFile(OutputStream out, HttpRequest request) throws IOException {
        String basePath = "src/main/resources/templates";
        if (request.getURI().startsWith("/css/") || request.getURI().startsWith("/fonts/")
                || request.getURI().startsWith("/images/") || request.getURI().startsWith("/js/"))
            basePath = "src/main/resources/static";

        Path filePath = Paths.get(basePath + request.getURI());

        if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
            byte[] content = Files.readAllBytes(filePath);
            ResponseBuilder.sendResponse(out, ResourceLoader.getContentType(request.getURI()), content, HttpStatus.OK);
        } else {
            ResponseBuilder.send404(out);
        }
    }

    public static void sendResponse(OutputStream out, String contentType, byte[] body, HttpStatus status) {
        try {
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeBytes("HTTP/1.1 " + status.getFullMessage() + " \r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + body.length + "\r\n");
            dos.writeBytes("\r\n");

            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void sendRedirect(OutputStream out, String redirectUrl) {
        try {
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeBytes("HTTP/1.1 302 Found\r\n");
            dos.writeBytes("Location: " + redirectUrl + "\r\n");
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void send404(OutputStream out) throws IOException {
        String errorMessage = "404 Not Found";
        byte[] errorBody = errorMessage.getBytes();
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeBytes("HTTP/1.1 404 Not Found \r\n");
        dos.writeBytes("Content-Type: text/plain;charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + errorBody.length + "\r\n");
        dos.writeBytes("\r\n");
        dos.write(errorBody, 0, errorBody.length);
        dos.flush();
    }
}
