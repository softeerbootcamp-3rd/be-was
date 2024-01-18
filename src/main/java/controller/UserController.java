package controller;

import request.HttpRequest;
import service.UserService;
import util.RequestUrl;

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
    public String route(HttpRequest httpRequest) {

        String path = httpRequest.getPath();

        if (path.startsWith(RequestUrl.USER_FORM.getUrl())) {
            return "200 " + path;
        }
        if (path.startsWith(RequestUrl.USER_LIST.getUrl())) {
            return "200 " + path;
        }
        if (path.startsWith(RequestUrl.USER_LOGIN.getUrl())) {
            return "200 " + path;
        }
        if (path.startsWith(RequestUrl.USER_LOGIN_FAILED.getUrl())) {
            return "200 " + path;
        }
        if (path.startsWith(RequestUrl.USER_PROFILE.getUrl())) {
            return "200 " + path;
        }
        if (path.startsWith(RequestUrl.USER_CREATE.getUrl())) {
            signUp(httpRequest);
            return "302 /index.html";
        }
        throw new IllegalArgumentException("올바르지 않은 요청입니다.");
    }

    // 회원가입 요청 처리
    private void signUp(HttpRequest httpRequest) {
        String request = httpRequest.getHttpRequst();
        userService.signUp(request); // 회원가입 요청 처리
    }
}
