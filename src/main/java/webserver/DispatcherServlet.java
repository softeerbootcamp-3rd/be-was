package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import controller.HomeController;
import controller.UserController;
import request.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.StatusCode;

import static webserver.ViewResolver.response;

public class DispatcherServlet implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private final Socket connection;
    private static final HomeController homeController = HomeController.getInstance();
    private static final UserController userController = UserController.getInstance();

    public DispatcherServlet(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            HttpRequest httpRequest = new HttpRequest(br);

            logger.debug("request method : {}, filePath : {}, http version : {}\n",
                    httpRequest.getMethod(), httpRequest.getUri(), httpRequest.getHttpVersion()); // request line 출력

            StatusCode status = findController(httpRequest); // request line을 통해 적절한 controller를 찾아서 처리 및 status 반환
            String URI = httpRequest.getUri(); // URI 반환

            response(status, URI, out);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static StatusCode findController(HttpRequest httpRequest) throws IOException {
        String URI = httpRequest.getUri();
        if (URI.startsWith("/user")) { // user로 시작하는 경로는 UserController에서 처리
            return userController.handleUserRequest(httpRequest);
        } else {                                         // 그 외의 경로는 HomeController에서 처리
            return homeController.handleUserRequest(httpRequest);
        }
    }
}
