package webserver;

import java.io.*;
import java.net.Socket;

import common.util.FileManager;
import dto.HttpRequest;
import dto.HttpResponse;
import http.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static dto.HttpResponse.*;
import static http.Status.*;
import static common.WebServerConfig.*;

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
                if (request.getMethod().equals("GET")
                        && (FileManager.getFile(path, getContentType(request.getPath()))) != null) {
                    response.makeBody(OK, path);
                }
                if (request.getMethod().equals("POST")) {
                    String body = request.getBody();
                    if (path.equals("/user/create")) {
                        response = userController.create(body);
                    }
                    if (path.equals("/user/login")) {
                        response = userController.login(body);
                    }
                }
            } catch (Exception e) {
                ExceptionHandler.process(e, response);
            }

            ResponseHandler.send(dos, response);
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
    }
}
