package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import http.HttpRequest;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = HttpRequest.from(in);

            DispatcherServlet dispatcherServlet = DispatcherServlet.getInstance();
            dispatcherServlet.doService(request, out);
        } catch (IOException e) {
            logger.error("IOException occurred while processing the request: {}", e.getMessage(), e);
        } catch (Throwable e) {
            logger.error("Unexpected error occurred: {}", e.getMessage(), e);
        } finally {
            try {
                connection.close();
            } catch (IOException e) {
                logger.error("Error occurred while closing the connection: {}", e.getMessage(), e);
            }
        }
    }
}
