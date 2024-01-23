package service;

import model.User;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MemberJoinService {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static List<User> userList = new ArrayList<>();

    public void createUser(Map<String, String> params) {
        String userId = params.get("userId");
        String password = params.get("password");
        String name = params.get("name");
        String email = params.get("email");

        User user = new User(userId, password, name, email);
        logger.debug("New User : {}", user);
        userList.add(user);
    }

    public List<User> getUserList() {
        return userList;
    }
}
