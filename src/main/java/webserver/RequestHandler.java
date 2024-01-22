package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import utils.FileLoader;
import utils.HttpRequestParser;
import utils.HttpResponseSender;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final UserService userService = new UserService();
    private Socket connection;
    private static String USER_CREATE_PATH = "/user/create";
    private static final String INDEX_HTML_PATH = "/index.html";

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    private boolean isUserRegistrationRequest(String path) {
        return path.contains(USER_CREATE_PATH);
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);
            HttpRequestParser parser = new HttpRequestParser();
            Map<String, String> map = parser.parseHttpRequest(in);
            HttpRequest httpRequest = new HttpRequest(map);
            HttpResponseSender responseSender = new HttpResponseSender();
            logger.debug(httpRequest.toString());

            //TODO 파일을 읽어오는 요청이 아닌 경우(예: 회원가입의 가입 버튼)
            String path = httpRequest.getPath();
            logger.debug("Path: {}", path);
            if (isUserRegistrationRequest(path)) {
                userService.registerUser(httpRequest.toUserDto());
                logger.debug("New User Registered!");
                responseSender.redirectToHomePage(dos);
            } else {
                logger.debug("MIME: {}", httpRequest.getResponseMimeType());
                byte[] body = new FileLoader().loadFileContent(path, httpRequest.getResponseMimeType());
                responseSender.sendHttpResponse(dos, body.length, body, httpRequest.getResponseMimeType());
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
