package webserver;

import header.RequestHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
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

            RequestHeader requestHeader = parseHeader(in);

            setResponse(dos, requestHeader.getPath());
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

    private void setResponse(DataOutputStream dos, String path) throws IOException {
        try{
            byte[] body = Files.readAllBytes(new File(RESOURCES_URL.getPath() + path).toPath());

            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e){
            byte[] body = "404 Not Found".getBytes();

            response404Header(dos, body.length);
            responseBody(dos, body);

            logger.error(e.getMessage());
        }
    }

    private RequestHeader parseHeader(InputStream in) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        RequestHeader requestHeader = RequestHeader.parseHeader(br);

        logger.debug(requestHeader.toString());

        return requestHeader;
    }
}
