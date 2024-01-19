package webserver;

import controller.Controller;
import controller.HomeController;
import controller.UserController;
import model.Response;
import java.io.*;
import java.net.Socket;

import model.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.HeaderBuilder;

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
            Request request = new Request(in, connection.getPort());

            logger.debug("port {} || method : {}, http : {}, url : {}", request.getPort(),
                    request.getMethod(), request.getHttp(), request.getUrl());

            String url = request.getUrl();

            Controller controller = getController(url);
            Response response = controller.route(url);

            DataOutputStream dos = new DataOutputStream(out);
            dos.writeBytes(HeaderBuilder.build(response));
            dos.write(response.getBody(), 0, response.getBody().length);
            dos.flush();
        } catch (Exception e) {
            logger.error("error in run", e);
        }
    }

    private Controller getController(String url) {
        if (url.startsWith("/user")) {
            return userController;
        }
        if (url.startsWith("/qna")) {
            // todo : qna 컨트롤러, 서비스 개발
        }
        return homeController;
    }
}
