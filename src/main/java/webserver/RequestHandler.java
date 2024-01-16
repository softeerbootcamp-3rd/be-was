package webserver;

import java.io.*;
import java.net.Socket;
import java.util.function.BiConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequest;
import util.ResponseBuilder;
import util.URLMapper;

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

            // TODO: request header class 생성하기

            logger.debug("Connection IP : {}, Port : {}, request: {}",
                    connection.getInetAddress(), connection.getPort(), request.getURI());

            BiConsumer<OutputStream, HttpRequest> handler = URLMapper.getMethod(request);
            if (handler != null) {
                handler.accept(out, request);
            } else if (request.getMethod().equals("GET")) {
                ResponseBuilder.responseFile(out, request);
            } else {
                ResponseBuilder.send404(out);
            }
        } catch (IOException e) {
            logger.error("Error processing request: {}", e.getMessage());
        }
    }
}
