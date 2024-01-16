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
            DataOutputStream dos = new DataOutputStream(out);

            response200Header(dos, body.length, request);
            responseBody(dos, body);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid request: {}", e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static Request getRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

        // 헤더의 첫 번째 line
        String line = br.readLine();
        Request request = new Request(line);
        logger.debug("start line : {}", line);

        // 전체 header 출력
        while(!line.isEmpty()){        // blank Line 을 만나기 전까지 반복
            line = br.readLine();
            request.addHeader(line);
        }
        return request;
    }

    private byte[] handleRequest(Request request) throws IOException {
        String filePath = request.getFilePath();
        byte[] body;

        if (filePath.isEmpty()) {
            return "Hello World".getBytes();
        }

        File file = new File(filePath);
        if (!file.exists()) {
            return "File not found".getBytes();
        }

        return Files.readAllBytes(file.toPath());
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