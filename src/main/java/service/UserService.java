package service;

import db.Database;
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

    public static boolean checkUserLogin(String sessionId) {
        return (sessionId != null && Session.containsSessionId(sessionId));
    }

    public static String login(UserInfo userInfo) {
        String userId = userInfo.getUserId();
        String password = userInfo.getPassword();
        User registeredUser = Database.findUserById(userId);
        if(registeredUser == null || !registeredUser.getPassword().equals(password))
            return null;
        String sessionId = String.valueOf(UUID.randomUUID());
        Session.addSession(sessionId, registeredUser);
        return sessionId;
    }
}
