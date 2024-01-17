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
                handler.apply(request).send(out, logger);
            } else if (request.getMethod().equals("GET")) {
                ResourceLoader.getFileResponse(request).send(out, logger);
            } else {
                HttpResponse.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .addHeader("Content-Type", "text/plain")
                        .body(HttpStatus.NOT_FOUND.getFullMessage())
                        .build()
                        .send(out, logger);
            }
        } catch (IOException e) {
            logger.error("Error processing request: {}", e.getMessage());
        }
    }
}
