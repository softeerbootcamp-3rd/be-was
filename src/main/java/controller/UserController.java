package controller;

import request.HttpRequest;
import service.UserService;
import util.RequestUrl;

import java.io.IOException;
import java.io.OutputStream;

import static util.ResponseBuilder.response;

public class UserController {

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

    // 요청 URI에 따라 요청을 처리할 컨트롤러를 선택하는 역할
    public void route(HttpRequest httpRequest, OutputStream out) throws IOException {

        String path = httpRequest.getPath();

        if (path.startsWith(RequestUrl.USER_FORM.getUrl())) {
            response("200", path, out);
        }
        if (path.startsWith(RequestUrl.USER_LIST.getUrl())) {
            response("200", path, out);
        }
        if (path.startsWith(RequestUrl.USER_LOGIN.getUrl())) {
            response("200", path, out);
        }
        if (path.startsWith(RequestUrl.USER_LOGIN_FAILED.getUrl())) {
            response("200", path, out);
        }
        if (path.startsWith(RequestUrl.USER_PROFILE.getUrl())) {
            response("200", path, out);
        }
        if (path.startsWith(RequestUrl.USER_CREATE.getUrl())) {
            signUp(httpRequest);
            response("302", "/index.html", out);
        }
        throw new IllegalArgumentException("올바르지 않은 요청입니다.");
    }

    // 회원가입 요청 처리
    public void signUp(HttpRequest httpRequest) {
        String request = httpRequest.getHttpRequst();
        userService.signUp(request); // 회원가입 요청 처리
    }
}
