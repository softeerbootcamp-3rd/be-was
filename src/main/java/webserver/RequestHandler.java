package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static util.HttpRequestHeader.getRequestHeader;


public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final int PATH_POS = 1;
    private static final int EXTENSION_POS = 1;
    private static final String PATH_DELIMITER = " ";
    private static final String EXTENSION_DELIMITER = "/";
    private Socket connection;
    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            String requestHeader = getRequestHeader(in);
            logger.debug(requestHeader);

            String path = requestHeader.split(PATH_DELIMITER)[PATH_POS];
            String extension = path.split(EXTENSION_DELIMITER)[EXTENSION_POS];

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = Files.readAllBytes(new File(ResponseEnum.getPathName(extension) + path).toPath());
            response200Header(dos, body.length, ResponseEnum.getContentType(extension));
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
