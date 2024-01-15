package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import model.Request;
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
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            // 헤더의 첫 번째 line
            String line = br.readLine();
            logger.debug("request line : {}", line);

            Request request = getRequest(line);

            /* 전체 header 출력
            while(!line.equals("")){        // 공백을 만나기 전까지 반복
                line = br.readLine();
                logger.debug("header : {}", line);
            }
            */

            String url = request.getUrl();
            String filePath = "src/main/resources/templates/index.html";
            byte[] body = "Hello World".getBytes();

            if (url.endsWith(".html")) {
                filePath = "src/main/resources/templates" + url;
                File file = new File(filePath);
                body = Files.readAllBytes(file.toPath());
            } 
            else if (!url.isEmpty()) {
                filePath = "src/main/resources/static" + url;
                File file = new File(filePath);
                body = Files.readAllBytes(file.toPath());
            }
            DataOutputStream dos = new DataOutputStream(out);

            response200Header(dos, body.length, filePath);
            responseBody(dos, body);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid request: {}", e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private Request getRequest(String line) {
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("Request line is null or empty");
        }

        String[] tokens = line.split(" ");

        if (tokens.length < 2) {
            throw new IllegalArgumentException("Invalid request line: " + line);
        }

        String method = tokens[0];
        String url = tokens[1];
        return new Request(method, url);
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String filePath) {
        try {
            String contentType = getContentType(filePath);

            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private String getContentType(String filePath) {
        if (filePath.endsWith(".css")) {
            return "text/css";
        }
        if (filePath.endsWith(".js")) {
            return "application/javascript";
        }
        if (filePath.endsWith(".png")) {
            return "image/png";
        }
        if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg")) {
            return "image/jpeg";
        }
        return "text/html";
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