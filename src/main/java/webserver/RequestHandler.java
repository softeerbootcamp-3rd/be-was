package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Map;

import common.util.FileManager;
import dto.HttpRequest;
import dto.HttpResponse;
import http.ContentType;
import http.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            String contentType = getContentType(path);

            try {
                if (request.getMethod().equals("GET") && (FileManager.getFile(path, contentType)) != null) {
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

    private static String getContentType(String path) {
        String extension = getFileExtension(path);
        return ContentType.getMimeType(extension);
    }

    private static String getFileExtension(String path) {
        File file = new File(path);
        String name = file.getName();
        return name.substring(name.lastIndexOf(".") + 1);
    }
}
