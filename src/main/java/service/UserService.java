package service;

import db.Database;
import model.Request;
import model.Session;
import model.User;
import model.UserInfo;

import java.util.HashMap;

public class UserService {

    public static User create(UserInfo userInfo) {
        User user = new User(userInfo);
        if(user.verifyUser()) {
            Database.addUser(user);
            return user;
        }
        else return null;
    }

    public static boolean checkUserLogin(Request request) {
        String sessionId = request.getCookie().get("sessionId");
        return (sessionId != null && Session.containsSessionId(sessionId));
    }
}
