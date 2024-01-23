package service;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.DispatcherServlet;

import static db.Database.*;

public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private volatile static UserService instance;

    private UserService() {
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    // 회원가입 요청을 처리하는 메소드
    public void join(User user) {

        addUser(user); // 데이터베이스에 회원정보 저장

        // 데이터베이스에 정상적으로 저장되었는지 확인하기 위해 로그를 출력
        logger.debug("Total User Count : {}\n", getUserCount());
        logger.debug("UserId : {}, Password : {}, Name : {}, Email : {}\n",
                user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }
}
