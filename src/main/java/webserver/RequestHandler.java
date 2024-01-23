package webserver;

import java.io.*;
import java.net.Socket;

import dto.Login;
import model.Session;
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

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);
            HttpRequestParser parser = new HttpRequestParser();
            HttpRequest httpRequest = new HttpRequest(parser.parseHttpRequest(in));
            HttpResponseSender responseSender = new HttpResponseSender();
            logger.debug(httpRequest.toString());

            //TODO: 회원가입과 로그인 분리
            String path = httpRequest.getPath();
            if(httpRequest.getMethod().equals("POST")){
                if(path.contains(USER_CREATE_PATH)){    //회원가입
                    userService.registerUser(httpRequest.toUserDto());
                    logger.debug("New User Registered!");
                    responseSender.redirectToHomePage(dos);
                }
                else{
                    Login loginInfo = httpRequest.getLoginInfo();
                    if(userService.isValidUser(loginInfo)){  //로그인 성공
                        Session session = userService.login(loginInfo);
                        //리다이렉션
                        responseSender.redirectToHomePage(dos, session.getSessionId(), session.getExpireDate());
                    }
                    else{   //로그인 실패
                        logger.debug("{} 로그인 실패", loginInfo.getUserId());
                        responseSender.redirectToLoginFailedPage(dos);
                    }
                }
            }
            else{
                byte[] body = new FileLoader().loadFileContent(path, httpRequest.getResponseMimeType());
                responseSender.sendHttpResponse(dos, body.length, body, httpRequest.getResponseMimeType());
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
