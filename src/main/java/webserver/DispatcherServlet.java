package webserver;

import java.io.*;
import java.net.Socket;

import controller.HomeController;
import controller.UserController;
import request.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import response.http.HttpResponse;

import static com.google.common.base.Charsets.UTF_8;
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
            BufferedReader br = new BufferedReader(new InputStreamReader(in, UTF_8));

            HttpRequest httpRequest = new HttpRequest(br);

            HttpResponse httpResponse = findController(httpRequest);

            response(httpResponse, out);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public static HttpResponse findController(HttpRequest httpRequest) throws Exception {
        String uri = httpRequest.getUri();
        if (uri.startsWith("/user")) { // user로 시작하는 경로는 UserController에서 처리
            return userController.handleUserRequest(httpRequest);
        } else {                                         // 그 외의 경로는 HomeController에서 처리
            return homeController.handleUserRequest(httpRequest);
        }
    }
}
