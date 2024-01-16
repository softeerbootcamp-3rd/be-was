package service;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.SignUpRequest;
import webserver.RequestHandler;

import static util.SingletonUtil.getDatabase;

public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public void signUp(SignUpRequest signUpRequest) {
        User user = new User(signUpRequest); // 회원정보 객체 생성
        getDatabase().addUser(user); // 데이터베이스에 회원정보 저장

        // 데이터베이스에 정상적으로 저장되었는지 확인하기 위해 로그를 출력
        logger.debug("Total User Count : {}\n", getDatabase().getUserCount());
        logger.debug("UserId : {}, Password : {}, Name : {}, Email : {}\n",
                signUpRequest.getUserId(), signUpRequest.getPassword(), signUpRequest.getName(), signUpRequest.getEmail());
    }
}
