package service;

import db.Database;
import model.QueryParams;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public void createUser(QueryParams queryParams) {
        User user = new User();
        Map<String, String> paramMap = queryParams.getParamMap();
        for (String key: paramMap.keySet()) {
            User.setUser(user, key, paramMap.get(key));
        }
        Database.addUser(user);
        logger.debug("User Size = {}", Database.findAll());
        for (User findUser : Database.findAll()) {
            logger.debug("User name = {}", findUser.getName());
        }

    }

}
