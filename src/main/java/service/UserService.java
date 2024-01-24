package service;

import db.Database;
import exception.SourceException;
import util.QueryParams;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ErrorCode;

import java.util.Map;

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

}
