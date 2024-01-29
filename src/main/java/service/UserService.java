package service;

import db.UserDatabase;
import exception.UserException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.SessionManager;
import java.util.Collection;

import static util.StringUtils.*;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public String create(String userId, String password, String name, String email) {
        if (userId == null)
            throw new UserException(UserException.NULL_ID);
        if (password == null)
            throw new UserException(UserException.NULL_PASSWORD);
        if (name == null)
            throw new UserException(UserException.NULL_NAME);
        if (email == null)
            throw new UserException(UserException.NULL_EMAIL);

        userId = decode(userId);
        password = decode(password);
        name = decode(name);
        email = decode(email);

        createValidate(userId, email);

        User user = new User(userId, password, name, email);
        UserDatabase.addUser(user);
        logger.debug("Created: " + user.toString());
        return user.getUserId();
    }

    private void createValidate(String userId, String email) {
        Collection<User> users = UserDatabase.findAll();
        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                throw new UserException(UserException.DUPLICATE_ID);
            }
            if (user.getEmail().equals(email)) {
                throw new UserException(UserException.DUPLICATE_EMAIL);
            }
        }
    }

    public String login(String userId, String password) {
        if (userId == null)
            return null;
        User user = UserDatabase.findUserById(userId);
        if (user == null || !user.getPassword().equals(password))
            return null;
        String sessionId = SessionManager.generateSessionId();
        SessionManager.addSession(sessionId, user);
        return sessionId;
    }

    public Collection<User> list() {
        return UserDatabase.findAll();
    }

    public String update(String userId, String password, String name, String email) {
        if (userId == null)
            throw new UserException(UserException.NULL_ID);
        if (password == null)
            throw new UserException(UserException.NULL_PASSWORD);
        if (name == null)
            throw new UserException(UserException.NULL_NAME);
        if (email == null)
            throw new UserException(UserException.NULL_EMAIL);

        userId = decode(userId);
        password = decode(password);
        name = decode(name);
        email = decode(email);

        updateValidate(userId, email);

        User user = new User(userId, password, name, email);
        UserDatabase.addUser(user);
        logger.debug("Updated: " + user.toString());
        return user.getUserId();
    }

    private void updateValidate(String userId, String email) {
        Collection<User> users = UserDatabase.findAll();
        for (User user : users) {
            if (user.getUserId().equals(userId))
                continue;
            if (user.getEmail().equals(email)) {
                throw new UserException(UserException.DUPLICATE_EMAIL);
            }
        }
    }
}