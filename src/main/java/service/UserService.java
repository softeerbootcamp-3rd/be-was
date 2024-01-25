package service;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.SessionStorage;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public String create(String userId, String password, String name, String email) {
        User user = new User(userId, password, name, email);
        Database.addUser(user);
        logger.debug(user.toString());
        return user.getUserId();
    }

    public String login(String userId, String password) {
        User user = Database.findUserById(userId);
        if (user == null || !user.getPassword().equals(password))
            return null;
        String sessionId = SessionStorage.generateSessionId();
        SessionStorage.addSession(sessionId, user);
        return sessionId;
    }
}