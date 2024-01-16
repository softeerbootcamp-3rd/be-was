package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import model.MimeType;
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
            Request request = getRequest(in);
            byte[] body = handleRequest(request);
            extracted(out, body, request);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid request: {}", e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static Request getRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

        String line = br.readLine();
        Request request = new Request(line);
        logger.debug("request : {}", request.toString());

        while (!line.isEmpty()) {
            line = br.readLine();
            request.addHeader(line);
        }
        return request;
    }

    private byte[] handleRequest(Request request) throws IOException {
        String filePath = request.getFilePath();
        byte[] body;
        // url 에서 예외 처리 되는 부분 생각
        File file = new File(filePath);
        if (!file.exists()) {
            return "404 File Not Found".getBytes();
        }

        return Files.readAllBytes(file.toPath());
    }

    private void extracted(OutputStream out, byte[] body, Request request) {
        DataOutputStream dos = new DataOutputStream(out);
        response200Header(dos, body.length, request);
        responseBody(dos, body);
    }


    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, Request request) {
        try {
            String contentType = getContentType(request.getFilePath());

            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private String getContentType(String filePath) {
        String extension = filePath.substring(filePath.lastIndexOf(".") + 1);
        return MimeType.getContentType(extension);
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