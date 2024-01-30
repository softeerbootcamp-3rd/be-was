package webserver;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

import auth.AuthFilter;
import controller.FrontController;
import http.HttpRequest;
import http.HttpResponse;
import http.ResponseHeaderMaker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    private final FrontController frontController;

    private final AuthFilter authFilter;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.frontController = new FrontController();
        this.authFilter = new AuthFilter();
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);

            HttpRequest request = new HttpRequest(in); //http request 정보 가져오기
            request.print(); //http request 정보 출력

            authFilter.doFilter(request);
            HttpResponse response = new HttpResponse(dos);

            frontController.process(request, response);

            response.send();

        } catch (IOException | InvocationTargetException | IllegalAccessException e) {
            logger.error(e.getMessage());
        }
    }
}

