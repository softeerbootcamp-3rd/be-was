package webserver;

import java.io.*;
import java.net.Socket;
import java.util.*;

import HTTPModel.HttpStatus;
import HTTPModel.Request;
import HTTPModel.Response;
import HandlerMapping.HandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static webserver.HTTPHandler.RequestMake.MakeRequest;
import static webserver.HTTPHandler.ResponseMake.*;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final int BUFFER_SIZE = 4096;
    private Socket connection;

    private static final List<String> contentType;
    static{
        contentType = new ArrayList<>();
    }

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);

            Request Request = MakeRequest(in);

            HandlerMapping handlerMapping = new HandlerMapping(Request);

            Response response = handlerMapping.Controller();

            sendResponse(Request,response,dos);


        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
