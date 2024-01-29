package service;

import db.Database;
import exception.SourceException;
import util.ParseParams;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ErrorCode;
import util.Session;

import java.util.Map;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public User createUser(ParseParams parseParams) {
        User user = new User();
        Map<String, String> paramMap = parseParams.getParamMap();
        for (String key: paramMap.keySet()) {
            User.setUser(user, key, paramMap.get(key));
        }
        logger.debug("user info = {}", user);
        if (Database.findUserById(user.getUserId()) != null) {
            throw new SourceException(ErrorCode.DUPLICATED_USER);
        }
        Database.addUser(user);
        logger.debug("DataBase Size = {}", Database.findAll().size());
        return user;
    }

    public String loginUser(ParseParams bodyData) {
        Map<String, String> data = bodyData.getParamMap();
        User user = Database.findUserById(data.get("userId"));
        if (user == null) {
            return null;
        }

        if (!user.getPassword().equals(data.get("password"))) {
            return null;
        }
        Session session = new Session(user.getUserId());
        Database.addSession(session);
        return session.getSessionId();
    }
}
