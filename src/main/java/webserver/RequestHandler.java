package webserver;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.nio.file.Files;

import dto.ResponseBuilder;
import handler.RequestMappingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.ControllerMapper;
import util.ResourceLoader;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final String FILE_PATH = "src/main/resources";
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.

            HttpRequest httpRequest = new HttpRequest(connection, in);
            logger.debug(httpRequest.toString());
            DataOutputStream dos = new DataOutputStream(out);

            File file;
            String path = httpRequest.getPath().equals("/") ? "/index.html" : httpRequest.getPath();
            if (path.endsWith(".html")) {
                file = new File(FILE_PATH + "/templates" + path);
            } else {
                file = new File(FILE_PATH + "/static" + path);
            }

            if (file.exists()) {
                String contentType = ResourceLoader.getContentType(path);
                byte[] body = Files.readAllBytes(file.toPath());
                ResponseBuilder responseBuilder = new ResponseBuilder(HttpStatus.OK, contentType, body);
                HttpResponse.response(dos, responseBuilder);
            } else {
                Class<?> controllerClass = ControllerMapper.getController(httpRequest.getHttpMethod());
                ResponseBuilder responseBuilder = RequestMappingHandler.handleRequest(controllerClass, httpRequest);
                HttpResponse.response(dos, responseBuilder);
            }

        } catch (IOException | InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException e) {
            logger.error(e.getMessage());
        }
    }

}