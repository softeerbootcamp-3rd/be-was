package webserver;

import java.io.*;
import java.net.Socket;

import model.RequestHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static util.FileUtil.*;


public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            RequestHeader requestHeader = new RequestHeader(br, connection.getPort());

            // request line 출력
            logger.debug("port : {}, request method : {}, filePath : {}, http version : {}\n",
                    requestHeader.getPort(), requestHeader.getMethod(), requestHeader.getPath(), requestHeader.getHttpVersion());

            byte[] body = getBody(requestHeader.getPath()); // 해당하는 경로의 파일을 읽고 byte[]로 반환
            String contentType = getContentType(requestHeader.getPath()); // 파일의 확장자에 따라 Content-Type을 결정

            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, body.length, contentType);
            responseBody(dos, body);
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


    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
