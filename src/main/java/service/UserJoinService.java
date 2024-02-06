package service;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.util.Map;

public class UserJoinService {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public boolean createUser(String userId, String password, String name, String email) {
        User user = new User(userId, password, name, email);
        logger.debug("New User : {}", user);
        Database.addUser(user);
        return true;
    }

    private static boolean validation(Map<String, String> params) {
        boolean valid = true;
        for (String value : params.values()) {
            if(value.equals("")) {
                return false;
            }
        }
        if (Database.findUserById(params.get("userId")) != null) {
            return false;
        }
        return true;
    }
}
