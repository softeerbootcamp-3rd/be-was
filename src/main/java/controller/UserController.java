package controller;

import request.HttpRequest;
import service.UserService;
import util.StatusCode;

import static util.RequestUrl.*;
import static util.StatusCode.*;

public class UserController implements Controller{

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
    public StatusCode route(HttpRequest httpRequest) {

        String URI = httpRequest.getURI();

        if (URI.startsWith(USER_FORM.getUrl()) ||
            URI.startsWith(USER_LIST.getUrl()) ||
            URI.startsWith(USER_LOGIN.getUrl()) ||
            URI.startsWith(USER_LOGIN_FAILED.getUrl()) ||
            URI.startsWith(USER_PROFILE.getUrl())
        )
            return OK;
        else if (URI.startsWith(USER_CREATE.getUrl())) {
            signUp(httpRequest);
            return FOUND;
        }
        return NOT_FOUND;
    }

    // 회원가입 요청 처리
    public void signUp(HttpRequest httpRequest) {
        String request = httpRequest.getHttpRequst();
        userService.signUp(request);
    }
}
