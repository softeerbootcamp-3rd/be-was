package webserver;

import java.io.*;
import java.net.Socket;
import java.util.function.Function;

import model.HttpRequest;
import model.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.URLMapper;

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
            HttpRequest httpRequest = HttpRequest.from(in);
            logger.debug(httpRequest.toString());

            Function<HttpRequest, HttpResponse> controller = URLMapper.getController(httpRequest);

            HttpResponse httpResponse = controller.apply(httpRequest);
            ResponseSender.getInstance().send(httpResponse, out);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
