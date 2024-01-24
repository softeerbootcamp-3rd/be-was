package webserver;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import controller.Controller;
import controller.FrontController;
import controller.UserController;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpResponseHandler;
import http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    private FrontController frontController;
    private HttpResponseHandler responseHandler;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.frontController = new FrontController();
        this.responseHandler = new HttpResponseHandler();
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);

            HttpRequest request = new HttpRequest(in); //http request 정보 가져오기
            request.print(); //http request 정보 출력

            HttpResponse response = new HttpResponse(dos);

            frontController.process(request, response);

            responseHandler.setHttpResponse(response);
            response.sendResponse();

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}

