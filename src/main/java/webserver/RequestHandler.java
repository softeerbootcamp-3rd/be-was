package webserver;

import java.io.*;
import java.net.Socket;

import http.request.Request;
import http.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.RequestGenerator;
import util.ResponseGenerator;

public class RequestHandler implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final Socket connection;
    private final RequestGenerator httpRequestGenerator = new RequestGenerator();
    private final ResponseGenerator httpResponseGenerator = new ResponseGenerator();

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            Request request = httpRequestGenerator.createHttpRequest(in);
            Response response = httpResponseGenerator.createHttpResponse(out);
            Dispatcher dispatcher = new Dispatcher(request, response);
            dispatcher.dispatch();
        } catch (IllegalArgumentException | IOException e) {
            logger.error(e.getMessage());
        }
    }


}