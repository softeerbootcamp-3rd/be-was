package service;

import db.Database;
import db.SessionStorage;
import model.Request;
import model.Session;
import model.User;
import model.UserInfo;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.UUID;

public class UserService {

    public static User create(UserInfo userInfo) {
        try {
            User user = new User(userInfo);
            Database.addUser(user);
            return user;
        }
        catch (Exception e) {
            return null;
        }
    }

    public static String login(UserInfo userInfo) {
        String userId = userInfo.getUserId();
        String password = userInfo.getPassword();
        User user = Database.findUserById(userId);
        if(user == null || !user.getPassword().equals(password))
            return null;
        Session createdSession = new Session(userId);
        SessionStorage.addSession(createdSession);
        return createdSession.getSessionId();
    }
}
