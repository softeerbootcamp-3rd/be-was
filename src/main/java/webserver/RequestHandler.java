package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

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
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String requestLine = br.readLine();

            String[] requestParts = requestLine.split(" ");
            String method = requestParts[0];
            String path = requestParts[1];
            String protocolVersion = requestParts[2];

            Map<String, String> headers = new HashMap<>();
            while (!(requestLine = br.readLine()).isEmpty()) {
                String[] header = requestLine.split(": ");
                headers.put(header[0], header[1]);
            }

            StringBuilder requestBody = new StringBuilder();
            if (headers.containsKey("Content-Length")) {
                int contentLength = Integer.parseInt(headers.get("Content-Length"));
                for (int i = 0; i < contentLength; i++) {
                    requestBody.append((char) br.read());
                }
            }

            HttpRequest httpRequest = new HttpRequest();
            httpRequest.setHttpRequest(method, path, protocolVersion, headers, requestBody.toString());

            logger.debug("========== HTTP Request ==========");
            logger.debug("Method: " + httpRequest.getMethod());
            logger.debug("Path: " + httpRequest.getPath());
            logger.debug("Protocol Version: " + httpRequest.getProtocolVersion());
            if (httpRequest.getHeaders() != null) {
                httpRequest.getHeaders().forEach((key, value) -> logger.debug("Header: {} = {}", key, value));
            }
            if (!httpRequest.getBody().isEmpty()) {
                logger.debug("Body: " + httpRequest.getBody());
            }
            logger.debug("==================================");

            DataOutputStream dos = new DataOutputStream(out);
            File file = new File("./src/main/resources/templates" + path);
            byte[] body;

            body = "Valid page is not found".getBytes(StandardCharsets.UTF_8);
            if (file.exists() && !"/".equals(path)) {
                body = Files.readAllBytes(file.toPath());
            }

            response200Header(dos, body.length);
            responseBody(dos, body);
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
}
