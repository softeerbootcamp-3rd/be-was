package webserver;

import controller.HomeController;
import java.io.*;
import java.net.Socket;

import model.RequestHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final HomeController homeController = new HomeController();

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            RequestHeader requestHeader = new RequestHeader(in, connection.getPort());

            logger.debug("port {} || method : {}, http : {}, urn : {}", requestHeader.getPort(),
                    requestHeader.getMethod(), requestHeader.getHttp(), requestHeader.getUrn());

            String urn = requestHeader.getUrn();
            if (urn.startsWith("/user")) {
                // todo : user 컨트롤러, 서비스 개발
            } else if (urn.startsWith("/qna")) {
                // todo : qna 컨트롤러, 서비스 개발
            } else {
                homeController.route(urn, out);
            }
        } catch (Exception e) {
            logger.error("error in run", e);
        }
    }
}
