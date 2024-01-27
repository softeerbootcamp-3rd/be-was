package service;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.SessionManager;
import java.util.Collection;

import static util.StringUtils.*;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public String create(String userId, String password, String name, String email) {
        String decodedUserId = decode(userId);
        String decodedPassword = decode(password);
        String decodedName = decode(name);
        String decodedEmail = decode(email);

//        isDuplicateUser();

        User user = new User(decodedUserId, decodedPassword, decodedName, decodedEmail);
        Database.addUser(user);
        logger.debug(user.toString());
        return user.getUserId();
    }

    public String login(String userId, String password) {
        User user = Database.findUserById(userId);
        if (user == null || !user.getPassword().equals(password))
            return null;
        String sessionId = SessionManager.generateSessionId();
        SessionManager.addSession(sessionId, user);
        return sessionId;
    }

    public Collection<User> list() {
        return Database.findAll();
    }


}