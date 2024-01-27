package service;

import db.Database;
import exception.SourceException;
import util.QueryParams;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ErrorCode;
import util.Session;

import java.util.Map;
import java.util.UUID;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public User createUser(QueryParams queryParams) {
        User user = new User();
        Map<String, String> paramMap = queryParams.getParamMap();
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

    public String loginUser(QueryParams bodyData) {
        Map<String, String> data = bodyData.getParamMap();
        User user = Database.findUserById(data.get("userId"));
        if (user == null) {
            throw new SourceException(ErrorCode.NOT_EXIST_USER);
        }

        if (!user.getPassword().equals(Database.findUserById(data.get("password")))) {
            throw new SourceException(ErrorCode.NOT_VALID_PASSWORD);
        }
        Session session = new Session(user.getUserId());
        Database.addSession(session);
        return session.getSessionId();
    }
}
