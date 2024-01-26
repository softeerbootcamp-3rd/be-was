package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Map;

import dto.HttpRequest;
import dto.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static webserver.Status.*;
import static webserver.WebServerConfig.*;

public class RequestHandler implements Runnable {
    public static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            DataOutputStream dos = new DataOutputStream(out);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            HttpRequest request = new HttpRequest(br);
            HttpResponse response = new HttpResponse();

            String path = request.getPath();

            try {
                if (request.getMethod().equals("GET")) {
                    response.makeBody(OK, path);
                }
                if (request.getMethod().equals("POST")) {
                    int contentLength = getContentLength(request);
                    String body = getRequestBody(br, contentLength);
                    if (path.equals("/user/create")) {
                        response = userController.create(body);
                    }
                    if (path.equals("/user/login")) {
                        response = userController.login(body);
                    }
                }
            } catch (Exception e) {
                ExceptionHandler.process(e, response);
                e.printStackTrace();
            }

            ResponseHandler.send(dos, response);
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
    }

    private int getContentLength(HttpRequest request) {
        Map<String, String> headers = request.getHeaders();
        String contentLength = headers.get("Content-Length");
        return Integer.parseInt(contentLength);
    }

    private String getRequestBody(BufferedReader br, int contentLength) throws IOException {
        char[] buffer = new char[contentLength];
        br.read(buffer, 0, contentLength);
        return new String(buffer);
    }
}
