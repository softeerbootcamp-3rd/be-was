package handler;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;

import model.HttpRequest;
import model.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.HttpUtil;
import model.HttpStatusCode;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private FileHandler fileHandler;
    private UserHandler userHandler;

    public RequestHandler(Socket connectionSocket, FileHandler fileHandler, UserHandler userHandler) {
        this.connection = connectionSocket;
        this.fileHandler = fileHandler;
        this.userHandler = userHandler;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

            HttpRequest httpRequest = HttpUtil.getHttpRequest(bufferedReader);
            String method = httpRequest.getMethod();
            String url = httpRequest.getUrl();

            DataOutputStream dos = new DataOutputStream(out);
            HttpResponse response;

            if (method.equals("GET")) {
                if (url.startsWith("/user") && !url.endsWith(".html")) {
                    response = userHandler.handle(httpRequest);
                } else {
                    response = fileHandler.handle(httpRequest);
                }
            } else {
                Map<String, String> properties = new TreeMap<>();
                properties.put("Allow", "GET");
                response = HttpResponse.emptyBodyResponse(HttpStatusCode.METHOD_NOT_ALLOWED, properties);
            }
            writeResponse(dos, response);
        } catch (IOException | NullPointerException e) {
            logger.error(e.getMessage());
        }
    }

    private void writeResponse(DataOutputStream dos, HttpResponse response) throws IOException {
        dos.writeBytes(response.getHeader().toString());
        dos.write(response.getBody(), 0, response.getBody().length);
        dos.flush();
    }
}
