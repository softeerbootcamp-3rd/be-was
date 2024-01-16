package controller;

import model.User;
import service.Service;

public class Controller {

    // 회원가입 처리
    public static User signup(String[] requestParams) {
        return Service.signup(requestParams);
    }

    // 파일 불러오기 요청
    public static String requestFile(String request_target) {
        return Service.requestFile(request_target);
    }
}
