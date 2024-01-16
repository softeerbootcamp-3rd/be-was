package webserver;

import controller.HomeController;
import controller.UserController;
import java.io.*;
import java.net.Socket;

import model.RequestHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final HomeController homeController = new HomeController();
    private static final UserController userController = new UserController();

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            RequestHeader requestHeader = new RequestHeader(in, connection.getPort());

            logger.debug("port {} || method : {}, http : {}, url : {}", requestHeader.getPort(),
                    requestHeader.getMethod(), requestHeader.getHttp(), requestHeader.getUrn());

            String url = requestHeader.getUrn();
            if (url.startsWith("/user")) {
                userController.route(url, out);
            } else if (url.startsWith("/qna")) {
                // todo : qna 컨트롤러, 서비스 개발
            } else {
                homeController.route(url, out);
            }
        } catch (Exception e) {
            logger.error("error in run", e);
        }
    }
}
