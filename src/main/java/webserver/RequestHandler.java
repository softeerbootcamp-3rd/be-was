package webserver;

import java.io.*;
import java.net.Socket;

import controller.Controller;
import controller.DefaultController;
import controller.UserController;
import dto.HttpRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.WebUtil;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequestDto request = WebUtil.httpRequestParse(in);
            logger.debug("HTTP Request >>\n" + request.toString() + "\n" +
                    "Connected IP: {}, Port: {}", connection.getInetAddress(), connection.getPort() + "\n");

            // Controller mapping
            Controller controller = mappingController(request);
            DataOutputStream dos = new DataOutputStream(out);
            controller.handleRequest(request, dos);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private Controller mappingController(HttpRequestDto request) {
        String uri = request.getUri();
        Controller controller;
        if (uri.startsWith("/user")) {
            controller = new UserController();
        } else {
            controller = new DefaultController();
        }
        return controller;
    }
}
