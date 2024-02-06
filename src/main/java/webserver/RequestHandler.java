package webserver;

import java.io.*;
import java.net.Socket;

import controller.BoardController;
import controller.HomeController;
import controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ResourceUtils;
import util.http.*;

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
            HttpRequest httpRequest = HttpRequest.parse(in);
            route(httpRequest, out);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void route(HttpRequest httpRequest, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        String path = httpRequest.getPath();

        ResponseEntity<?> responseEntity = null;
        if (path.startsWith("/user")) {
            responseEntity = new UserController(httpRequest).run();
        }
        if (path.startsWith("/board")) {
            responseEntity = new BoardController(httpRequest).run();
        }

        if (responseEntity == null)
            responseEntity = new HomeController(httpRequest).run();

        HttpResponse.send(dos, responseEntity);
    }
}
