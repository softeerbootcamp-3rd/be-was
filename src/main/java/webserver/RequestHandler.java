package webserver;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.nio.file.Files;

import dto.Response;
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
        try {
            InputStream in = connection.getInputStream();
            OutputStream out = connection.getOutputStream();
            DataOutputStream dos = new DataOutputStream(out);

            execute(in, dos);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void execute(InputStream in, DataOutputStream dos) throws Exception {
        try {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpRequest httpRequest = new HttpRequest(connection, in);
            logger.debug(httpRequest.toString());

            Class<?> controllerClass = ControllerMapper.getController(httpRequest.getHttpMethod());
            Response response = RequestMappingHandler.handleRequest(controllerClass, httpRequest);
            HttpResponse.response(dos, response);
        } catch (IndexOutOfBoundsException e) {
            HttpResponse.response(dos, new Response(HttpStatus.BAD_REQUEST));
        }
    }

}