package webserver;

import java.io.*;
import java.net.Socket;

import controller.BoardController;
import controller.HomeController;
import controller.UserController;
import request.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import response.http.HttpResponse;

import static webserver.HttpResponseHandler.response;

public class DispatcherServlet implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private final Socket connection;
    private static final HomeController homeController = HomeController.getInstance();
    private static final UserController userController = UserController.getInstance();
    private static final BoardController boardController = BoardController.getInstance();

    public DispatcherServlet(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = new HttpRequest(in); // InputStream을 통해 요청을 byte[] 형태로 읽어들임 -> HttpRequest 객체 생성

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
        }
        if (uri.startsWith("/board")) { // board로 시작하는 경로는 BoardController에서 처리
            return boardController.handleUserRequest(httpRequest);
        }
        return homeController.handleUserRequest(httpRequest); // 그 외의 경로는 HomeController에서 처리
    }
}
