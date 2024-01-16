package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

import dto.HttpRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.WebUtil;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequestDto request = WebUtil.HttpRequestParse(in);
            logger.debug("HTTP Request >>\n" + request.toString() + "\n" +
                    "Connected IP: {}, Port: {}", connection.getInetAddress(), connection.getPort() + "\n");

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = null;

            try {
                body = Files.readAllBytes(new File(WebUtil.getPath(request.getUri())).toPath());
            } catch (Exception e) {
                logger.error(e.getMessage());
                body = "<h1>Hello, Suji👋</h1>".getBytes();
            }
            response200Header(dos, body.length, WebUtil.getContentType(request.getUri()));
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
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
