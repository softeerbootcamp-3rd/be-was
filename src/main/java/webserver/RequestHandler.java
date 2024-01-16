package webserver;

import java.io.*;
import java.net.Socket;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.*;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            HttpRequest request = new HttpRequest(reader);

            logger.debug("Connection IP : {}, Port : {}, request: {}",
                    connection.getInetAddress(), connection.getPort(), request.getURI());

            Function<HttpRequest, HttpResponse> handler = URLMapper.getMethod(request);
            if (handler != null) {
                HttpResponse response = handler.apply(request);
                ResponseBuilder.sendResponse(out, response);
            } else if (request.getMethod().equals("GET")) {
                ResponseBuilder.responseFile(out, request);
            } else {
                ResponseBuilder.sendResponse(out, new HttpResponse(HttpStatus.NOT_FOUND));
            }
        } catch (IOException e) {
            logger.error("Error processing request: {}", e.getMessage());
        }
    }
}
