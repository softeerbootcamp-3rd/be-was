package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileReader;
import utils.RequestLogger;
import utils.RequestParser;
import utils.ResponseSender;

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
            //HTTP Request Logging
            RequestLogger.logRequest(in);
            //파일 읽기
            byte[] body = FileReader.readFile(RequestParser.getPath());
            //HTTP Response
            DataOutputStream dos = new DataOutputStream(out);
            ResponseSender.sendResponse(dos, body.length, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
