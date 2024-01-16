package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.HttpHeaderUtil;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

            String[] httpMethodAndUrl = HttpHeaderUtil.getHttpMethodAndUrl(bufferedReader);
            String httpMethod = httpMethodAndUrl[0];
            String requestUrl = httpMethodAndUrl[1];

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body;
            String contentType;

            if (httpMethod.equals("GET")) {
                String mimeType = HttpHeaderUtil.getContentType(requestUrl);
                contentType = mimeType;

                if (mimeType.equals("text/html")) {
                    body = Files.readAllBytes(
                            new File("src/main/resources/templates" + requestUrl).toPath());
                }
                else {
                    body = Files.readAllBytes(
                            new File("src/main/resources/static" + requestUrl).toPath());
                }
            } else {
                body = "Hello World".getBytes();
                contentType = "text/html";
            }

            response200Header(dos, body.length, contentType);
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
