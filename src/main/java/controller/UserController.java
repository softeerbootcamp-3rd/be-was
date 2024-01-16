package controller;

import request.SignUpRequest;
import util.SingletonUtil;

import static request.SignUpRequest.parseSignUpRequest;


public class UserController {

    public String route(String path) {
        if (path.startsWith("/user/create")) {
            signUp(path);
            return "302 /index.html"; // index.html로 리다이렉트
        }
        else {
            return "200 " + path; // 정적 파일 응답
        }
    }

    private void signUp(String request) {
        SignUpRequest signUpRequest = parseSignUpRequest(request);
        SingletonUtil.getUserService().signUp(signUpRequest);
    }
}
