package webserver;

import java.io.*;
import java.net.Socket;
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
    private final String RESOURCES_STATIC_URL = "src/main/resources/static";
    private final String DEFAULT_URL = "/index.html";

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

            HttpRequest request = new HttpRequest(in); //http request 정보 가져오기
            request.print(); //http request 정보 출력

            HttpResponse response = new HttpResponse(dos);

            String path;
            Controller controller = controllerMap.get(request.getUrl());
            if (controller == null) { //.html
                if (request.getUrl().endsWith(".html")) {
                    path = RESOURCES_TEMPLATES_URL + request.getUrl();

                } else { //.js .css ...
                    path = RESOURCES_STATIC_URL + request.getUrl();
                }
            } else {
                if (request.getMethod().equals("GET")) {
                    path = controller.process(request.getRequestParam()) + ".html";
                } else if (request.getMethod().equals("POST")) {
                    path = controller.process(request.getFormData()) + ".html";
                } else {
                    path=null;
                }
            }

            byte[] body = null;

            if (path.startsWith("redirect:")) {
                response.response302RedirectHeader(path.substring("redirect:".length()));
                response.responseBody(body);
            } else {
                response.response200Header(path);
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}

