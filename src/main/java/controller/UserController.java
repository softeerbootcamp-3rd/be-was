package controller;

import model.User;
import request.http.HttpRequest;
import request.user.LoginRequest;
import response.http.HttpResponse;
import service.UserService;
import util.Parser;

import java.io.File;

import static db.Session.*;
import static util.Uri.*;
import static util.StatusCode.*;

public class UserController implements Controller {

    private volatile static UserController instance = new UserController();

    private UserController() {
    }

    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    private final UserService userService = UserService.getInstance();

    @Override
    public HttpResponse handleUserRequest(HttpRequest httpRequest) throws Exception {

        String uri = httpRequest.getUri();
        String filePath = httpRequest.getFilePath(uri);
        String method = httpRequest.getMethod();

        File file = new File(filePath);

        if (file.exists() && method.equals("GET")) {
            return new HttpResponse(OK, filePath);
        }

        if (method.equals("POST")) {

            if (uri.equals(USER_CREATE.getUri())) { // 회원가입
                try {
                    User user = Parser.jsonParser(User.class, httpRequest.getRequestBody());
                    userService.join(user);

                    return new HttpResponse(FOUND, "text/html", HOME_INDEX.getUri(), null);
                } catch (Exception e) {
                    return new HttpResponse(FOUND, "text/html", USER_FORM_FAILED.getUri(), null);
                }
            }

            if (uri.equals("/user/login")) { // 로그인
                try {
                    LoginRequest loginRequest = Parser.jsonParser(LoginRequest.class, httpRequest.getRequestBody());
                    if (!userService.login(loginRequest)) {
                        return new HttpResponse(FOUND, "text/html", USER_LOGIN_FAILED.getUri(), null);
                    }
                    String sessionId = getSessionId(loginRequest.getUserId());

                    return new HttpResponse(FOUND, "text/html", HOME_INDEX.getUri(), sessionId);
                } catch (Exception e) {
                    return new HttpResponse(FOUND, "text/html", USER_LOGIN_FAILED.getUri(), null);
                }
            }
        }
        return new HttpResponse(NOT_FOUND);
    }
}
