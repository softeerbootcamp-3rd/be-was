package controller;

import request.SignUpRequest;
import service.UserService;

public class UserController {

    private static UserController instance = new UserController();

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
    public String route(String path) {
        if (path.startsWith("/user/form")) { // 회원가입 페이지 요청
            return "200 " + path;
        }
        else if (path.startsWith("/user/list")) {
            return "200 " + path;
        }
        else if (path.startsWith("/user/login")) {
            return "200 " + path;
        }
        else if (path.startsWith("/user/login_failed")) {
            return "200 " + path;
        }
        else if (path.startsWith("/user/profile")) {
            return "200 " + path;
        }
        else if (path.startsWith("/user/create")) { // 회원가입 요청
            signUp(path); // 회원가입 요청 처리
            return "302 /index.html";
        }
        else {
            throw new IllegalArgumentException("요청 URI에 해당하는 컨트롤러가 없습니다.");
        }
    }

    // 회원가입 요청 처리
    private void signUp(String request) {
        SignUpRequest signUpRequest = new SignUpRequest(request); // 회원가입 요청 객체 생성
        userService.signUp(signUpRequest); // 회원가입 요청 처리
    }
}
