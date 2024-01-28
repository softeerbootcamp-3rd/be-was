package webserver;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.Map;

import controller.Controller;
import controller.ControllerMappingMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.HttpRequest;
import response.HttpResponse;
import utils.HttpRequestUtils;
import utils.HttpResponseUtils;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpRequest request = HttpRequestUtils.makeHttpRequest(in);
            HttpResponse response = new HttpResponse();


            if (request.getMethod().equals("GET")) {
                GetMethodHandler getMethodHandler = new GetMethodHandler();
                getMethodHandler.process(request, response);
            }

//            Controller controller = ControllerMappingMap.getController(request.getMethod(), request.getUrl());
//            controller.process(request, response);

            DataOutputStream dos = new DataOutputStream(out);
            HttpResponseUtils.renderResponse(dos, response);

        } catch (IOException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            logger.error(e.getMessage());
        }
    }

}
