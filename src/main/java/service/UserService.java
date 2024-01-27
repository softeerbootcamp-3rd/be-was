package service;

import db.Database;
import exception.DuplicateUserException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.SessionManager;
import java.util.Collection;

import static util.StringUtils.*;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public String create(String userId, String password, String name, String email) {
        userId = decode(userId);
        password = decode(password);
        name = decode(name);
        email = decode(email);

        isDuplicateUser(userId, email);

        User user = new User(userId, password, name, email);
        Database.addUser(user);
        logger.debug(user.toString());
        return user.getUserId();
    }

    private void isDuplicateUser(String userId, String email) {
        Collection<User> users = Database.findAll();
        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                throw new DuplicateUserException(DuplicateUserException.duplicateId);
            }
            if (user.getEmail().equals(email)) {
                throw new DuplicateUserException(DuplicateUserException.duplicateEmail);
            }
        }
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