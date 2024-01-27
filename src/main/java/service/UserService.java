package service;

import db.Database;
import exception.CreateUserException;
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
            throw new CreateUserException(CreateUserException.NULL_ID);
        if (password == null)
            throw new CreateUserException(CreateUserException.NULL_PASSWORD);
        if (name == null)
            throw new CreateUserException(CreateUserException.NULL_NAME);
        if (email == null)
            throw new CreateUserException(CreateUserException.NULL_EMAIL);

        userId = decode(userId);
        password = decode(password);
        name = decode(name);
        email = decode(email);

        isDuplicateId(userId);
        isDuplicateEmail(email);

        User user = new User(userId, password, name, email);
        Database.addUser(user);
        logger.debug("Created: " + user.toString());
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

    public String update(String userId, String password, String name, String email) {
        if (userId == null)
            throw new CreateUserException(CreateUserException.NULL_ID);
        if (password == null)
            throw new CreateUserException(CreateUserException.NULL_PASSWORD);
        if (name == null)
            throw new CreateUserException(CreateUserException.NULL_NAME);
        if (email == null)
            throw new CreateUserException(CreateUserException.NULL_EMAIL);

        userId = decode(userId);
        password = decode(password);
        name = decode(name);
        email = decode(email);

        isDuplicateEmail(email);

        User user = new User(userId, password, name, email);
        Database.addUser(user);
        logger.debug("Updated: " + user.toString());
        return user.getUserId();
    }

    private void isDuplicateId(String userId) {
        Collection<User> users = Database.findAll();
        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                throw new CreateUserException(CreateUserException.DUPLICATE_ID);
            }
        }
    }

    private void isDuplicateEmail(String email) {
        Collection<User> users = Database.findAll();
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                throw new CreateUserException(CreateUserException.DUPLICATE_EMAIL);
            }
        }
    }


}