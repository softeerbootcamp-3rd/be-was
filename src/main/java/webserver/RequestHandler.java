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

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.

            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
//            System.out.println("Request : " + br.readLine());
            // Request : GET /index.html HTTP/1.1
            // Request : GET /favicon.ico HTTP/1.1
            String line = br.readLine();
//            System.out.println("Request : " + line);
            logger.debug("Request line : {}", line);

            String[] tokens = line.split(" ");

//            while(line != null) {
            while(!line.equals("")) { // 헤더의 끝은 빈 공백 문자열이 들어있다.
                line = br.readLine();
//                System.out.println("Request : " + line);
                logger.debug("Header : {}", line);
            }

            DataOutputStream dos = new DataOutputStream(out);
//            byte[] body = "Hello World".getBytes();
//            byte[] body = "Hello, World!!".getBytes();
            String url = tokens[1];

            String extension = getFileExtension(url);

            Path htmlFilePath;
            if (extension.equals("html")) {
                htmlFilePath = Paths.get("/Users/admin/Softeer/be-was/src/main/resources/templates" + url);
                extension = "text/" + extension;
            } else if (extension.equals("css")) {
                htmlFilePath = Paths.get("/Users/admin/Softeer/be-was/src/main/resources/static" + url);
                extension = "text/" + extension;
            } else if (extension.equals("js")) {
                htmlFilePath = Paths.get("/Users/admin/Softeer/be-was/src/main/resources/static" + url);
                extension = "text/javascript";
            } else if (extension.equals("ttf") || extension.equals("woff")) {
                htmlFilePath = Paths.get("/Users/admin/Softeer/be-was/src/main/resources/static" + url);
                extension = "font/" + extension;
            } else if (extension.equals("ico")) {
                htmlFilePath = Paths.get("/Users/admin/Softeer/be-was/src/main/resources/static" + url);
                extension = "image/x-icon";
            } else {
                htmlFilePath = Paths.get("/Users/admin/Softeer/be-was/src/main/resources/templates" + url);
            }

            byte[] body = Files.readAllBytes(htmlFilePath);

            response200Header(dos, body.length, extension);
            responseBody(dos, body);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1);
        } else {
            return ""; // 확장자가 없는 경우
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String extension) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + extension + ";charset=utf-8\r\n");
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
