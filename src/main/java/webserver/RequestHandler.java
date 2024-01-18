package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import controller.Controller;
import controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    private Map<String, Controller> controllerMap = new HashMap<>(); //controller list

    private final String RESOURCES_TEMPLATES_URL = "src/main/resources/templates";

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        controllerMap.put("/user/create", new UserController());
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);

            HttpRequest request = new HttpRequest(in); //http request 정복 가져오기
            request.print(); //http request 정보 출력

            HttpResponse response = new HttpResponse(dos);

            Map<String, Object> view = new HashMap<>();
            Path filePath = null;

            Controller controller = controllerMap.get(request.getUrl());
            if (controller == null) { //.html
                if (request.getUrl().contains(".html")) {
                    filePath = Paths.get(RESOURCES_TEMPLATES_URL + request.getUrl());

                } else { //.js .css ...
                    ;
                }
            } else {
                String path = controller.process(request.getRequestParam(), view);
                filePath = Paths.get(path + ".html");
            }




            byte[] body = null;

            if (filePath == null) {
                response.respond404();
            } else {
                body = Files.readAllBytes(filePath);
                response.response200Header(body.length);
                response.responseBody(body);
            }



        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}

