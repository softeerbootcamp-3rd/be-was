package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import controller.HomeController;
import controller.UserController;
import request.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final Socket connection;
    private static final HomeController homeController = HomeController.getInstance();
    private static final UserController userController = UserController.getInstance();

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            HttpRequest httpRequest = new HttpRequest(br);
            // request line 출력
            logger.debug("port : {}, request method : {}, filePath : {}, http version : {}\n",
                    httpRequest.getMethod(), httpRequest.getPath(), httpRequest.getHttpVersion());

            getStatusAndRoute(httpRequest, out);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void getStatusAndRoute(HttpRequest httpRequest, OutputStream out) throws IOException {
        if (httpRequest.getPath().startsWith("/user")) { // user로 시작하는 경로는 UserController에서 처리
            userController.route(httpRequest, out);
        } else { // 그 외의 경로는 HomeController에서 처리
            homeController.route(httpRequest, out);
        }
    }
}
