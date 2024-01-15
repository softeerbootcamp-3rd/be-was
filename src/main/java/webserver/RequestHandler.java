package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            // Request Header 출력
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String requestUrl = getRequestUrl(bufferedReader);

            while (bufferedReader.ready()) {
                logger.debug(bufferedReader.readLine());
            }

            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);
            if(requestUrl.equals("/index.html")) {
                byte[] body = Files.readAllBytes(new File("src/main/resources/templates" + requestUrl).toPath());
                response200Header(dos, body.length);
                responseBody(dos, body);
            } else {
                byte[] body = "Hello World".getBytes();
                response200Header(dos, body.length);
                responseBody(dos, body);
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

    private String getRequestUrl(BufferedReader bufferedReader) {
        try {
            String[] tokens = bufferedReader.readLine().split(" ");
            for (String s : tokens) {
                logger.debug(s);
            }
            return tokens[1];
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return null;
    }
}
