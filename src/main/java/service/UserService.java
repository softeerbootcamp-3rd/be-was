package service;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public void createUser(String userId, String password, String name, String email){
        User user = new User.Builder()
                .userId(userId)
                .password(password)
                .name(name)
                .email(email)
                .build();

        Database.addUser(user);
        logger.debug("User 생성 완료");
        logger.debug(user.toString());
    }
}
