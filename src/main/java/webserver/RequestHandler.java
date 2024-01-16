package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

import model.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            Request request = new Request(in, connection.getPort(), logger);

            DataOutputStream dos = new DataOutputStream(out);
            // Content-Type, filePath 설정
            byte[] body = Files.readAllBytes(new File(request.getFilePath()).toPath());
            response200Header(dos, body.length, request.getContentType());
            responseBody(dos, body);
        } catch (Exception e) {
            logger.error("error in run", e);
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent,
        String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
