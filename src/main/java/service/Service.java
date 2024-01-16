package service;

import db.Database;
import model.User;


public class Service {

    // 회원가입 처리
    public static User signup(String[] requestParams) {
        if(requestParams == null || requestParams.length != 4)
            return null;
        // User 객체 생성
        User user = new User(requestParams[0], requestParams[1], requestParams[2], requestParams[3]);
        // 데이터베이스에 저장
        Database.addUser(user);
        return user;
    }

    // 파일 불러오기 요청
    public static String requestFile(String request_target) {
        // 해당 파일을 읽고 응답
        String path = "./src/main/resources";
        // 1. html일 경우
        if(request_target.contains(".html")) {
            path += "/templates" + request_target;
        }
        // 2. css, fonts, images, js, ico일 경우
        else {
            path += "/static" + request_target;
        }
        return path;
    }
}
