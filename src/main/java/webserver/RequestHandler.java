package webserver;

import java.io.*;
import java.net.Socket;

import controller.Controller;
import controller.DefaultController;
import controller.UserController;
import dto.HttpRequestDto;
import dto.HttpResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpResponseUtil;
import util.WebUtil;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // Parsing HTTP Request
            HttpRequestDto request = WebUtil.httpRequestParse(in);
            logger.debug("HTTP Request >>\n" + request.toString() + "\n" +
                    "Connected IP: {}, Port: {}", connection.getInetAddress(), connection.getPort() + "\n");

            // mapping & executing Controller
            Controller controller = mappingController(request);
            HttpResponseDto httpResponse = controller.handleRequest(request);

            // Send HTTP Response
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeBytes(HttpResponseUtil.responseHeaderBuilder(httpResponse));
            dos.write(httpResponse.getBody(), 0, httpResponse.getBody().length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    // mapping Controller by URL
    public Controller mappingController(HttpRequestDto request) {
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
