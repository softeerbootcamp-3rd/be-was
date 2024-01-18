package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    private final String RESOURCES_TEMPLATES_URL = "src/main/resources/templates";

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);

            HttpRequest request = new HttpRequest(in); //http request 정복 가져오기
            request.print(); //http request 정보 출력


            byte[] body = null;
            if (request.getUrl() != null && request.getAccept().startsWith("text/html")) {
                Path filePath = Paths.get(RESOURCES_TEMPLATES_URL + request.getUrl());
                body = Files.readAllBytes(filePath);
                response200Header(dos, body.length);
                responseBody(dos, body);
            } else {
                respond404(dos);
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
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

    private void respond404(DataOutputStream dos) {
        try {
            String response = "HTTP/1.1 404 Not Found\r\n\r\n";
            dos.writeBytes(response);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}

