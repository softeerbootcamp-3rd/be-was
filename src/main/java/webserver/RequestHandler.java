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
    private final String RESOURCES_STATIC_URL = "src/main/resources/static";

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
                if (request.getUrl().endsWith(".html")) {
                    filePath = Paths.get(RESOURCES_TEMPLATES_URL + request.getUrl());

                } else { //.js .css ...
                    filePath = Paths.get(RESOURCES_STATIC_URL + request.getUrl());
                }
            } else {
                String path = controller.process(request.getRequestParam());
                filePath = Paths.get(path + ".html");
            }

            byte[] body = null;

            if (filePath == null) {
                response.respond404();
            } else if (filePath.toString().startsWith("redirect:")) {
                String path=filePath.toString();
                response.response301RedirectHeader(path.substring("redirect:".length()));
                response.responseBody(body);
            } else if (filePath.toString().endsWith(".html")) {
                body = Files.readAllBytes(filePath);
                response.response200Header(body.length, "text/html");
                response.responseBody(body);
            } else if (filePath.toString().endsWith(".css")) {
                body = Files.readAllBytes(filePath);
                response.response200Header(body.length, "text/css");
                response.responseBody(body);
            } else if (filePath.toString().endsWith(".js")) {
                body = Files.readAllBytes(filePath);
                response.response200Header(body.length, "text/javascript");
                response.responseBody(body);
            } else if (filePath.toString().endsWith(".woff") || filePath.toString().endsWith(".ttf")) {
                body = Files.readAllBytes(filePath);
                response.response200Header(body.length, "application/x-font");
                response.responseBody(body);
            } else if(filePath.toString().endsWith(".ico")) {
                body = Files.readAllBytes(filePath);
                response.response200Header(body.length, "image/x-icon");
                response.responseBody(body);
            } else if (filePath.toString().endsWith(".jpg")) {
                body = Files.readAllBytes(filePath);
                response.response200Header(body.length, "image/jpeg");
                response.responseBody(body);
            } else if (filePath.toString().endsWith(".png")) {
                body = Files.readAllBytes(filePath);
                response.response200Header(body.length, "image/png");
                response.responseBody(body);
            } else {
                response.respond404();
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}

