package service;

import db.Database;
import model.Request;
import model.Session;
import model.User;
import model.UserInfo;

import javax.xml.crypto.Data;
import java.util.HashMap;

public class UserService {

    public static User create(HashMap<String, String> params) {
        try {
            UserInfo userInfo = new UserInfo(params);
            User user = new User(userInfo);
            Database.addUser(user);
            return user;
        }
        catch (Exception e) {
            return null;
        }
    }

    public static boolean checkUserLogin(Request request) {
        String sessionId = request.getCookie().get("sessionId");
        return (sessionId != null && Session.containsSessionId(sessionId));
    }
}
