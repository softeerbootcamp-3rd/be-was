package webserver;

import java.io.*;
import java.net.Socket;

import dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import utils.FileReader;
import utils.RequestLogger;
import utils.RequestParser;
import utils.ResponseSender;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final UserService userService = new UserService();
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);
            //HTTP Request Logging
            RequestLogger.logRequest(in);

            //TODO 파일을 읽어오는 요청이 아닌 경우(예: 회원가입의 가입 버튼)
            String path = RequestParser.getPath();
            //가입 저장 요청
            if (path.contains("user/create")) {
                UserDTO userInfo = RequestParser.parseNewUserInfo();
                String userId = userService.registerUser(userInfo);
                logger.debug("New User Registered! User Id: {}", userId);

                //홈 화면(/index.html)로 리다이렉트
                String redirectLocation = "/index.html";
                ResponseSender.sendRedirectResponse(dos, redirectLocation);
            } else {   //html, js, css 등 페이지 요청
                //파일 읽기
                byte[] body = FileReader.readFile(RequestParser.getPath());
                //HTTP Response
                ResponseSender.sendResponse(dos, body.length, body);
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
