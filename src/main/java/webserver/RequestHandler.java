package webserver;

import java.io.*;
import java.lang.reflect.Method;
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
        InputStream in = null;
        OutputStream out = null;
        try {
            in = connection.getInputStream();
            out = connection.getOutputStream();
        } catch (IOException e) {
            logger.error("error processing request: {}", e.getMessage());
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            HttpRequest request = new HttpRequest(reader);

            logger.debug("Connection IP : {}, Port : {}, request: {}",
                    connection.getInetAddress(), connection.getPort(), request.getPath());

            Method handler = RequestMapper.getMethod(request);
            if (handler != null) {
                RequestMapper.invoke(handler, request).send(out, logger);
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
            in.close();
            out.close();
        } catch (IllegalStateException | IOException e) {
            logger.error("error processing request: {}", e.getMessage());
            HttpResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage())
                    .build()
                    .send(out, logger);
        }

    }
}
