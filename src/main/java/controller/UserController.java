package controller;

import request.SignUpRequest;

import static util.SingletonUtil.getUserService;

public class UserController {

    // 요청 URI에 따라 요청을 처리할 컨트롤러를 선택하는 역할
    public String route(String path) {
        if (path.startsWith("/user/create")) { // 회원가입 요청
            signUp(path);
            return "302 /index.html";
        }
        else { // 정적 파일 요청
            return "200 " + path;
        }
    }

    // 회원가입 요청 처리
    private void signUp(String request) {
        SignUpRequest signUpRequest = new SignUpRequest(request); // 회원가입 요청 객체 생성
        getUserService().signUp(signUpRequest); // 회원가입 요청 처리
    }
}
