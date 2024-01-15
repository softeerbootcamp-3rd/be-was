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
//            while(!line.equals("")) { // 헤더의 끝은 빈 공백 문자열이 들어있다.
//                line = br.readLine();
////                System.out.println("Request : " + line);
//                logger.debug("Header : {}", line);
//            }

            DataOutputStream dos = new DataOutputStream(out);
//            byte[] body = "Hello World".getBytes();
//            byte[] body = "Hello, World!!".getBytes();
            String url = tokens[1];

            Path htmlFilePath = Paths.get("/Users/admin/Softeer/be-was/src/main/resources/templates" + url);

            byte[] body = Files.readAllBytes(htmlFilePath);

            Path htmlDirectory = htmlFilePath.getParent().getParent();

            response200Header(dos, body.length);
            responseBody(dos, body);

            // Send related files
            sendRelatedFile(htmlDirectory, "/static/css/bootstrap.min.css", dos);
            sendRelatedFile(htmlDirectory, "/static/js/jquery-2.2.0.min.js", dos);
            sendRelatedFile(htmlDirectory, "/static/js/bootstrap.min.js", dos);
            sendRelatedFile(htmlDirectory, "/static/js/scripts.js", dos);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void sendRelatedFile(Path baseDirectory, String relativePath, DataOutputStream dos) throws IOException {
        Path staticFilePath = Paths.get(baseDirectory.toString(), relativePath);
        logger.debug("static path: "+staticFilePath);
        if (Files.exists(staticFilePath)) {
            byte[] fileContent = Files.readAllBytes(staticFilePath);
            response200Header(dos, fileContent.length);
            responseBody(dos, fileContent);
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
