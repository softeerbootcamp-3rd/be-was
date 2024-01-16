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
            ResponseBuilder.sendData(out, ResourceLoader.getContentType(request.getURI()),
                    new HttpResponse(HttpStatus.OK, content));
        } else {
            ResponseBuilder.sendData(out, "text/plain", new HttpResponse(HttpStatus.NOT_FOUND));
        }
    }

    public static void sendResponse(OutputStream out, HttpResponse response) {
        switch (response.getStatus()) {
            case OK:
                sendData(out, "application/json", response);
                break;
            case FOUND:
                sendRedirect(out, response);
                break;
            case NOT_FOUND:

                break;
        }
    }

    private static void sendData(OutputStream out, String contentType, HttpResponse response) {
        try {
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeBytes("HTTP/1.1 " + response.getStatus().getFullMessage() + " \r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + response.getData().length + "\r\n");
            dos.writeBytes("\r\n");

            dos.write(response.getData(), 0, response.getData().length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static void sendRedirect(OutputStream out, HttpResponse response) {
        try {
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeBytes("HTTP/1.1 302 Found\r\n");
            dos.writeBytes("Location: " + new String(response.getData()) + "\r\n");
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
