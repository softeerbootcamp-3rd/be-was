package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import request.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static util.ResponseUtil.response;
import static util.SingletonUtil.getHomeController;
import static util.SingletonUtil.getUserController;


public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final Socket connection;
    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            HttpRequest httpRequest = new HttpRequest(br, connection.getPort());
            String path = httpRequest.getPath();
            String response = "";

           if (path.startsWith("/user")) { // user로 시작하는 경로는 UserController에서 처리
                response = getUserController().route(path);
           }
           else { // 그 외의 경로는 HomeController에서 처리
               response = getHomeController().route(path);
           }

            // request line 출력
            logger.debug("port : {}, request method : {}, filePath : {}, http version : {}\n",
                    httpRequest.getPort(), httpRequest.getMethod(), httpRequest.getPath(), httpRequest.getHttpVersion());

            String status = response.split(" ")[0];
            String route = response.split(" ")[1];

            response(status, route, out);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
