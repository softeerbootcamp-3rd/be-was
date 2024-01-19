package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import controller.HomeController;
import controller.UserController;
import request.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import response.HttpResponse;
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
            String URI = httpRequest.getURI();

            // request line 출력
            logger.debug("request method : {}, filePath : {}, http version : {}\n",
                    httpRequest.getMethod(), httpRequest.getURI(), httpRequest.getHttpVersion());

            StatusCode status = getStatus(httpRequest); // 상태코드 반환

            response(status, URI, out);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static StatusCode getStatus(HttpRequest httpRequest) throws IOException {
        if (httpRequest.getURI().startsWith("/user")) { // user로 시작하는 경로는 UserController에서 처리
            return userController.route(httpRequest);
        } else {                                         // 그 외의 경로는 HomeController에서 처리
            return homeController.route(httpRequest);
        }
    }
}
