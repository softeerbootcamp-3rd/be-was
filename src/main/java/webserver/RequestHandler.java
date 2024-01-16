package webserver;

import header.RequestHeader;
import handler.GetRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parser.GetRequestParser;
import parser.RequestHeaderParser;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final URL RESOURCES_URL = this.getClass().getClassLoader().getResource("./templates");
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);

            RequestHeader requestHeader = RequestHeaderParser.parse(in);

            setResponse(dos, requestHeader);
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

    private void response404Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 404 NotFound \r\n");
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

    private void setResponse(DataOutputStream dos, RequestHeader requestHeader) throws IOException {
        try{
            File file = new File(RESOURCES_URL.getPath() + requestHeader.getPath());

            if(file.exists()) {
                byte[] body = Files.readAllBytes(file.toPath());

                response200Header(dos, body.length);
                responseBody(dos, body);

                return;
            }

            if(requestHeader.getMethod().equals("GET")){
                GetRequestHandler.map(GetRequestParser.parse(requestHeader.getPath()));

                byte[] body = "요청 완료".getBytes();

                response200Header(dos, body.length);
                responseBody(dos, body);
            }

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | IOException e){
            byte[] body = "404 Not Found".getBytes();

            response404Header(dos, body.length);
            responseBody(dos, body);

            logger.error(e.getMessage());
        }
    }
}
