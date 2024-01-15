package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ResourceLoader;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String requestLine = reader.readLine();

            StringBuilder sb = new StringBuilder(requestLine);

            String line;
            while (!(line = reader.readLine()).isEmpty())
                sb.append(line).append("\n");

            logger.debug("Connection IP : {}, Port : {}, request: {}",
                    connection.getInetAddress(), connection.getPort(), sb);

            if (requestLine != null) {
                String[] requestParts = requestLine.split(" ");
                String requestPath = requestParts[1];
                responseFile(out, requestPath);
            }
        } catch (IOException e) {
            logger.error("Error processing request: {}", e.getMessage());
        }
    }

    private void responseFile(OutputStream out, String requestPath) throws IOException {
        String basePath = "src/main/resources/templates";
        if (requestPath.startsWith("/css/") || requestPath.startsWith("/fonts/")
            || requestPath.startsWith("/images/") || requestPath.startsWith("/js/"))
            basePath = "src/main/resources/static";

        Path filePath = Paths.get(basePath + requestPath);

        if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
            byte[] content = Files.readAllBytes(filePath);
            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, ResourceLoader.getContentType(requestPath), content.length);
            responseBody(dos, content);
        } else {
            send404(out);
        }
    }

    private void response200Header(DataOutputStream dos, String contentType, int lengthOfBodyContent) {
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

    private void send404(OutputStream out) throws IOException {
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
