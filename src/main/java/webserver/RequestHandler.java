package webserver;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

import dispatcher.RequestDispatcher;
import http.HttpRequest;
import http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import http.status.HttpStatus;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            logger.debug("IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);

            RequestDispatcher dispatcher = RequestDispatcher.getInstance();

            try {
                HttpRequest httpRequest = HttpRequest.createFromReader(bufferedReader);
                logger.debug(httpRequest.toString());
                HttpResponse httpResponse = HttpResponse.of();
                dispatcher.dispatchHandler(httpRequest, httpResponse);

                writeResponse(dos, httpResponse);
            } catch (Exception e) {
                writeResponse(dos, HttpResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "".getBytes(), new HashMap<>()));
                logger.error("Server request: " + e.getMessage());
            }
        } catch (IOException e) {
            logger.error("Connection: " + e.getMessage());
        }
    }

    private void writeResponse(DataOutputStream dos, HttpResponse response) throws IOException {
        dos.writeBytes(response.getHeader().toString());
        dos.write(response.getBody(), 0, response.getBody().length);
        dos.flush();
    }
}
