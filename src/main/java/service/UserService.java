package service;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import javax.xml.crypto.Data;
import java.util.Map;

public class UserService {

    public boolean signUp(Map<String, String> queryParams){
        User newUser = createUser(queryParams);
        if (Database.findUserById(newUser.getUserId()) != null) {
            return false;
        }
        Database.addUser(newUser);
        return true;
    }

    private User createUser(Map<String, String> queryParams) {
        String userId = queryParams.getOrDefault("userId", "");
        String password = queryParams.getOrDefault("password", "");
        String name = queryParams.getOrDefault("name", "");
        String email = queryParams.getOrDefault("email", "");
        return new User(userId, password, name, email);
    }
}