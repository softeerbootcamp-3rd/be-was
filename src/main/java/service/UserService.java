package service;

import db.Database;
import exception.AlreadyExistUserException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import javax.xml.crypto.Data;


public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public void addUser(String userId, String password, String name, String email) {
        User userById = Database.findUserById(userId);
        if (userById == null) {
            User user = new User(userId, password, name, email);
            logger.debug("user info = {}", user);
            Database.addUser(user);
            return;
        }

        throw new AlreadyExistUserException("이미 존재하는 userId입니다.");
    }

    public User findUserById(String userId) {
        return Database.findUserById(userId);
    }
}
