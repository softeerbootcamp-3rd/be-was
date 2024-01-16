package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import request.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static util.FileUtil.*;
import static util.SingletonUtil.getUserController;


public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            DataOutputStream dos = new DataOutputStream(out);

            HttpRequest httpRequest = new HttpRequest(br, connection.getPort());
            String response = getUserController().route(httpRequest.getPath());

            String status = response.split(" ")[0];
            String route = response.split(" ")[1];

            // request line 출력
            logger.debug("port : {}, request method : {}, filePath : {}, http version : {}\n",
                    httpRequest.getPort(), httpRequest.getMethod(), httpRequest.getPath(), httpRequest.getHttpVersion());

            if (status.equals("200")) {
                byte[] body = getBody(route); // 해당하는 경로의 파일을 읽고 byte[]로 반환
                String contentType = getContentType(route); // 파일의 확장자에 따라 Content-Type을 결정
                response200Header(dos, body.length, contentType);
                responseBody(dos, body);
            }
            else if (status.equals("302")) {
                response302Header(dos, route);
            }
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

    private void response302Header(DataOutputStream dos, String location) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + location + "\r\n");
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
